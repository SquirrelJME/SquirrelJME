// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.expanded;

/**
 * This interface is associated with .
 *
 * Classes implementing this interface must extend and correctly implement
 * {@link #equals(Object)} and {@link #hashCode()}.
 *
 * @since 2017/08/07
 */
public interface BasicBlockKey
{
	/**
	 * {@inheritDoc}
	 * @since 2017/08/07
	 */
	@Override
	public abstract boolean equals(Object __o);
	
	/**
	 * {@inheritDoc}
	 * @since 2017/08/07
	 */
	@Override
	public abstract int hashCode();
}

