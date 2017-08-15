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
 * This is used for marking future positions to potentially be used for jumps
 * to instruction addresses which are in the future. Note that despite being
 * intended to be used as a future type of object, it may be used for past
 * events also provided it is created beforehand.
 *
 * The methods {@link #equals(Object)} and {@link hashCode()} must be
 * implemented.
 *
 * @since 2017/08/15
 */
public interface FuturePositionMarker
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

