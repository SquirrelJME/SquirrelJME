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
import net.multiphasicapps.collections.MissingCollections;
import net.multiphasicapps.descriptors.FieldSymbol;
import net.multiphasicapps.descriptors.MethodSymbol;

/**
 * This represents a single operation in the byte code chain.
 *
 * @since 2016/03/30
 */
public class CPOp
{
	/** Virtual machine workers. */
	private static final __VMWorkers__ _VMWORKERS =
		new __VMWorkers__();
	
	/** The nullary compute machine (does nothing). */
	private static volatile Reference<CPComputeMachine<Object>>
		_NULL_CM;
	
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
	
	/** Natural and conditional Jump sources. */
	protected final List<CPOp> jumpsources;
	
	/** String representation of this operation. */
	private volatile Reference<String> _string;
	
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
	 * @param __tjs Temporary jump source map.
	 * @throws NullPointerException On null arguments, except for
	 * {@code __tjs}.
	 * @since 2016/04/10
	 */
	CPOp(CPProgram __prg, byte[] __code, List<CPRawException> __exs,
		Map<Integer, CPVerifyState> __vmap, CPOp[] __ops, int __lognum,
		Map<CPOp, Set<CPOp>> __tjs)
		throws NullPointerException
	{
		// Check
		if (__prg == null || __code == null || __exs == null ||
			__vmap == null || __ops == null)
			throw new NullPointerException("NARG");
		
		// If missing, setup the jump source map
		if (__tjs == null)
			__tjs = new HashMap<>();
		
		// Instruction in the array becomes this
		if (__ops[__lognum] == null)
			__ops[__lognum] = this;
		
		// Set
		program = __prg;
		logicaladdress = __lognum;
		physicaladdress = __prg.logicalToPhysical(__lognum);
		
		// Determine the used opcode, handle wide operations also.
		int rawoc = ((int)__code[physicaladdress]) & 0xFF;
		if (rawoc == CPOpcodes.WIDE)
			rawoc = (rawoc << 8) | (((int)__code[physicaladdress + 1]) & 0xFF);
		opcode = rawoc;
		
		// Setup exceptions
		int n = __exs.size();
		CPException[] rxe = new CPException[n];
		for (int i = 0; i < n; i++)
			rxe[i] = __exs.get(i).__create(program);
		exceptions = MissingCollections.<CPException>unmodifiableList(
			Arrays.<CPException>asList(rxe));
		
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
						jlog, __tjs));
			
			// Set it
			destjts[i] = xop;
			if (xop == null)
				throw new RuntimeException(String.format("WTFX %d %d",
					logicaladdress, jlog));
			
			// Add destination jump source
			Set<CPOp> vvjs = __tjs.get(xop);
			if (vvjs == null)
			{
				vvjs = new HashSet<>();
				__tjs.put(xop, vvjs);
			}
			vvjs.add(this);
		}
		jumptargets = MissingCollections.<CPOp>unmodifiableList(
			Arrays.<CPOp>asList(destjts));
		
		// Go through all instructions that already exist and check ones where
		// this is an exception handler for
		Set<CPOp> hx = new LinkedHashSet<>();
		Set<CPOp> js = new LinkedHashSet<>();
		int pgn = __ops.length;
		for (int i = 0; i < pgn; i++)
		{
			// Get operation here
			CPOp xop = __ops[i];
			
			// If missing, it requires initialization
			if (xop == null)
				__ops[i] =
					(xop = new CPOp(__prg, __code, __exs, __vmap, __ops, i,
						__tjs));
			
			// Go through that instruction's exception handlers
			// If this is a handler for that exception then add it
			for (CPException ex : xop.exceptions)
				if (ex.handlerPC() == __lognum)
					hx.add(xop);
		}
		handles = MissingCollections.<CPOp>unmodifiableList(
			new ArrayList<>(hx));
		
		// Setup jump sources
		Set<CPOp> vvjs = __tjs.get(this);
		if (vvjs == null)
			jumpsources = MissingCollections.<CPOp>emptyList();
		else
			jumpsources = MissingCollections.<CPOp>unmodifiableList(
				new ArrayList<>(vvjs));
		__tjs.put(this, MissingCollections.<CPOp>emptySet());
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
	 * Performs computations on the current instruction.
	 *
	 * @param <A> The type of value to pass.
	 * @param __cm The computational machine to compute with.
	 * @param __a The value to pass.
	 * @return {@code this}.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/10
	 */
	public <A> CPOp compute(CPComputeMachine<A> __cm, A __a)
		throws NullPointerException
	{
		// Check
		if (__a == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
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
		return jumpsources;
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
			
			// Finish
			sb.append('}');
			_string = new WeakReference<>((rv = sb.toString()));
		}
		
		// Return it
		return rv;
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
}

