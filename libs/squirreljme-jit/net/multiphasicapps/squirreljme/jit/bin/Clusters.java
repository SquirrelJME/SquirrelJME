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
 * This manages {@link Cluster}s which provide an abstract representation
 * of JAR resources within the requirements of Java ME. Classes exist within a
 * single table ({@link Units}) but they have a weak association to clusters
 * which is needed to simulate the effect of obtaining resources being limited
 * to the input JAR files.
 *
 * @since 2017/06/17
 */
public class Clusters
	extends __SubState__
{
	/** The mapping of clusters. */
	private final Map<ClusterKey, Cluster> _clusters =
		new SortedTreeMap<>();
	
	/** Logical names of clusters which are mostly used for debug. */
	private final Map<ClusterKey, String> _names =
		new SortedTreeMap<>();
	
	/** The next cluster identifier to use. */
	private volatile int _next =
		1;
	
	/**
	 * Initializes the resource group manager.
	 *
	 * @param __ls The owning linker state.
	 * @since 2017/06/17
	 */
	Clusters(Reference<LinkerState> __ls)
		throws NullPointerException
	{
		super(__ls);
	}
	
	/**
	 * Creates a new cluster with the given name.
	 *
	 * @param __n The name of the cluster.
	 * @return The newly created cluster.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/06/18
	 */
	public Cluster createCluster(String __n)
		throws NullPointerException
	{
		// Check
		if (__n == null)
			throw new NullPointerException("NARG");
		
		// Setup new identifier
		ClusterKey key = new ClusterKey(this._next++);
		
		// Create cluster
		Cluster rv = new Cluster(this.linkerstate, key);
		
		// Store new cluster
		this._clusters.put(key, rv);
		this._names.put(key, __n);
		return rv;
	}
}

