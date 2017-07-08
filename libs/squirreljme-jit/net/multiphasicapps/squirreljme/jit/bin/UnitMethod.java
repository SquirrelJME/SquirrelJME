// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.bin;

import java.lang.ref.Reference;

/**
 * This represents a method which exists within a unit and contains a
 * reference to the method's machine code and some other information which is
 * needed for verifications.
 *
 * @since 2017/07/07
 */
public class UnitMethod
	extends __SubState__
{
	/**
	 * Initializes the unit method.
	 *
	 * @param __ls The owning linker state.
	 * @since 2017/07/07
	 */
	public UnitMethod(Reference<LinkerState> __ls)
	{
		super(__ls);
	}
}

