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
 * This represents memory pointing to a fixed location.
 *
 * @since 2019/02/23
 */
public final class FixedMemoryLocation
	implements MemoryLocation
{
	/** The address of the location. */
	protected final long address;
	
	/** String representation. */
	private Reference<String> _string;
	
	/**
	 * Initializes the location.
	 *
	 * @param __p The memory address.
	 * @since 2019/02/23
	 */
	public FixedMemoryLocation(long __p)
	{
		this.address = __p;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/02/23
	 */
	@Override
	public final boolean equals(Object __o)
	{
		if (this == __o)
			return true;
		
		if (!(__o instanceof FixedMemoryLocation))
			return false;
		
		FixedMemoryLocation o = (FixedMemoryLocation)__o;
		return this.address == o.address;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/02/23
	 */
	@Override
	public final int hashCode()
	{
		long address = this.address;
		return (int)((address >>> 32) | address);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/02/23
	 */
	@Override
	public final String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		if (ref == null || null == (rv = ref.get()))
			this._string = new WeakReference<>((rv =
				String.format("m@%x", this.address)));
		
		return rv;
	}
}

