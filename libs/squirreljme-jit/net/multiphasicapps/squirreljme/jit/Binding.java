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
 * This interface describes binding information which is assigned to an
 * abstract code variable to a native representation of where it should be
 * placed and how it is represented. Since this is architecture specific, the
 * actual binding information is not defined.
 *
 * Bindings may be and are expected to be mutable. {@link #equals(Object)}
 * and {@link #hashCode()} must be implemented although it is unspecified how
 * they are to be implemented provided the equality contract is valid.
 *
 * @since 2017/02/18
 */
public interface Binding
{
	/**
	 * Generates a copy of this binding and returns it.
	 *
	 * @return A copy of this binding.
	 * @since 2017/02/18
	 */
	public Binding copy();
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/18
	 */
	@Override
	public boolean equals(Object __o);
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/18
	 */
	@Override
	public int hashCode();
}

