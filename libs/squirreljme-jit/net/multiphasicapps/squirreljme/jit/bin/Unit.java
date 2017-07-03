// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.bin;

import java.lang.ref.Reference;

/**
 * This represents a single class unit.
 *
 * @since 2017/06/17
 */
public final class Unit
	extends __SubState__
{
	/**
	 * Initializes the individual class unit.
	 *
	 * @param __ls The owning linker state.
	 * @since 2017/06/18
	 */
	Unit(Reference<LinkerState> __ls)
	{
		super(__ls);
	}
}

