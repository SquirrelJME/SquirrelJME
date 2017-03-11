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
 * This is the base class for bindings which are used to represent state.
 *
 * {@link #equals(Object)} and {@link #hashCode()} must be implemented and
 * they must be equal at the level of this interface and not any sub-interface.
 *
 * @since 2017/03/03
 */
@Deprecated
public interface Binding
{
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

