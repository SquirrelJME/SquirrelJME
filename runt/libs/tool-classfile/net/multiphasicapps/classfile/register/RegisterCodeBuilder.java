// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile.register;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.multiphasicapps.classfile.InstructionJumpTarget;

/**
 * This is used to build {@link RegisterCode} and add instructions to it.
 *
 * @since 2019/03/16
 */
public final class RegisterCodeBuilder
{
	/** Temporary instruction layout. */
	final Map<Integer, RegisterInstruction> _instructions =
		new LinkedHashMap<>();
	
	/** Label positions. */
	final Map<RegisterCodeLabel, Integer> _labels =
		new LinkedHashMap<>();
	
	/** Next address to use. */
	int _nextaddr;
	
	/**
	 * Initializes the code builder at the default start address.
	 *
	 * @since 2019/03/22
	 */
	public RegisterCodeBuilder()
	{
		this._nextaddr = 0;
	}
	
	/**
	 * Initializes the code builder at the given start address.
	 *
	 * @param __pc The address to start at.
	 * @since 2019/03/22
	 */
	public RegisterCodeBuilder(int __pc)
	{
		this._nextaddr = __pc;
	}
	
	/**
	 * Adds a new instruction.
	 *
	 * @param __op The operation to add.
	 * @param __args The arguments to the operation.
	 * @return The resulting temporary instruction.
	 * @since 2019/03/16
	 */
	public final RegisterInstruction add(int __op, Object... __args)
	{
		// Create instruction
		int atdx = this._nextaddr++;
		RegisterInstruction rv = new RegisterInstruction(__op, __args);
		
		// Debug
		todo.DEBUG.note("@%d -> %s %s", atdx,
			RegisterOperationMnemonics.toString(__op), Arrays.asList(__args));
		
		// Store and return the instruction, it will have the address
		this._instructions.put(atdx, rv);
		return rv;
	}
	
	/**
	 * Builds the register code, all references to other portions in the
	 * code itself are resolved.
	 *
	 * @return The built register code.
	 * @since 2019/03/22
	 */
	public final RegisterCode build()
	{
		// Working area for arguments
		List<Object> workargs = new ArrayList<>();

		// Labels which point to addresses
		Map<RegisterCodeLabel, Integer> labels = this._labels;
		
		// If there are any jump points which refer to the instruction index
		// directly following it, then remove the jump
		// This will happen in constructors that call another constructor since
		// there will be an exception handler jump that points to the next
		// instruction
		// This only handle conditional and basic jumps.
		List<RegisterInstruction> in = new ArrayList<>(
			this._instructions.values());
		for (int i = in.size() - 1; i >= 0; i--)
		{
			RegisterInstruction ri = in.get(i);
			
			// Determine if it is a basic conditional jump
			RegisterCodeLabel lt;
			switch (ri.op)
			{
				case RegisterOperationType.JUMP:
				case RegisterOperationType.JUMP_ON_EXCEPTION:
					lt = (RegisterCodeLabel)ri._args[0];
					break;
				
					// Not a jump
				default:
					continue;
			}
			
			// If this points to the index directly following this, then delete
			// this instruction and move down every single label which targets
			// an index following this since it would be off by one
			if ((i + 1) == labels.get(lt))
			{
				// Remove this instruction, it is pointless
				in.remove(i);
				
				// Move all of the label values down
				for (Map.Entry<RegisterCodeLabel, Integer> e :
					labels.entrySet())
				{
					int val = e.getValue();
					if (val > i)
						e.setValue(val - 1);
				}
			}
		}
		
		// Output instructions
		List<RegisterInstruction> out = new ArrayList<>();
		
		// Go through input instructions and map them to real instructions
		for (RegisterInstruction i : in)
		{
			// Fill in working arguments, with translated labels
			workargs.clear();
			for (Object a : i._args)
			{
				// Map any labels to indexes
				if (a instanceof RegisterCodeLabel)
					a = new InstructionJumpTarget(
						labels.get((RegisterCodeLabel)a));
				
				workargs.add(a);
			}
			
			// Build instruction
			out.add(new RegisterInstruction(i.op, workargs));
		}
		
		// Debug
		for (int i = 0, n = out.size(); i < n; i++)
			todo.DEBUG.note("@%-3d: %s", i, out.get(i));
		
		// Build
		return new RegisterCode(out);
	}
	
	/**
	 * Adds a label at the current position.
	 *
	 * @param __lo The locality.
	 * @param __dx The index.
	 * @return The added label.
	 * @since 2019/03/22
	 */
	public final RegisterCodeLabel label(String __lo, int __dx)
	{
		return this.label(new RegisterCodeLabel(__lo, __dx), this._nextaddr);
	}
	
	/**
	 * Adds a label.
	 *
	 * @param __lo The locality.
	 * @param __dx The index.
	 * @param __pc The address to target.
	 * @return The added label.
	 * @since 2019/03/22
	 */
	public final RegisterCodeLabel label(String __lo, int __dx, int __pc)
	{
		return this.label(new RegisterCodeLabel(__lo, __dx), __pc);
	}
	
	/**
	 * Adds a label at the current position.
	 *
	 * @param __l The label to add.
	 * @param __pc The address to target.
	 * @return The added label.
	 * @since 2019/03/22
	 */
	public final RegisterCodeLabel label(RegisterCodeLabel __l)
	{
		return this.label(__l, this._nextaddr);
	}
	
	/**
	 * Adds a label.
	 *
	 * @param __pc The address to target.
	 * @return The added label.
	 * @since 2019/03/22
	 */
	public final RegisterCodeLabel label(RegisterCodeLabel __l, int __pc)
	{
		// Debug
		todo.DEBUG.note("Label %s -> @%d", __l, __pc);
		
		// Add
		this._labels.put(__l, __pc);
		return __l;
	}
	
	/**
	 * Returns the target of the specified label.
	 *
	 * @param __l The label to get the target of.
	 * @return The index of the instruction or {@code -1} if the label is not
	 * valid or the position is not yet known.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/03/22
	 */
	public final int labelTarget(RegisterCodeLabel __l)
		throws NullPointerException
	{
		if (__l == null)
			throw new NullPointerException("NARG");
		
		Integer rv = this._labels.get(__l);
		return (rv == null ? -1 : rv);
	}
}

