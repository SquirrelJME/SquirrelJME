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

import java.io.DataInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Map;
import net.multiphasicapps.squirreljme.jit.JITException;
import net.multiphasicapps.util.sorted.SortedTreeMap;

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
	
	/** Resources within the cluster. */
	private final Map<String, Resource> _resources =
		new SortedTreeMap<>();
	
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
	
	/**
	 * Processes the specified resource or class and adds it to this cluster.
	 *
	 * @param __n The name of the stream.
	 * @param __is The input stream containing the data to process.
	 * @throws JITException If the class or resource could not be processed.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/06/19
	 */
	public final void processStream(String __n, InputStream __is)
		throws JITException, NullPointerException
	{
		// Check
		if (__n == null || __is == null)
			throw new NullPointerException("NARG");
		
		// Could fail
		LinkerState ls = __linkerState();
		Reference<LinkerState> lsref = ls.__reference();
		try
		{
			// Process as class file
			if (__n.endsWith(".class"))
			{
				throw new todo.TODO();
			}
			
			// Process as resource
			else
			{
				// {@squirreljme.error JI0w A resource is duplicated within this
				// cluster. (The cluster ID; The name of the resource)}
				Map<String, Resource> resources = this._resources;
				if (resources.containsKey(__n))
					throw new JITException(String.format("JI0w %s", this.key,
						__n));
				
				// Create resource
				Resource rc = new Resource(lsref, __n,
					new WeakReference<>(this));
				
				// Parse resource
				rc.__parse(__is);
				
				// Store it
				resources.put(__n, rc);
			}
		}
		
		// {@squirreljme.error JI0v (The name of the stream)}
		catch (IOException e)
		{
			throw new JITException(String.format("JI0v %s", __n), e);
		}
	}
}

