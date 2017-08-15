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

/**
 * This interface represents a register which is used to provide a virtual
 * representation of a native CPU register.
 *
 * The methods {@link #equals(Object)} and {@link hashCode()} must be
 * implemented.
 *
 * @since 2017/08/15
 */
public interface Register
{
	/**
	 * {@inheritDoc}
	 * @since 2017/08/15
	 */
	@Override
	public abstract boolean equals(Object __o);
	
	/**
	 * {@inheritDoc}
	 * @since 2017/08/15
	 */
	@Override
	public abstract int hashCode();
}

