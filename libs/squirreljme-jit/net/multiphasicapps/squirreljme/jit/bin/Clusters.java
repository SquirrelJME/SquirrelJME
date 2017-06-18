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
 * This manages {@link Cluster}s which provide an abstract representation
 * of JAR resources within the requirements of Java ME.
 *
 * @since 2017/06/17
 */
public class Clusters
{
	/** The reference state which these packages are a part of. */
	protected final Reference<LinkerState> linkerstate;
	
	/**
	 * Initializes the resource group manager.
	 *
	 * @param __ls The owning linker state.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/06/17
	 */
	Clusters(Reference<LinkerState> __ls)
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

