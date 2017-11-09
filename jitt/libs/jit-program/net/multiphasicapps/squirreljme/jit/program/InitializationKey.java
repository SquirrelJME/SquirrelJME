// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.program;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * This represents an initialization key which is used as an indicator as to
 * which variables have been initialized or not.
 *
 * @since 2017/10/20
 */
public final class InitializationKey
	implements Comparable<InitializationKey>
{
	/** The initialization key index. */
	protected final int index;
	
	/** String representation. */
	private volatile Reference<String> _string;
	
	/**
	 * Initializes the key.
	 *
	 * @param __id The index of the key.
	 * @since 2017/10/20
	 */
	public InitializationKey(int __id)
	{
		this.index = __id;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/10/20
	 */
	@Override
	public int compareTo(InitializationKey __o)
	{
		return this.index - __o.index;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/10/20
	 */
	@Override
	public boolean equals(Object __o)
	{
		if (!(__o instanceof InitializationKey))
			return false;
		
		return this.index == ((InitializationKey)__o).index;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/10/20
	 */
	@Override
	public int hashCode()
	{
		return this.index;
	}
	
	/**
	 * Returns the index of the initialization key.
	 *
	 * @return The initialization key index.
	 * @since 2017/10/20
	 */
	public int index()
	{
		return this.index;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/10/20
	 */
	@Override
	public String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		if (ref == null || null == (rv = ref.get()))
			this._string = new WeakReference<>(
				(rv = String.format("initkey#%d", this.index)));
		
		return rv;
	}
}

