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
 * This represents a single operation in the byte code chain.
 *
 * @since 2016/03/30
 */
public class VMCOp
{
	/** Operation lock. */
	final Object lock;
	
	/** The program to use. */
	protected final VMCProgram program;	
	
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
	 * @param __prg The owning program.
	 * @param __pc The operation address.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/30
	 */
	VMCOp(VMCProgram __prg, int __pc)
		throws NullPointerException
	{
		// Check
		if (__prg == null)
			throw new NullPointerException("NARG");
		
		program = __prg;
		lock = program.lock;
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
		if (rv == VMCInstructionIDs.WIDE)
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
	public List<VMCJumpSource> jumpSources()
	{
		// Explicit jump sources?
		List<VMCJumpSource> rv = program._expjumps.get(logical);
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
		// Lock
		synchronized (lock)
		{
			// Cached?
			Reference<List<VMCJumpTarget>> ref = _ijtar;
			List<VMCJumpTarget> rv;
		
			// Needs creation?
			if (ref == null || null == (rv = ref.get()))
			{
				// Get the instruction code
				byte[] bc = program._code;
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
				{
					// Align pointer to read the jump values
					int ap = ((physical + 3) & (~3));
				
					// Read the default offset
					int defo = __ByteUtils__.__readSInt(bc, ap);
				
					// Read the pair count
					int np = __ByteUtils__.__readSInt(bc, ap + 4);
				
					// There are a specific number of pairs so an array can
					// be used
					VMCJumpTarget[] ojta = new VMCJumpTarget[np + 1];
				
					// Setup default first
					ojta[0] = new VMCJumpTarget(program,
						VMCJumpType.CONDITIONAL,
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
						if (keyv < lastdx && lastisval)
							throw new JVMVerifyException(String.format(
								"IN2e %s", physical));
					
						// Set the last value which is checked to make sure the
						// key is actually higher
						lastdx = keyv;
						lastisval = true;
					
						// Read the offset and calculate the jump
						int joff = __ByteUtils__.__readSInt(bc, baseoff + 4);
						ojta[1 + j] = new VMCJumpTarget(program,
							VMCJumpType.CONDITIONAL,
							program.physicalToLogical(physical + joff));
					}
				
					// Wrap and lock it in
					rv = MissingCollections.<VMCJumpTarget>unmodifiableList(
						Arrays.<VMCJumpTarget>asList(ojta));
				}
			
				// Table switch
				else if (ik == VMCInstructionIDs.TABLESWITCH)
					throw new Error("TODO");
			
				// Goto a single address (16-bit)
				else if (ik == VMCInstructionIDs.GOTO)
					throw new Error("TODO");
			
				// Goto a single address (32-bit)
				else if (ik == VMCInstructionIDs.GOTO_W)
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
								program, VMCJumpType.NATURAL,
								logical + 1),
							new VMCJumpTarget(program,
								VMCJumpType.CONDITIONAL,
								program.physicalToLogical(physical +
									__ByteUtils__.__readUShort(
										bc, physical + 1)))));
			
				// Implicit next instruction
				else
					rv = MissingCollections.<VMCJumpTarget>unmodifiableList(
						Arrays.<VMCJumpTarget>asList(new VMCJumpTarget(
							program, VMCJumpType.NATURAL,
							logical + 1)));
			
				// Cache it
				_ijtar = new WeakReference<>(rv);
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
	public VMCOp next()
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
	public VMCOp previous()
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
}

