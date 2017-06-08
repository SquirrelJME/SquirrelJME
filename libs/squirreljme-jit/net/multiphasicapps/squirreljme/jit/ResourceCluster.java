// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

import java.io.OutputStream;
import java.io.IOException;

/**
 * This class is associated with a link table and specifies resources which
 * are part of a given cluster.
 *
 * @since 2017/06/06
 */
public final class ResourceCluster
{
	/** The identifier for this cluster. */
	protected final ClusterIdentifier id;
	
	/**
	 * Initializes the resource cluster.
	 *
	 * @param __ci The identifier for the cluster.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/06/06
	 */
	ResourceCluster(ClusterIdentifier __ci)
		throws NullPointerException
	{
		// Check
		if (__ci == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.id = __ci;
	}
	
	/**
	 * Returns the cluster identifier.
	 *
	 * @return The cluster identifier.
	 * @since 2017/06/08
	 */
	public final ClusterIdentifier id()
	{
		return this.id;
	}
	
	/**
	 * Creates a new resource and returns the output stream which is used to
	 * write the resource data.
	 *
	 * @param __n The name of the resource to create.
	 * @return The output stream which contains the target resource data.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/06/08
	 */
	final OutputStream __createResource(String __n)
		throws NullPointerException
	{
		// Check
		if (__n == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

