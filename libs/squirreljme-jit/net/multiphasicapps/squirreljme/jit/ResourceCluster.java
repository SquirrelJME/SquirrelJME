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

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.util.Map;
import net.multiphasicapps.util.sorted.SortedTreeMap;

/**
 * This class is associated with a link table and specifies resources which
 * are part of a given cluster.
 *
 * @since 2017/06/06
 */
@Deprecated
public final class ResourceCluster
{
	/** The identifier for this cluster. */
	protected final ClusterIdentifier id;
	
	/** Resources which exist within this cluster. */
	private final Map<String, Resource> _resources =
		new SortedTreeMap<>();
	
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
		
		return new __Output__(__n);
	}
	
	/**
	 * This is used to write 
	 *
	 * @since 2017/05/08
	 */
	private final class __Output__
		extends OutputStream
	{
		/** The output byte array. */
		protected final ByteArrayOutputStream out =
			new ByteArrayOutputStream();
		
		/** The name of the resource. */
		protected final String name;
		
		/** Has the resource been added? */
		private volatile boolean _added;
		
		/**
		 * Initializes the resource output.
		 *
		 * @param __n The name of the resource to add.
		 * @throws NullPointerException On null arguments.
		 * @since 2017/06/08
		 */
		private __Output__(String __n)
			throws NullPointerException
		{
			// Check
			if (__n == null)
				throw new NullPointerException("NARG");
			
			// Set
			this.name = __n;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2017/06/08
		 */
		@Override
		public void close()
			throws IOException
		{
			// Only add resources once
			if (this._added)
				return;
			this._added = true;
			
			// {@squirreljme.error JI05 Cannot add the specified resource
			// because it already exists in the cluster. (The resource name)}
			Map<String, Resource> resources = ResourceCluster.this._resources;
			String name = this.name;
			if (null != resources.get(name))
				throw new IOException(String.format("JI05 %s", name));
			
			// Store it
			resources.put(name, new Resource(name, this.out.toByteArray()));
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2017/06/08
		 */
		@Override
		public void write(int __b)
			throws IOException
		{
			// {@squirreljme.error JI03 Cannot write single byte to the resource
			// because it has already been added.}
			if (this._added)
				throw new IOException("JI03");
			
			this.out.write(__b);
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2017/06/08
		 */
		@Override
		public void write(byte[] __b, int __o, int __l)
			throws IOException
		{
			// {@squirreljme.error JI04 Cannot write multiple bytes to the
			// resource because it has already been added.}
			if (this._added)
				throw new IOException("JI04");
			
			this.out.write(__b, __o, __l);
		}
	}
}

