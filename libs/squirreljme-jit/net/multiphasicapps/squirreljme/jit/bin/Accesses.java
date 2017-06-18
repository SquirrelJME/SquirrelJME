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
 * This class contains all of the access checks which must be performed by the
 * compiler once all of the input code has been read. Virtually all access
 * checks (except for {@link Class#newInstance()} are able to be statically
 * verified at link time. This ensures that anything which is accessible is
 * correctly accessed.
 *
 * @since 2017/06/15
 */
public class Accesses
	extends __SubState__
{
	/**
	 * Initializes the access table.
	 *
	 * @param __ls The reference to the owning linker state.
	 * @since 2017/06/15
	 */
	Accesses(Reference<LinkerState> __ls)
	{
		super(__ls);
		
		throw new todo.TODO();
	}
}

