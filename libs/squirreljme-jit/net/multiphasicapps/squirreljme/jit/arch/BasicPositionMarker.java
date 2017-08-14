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
 * This is a position marker which stores only a single value.
 *
 * @since 2017/08/14
 */
public final class BasicPositionMarker
	implements PositionMarker	
{
	/** The marked position. */
	protected final int position;
	
	/** String representation. */
	private volatile Reference<String> _string;
	
	/**
	 * Initializes the position marker.
	 *
	 * @param __p The position to mark.
	 * @since 2017/08/14
	 */
	public BasicPositionMarker(int __p)
	{
		this.position = __p;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/08/14
	 */
	@Override
	public final boolean equals(Object __o)
	{
		// Check
		if (!(__o instanceof BasicPositionMarker))
			return false;
		
		return this.position == ((BasicPositionMarker)__o).position;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/08/14
	 */
	@Override
	public final int hashCode()
	{
		return this.position;
	}
	
	/**
	 * Returns the position that this marks.
	 *
	 * @return The marked position.
	 * @since 2017/08/14
	 */
	public final int position()
	{
		return this.position;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/08/14
	 */
	@Override
	public final String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		// Check
		if (ref == null || null == (rv = ref.get()))
			this._string = new WeakReference<>(
				(rv = String.format("@%d", this.position)));
		
		return rv;
	}
}

