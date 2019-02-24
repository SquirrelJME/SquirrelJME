// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.scrf;

/**
 * This class stores the intermediate SummerCoat language.
 *
 * @since 2019/02/16
 */
public final class ILCode
{
	/** Instructions in this code. */
	private final ILInstruction[] _insts;
	
	/**
	 * Initializes the code.
	 *
	 * @param __i Input instructions.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/02/24
	 */
	public ILCode(ILInstruction[] __i)
		throws NullPointerException
	{
		if (__i == null)
			throw new NullPointerException("NARG");
		
		this._insts = (__i == null ? new ILInstruction[0] : __i.clone());
	}
}

