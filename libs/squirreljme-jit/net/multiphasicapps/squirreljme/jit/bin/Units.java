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
import java.lang.ref.WeakReference;

/**
 * This contains all of the units (which are classes) which are associated with
 * the linker.
 *
 * @since 2017/06/17
 */
public class Units
{
	/** The reference state which these packages are a part of. */
	protected final Reference<LinkerState> linkerstate;
	
	/**
	 * Initializes the unit manager.
	 *
	 * @param __ls The owning linker state.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/06/16
	 */
	Units(Reference<LinkerState> __ls)
		throws NullPointerException
	{
		// Check
		if (__ls == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.linkerstate = __ls;
		
		throw new todo.TODO();
	}
}

