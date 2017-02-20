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

/**
 * This represents a global binding which is associated with a
 * {@link CacheState} as a whole. This may be used to store information related
 * to the stack such as how deep it is for example.
 *
 * Bindings may be and are expected to be mutable. {@link #equals(Object)}
 * and {@link #hashCode()} must be implemented although it is unspecified how
 * they are to be implemented provided the equality contract is valid.
 *
 * @since 2017/02/20
 */
public interface GlobalBinding
{
	/**
	 * Generates a copy of this binding and returns it.
	 *
	 * @return A copy of this binding.
	 * @since 2017/02/20
	 */
	public GlobalBinding copy();
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/20
	 */
	@Override
	public boolean equals(Object __o);
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/20
	 */
	@Override
	public int hashCode();
}

