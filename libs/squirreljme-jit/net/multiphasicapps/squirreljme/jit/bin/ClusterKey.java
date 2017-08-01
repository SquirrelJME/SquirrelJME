// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.bin;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import net.multiphasicapps.squirreljme.jit.JITException;

/**
 * This represents a single cluster which identifies the logical group that
 * resources and classes are a part of. Clusters are identified using a basic
 * integer index
 *
 * This class is immutable.
 *
 * @since 2017/06/18
 */
public final class ClusterKey
	implements Comparable<ClusterKey>
{
	/** The identifier. */
	protected final int id;
	
	/** The string representation. */
	private volatile Reference<String> _string;
	
	/**
	 * Initializes the cluster key.
	 *
	 * @param __i The unique cluster key.
	 * @throws JITException If the key is less than or equal to zero.
	 * @since 2017/06/18
	 */
	ClusterKey(int __i)
		throws JITException
	{
		// {@squirreljme.error JI0t The cluster key cannot be zero or less
		// than zero.}
		if (__i <= 0)
			throw new JITException("JI0t");
		
		this.id = __i;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/06/18
	 */
	@Override
	public int compareTo(ClusterKey __o)
	{
		return this.id - __o.id;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/06/18
	 */
	@Override
	public boolean equals(Object __o)
	{
		// Check
		if (!(__o instanceof ClusterKey))
			return false;
		
		return this.id == ((ClusterKey)__o).id;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/06/18
	 */
	@Override
	public int hashCode()
	{
		return this.id;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/06/18
	 */
	@Override
	public String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
			this._string = new WeakReference<>(
				(rv = Integer.toString(this.id)));
		
		return rv;
	}
}

