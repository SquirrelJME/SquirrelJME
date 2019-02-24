// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.scrf;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * This represents a position within the dynamic table of the current method.
 *
 * @since 2019/02/23
 */
public final class DynTableLocation
	implements MemoryLocation
{
	/** The index of the location. */
	protected final int index;
	
	/** String representation. */
	private Reference<String> _string;
	
	/**
	 * Initializes the location.
	 *
	 * @param __i The dynamic table address.
	 * @since 2019/02/24
	 */
	public DynTableLocation(int __i)
	{
		this.index = __i;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/02/24
	 */
	@Override
	public final boolean equals(Object __o)
	{
		if (this == __o)
			return true;
		
		if (!(__o instanceof DynTableLocation))
			return false;
		
		DynTableLocation o = (DynTableLocation)__o;
		return this.index == o.index;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/02/24
	 */
	@Override
	public final int hashCode()
	{
		return this.index;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/02/24
	 */
	@Override
	public final String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		if (ref == null || null == (rv = ref.get()))
			this._string = new WeakReference<>((rv = "dt%" + this.index));
		
		return rv;
	}
}

