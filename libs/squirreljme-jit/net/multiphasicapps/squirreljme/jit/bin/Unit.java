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
import net.multiphasicapps.squirreljme.jit.java.ClassName;

/**
 * This represents a single class unit.
 *
 * @since 2017/06/17
 */
public final class Unit
	extends __SubState__
{
	/** The name of this class. */
	protected final ClassName classname;
	
	/**
	 * Initializes the individual class unit.
	 *
	 * @param __ls The owning linker state.
	 * @param __n The name of the class.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/06/18
	 */
	Unit(Reference<LinkerState> __ls, ClassName __n)
		throws NullPointerException
	{
		super(__ls);
		
		// Check
		if (__n == null)
			throw new NullPointerException("NARG");
		
		this.classname = __n;
	}
	
	/**
	 * Returns the name of the class this unit is for.
	 *
	 * @return The name of this unit.
	 * @since 2017/07/08
	 */
	public final ClassName className()
	{
		return this.classname;
	}
}

