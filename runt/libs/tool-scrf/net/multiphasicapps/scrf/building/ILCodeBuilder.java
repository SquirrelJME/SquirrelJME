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
import net.multiphasicapps.scrf.ILInstruction;
import net.multiphasicapps.scrf.ILInstructionType;
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
	 * Adds a copy from one location to another.
	 *
	 * @param __from The source to copy from.
	 * @param __to The destination to copy to.
	 * @return The location of the added instruction.
	 * @throws NullPointerException On null arguments.
	 * @throws SummerFormatException If the copy is not valid.
	 * @since 2019/02/23
	 */
	public final CodeLocation addCopy(RegisterLocation __from,
		RegisterLocation __to)
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
}

