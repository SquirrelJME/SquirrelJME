// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.interpreter.program;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import net.multiphasicapps.collections.MissingCollections;
import net.multiphasicapps.descriptors.FieldSymbol;
import net.multiphasicapps.descriptors.MethodSymbol;
import net.multiphasicapps.interpreter.JVMVariableType;
import net.multiphasicapps.interpreter.JVMVerifyException;

/**
 * This class is given a chunk of byte code .
 *
 * @since 2016/03/29
 */
public class VMCProgram
	extends AbstractList<VMCOp>
{
	/** Lock. */
	final Object lock =
		new Object();
	
	/** The operation cache. */
	private final Map<VMCOp, Reference<VMCOp>> _opcache =
		new WeakHashMap<>();
	
	/** The code length. */
	private final int length;
	
	/** The position of each logical instruction to a physical one. */
	private final int[] _ipos;
	
	/** Jump sources in the program which are explicit and not implicit. */
	final Map<Integer, List<VMCJumpSource>> _expjumps;
	
	/** Initial variable state. */
	final VMCVariableStates _entrystate;
	
	/** The buffer containing the raw byte code. */
	final byte[] _code;
	
	/** Maximum local variables. */
	final int _maxlocals;
	
	/** Maximum stack variables. */
	final int _maxstack;
	
	/** Have exceptions been set yet? */
	private volatile boolean _exceptionsset;
	
	/** There are no exceptions to handle. */
	private volatile boolean _noexceptions;
	
	/**
	 * This initializes the program using the specified code array.
	 *
	 * @param __ml Maximum number of local variables.
	 * @param __ms Maximum number of stack entries.
	 * @param __inst Is this an instance method?
	 * @param __desc The descriptor which describes the argument of this
	 * method, this is used to seed the initial stack.
	 * @param __ins Is this an instance method (which has a {@code this}?).
	 * @param __code The input byte code, note that it is not copied and that
	 * it is used directly.
	 * @throws JVMVerifyException If the maximum stack and local entries are
	 * negative, or there is not enough room
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/29
	 */
	public VMCProgram(int __ml, int __ms, MethodSymbol __desc,
		boolean __ins, byte... __code)
		throws JVMVerifyException, NullPointerException
	{
		// Check
		if (__desc == null || __code == null)
			throw new NullPointerException("NARG");
		
		// Set
		_maxlocals = __ml;
		_maxstack = __ms;
		_code = __code;
		length = _code.length;
		
		// Determine the position of all operations so that they can be
		// condensed into single index points (they all consume a single
		// address rather than multiple ones).
		int pn = length;
		int[] bp = new int[pn];
		int bpa = 0;
		for (int i = 0; i < pn;)
		{
			// Set position where this instruction starts
			bp[bpa++] = i;
			
			// Get instruction size here
			int sz = __ByteCodeSizes__.__sizeOf(i, __code);
			
			// Negative or zero size?
			if (sz <= 0)
				throw new RuntimeException("WTFX");
			
			// Go to next instruction
			i += sz;
		}
		
		// The byte code for this method entirely uses single byte instructions
		// so no condensation is needed
		if (bpa == pn)
			_ipos = bp;
		
		// Otherwise, condense
		else
		{
			// Setup array
			int[] actbp = new int[bpa];
			
			// Copy into it
			for (int i = 0; i < bpa; i++)
				actbp[i] = bp[i];
			
			// Use this array instead
			_ipos = actbp;
		}
		
		// Not needed
		bp = null;
		
		// The control flow graph needs to be determined. Since most
		// instructions naturally flow to the next instruction, this has to be
		// handled initially so that instructions which follow non-natural
		// program flow sources have no jump sources set.
		Map<Integer, List<VMCJumpSource>> xj = new HashMap<>();
		int n = size();
		for (int i = 0; i < n; i++)
		{
			// Get the operation here
			VMCOp op = get(i);
			int ik = op.instructionCode();
			
			// No instruction following these has a naturally implicit jump
			// source from the previous instruction
			if ((i + 1 < n) && (i == 0 || 
				ik == VMCInstructionIDs.ARETURN ||
				ik == VMCInstructionIDs.ATHROW ||
				ik == VMCInstructionIDs.DRETURN ||
				ik == VMCInstructionIDs.FRETURN ||
				ik == VMCInstructionIDs.GOTO ||
				ik == VMCInstructionIDs.GOTO_W ||
				ik == VMCInstructionIDs.IRETURN ||
				ik == VMCInstructionIDs.LOOKUPSWITCH ||
				ik == VMCInstructionIDs.LRETURN ||
				ik == VMCInstructionIDs.RETURN ||
				ik == VMCInstructionIDs.TABLESWITCH))
				xj.put(i + 1, new ArrayList<VMCJumpSource>());
		}
		
		// Now go through the program again and add any jump sources which
		// are specified in the byte code via conditions and such.
		for (int i = 0; i < n; i++)
		{
			// Get the operation here
			VMCOp op = get(i);
			
			// Get the jump targets for the operation
			List<VMCJumpTarget> jts = op.jumpTargets();
			int nj = jts.size();
			
			// No target jumps are made, this instruction ends execution
			if (nj <= 0)
				continue;
			
			// If only a natural jump is performed then do not perform any work
			// to jump sources. This is performed later for each target when
			// there are actually other jump targets to consider. Otherwise
			// every instruction will end up getting an explicit jump source
			// when it is not truly needed, unless exceptions are being used.
			else if (nj == 1 && jts.get(0).getType() == VMCJumpType.NATURAL)
				continue;
			
			// Add source target jumps to the destination operations
			for (VMCJumpTarget jt : jts)
			{
				// Get target address
				int addr = jt.address();
				
				// Get potential explicit source for a target
				List<VMCJumpSource> srcs = xj.get(addr);
				
				// If it does not exist, add it
				if (srcs == null)
				{
					// Setup new list
					srcs = new ArrayList<>();
					
					// Add an implicit from the previous instruction because
					// this would not have been formerly hit when that code was
					// ran previously
					// But only if it is not zero
					if (addr != 0)
						srcs.add(new VMCJumpSource(this, VMCJumpType.NATURAL,
							addr - 1));
					
					// Add it
					xj.put(addr, srcs);
				}
				
				// Is there a natural jump point in this?
				boolean hasnat = false;
				for (VMCJumpTarget jtb : jts)
					if (jtb.getType() == VMCJumpType.NATURAL)
					{
						hasnat |= true;
						break;
					}
				
				// Get the current jump type
				VMCJumpType mjt = jt.getType();
				
				// If the jump type is natural and the list already has one
				// then do not add it
				if (hasnat && mjt == VMCJumpType.NATURAL)
					continue;
				
				// Add jump with all details
				srcs.add(new VMCJumpSource(this, mjt, i));
			}
		}
		
		// Lock in the explicit jump map
		for (Map.Entry<Integer, List<VMCJumpSource>> e : xj.entrySet())
			e.setValue(MissingCollections.<VMCJumpSource>unmodifiableList(
				e.getValue()));
		_expjumps = MissingCollections.<Integer, List<VMCJumpSource>>
			unmodifiableMap(xj);
		
		// Setup the initial program state based on the method descriptor.
		VMCVariableStates entrystate = new VMCVariableStates(this, 0, false);
		_entrystate = entrystate;
		
		// If this is an instance method then the first argument is this.
		int spot = 0;
		if (__ins)
			entrystate.get(spot++).__setType(JVMVariableType.OBJECT);
		
		// Parse
		int na = __desc.argumentCount();
		for (int i = 0; i < na; i++)
		{
			// Get argument and its type
			FieldSymbol arg = __desc.get(i);
			JVMVariableType vt = JVMVariableType.bySymbol(arg);
			
			// Set it here
			entrystate.get(spot++).__setType(vt);
			
			// If wide, add TOP
			if (vt.isWide())
				entrystate.get(spot++).__setType(JVMVariableType.TOP);
		}
	}
	
	/**
	 * Returns {@code true} if there are no exceptions to be caught at all.
	 *
	 * This is used so that some operations which handle exceptions can be
	 * done faster.
	 *
	 * @return {@code true} if this method catches no exceptions.
	 * @since 2016/03/31
	 */
	public boolean areNoExceptions()
	{
		synchronized (lock)
		{
			return _noexceptions;
		}
	}
	
	/**
	 * Returns the entry verification state of the program.
	 *
	 * @return The entry verification state.
	 * @throws IllegalStateException If entry state was specified.
	 * @since 2016/03/31
	 */
	public VMCVerifyState entryVerificationState()
		throws IllegalStateException
	{
		// Lock
		synchronized (lock)
		{
			// Get entry state
			VMCVariableStates entrystate = _entrystate;
			if (entrystate == null)
				throw new IllegalStateException("IN2i");
			
			throw new Error("TODO");
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/30
	 */
	@Override
	public VMCOp get(int __i)
		throws IndexOutOfBoundsException
	{
		// Check
		if (__i < 0 || __i >= size())
			throw new IndexOutOfBoundsException(String.format("IOOB %d", __i));
		
		// lock
		synchronized (lock)
		{
			// The keyed integer
			Integer key = Integer.valueOf(__i);
			
			// Obtain reference
			Map<VMCOp, Reference<VMCOp>> cache = _opcache;
			Reference<VMCOp> ref = cache.get(key);
			VMCOp rv;
			
			// Needs caching?
			if (ref == null || null == (rv = ref.get()))
			{
				// Set it up, since the value is both a key and a value
				rv = new VMCOp(this, __i);
				
				// Store into the map
				cache.put(rv, new WeakReference<>(rv));
			}
			
			// Return it
			return rv;
		}
	}
	
	/**
	 * Converts a logical instruction address to a physical one.
	 *
	 * @param __log The logical instruction position.
	 * @return The physical position of the instruction.
	 * @since 2016/03/30
	 */
	public int logicalToPhysical(int __log)
	{
		if (__log < 0 || __log >= size())
			return -1;
		return _ipos[__log];
	}
	
	/**
	 * Returns the maximum number of local variables.
	 *
	 * @return The local variable count.
	 * @since 2016/03/31
	 */
	public int maxLocals()
	{
		return _maxlocals;
	}
	
	/**
	 * Returns the maximum number of stack variables.
	 *
	 * @return The maximum size of the stack.
	 * @since 2016/03/31
	 */
	public int maxStack()
	{
		return _maxstack;
	}
	
	/**
	 * Converts a physical address to a logical one.
	 *
	 * @param __phy The physical address to convert.
	 * @return The logical address from the given physical address or
	 * {@code -1} if no logical address is associated with one.
	 * @since 2016/03/30
	 */
	public int physicalToLogical(int __phy)
	{
		return Math.max(-1, Arrays.binarySearch(_ipos, __phy));
	}
	
	/**
	 * Sets that the method catches no exceptions and exception handling is not
	 * to be done at all in this method.
	 *
	 * @return {@code this}.
	 * @throws IllegalStateException If exceptions were already set.
	 * @since 2016/03/31
	 */
	public VMCProgram setNoExceptions()
		throws IllegalStateException
	{
		// Lock
		synchronized (lock)
		{
			// Check
			if (_exceptionsset)
				throw new IllegalStateException("IN2h");
			
			// Set both
			_exceptionsset = true;
			_noexceptions = true;
		}
		
		// Self
		return this;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/30
	 */
	@Override
	public int size()
	{
		return _ipos.length;
	}
	
	/**
	 * Returns {@code true} if the exceptions have been set.
	 *
	 * This is used by the operation target and source jump caches to determine
	 * if they should initially be cached or not.
	 *
	 * @return {@code true} if exceptions were set.
	 * @since 2016/03/31
	 */
	boolean __areExceptionsSet()
	{
		synchronized (lock)
		{
			return _exceptionsset;
		}
	}
}

