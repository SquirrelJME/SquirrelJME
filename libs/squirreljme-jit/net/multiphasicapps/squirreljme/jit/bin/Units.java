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
import java.util.Map;
import net.multiphasicapps.util.sorted.SortedTreeMap;

/**
 * This contains all of the units (which are classes) which are associated with
 * the linker.
 *
 * @since 2017/06/17
 */
public final class Units
	extends __SubState__
{
	/** Mapping of class names to units. */
	private final Map<ClassName, Unit> _units =
		new SortedTreeMap<>();
	
	/**
	 * Initializes the unit manager.
	 *
	 * @param __ls The owning linker state.
	 * @since 2017/06/17
	 */
	Units(Reference<LinkerState> __ls)
	{
		super(__ls);
	}
}

