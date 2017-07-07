// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.bin.cond;

/**
 * This is the base interface for a condition which is used to verifiy whether
 * a condition is met by the virtual machine.
 *
 * @since 2017/07/07
 */
public abstract class Condition
{
	/**
	 * Initialized within this package only.
	 *
	 * @since 2017/07/07
	 */
	Condition()
	{
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/07/07
	 */
	@Override
	public abstract boolean equals(Object __o);
	
	/**
	 * {@inheritDoc}
	 * @since 2017/07/07
	 */
	@Override
	public abstract int hashCode();
	
	/**
	 * {@inheritDoc}
	 * @since 2017/07/07
	 */
	@Override
	public abstract String toString();
}

