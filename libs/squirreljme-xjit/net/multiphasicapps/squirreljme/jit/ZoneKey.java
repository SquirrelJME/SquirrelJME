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
 * This is a key which is used in zones to specify which part of the method
 * it has code for. This would in most cases be a range of instructions but
 * it may also be exception handlers and handling for synchronization.
 *
 * @since 2017/05/28
 */
public abstract class ZoneKey
{
	/**
	 * {@inheritDoc}
	 * @since 2017/05/28
	 */
	@Override
	public abstract boolean equals(Object __o);
	
	/**
	 * {@inheritDoc}
	 * @since 2017/05/28
	 */
	@Override
	public abstract int hashCode();
}

