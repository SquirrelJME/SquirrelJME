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
 * This represents information about a single resource in the output binary
 * which translates from a resource within the JAR.
 *
 * @since 2017/06/17
 */
public class Resource
	extends __SubState__
{
	/**
	 * Initializes the resource.
	 *
	 * @param __ls The owning linker state.
	 * @since 2017/06/18
	 */
	Resource(Reference<LinkerState> __ls)
	{
		super(__ls);
		
		throw new todo.TODO();
	}
}

