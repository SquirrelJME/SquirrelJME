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
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
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
	
	/** Operation lock. */
	final Object lock =
		new Object();
	
	/** The program to use. */
	protected final CPProgram program;	
	
	/** The logical address of this operation. */
	protected final int logical;
	
	/** The physical address of this operation. */
	protected final int physical;
	
	/** Jump sources (if implicit). */
	private volatile Reference<List<CPJumpSource>> _ijsrc;
	
	/** Jump targets (always implicit). */
	private volatile Reference<List<CPJumpTarget>> _ijtar;
	
	/** Input state cache. */
	private volatile Reference<CPVariableStates> _vsinput;
	
	/** Output state cache. */
	private volatile Reference<CPVariableStates> _vsoutput;
	
	/** Verification state cache. */
	private volatile Reference<CPVerifyState> _vstate;
	
	/**
	 * Initializes the operation.
	 *
	 * @param __prg The owning program.
	 * @param __pc The operation address.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/30
	 */
	CPOp(CPProgram __prg, int __pc)
		throws NullPointerException
	{
		// Check
		if (__prg == null)
			throw new NullPointerException("NARG");
		
		program = __prg;
		logical = __pc;
		physical = program.logicalToPhysical(logical);
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
	 * Computes this instruction and has it call the computational machine so
	 * that interpretation or compilation may be performed.
	 *
	 * @param <A> The first type to pass.
	 * @param <B> The second type to pass.
	 * @param __cm The compute machine.
	 * @param __a The first value to pass.
	 * @param __b The second value to pass.
	 * @since 2016/04/08
	 */
	public <A, B> void compute(CPComputeMachine<A, B> __cm, A __a, B __b)
	{
		// Obtain the worker for this instruction
		int opcode = instructionCode();
		__VMWorkers__.__Worker__ worker = _VMWORKERS.__lookup(opcode);
		
		// {@squirreljme.error CP0m Method contains an illegal opcode. (The
		// current logical address; The instruction opcode})
		if (worker == null)
			throw new CPProgramException(String.format("CP0m %d %d", logical,
				opcode));
		
		throw new Error("TODO");
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
	public CPVariableStates inputState()
	{
		// If this is the first instruction then return the entry state
		// of the method
		if (logical == 0)
			return program._entrystate;
		
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
		byte[] bc = program._code;
		int rv = ((int)bc[physical]) & 0xFF;
		
		// If wide, read some more bytes
		if (rv == CPOpcodes.WIDE)
			return (rv << 8) | (((int)bc[physical + 1]) & 0xFF);
		
		// Otherwise return the code
		return rv;
	}
	
	/**
	 * Returns the jump sources for this instruction, a list of addresses
	 * which jump to this given instruction.
	 *
	 * @return The list of jump sources, the list cannot be modified.
	 * @since 2016/03/30
	 */
	public List<CPJumpSource> jumpSources()
	{
		// Lock
		synchronized (lock)
		{
			// Have exceptions been set?
			boolean exset = program.__areExceptionsSet();
			
			// Explicit jump sources?
			List<CPJumpSource> exp = program._expjumps.get(logical);
			if ((!exset || program.areNoExceptions()) && exp != null)
				return exp;
			
			throw new Error("TODO");
		}
	}
	
	/**
	 * Returns the jump targets for the current instruction.
	 *
	 * @return The list of jump targets.
	 * @since 2016/03/30
	 */
	public List<CPJumpTarget> jumpTargets()
	{
		// Lock
		synchronized (lock)
		{
			// Have exceptions been set?
			boolean exset = program.__areExceptionsSet();
			
			// Cached?
			Reference<List<CPJumpTarget>> ref = _ijtar;
			List<CPJumpTarget> rv;
		
			// Needs creation?
			if (ref == null || null == (rv = ref.get()))
			{
				// Get the instruction code
				byte[] bc = program._code;
				int ik = instructionCode();
			
				// The following have no jump targets
				if (ik == CPOpcodes.ARETURN ||
					ik == CPOpcodes.ATHROW ||
					ik == CPOpcodes.DRETURN ||
					ik == CPOpcodes.FRETURN ||
					ik == CPOpcodes.IRETURN ||
					ik == CPOpcodes.LRETURN ||
					ik == CPOpcodes.RETURN)
					rv = MissingCollections.<CPJumpTarget>unmodifiableList(
						Arrays.<CPJumpTarget>asList());
			
				// Lookup switch
				else if (ik == CPOpcodes.LOOKUPSWITCH)
				{
					// Align pointer to read the jump values
					int ap = ((physical + 3) & (~3));
				
					// Read the default offset
					int defo = __ByteUtils__.__readSInt(bc, ap);
				
					// Read the pair count
					int np = __ByteUtils__.__readSInt(bc, ap + 4);
				
					// There are a specific number of pairs so an array can
					// be used
					CPJumpTarget[] ojta = new CPJumpTarget[np + 1];
				
					// Setup default first
					ojta[0] = new CPJumpTarget(program,
						CPJumpType.CONDITIONAL,
						program.physicalToLogical(physical + defo));
				
					// Add extra value pairs now
					int lastdx = Integer.MIN_VALUE;
					boolean lastisval = false;
					for (int j = 0; j < np; j++)
					{
						// Read the key here
						int baseoff = ap + 8 + (j * 8);
						int keyv = __ByteUtils__.__readSInt(bc, baseoff);
					
						// It MUST be higher than the last index, that is all
						// entries in the switch are sorted.
						// {@squirreljme.error CP0a Subsequent keys in the
						// {@link lookupswitch} operation must have higher
						// valued keys placed higher so that they may be
						// binary searched. (The physical address)}
						if (keyv < lastdx && lastisval)
							throw new CPProgramException(String.format(
								"CP0a %s", physical));
					
						// Set the last value which is checked to make sure the
						// key is actually higher
						lastdx = keyv;
						lastisval = true;
					
						// Read the offset and calculate the jump
						int joff = __ByteUtils__.__readSInt(bc, baseoff + 4);
						ojta[1 + j] = new CPJumpTarget(program,
							CPJumpType.CONDITIONAL,
							program.physicalToLogical(physical + joff));
					}
				
					// Wrap and lock it in
					rv = MissingCollections.<CPJumpTarget>unmodifiableList(
						Arrays.<CPJumpTarget>asList(ojta));
				}
			
				// Table switch
				else if (ik == CPOpcodes.TABLESWITCH)
				{
					// Align pointer to read the jump values
					int ap = ((physical + 3) & (~3));
					
					// Read the default offset
					int defo = __ByteUtils__.__readSInt(bc, ap);
					
					// Read the low to high values
					int lo = __ByteUtils__.__readSInt(bc, ap + 4);
					int hi = __ByteUtils__.__readSInt(bc, ap + 8);
					
					// The number of items
					int ni = (hi - lo) + 1;
					
					// Setup the target array
					int num = ni + 1;
					CPJumpTarget[] tsjt = new CPJumpTarget[num];
					
					// Default jump
					tsjt[0] = new CPJumpTarget(program,
						CPJumpType.CONDITIONAL,
						program.physicalToLogical(physical + defo));
					
					// Read all the other offsets in
					int baseoff = ap + 12;
					for (int i = 0; i < ni; i++)
						tsjt[1 + i] = new CPJumpTarget(program,
							CPJumpType.CONDITIONAL,
							program.physicalToLogical(physical +
								__ByteUtils__.__readSInt(bc,
									baseoff + (4 * i))));
					
					// Wrap and lock it in
					rv = MissingCollections.<CPJumpTarget>unmodifiableList(
						Arrays.<CPJumpTarget>asList(tsjt));
				}
			
				// Goto a single address (16-bit)
				else if (ik == CPOpcodes.GOTO)
				{
					// Read offset
					int off = __ByteUtils__.__readSShort(bc, physical + 1);
					
					// Only a single set is used
					rv = MissingCollections.<CPJumpTarget>unmodifiableList(
						Arrays.<CPJumpTarget>asList(new CPJumpTarget(
							program, CPJumpType.EXPLICIT,
							program.physicalToLogical(physical + off))));
				}
			
				// Goto a single address (32-bit)
				else if (ik == CPOpcodes.GOTO_W)
				{
					// Read offset
					int off = __ByteUtils__.__readSInt(bc, physical + 1);
					
					// Only a single set is used
					rv = MissingCollections.<CPJumpTarget>unmodifiableList(
						Arrays.<CPJumpTarget>asList(new CPJumpTarget(
							program, CPJumpType.EXPLICIT,
							program.physicalToLogical(physical + off))));
				}
			
				// Conditional to a given instruction or if false, the next
				// instruction.
				else if (ik == CPOpcodes.IF_ACMPEQ ||
					ik == CPOpcodes.IF_ACMPNE ||
					ik == CPOpcodes.IFEQ ||
					ik == CPOpcodes.IFGE ||
					ik == CPOpcodes.IFGT ||
					ik == CPOpcodes.IF_ICMPEQ ||
					ik == CPOpcodes.IF_ICMPGE ||
					ik == CPOpcodes.IF_ICMPGT ||
					ik == CPOpcodes.IF_ICMPLE ||
					ik == CPOpcodes.IF_ICMPLT ||
					ik == CPOpcodes.IF_ICMPNE ||
					ik == CPOpcodes.IFLE ||
					ik == CPOpcodes.IFLT ||
					ik == CPOpcodes.IFNE ||
					ik == CPOpcodes.IFNONNULL ||
					ik == CPOpcodes.IFNULL)
					rv = MissingCollections.<CPJumpTarget>unmodifiableList(
						Arrays.<CPJumpTarget>asList(new CPJumpTarget(
								program, CPJumpType.NATURAL,
								logical + 1),
							new CPJumpTarget(program,
								CPJumpType.CONDITIONAL,
								program.physicalToLogical(physical +
									__ByteUtils__.__readUShort(
										bc, physical + 1)))));
			
				// Implicit next instruction
				else
					rv = MissingCollections.<CPJumpTarget>unmodifiableList(
						Arrays.<CPJumpTarget>asList(new CPJumpTarget(
							program, CPJumpType.NATURAL,
							logical + 1)));
			
				// If exceptions were set then add any exceptional targets
				// which would be missing from the list
				if (exset || program.areNoExceptions())
				{
					// Add any missing exceptions
					if (!program.areNoExceptions())
						throw new Error("TODO");
					
					// Cache it
					_ijtar = new WeakReference<>(rv);
				}
			}
		
			// Return it
			return rv;
		}
	}
	
	/**
	 * Returns the next operation.
	 *
	 * @return The next operation or {@code null} if this is the last.
	 * @since 2016/03/30
	 */
	public CPOp next()
	{
		if (logical >= (program.size() - 1))
			return null;
		return program.get(logical + 1);
	}
	
	/**
	 * Returns the output state of the current address after the operation
	 * it performs has been performed.
	 *
	 * @return The operation output state.
	 * @since 2016/03/30
	 */
	public CPVariableStates outputState()
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
	public CPOp previous()
	{
		if (logical <= 0)
			return null;
		return program.get(logical - 1);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/30
	 */
	@Override
	public String toString()
	{
		// Setup
		StringBuilder sb = new StringBuilder("{");
		
		// The position
		sb.append('@');
		sb.append(logical);
		
		// The operation
		sb.append(", op=");
		sb.append(instructionCode());
		
		// Source jumps
		sb.append(", sj=");
		sb.append(jumpSources());
		
		// Target jumps
		sb.append(", tj=");
		sb.append(jumpTargets());
		
		// Finish it
		sb.append('}');
		return sb.toString();
	}
	
	/**
	 * Returns the verification state of this operation.
	 *
	 * @return The verification state.
	 * @throws IllegalStateException If no state is available for derivation.
	 * @since 2016/03/31
	 */
	public CPVerifyState verificationState()
		throws IllegalStateException
	{
		// Lock
		synchronized (lock)
		{
			// Get reference
			Reference<CPVerifyState> ref = _vstate;
			CPVerifyState rv;
			
			// Needs caching?
			if (ref == null || null == (rv = ref.get()))
			{
				// Is there an explicit verification state?
				CPVerifyState exp = program._expvstate.get(logical);
				if (exp != null)
					rv = exp;
				
				// Need to generate otherwise
				else
				{
					// Get input state
					CPVariableStates inputstate = inputState();
					
					// {@squirreljme.error CP0b The instruction has no input
					// state. (The logical address)}
					if (inputstate == null)
						throw new IllegalStateException(String.format(
							"CP0b %d", logical));
				
					// Setup base state
					rv = new CPVerifyState(program);
					
					// Copy them all
					int n = program.maxLocals() + program.maxStack();
					for (int i = 0; i < n; i++)
						rv.set(i, inputstate.get(i).getType());
				}
				
				// Cache it
				_vstate = new WeakReference<>(rv);
			}
			
			// Return it
			return rv;
		}
	}
}

