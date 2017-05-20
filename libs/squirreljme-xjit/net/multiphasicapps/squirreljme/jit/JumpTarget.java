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

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * This is used to store jump targets to other instructions in a type safe
 * manner so that raw integers do not cause confusion as to what an argument
 * is.
 *
 * @since 2017/05/20
 */
public final class JumpTarget
	implements Comparable<JumpTarget>
{
	/** The target of the jump. */
	protected final int address;
	
	/** String cache. */
	private volatile Reference<String> _string;
	
	/**
	 * Initializes the jump target.
	 *
	 * @param __a The target of the jump.
	 * @since 2017/05/20
	 */
	public JumpTarget(int __a)
	{
		this.address = __a;
	}
	
	/**
	 * Returns the jump target address.
	 *
	 * @return The target address.
	 * @since 2017/05/20
	 */
	public int address()
	{
		return this.address;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/05/20
	 */
	@Override
	public int compareTo(JumpTarget __b)
	{
		return this.address - __b.address;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/05/20
	 */
	@Override
	public boolean equals(Object __o)
	{
		// Check
		if (!(__o instanceof JumpTarget))
			return false;
		
		return this.address == ((JumpTarget)__o).address;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/05/20
	 */
	@Override
	public int hashCode()
	{
		return this.address;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/05/20
	 */
	@Override
	public String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		if (ref == null || null == (rv = ref.get()))
			this._string = new WeakReference<>(
				(rv = String.format("@%d", this.address)));
		
		return rv;
	}
}

