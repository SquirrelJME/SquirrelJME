// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * This is used to identify the cluster a project is within and it is used to
 * group projects together into a single unit.
 *
 * @since 2017/06/01
 */
@Deprecated
public final class ClusterIdentifier
	implements Comparable<ClusterIdentifier>
{
	/** The identifier for the cluster. */
	protected final int id;
	
	/** The string representation of the cluster identifier. */
	private volatile Reference<String> _string;
	
	/**
	 * Initializes the cluster identifier.
	 *
	 * @param __id The cluster's identifier.
	 * @since 2017/06/01
	 */
	public ClusterIdentifier(int __id)
	{
		this.id = __id;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/06/06
	 */
	@Override
	public int compareTo(ClusterIdentifier __o)
	{
		return this.id - __o.id;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/06/01
	 */
	@Override
	public boolean equals(Object __o)
	{
		// Check
		if (!(__o instanceof ClusterIdentifier))
			return false;
		
		return this.id == ((ClusterIdentifier)__o).id;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/06/01
	 */
	@Override
	public int hashCode()
	{
		return this.id;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/06/01
	 */
	@Override
	public String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
			this._string = new WeakReference<>(rv = Integer.toString(this.id));
		
		return rv;
	}
}

