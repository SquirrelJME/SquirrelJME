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
import net.multiphasicapps.scrf.RegisterIndex;
import net.multiphasicapps.scrf.RegisterInstruction;
import net.multiphasicapps.scrf.RegisterInstructionType;
import net.multiphasicapps.scrf.MemorySource;
import net.multiphasicapps.scrf.MemoryType;

/**
 * This is used to build the register based code which is for later execution.
 *
 * @since 2019/01/22
 */
@Deprecated
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
		
		// Debug
		todo.DEBUG.note("I@%d: %s", rv, __i);
		
		// Return the index
		return rv;
	}
	
	/**
	 * Adds constant value.
	 *
	 * @param __t The type of memory to form a constant from.
	 * @param __dest The destination register.
	 * @param __v The value to set.
	 * @return The instruction index.
	 * @since 2019/01/24
	 */
	public final int addConst(MemoryType __t, int __dest, long __v)
		throws NullPointerException
	{
		if (__t == null)
			throw new NullPointerException("NARG");
		
		return this.add(new RegisterInstruction(RegisterInstructionType.CONST,
			__t, new RegisterIndex(__dest), __v));
	}
	
	/**
	 * Adds a copy from one register to another.
	 *
	 * @param __t The type of value to copy.
	 * @param __from The source.
	 * @param __to The destination.
	 * @throws NullPointerException
	 * @return The instruction index.
	 * @since 2019/01/23
	 */
	public final int addCopy(MemoryType __t, int __from, int __to)
		throws NullPointerException
	{
		if (__t == null)
			throw new NullPointerException("NARG");
		
		return this.add(new RegisterInstruction(
			RegisterInstructionType.COPY, __t, new RegisterIndex(__from),
			new RegisterIndex(__to)));
	}
	
	/**
	 * Adds invoke of a pointer, potentially somewhere in the vtable or
	 * otherwise.
	 *
	 * @param __ptr The memory pointer.
	 * @param __rr Register to contain the return value of the invoked
	 * method, negative values are not used.
	 * @param __rt Register to contain the thrown exception, if applicable.
	 * @param __ra Registers which contain the input arguments to the method
	 * call.
	 * @return The index of the operation.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/02/14
	 */
	public final int addInvoke(MemorySource __ptr, int __rr, int __rt,
		int... __ra)
		throws NullPointerException
	{
		if (__ptr == null)
			throw new NullPointerException("NARG");
		
		__ra = (__ra == null ? new int[0] : __ra.clone());
		
		throw new todo.TODO();
	}
	
	/**
	 * Adds a load from memory instruction.
	 *
	 * @param __t The type of value to copy.
	 * @param __from The memory source to read from.
	 * @param __to The destination register.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/02/05
	 */
	public final int addLoad(MemoryType __t, MemorySource __from, int __to)
		throws NullPointerException
	{
		if (__t == null || __from == null)
			throw new NullPointerException("NARG");
	
		return this.add(new RegisterInstruction(
			RegisterInstructionType.LOAD, __t, __from,
			new RegisterIndex(__to)));
	}
	
	/**
	 * Adds a NOP instruction.
	 *
	 * @return The instruction index.
	 * @since 2019/01/23
	 */
	public final int addNop()
	{
		return this.add(new RegisterInstruction(RegisterInstructionType.NOP));
	}
}

