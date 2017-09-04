// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.java;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * This is used as a key which is used to determine which variable share the
 * same initialization state if they are not initialized.
 *
 * @since 2017/09/02
 */
public final class InitializationKey
{
	/** The identifier for this key. */
	protected final int id;
	
	/** The string representation of this key. */
	private volatile Reference<String> _string;
	
	/**
	 * Initializes the key.
	 *
	 * @param __id The key identifier.
	 * @since 2017/09/02
	 */
	public InitializationKey(int __id)
	{
		this.id = __id;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/09/02
	 */
	@Override
	public boolean equals(Object __o)
	{
		// Check
		if (!(__o instanceof InitializationKey))
			return false;
		
		InitializationKey o = (InitializationKey)__o;
		return this.id == o.id;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/09/02
	 */
	@Override
	public int hashCode()
	{
		return this.id;
	}
	
	/**
	 * Returns the initialization key identifier.
	 *
	 * @return The initialization key identifier.
	 * @since 2017/09/02
	 */
	public int id()
	{
		return this.id;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/09/02
	 */
	@Override
	public String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
			this._string = new WeakReference<>(
				(rv = String.format("IKey#%d", this.id)));
		
		return rv;
	}
}

