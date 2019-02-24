// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.scrf.building;

import java.util.LinkedHashMap;
import java.util.Map;
import net.multiphasicapps.scrf.CodeLocation;
import net.multiphasicapps.scrf.DynTableLocation;
import net.multiphasicapps.scrf.FixedMemoryLocation;
import net.multiphasicapps.scrf.ILInstruction;
import net.multiphasicapps.scrf.ILInstructionType;
import net.multiphasicapps.scrf.MemoryLocation;
import net.multiphasicapps.scrf.RegisterLocation;
import net.multiphasicapps.scrf.SummerFormatException;

/**
 * This class is used to store the generated instructions for the intermediate
 * language code.
 *
 * @since 2019/02/17
 */
public final class ILCodeBuilder
{
	/** Added instructions. */
	private final Map<Integer, ILInstruction> _code =
		new LinkedHashMap<>();
	
	/** Next address. */
	private int _nextaddr;
	
	/**
	 * Adds a single instruction.
	 *
	 * @param __type The instruction type.
	 * @param __args Arguments.
	 * @return The location in the code.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/02/23
	 */
	public final CodeLocation add(ILInstructionType __type, Object... __args)
		throws NullPointerException
	{
		if (__type == null)
			throw new NullPointerException("NARG");
		
		// Calculate instruction address
		int now = this._nextaddr++;
		this._code.put(now, new ILInstruction(__type, __args));
		
		// Return resulting address
		return new CodeLocation(now);
	}
	
	/**
	 * Adds constant reference.
	 *
	 * @param __dest Destination register.
	 * @param __v The value to store.
	 * @return The index of the added instruction.
	 * @throws NullPointerException On null arguments.
	 * @throws SummerFormatException If the value is not valid.
	 * @since 2019/02/23
	 */
	public final CodeLocation addConst(RegisterLocation __dest, Object __v)
		throws NullPointerException, SummerFormatException
	{
		if (__dest == null || __v == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error AV07 Wide value cannot be placed in a narrow
		// register. (The destination; The value)}
		if ((__v instanceof Long || __v instanceof Double) &&
			!__dest.isWide())
			throw new SummerFormatException(String.format(
				"AV07 %s %s", __dest, __v));
		
		// {@squirreljme.error AV08 Cannot set register to constant value
		// because it's value is not actually constant.}
		if (!(__v instanceof Integer ||
			__v instanceof Long ||
			__v instanceof Float ||
			__v instanceof Double ||
			__v instanceof FixedMemoryLocation))
			throw new SummerFormatException("AV08 " + __v);
		
		// Generate
		return this.add(ILInstructionType.CONST, __dest, __v);
	}
	
	/**
	 * Adds a copy from one location to another.
	 *
	 * @param __to The destination to copy to.
	 * @param __from The source to copy from.
	 * @return The location of the added instruction.
	 * @throws NullPointerException On null arguments.
	 * @throws SummerFormatException If the copy is not valid.
	 * @since 2019/02/23
	 */
	public final CodeLocation addCopy(RegisterLocation __to,
		RegisterLocation __from)
		throws NullPointerException, SummerFormatException
	{
		if (__from == null || __to == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error AV06 Invalid copy. (Source; Destination)}
		if (__from.isWide() != __to.isWide() ||
			__from.equals(__to))
			throw new SummerFormatException("AV06 " + __from + " " + __to);
		
		return this.add(ILInstructionType.COPY, __from, __to);
	}
	
	/**
	 * Adds invocation for adding.
	 *
	 * @param __l Location of the reference in the dynamic table.
	 * @param __rv The return register.
	 * @param __args Arguments to the method call.
	 * @return The location of the instruction in code.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/02/24
	 */
	public final CodeLocation addInvoke(DynTableLocation __l,
		RegisterLocation __rv, RegisterLocation... __args)
		throws NullPointerException
	{
		if (__l == null || __rv == null || __args == null)
			throw new NullPointerException("NARG");
		
		return this.add(ILInstructionType.INVOKE, __l, __rv, __args);
	}
	
	/**
	 * Adds a read from memory to the given destination.
	 *
	 * @param __dest The destination register.
	 * @param __src The source memory location.
	 * @return The code location.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/02/24
	 */
	public final CodeLocation addRead(RegisterLocation __dest,
		MemoryLocation __src)
		throws NullPointerException
	{
		if (__dest == null || __src == null)
			throw new NullPointerException("NARG");
		
		return this.add(ILInstructionType.READ, __dest, __src);
	}
}

