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

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import net.multiphasicapps.util.sorted.SortedTreeMap;

/**
 * This is the link table which contains every exported and imported class,
 * field, method, and resource.
 *
 * This class is not thread safe.
 *
 * @since 2017/05/29
 */
public class LinkTable
{
	/** Clusters which are available for resource usage. */
	private final Map<ClusterIdentifier, ResourceCluster> _clusters =
		new SortedTreeMap<>();
	
	/**
	 * Creates the specified resource and returns the output stream used to
	 * write the resource data.
	 *
	 * @param __ci The cluster the resource is in.
	 * @param __n The name of the resource.
	 * @throws JITException If the resource could not be created.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/06/07
	 */
	public OutputStream createResource(ClusterIdentifier __ci, String __n)
		throws NullPointerException
	{
		// Check
		if (__ci == null || __n == null)
			throw new NullPointerException("NARG");
		
		// Obtain the resource cluster, create if missing
		Map<ClusterIdentifier, ResourceCluster> clusters = this._clusters;
		ResourceCluster rc = clusters.get(__ci);
		if (rc == null)
			clusters.put(__ci, (rc = new ResourceCluster(__ci)));
		
		// Add resource
		return rc.__createResource(__n);
	}
}

