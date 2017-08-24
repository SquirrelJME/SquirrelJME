// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.bin;

/**
 * This contains a linking point which is used to describe a part of the
 * binary which exports a link which may then be utilized by other sections
 * of the binary as needed.
 *
 * This must implement {@link #equals(Object)} and {@link #hashCode()}.
 *
 * @since 2017/08/24
 */
public interface LinkingPoint
{
	/**
	 * {@inheritDoc}
	 * @since 2017/08/24
	 */
	@Override
	public abstract boolean equals(Object __o);
	
	/**
	 * {@inheritDoc}
	 * @since 2017/08/24
	 */
	@Override
	public abstract int hashCode();
}

