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
 * This is used as a position marker which can later be used with a jump to
 * go to the specified position.
 *
 * The methods {@link #equals(Object)} and {@link hashCode()} must be
 * implemented.
 *
 * @since 2017/08/14
 */
public interface PositionMarker
{
	/**
	 * {@inheritDoc}
	 * @since 2017/08/14
	 */
	@Override
	public abstract boolean equals(Object __o);
	
	/**
	 * {@inheritDoc}
	 * @since 2017/08/14
	 */
	@Override
	public abstract int hashCode();
}

