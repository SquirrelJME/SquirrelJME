// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classprogram;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import net.multiphasicapps.classfile.CFConstantPool;
import net.multiphasicapps.classfile.CFMethod;
import net.multiphasicapps.collections.MissingCollections;
import net.multiphasicapps.descriptors.FieldSymbol;
import net.multiphasicapps.descriptors.MethodSymbol;

/**
 * This represents a single operation in the byte code chain.
 *
 * @since 2016/03/30
 */
public class CPOp
	implements Comparable<CPOp>
{
	/** Virtual machine workers. */
	private static final __VMWorkers__ _VMWORKERS =
		new __VMWorkers__();
	
	/** The nullary compute machine (does nothing). */
	private static volatile Reference<CPComputeMachine<Object>>
		_NULL_CM;
	
	/** Lock. */
	protected final Object lock =
		new Object();
	
	/** The program to use. */
	protected final CPProgram program;
	
	/** The logical position of this instruction. */
	protected final int logicaladdress;
	
	/** The physical address of this instruction. */
	protected final int physicaladdress;
	
	/** The opcode used. */
	protected final int opcode;
	
	/** Exceptions that must get handled. */
	protected final List<CPException> exceptions;
	
	/** Logical operations this handles exceptions for. */
	protected final List<CPOp> handles;
	
	/** Natural and conditional jump targets. */
	protected final List<CPOp> jumptargets;
	
	/** Explicit variable states? */
	protected final CPVariables expvars;
	
	/** Actual jump sources. */
	private volatile CPOp[] _realjumpsources;
	
	/** Jump source list. */
	private volatile Reference<List<CPOp>> _jumpsources;
	
	/** String representation of this operation. */
	private volatile Reference<String> _string;
	
	/** Recursive computation detection. */
	private volatile int _recursivity;
	
	/** Calculation counter. */
	volatile int _calccount;
	
	/**
	 * Initializes the operation data.
	 *
	 * @param __prg The owning program.
	 * @param __code The raw byte code array.
	 * @param __exs The list of exceptions in the program.
	 * @param __vmap Verification map.
	 * @param __ops The operations in the program in the event that recursive
	 * future initialization is required.
	 * @param __lognum The logical ID of this instruction.
	 * @param __method The method this is an operation for.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/10
	 */
	CPOp(CPProgram __prg, byte[] __code, CPException[] __exs,
		Map<Integer, CPVerifyState> __vmap, CPOp[] __ops, int __lognum,
		CFMethod __method)
		throws NullPointerException
	{
		// Check
		if (__prg == null || __code == null || __exs == null ||
			__vmap == null || __ops == null || __method == null)
			throw new NullPointerException("NARG");
		
		// Instruction in the array becomes this
		if (__ops[__lognum] == null)
			__ops[__lognum] = this;
		
		System.err.printf("DEBUG -- Init %d%n", __lognum);
		
		// Set
		program = __prg;
		logicaladdress = __lognum;
		physicaladdress = __prg.logicalToPhysical(__lognum);
		
		// Create explicit varaible state?
		CPVerifyState xpvs = __vmap.get(__lognum);
		if (__lognum == 0)
			expvars = new CPVariables(this, __method);
		
		// Defined by the StackMap/StackMapTable
		else if (xpvs != null)
			expvars = new CPVariables(this, xpvs);
		
		// None, fully implicit
		else
			expvars = new CPVariables(this);
		
		// Determine the used opcode, handle wide operations also.
		int rawoc = ((int)__code[physicaladdress]) & 0xFF;
		if (rawoc == CPOpcodes.WIDE)
			rawoc = (rawoc << 8) | (((int)__code[physicaladdress + 1]) & 0xFF);
		opcode = rawoc;
		
		// Calculate jump targets for this instruction
		int[] jts = __JumpTargetCalc__.__calculate(opcode, __code,
			physicaladdress);
		int jtn = jts.length;
		CPOp[] destjts = new CPOp[jtn];
		for (int i = 0; i < jtn; i++)
		{
			// Get physical and its logical address
			int jphy = jts[i];
			int jlog = (jphy == Integer.MIN_VALUE ? logicaladdress + 1 :
				program.physicalToLogical(jphy));
			
			// {@squirreljme.error CP0o Logical instruction has a jump to
			// an instruction which is not within the program bounds or does
			// not point to the start of an instruction. (The current
			// instruction address; The destination physical address of the
			// jump; The current opcode)}
			if (jlog < 0)
				throw new CPProgramException(String.format("CP0o %d %d %d",
					logicaladdress, jphy, opcode));
			
			// Potentially create it
			CPOp xop = __ops[jlog];
			if (xop == null)
				__ops[jlog] =
					(xop = new CPOp(__prg, __code, __exs, __vmap, __ops,
						jlog, __method));
			
			// Set it
			destjts[i] = xop;
			if (xop == null)
				throw new RuntimeException(String.format("WTFX %d %d",
					logicaladdress, jlog));
			
			// Need to append jump source?
			CPOp[] xrjs = xop._realjumpsources;
			boolean xdonothing = false;
			if (xrjs != null)
				for (CPOp xsop : xrjs)
					if (xsop == this)
					{
						xdonothing = true;
						break;
					}
			
			// Will add it
			if (!xdonothing)
			{
				// Initial empty array
				if (xrjs == null)
					xop._realjumpsources = (xrjs = new CPOp[1]);
				
				// Increase size
				else
					xop._realjumpsources = (xrjs = Arrays.<CPOp>copyOf(xrjs,
						xrjs.length + 1));
				
				// Add to the end
				xrjs[xrjs.length - 1] = this;
			}
		}
		jumptargets = MissingCollections.<CPOp>unmodifiableList(
			Arrays.<CPOp>asList(destjts));
		
		// Setup exceptions that there are handlers here for and any exceptions
		// that this instruction handles (point to those instructions)
		List<CPException> dxx = new ArrayList<>();
		Set<CPOp> hx = new LinkedHashSet<>();
		for (CPException rx : __exs)
		{
			// Get addresses (all inclusive)
			int spc = rx.startPC();
			int epc = rx.endPC();
			int hpc = rx.handlerPC();
			
			// This instruction has an exception handler available
			if (__lognum >= spc && __lognum <= epc)
				dxx.add(rx);
			
			// Handles an exception for an address
			// Go through the start and end addresses
			if (__lognum == hpc)
				for (int xpc = spc; xpc <= epc; xpc++)
				{
					// Get operation here
					CPOp xop = __ops[xpc];
			
					// If missing, it requires initialization
					if (xop == null)
						__ops[xpc] =
							(xop = new CPOp(__prg, __code, __exs, __vmap,
								__ops, xpc, __method));
					
					// Add that instruction
					hx.add(xop);
				}
		}
		exceptions = MissingCollections.<CPException>unmodifiableList(dxx);
		handles = MissingCollections.<CPOp>unmodifiableList(
			new ArrayList<>(hx));
	}
	
	/**
	 * Returns the address of this instruction.
	 *
	 * @return The instruction address.
	 * @since 2016/04/10
	 */
	public int address()
	{
		return logicaladdress;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/10
	 */
	@Override
	public int compareTo(CPOp __o)
	{
		int ml = logicaladdress;
		int ol = __o.logicaladdress;
		if (ml < ol)
			return -1;
		else if (ml > ol)
			return 1;
		return 0;
	}
	
	/**
	 * Performs computations on the current instruction.
	 *
	 * @param <A> The type of value to pass.
	 * @param __cm The computational machine to compute with.
	 * @param __a The value to pass.
	 * @return {@code this}.
	 * @throws NullPointerException If no compute machine was specified.
	 * @since 2016/04/10
	 */
	public <A> CPOp compute(CPComputeMachine<A> __cm, A __a)
		throws NullPointerException
	{
		// Check
		if (__cm == null)
			throw new NullPointerException("NARG");
		
		// Lock
		synchronized (lock)
		{
			// Detects recursion
			try
			{
				// Increase recursion count
				if ((++_recursivity) < 0)
					throw new RuntimeException("WTFX");
				
				// Lookup operation worker
				__VMWorkers__.__Worker__ worker = _VMWORKERS.__lookup(opcode);
				
				// Run computation on it
				try
				{
					// Not valid?
					if (worker == null)
						throw new __VMWorkers__.__UnknownOp__();
			
					// Compute it
					worker.compute(__cm, __a, this);
				}
		
				// Unknown
				catch (__VMWorkers__.__UnknownOp__ e)
				{
					// {@squirreljme.error CP0m Method contains an illegal
					// opcode. (The current logical address; The instruction
					// opcode})
					throw new CPProgramException(String.format("CP0m %d %d",
						logicaladdress, opcode), e);
				}
			}
			
			// Reduce recursion amount
			finally
			{
				// Underflows
				if ((--_recursivity) < 0)
					throw new RuntimeException("WTFX");
			}
		}
		
		// Self
		return this;
	}
	
	/**
	 * Returns the exceptions that this instruction handles.
	 *
	 * @since 2016/04/10
	 */
	public List<CPException> exceptions()
	{
		return exceptions;
	}
	
	/**
	 * Returns the list of operations which contain exception handlers for this
	 * operation.
	 *
	 * @return The list of handling exceptions.
	 * @since 2016/04/10
	 */
	public List<CPOp> exceptionsHandled()
	{
		return handles;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/10
	 */
	@Override
	public int hashCode()
	{
		return logicaladdress;
	}
	
	/**
	 * Returns the instruction identifier.
	 *
	 * @return The instruction identifier.
	 * @since 2016/04/10
	 */
	public int instructionCode()
	{
		return opcode;
	}
	
	/**
	 * The instructions which jump to this instruction. This does not include
	 * exceptions.
	 *
	 * @return The list of source operations.
	 * @since 2016/04/10
	 */
	public List<CPOp> jumpSources()
	{
		// Get reference
		Reference<List<CPOp>> ref = _jumpsources;
		List<CPOp> rv;
		
		// Needs to be cached?
		if (ref == null || null == (rv = ref.get()))
		{
			// Get real sources
			CPOp[] rjs = _realjumpsources;
			
			// There are none
			if (rjs == null || rjs.length <= 0)
				_jumpsources = new WeakReference<>(
					(rv = MissingCollections.<CPOp>emptyList()));
			
			// Wrap them
			else
				_jumpsources = new WeakReference<>(
					(rv = MissingCollections.<CPOp>unmodifiableList(
						Arrays.<CPOp>asList(rjs))));
		}
		
		// Return it
		return rv;
	}
	
	/**
	 * The instructions this jumps to naturally or conditionally. This does
	 * not include exception handler.
	 *
	 * @return The list of target instructions.
	 * @since 2016/04/10
	 */
	public List<CPOp> jumpTargets()
	{
		return jumptargets;
	}
	
	/**
	 * Returns the physical address of this instruction.
	 *
	 * @return The physical address.
	 * @since 2016/04/10
	 */
	public int physicalAddress()
	{
		return physicaladdress;
	}
	
	/**
	 * Returns the program which contains this operation.
	 *
	 * @return The containing program.
	 * @since 2016/04/11
	 */
	public CPProgram program()
	{
		return program;
	}
	
	/**
	 * Returns the variable states.
	 *
	 * @return The state of variables for this operation.
	 * @since 2016/04/10
	 */
	public CPVariables variables()
	{
		// Explicit variable states always exist
		return expvars;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/10
	 */
	@Override
	public String toString()
	{
		// Get reference
		Reference<String> ref = _string;
		String rv;
		
		// Needs creation?
		if (ref == null || null == (rv = ref.get()))
		{
			StringBuilder sb = new StringBuilder("{");
			
			// The logical position
			sb.append('@');
			sb.append(logicaladdress);
			
			// The physical address
			sb.append('(');
			sb.append(physicaladdress);
			sb.append(')');
			
			// The opcode
			sb.append(", oc=");
			sb.append(opcode);
			
			// Exception handlers
			if (!exceptions.isEmpty())
			{
				sb.append(", eh=");
				sb.append(exceptions);
			}
			
			// Exceptions that this handles
			if (!handles.isEmpty())
			{
				sb.append(", hi=");
				sb.append('[');
				boolean comma = false;
				for (CPOp xop : handles)
				{
					if (comma)
						sb.append(", ");
					comma = true;
					sb.append(xop.logicaladdress);
				}
				sb.append(']');
			}
			
			// Jump targets?
			if (!jumptargets.isEmpty())
			{
				sb.append(", jt=");
				sb.append('[');
				boolean comma = false;
				for (CPOp xop : jumptargets)
				{
					if (comma)
						sb.append(", ");
					comma = true;
					sb.append(xop.logicaladdress);
				}
				sb.append(']');
			}
			
			// Jump sources?
			List<CPOp> jss = jumpSources();
			if (!jss.isEmpty())
			{
				sb.append(", js=");
				sb.append('[');
				boolean comma = false;
				for (CPOp xop : jss)
				{
					if (comma)
						sb.append(", ");
					comma = true;
					sb.append(xop.logicaladdress);
				}
				sb.append(']');
			}
			
			// Variable states
			sb.append(", vs=");
			sb.append(variables());
			
			// Finish
			sb.append('}');
			_string = new WeakReference<>((rv = sb.toString()));
		}
		
		// Return it
		return rv;
	}
	
	/**
	 * Computes this operation which performs the executional work and possibly
	 * initializes the state of variables.
	 *
	 * @return {@code this}.
	 * @since 2016/04/11
	 */
	CPOp __compute()
	{
		return this.<Object>compute(__nullComputer(), null);
	}
	
	/**
	 * Returns the null machine computer which is used so that the input/output
	 * variable states can be computing without performing any code generation.
	 *
	 * @return The cached null computer.
	 * @since 2016/04/09
	 */
	private static CPComputeMachine<Object> __nullComputer()
	{
		// Get reference
		Reference<CPComputeMachine<Object>> ref = _NULL_CM;
		CPComputeMachine<Object> rv;
		
		// Needs caching?
		if (ref == null || null == (rv = ref.get()))
			_NULL_CM = new WeakReference<>(
				(rv = new __NullComputeMachine__()));
		
		// Return it
		return rv;
	}
	
	/**
	 * Reads a signed integer from the code array.
	 *
	 * @param __off The offset from the instruction address.
	 * @return The read value.
	 * @since 2016/04/13
	 */
	int __readSInt(int __off)
	{
		return program.__readSInt(physicaladdress + __off);
	}
	
	/**
	 * Reads a signed short from the code array.
	 *
	 * @param __off The offset from the instruction address.
	 * @return The signed short value which was read.
	 * @since 2016/03/30
	 */
	int __readSShort(int __off)
	{
		return program.__readSShort(physicaladdress + __off);
	}
	
	/**
	 * Reads an unsigned short from the code array.
	 *
	 * @param __off The offset from the instruction address.
	 * @return The read value.
	 * @since 2016/03/30
	 */
	int __readUShort(int __off)
	{
		return program.__readUShort(physicaladdress + __off);
	}
}

