// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.arch;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * This is a basic future position marker which can be used to mark a future
 * position.
 *
 * @since 2017/08/15
 */
public class BasicFuturePositionMarker
	implements FuturePositionMarker
{
	/** The ID of this marker. */
	protected final int id;
	
	/** String reference. */
	private volatile Reference<String> _string;
	
	/**
	 * Initializes the basic future position with the given ID.
	 *
	 * @param __id The ID of this future marker.
	 * @since 2017/08/15
	 */
	public BasicFuturePositionMarker(int __id)
	{
		// Set
		this.id = __id;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/08/15
	 */
	@Override
	public boolean equals(Object __o)
	{
		// Check
		if (!(__o instanceof BasicFuturePositionMarker))
			return false;
		
		return this.id == ((BasicFuturePositionMarker)__o).id;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/08/15
	 */
	@Override
	public int hashCode()
	{
		return this.id;
	}
	
	/**
	 * Returns the ID of this future position.
	 *
	 * @return The ID of this future position.
	 * @since 2017/08/15
	 */
	public int id()
	{
		return this.id;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/08/15
	 */
	@Override
	public String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
			this._string = new WeakReference<>(
				(rv = String.format("Future#%d", this.id)));
		
		return rv;
	}
}

