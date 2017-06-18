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
 * This class represents a single dynamic which is essentially information that
 * the linker needs but does not yet know about. An example of a dynamic would
 * be a reference to a method to be invoked which is at a given address but
 * that address is not known at code parsing time.
 *
 * Before the final output stage, all dynamics must be resolved before the
 * output code may be placed into an output executable for actual generation.
 *
 * @since 2017/06/15
 */
public class Dynamic
	extends __SubState__
{
	/**
	 * Initializes this dynamic.
	 *
	 * @param __ls The reference to the owning linker state.
	 * @since 2017/06/15
	 */
	Dynamic(Reference<LinkerState> __ls)
	{
		super(__ls);
		
		throw new todo.TODO();
	}
}

