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
 * This represents a group of resources which are referenced by classes, since
 * {@link Class#getResourceAsStream(String)} for Java ME requires that classes
 * only access resources from within their own JAR. This makes it so that
 * behavior is duplicated as intended.
 *
 * @since 2017/06/17
 */
public class Cluster
	extends __SubState__
{
	/** The key for this cluster. */
	protected final ClusterKey key;
	
	/**
	 * Initializes this individual cluster.
	 *
	 * @param __ls The owning linker state.
	 * @param __k The key for the cluster.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/06/17
	 */
	Cluster(Reference<LinkerState> __ls, ClusterKey __k)
		throws NullPointerException
	{
		super(__ls);
		
		// Check
		if (__k == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.key = __k;
	}
}

