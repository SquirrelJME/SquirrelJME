// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.tac;

/**
 * This is an indicator that an exception was thrown.
 *
 * @since 2018/10/06
 */
final class __ExceptionThrown__
{
	/**
	 * {@inheritDoc}
	 * @since 2018/10/06
	 */
	@Override
	public final boolean equals(Object __o)
	{
		return __o == this || (__o instanceof __ExceptionThrown__);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/06
	 */
	@Override
	public final int hashCode()
	{
		return 0;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/06
	 */
	@Override
	public final String toString()
	{
		return "ExceptionThrown";
	}
}

