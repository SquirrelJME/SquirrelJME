// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.scrf.compiler;

import java.util.ArrayList;
import java.util.List;
import net.multiphasicapps.scrf.RegisterInstruction;
import net.multiphasicapps.scrf.RegisterInstructionIndex;

/**
 * This is used to build the register based code which is for later execution.
 *
 * @since 2019/01/22
 */
public final class RegisterCodeBuilder
{
	/** The VTable for this class. */
	protected final VTableBuilder vtable;
	
	/** Instructions to use in the target method. */
	private final List<RegisterInstruction> _insts =
		new ArrayList<>();
	
	/**
	 * Initializes the code builder.
	 *
	 * @param __vt The VTable.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/01/22
	 */
	public RegisterCodeBuilder(VTableBuilder __vt)
		throws NullPointerException
	{
		if (__vt == null)
			throw new NullPointerException("NARG");
		
		this.vtable = __vt;
	}
	
	/**
	 * Adds the specified instruction to the code.
	 *
	 * @param __i The instruction to add.
	 * @return The index of the instruction.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/01/23
	 */
	public final int add(RegisterInstruction __i)
		throws NullPointerException
	{
		if (__i == null)
			throw new NullPointerException("NARG");
		
		// The instruction index gets returned
		List<RegisterInstruction> insts = this._insts;
		int rv = insts.size();
		insts.add(__i);
		return rv;
	}
	
	/**
	 * Adds constant value.
	 *
	 * @param __dest The destination.
	 * @param __v The value to set.
	 * @return The instruction index.
	 * @since 2019/01/24
	 */
	public final int addConst(int __dest, int __v)
	{
		return this.add(new RegisterInstruction(
			RegisterInstructionIndex.CONST, __dest, __v));
	}
	
	/**
	 * Adds constant value (pointer).
	 *
	 * @param __dest The destination.
	 * @param __v The value to set.
	 * @return The instruction index.
	 * @since 2019/01/24
	 */
	public final int addConstPointer(int __dest, long __v)
	{
		return this.add(new RegisterInstruction(
			RegisterInstructionIndex.CONST_POINTER, __dest, __v));
	}
	
	/**
	 * Adds a copy from one register to another.
	 *
	 * @param __from The source.
	 * @param __to The destination.
	 * @return The instruction index.
	 * @since 2019/01/23
	 */
	public final int addCopy(int __from, int __to)
	{
		return this.add(new RegisterInstruction(
			RegisterInstructionIndex.COPY, __from, __to));
	}
	
	/**
	 * Adds a copy pointer from one register to another.
	 *
	 * @param __from The source.
	 * @param __to The destination.
	 * @return The instruction index.
	 * @since 2019/01/23
	 */
	public final int addCopyPointer(int __from, int __to)
	{
		return this.add(new RegisterInstruction(
			RegisterInstructionIndex.COPY_POINTER, __from, __to));
	}
	
	/**
	 * Adds a NOP instruction.
	 *
	 * @return The instruction index.
	 * @since 2019/01/23
	 */
	public final int addNop()
	{
		return this.add(new RegisterInstruction(RegisterInstructionIndex.NOP));
	}
}

