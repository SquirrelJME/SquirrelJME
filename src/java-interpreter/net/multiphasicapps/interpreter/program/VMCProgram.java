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
	extends AbstractList<VMCProgram.Op>
{
	/** Lock. */
	protected final Object lock =
		new Object();
	
	/** The code length. */
	private final int length;
	
	/** The buffer containing the raw byte code. */
	private final byte[] _code;
	
	/** The position of each logical instruction to a physical one. */
	private final int[] _ipos;
	
	/** The operation cache. */
	private final Map<Op, Reference<Op>> _opcache =
		new WeakHashMap<>();
	
	/** Jump sources in the program which are explicit and not implicit. */
	private final Map<Integer, List<VMCJumpSource>> _expjumps;
	
	/** Initial variable state. */
	private final VMCVariableStates _entrystate;
	
	/** Maximum local variables. */
	final int _maxlocals;
	
	/** Maximum stack variables. */
	final int _maxstack;
	
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
			Op op = get(i);
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
			Op op = get(i);
			
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
			throw new Error("TODO");
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
			throw new Error("TODO");
		
		// Parse
		int na = __desc.argumentCount();
		for (int i = 0; i < na; i++)
		{
			// Get argument
			FieldSymbol arg = __desc.get(i);
			
			throw new Error("TODO");
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/30
	 */
	@Override
	public Op get(int __i)
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
			Map<Op, Reference<Op>> cache = _opcache;
			Reference<Op> ref = cache.get(key);
			Op rv;
			
			// Needs caching?
			if (ref == null || null == (rv = ref.get()))
			{
				// Set it up, since the value is both a key and a value
				rv = new Op(__i);
				
				// Store into the map
				cache.put(rv, new WeakReference<>(rv));
			}
			
			// Return it
			return rv;
		}
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
	 * {@inheritDoc}
	 * @since 2016/03/30
	 */
	@Override
	public int size()
	{
		return _ipos.length;
	}
	
	/**
	 * This represents a single operation in the byte code chain.
	 *
	 * @since 2016/03/30
	 */
	public class Op
	{
		/** The logical address of this operation. */
		protected final int logical;
		
		/** The physical address of this operation. */
		protected final int physical;
		
		/** Jump sources (if implicit). */
		private volatile Reference<List<VMCJumpSource>> _ijsrc;
		
		/** Jump targets (always implicit). */
		private volatile Reference<List<VMCJumpTarget>> _ijtar;
		
		/** Input state cache. */
		private volatile Reference<VMCVariableStates> _vsinput;
		
		/** Output state cache. */
		private volatile Reference<VMCVariableStates> _vsoutput;
		
		/**
		 * Initializes the operation.
		 *
		 * @param __pc The operation address.
		 * @since 2016/03/30
		 */
		private Op(int __pc)
		{
			logical = __pc;
			physical = _ipos[logical];
		}
		
		/**
		 * Returns the address of this operation.
		 *
		 * @return The operation address.
		 * @since 2016/03/30
		 */
		public int address()
		{
			return logical;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/03/30
		 */
		@Override
		public boolean equals(Object __o)
		{
			// If an integer, compare against the address
			if (__o instanceof Integer)
				return (logical == ((Integer)__o).intValue());
			
			// Otherwise must be this
			return this == __o;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/03/30
		 */
		@Override
		public int hashCode()
		{
			// Just use the address
			return logical;
		}
		
		/**
		 * Returns the state of the instruction when it is first entered which
		 * acts as an input state to the operation it performs.
		 *
		 * Note that the input state of an instruction (except for the first
		 * one) may be the output of the previous operation, unless there are
		 * multiple entry points for the operation in which the intersection
		 * will be calculated if a verification state exists. If a verification
		 * state does not exist, then all previous outputs must carry the same
		 * type and value information.
		 *
		 * @return The input state.
		 * @since 2016/03/30
		 */
		public VMCVariableStates inputState()
		{
			// If this is the first instruction then return the entry state
			// of the method
			if (logical == 0)
				return _entrystate;
			
			// Otherwise cache it
			throw new Error("TODO");
		}
		
		/**
		 * Reads the instruction specified code that this operation performs.
		 *
		 * @return The instruction ID of this operation.
		 * @since 2016/03/30
		 */
		public int instructionCode()
		{
			// Read code here
			int bc = ((int)_code[physical]) & 0xFF;
			
			// If wide, read some more bytes
			if (bc == VMCInstructionIDs.WIDE)
				return (bc << 8) | (((int)_code[physical + 1]) & 0xFF);
			
			// Otherwise return the code
			return bc;
		}
		
		/**
		 * Returns the jump sources for this instruction, a list of addresses
		 * which jump to this given instruction.
		 *
		 * @return The list of jump sources, the list cannot be modified.
		 * @since 2016/03/30
		 */
		public List<VMCJumpSource> jumpSources()
		{
			// Explicit jump sources?
			List<VMCJumpSource> rv = _expjumps.get(logical);
			if (rv != null)
				return rv;
			
			throw new Error("TODO");
		}
		
		/**
		 * Returns the jump targets for the current instruction.
		 *
		 * @return The list of jump targets.
		 * @since 2016/03/30
		 */
		public List<VMCJumpTarget> jumpTargets()
		{
			// Cached?
			Reference<List<VMCJumpTarget>> ref = _ijtar;
			List<VMCJumpTarget> rv;
			
			// Needs creation?
			if (ref == null || null == (rv = ref.get()))
			{
				// Get the instruction code
				int ik = instructionCode();
				
				// The following have no jump targets
				if (ik == VMCInstructionIDs.ARETURN ||
					ik == VMCInstructionIDs.ATHROW ||
					ik == VMCInstructionIDs.DRETURN ||
					ik == VMCInstructionIDs.FRETURN ||
					ik == VMCInstructionIDs.IRETURN ||
					ik == VMCInstructionIDs.LRETURN ||
					ik == VMCInstructionIDs.RETURN)
					rv = MissingCollections.<VMCJumpTarget>unmodifiableList(
						Arrays.<VMCJumpTarget>asList());
				
				// Lookup switch
				else if (ik == VMCInstructionIDs.LOOKUPSWITCH)
					throw new Error("TODO");
				
				// Table switch
				else if (ik == VMCInstructionIDs.TABLESWITCH)
					throw new Error("TODO");
				
				// An expilcit address
				else if (ik == VMCInstructionIDs.GOTO ||
					ik == VMCInstructionIDs.GOTO_W)
					throw new Error("TODO");
				
				// Conditional to a given instruction or if false, the next
				// instruction.
				else if (ik == VMCInstructionIDs.IF_ACMPEQ ||
					ik == VMCInstructionIDs.IF_ACMPNE ||
					ik == VMCInstructionIDs.IFEQ ||
					ik == VMCInstructionIDs.IFGE ||
					ik == VMCInstructionIDs.IFGT ||
					ik == VMCInstructionIDs.IF_ICMPEQ ||
					ik == VMCInstructionIDs.IF_ICMPGE ||
					ik == VMCInstructionIDs.IF_ICMPGT ||
					ik == VMCInstructionIDs.IF_ICMPLE ||
					ik == VMCInstructionIDs.IF_ICMPLT ||
					ik == VMCInstructionIDs.IF_ICMPNE ||
					ik == VMCInstructionIDs.IFLE ||
					ik == VMCInstructionIDs.IFLT ||
					ik == VMCInstructionIDs.IFNE ||
					ik == VMCInstructionIDs.IFNONNULL ||
					ik == VMCInstructionIDs.IFNULL)
					rv = MissingCollections.<VMCJumpTarget>unmodifiableList(
						Arrays.<VMCJumpTarget>asList(new VMCJumpTarget(
								VMCProgram.this, VMCJumpType.NATURAL,
								logical + 1),
							new VMCJumpTarget(VMCProgram.this,
								VMCJumpType.CONDITIONAL,
								physicalToLogical(__ByteUtils__.__readUShort(
									_code, physical + 1)))));
				
				// Implicit next instruction
				else
					rv = MissingCollections.<VMCJumpTarget>unmodifiableList(
						Arrays.<VMCJumpTarget>asList(new VMCJumpTarget(
							VMCProgram.this, VMCJumpType.NATURAL,
							logical + 1)));
				
				// Cache it
				_ijtar = new WeakReference<>(rv);
			}
			
			// Return it
			return rv;
		}
		
		/**
		 * Returns the next operation.
		 *
		 * @return The next operation or {@code null} if this is the last.
		 * @since 2016/03/30
		 */
		public Op next()
		{
			if (logical >= (_ipos.length - 1))
				return null;
			return VMCProgram.this.get(logical + 1);
		}
		
		/**
		 * Returns the output state of the current address after the operation
		 * it performs has been performed.
		 *
		 * @return The operation output state.
		 * @since 2016/03/30
		 */
		public VMCVariableStates outputState()
		{
			throw new Error("TODO");
		}
		
		/**
		 * Returns the physical address of this operation.
		 *
		 * @return The physical address.
		 * @since 2016/03/30
		 */
		public int physicalAddress()
		{
			return physical;
		}
		
		/**
		 * Returns the previous operation.
		 *
		 * @return The previous operation or {@code null} if this is the first.
		 * @since 2016/03/30
		 */
		public Op previous()
		{
			if (logical <= 0)
				return null;
			return VMCProgram.this.get(logical - 1);
		}
	}
}

