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
	
	/** Instruction jump targets. */
	protected final List<CPJumpTarget> jumptargets;
	
	/** Exceptions that must get handled. */
	protected final List<CPException> exceptions;
	
	/** Logical operations this handles exceptions for. */
	protected final List<CPOp> handles;
	
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
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/10
	 */
	CPOp(CPProgram __prg, byte[] __code, List<CPRawException> __exs,
		Map<Integer, CPVerifyState> __vmap, CPOp[] __ops, int __lognum)
		throws NullPointerException
	{
		// Check
		if (__prg == null || __code == null || __exs == null ||
			__vmap == null || __ops == null)
			throw new NullPointerException("NARG");
		
		// Instruction in the array becomes this
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
		
		// Go through all instructions that already exist and check ones where
		// this is an exception handler for
		Set<CPOp> hx = new LinkedHashSet<>();
		int pgn = __ops.length;
		for (int i = 0; i < pgn; i++)
		{
			// Get operation here
			CPOp xop = __ops[i];
			
			// If missing, it requires initialization
			if (xop == null)
				__ops[i] =
					(xop = new CPOp(__prg, __code, __exs, __vmap, __ops, i));
			
			// Go through that instruction's exception handlers
			// If this is a handler for that exception then add it
			for (CPException ex : xop.exceptions)
				if (ex.handlerPC() == __lognum)
					hx.add(xop);
		}
		handles = MissingCollections.<CPOp>unmodifiableList(
			new ArrayList<>(hx));
		
		jumptargets = null;
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
	 * Returns the list of jump targets (the instructions this one jumps to).
	 *
	 * @return The list of jump targets.
	 * @since 2016/04/10
	 */
	public List<CPJumpTarget> jumpTargets()
	{
		return jumptargets;
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

