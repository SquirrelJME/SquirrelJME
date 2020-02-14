// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirrelquarrel.units;

/**
 * This is thrown when the unit a pointer points to, has been deleted.
 *
 * @since 2017/02/14
 */
public class UnitDeletedException
	extends RuntimeException
{
	/**
	 * Initialize the exception with no message or cause.
	 *
	 * @since 2017/02/14
	 */
	public UnitDeletedException()
	{
	}
	
	/**
	 * Initialize the exception with a message and no cause.
	 *
	 * @param __m The message.
	 * @since 2017/02/14
	 */
	public UnitDeletedException(String __m)
	{
		super(__m);
	}
	
	/**
	 * Initialize the exception with a message and cause.
	 *
	 * @param __m The message.
	 * @param __c The cause.
	 * @since 2017/02/14
	 */
	public UnitDeletedException(String __m, Throwable __c)
	{
		super(__m, __c);
	}
	
	/**
	 * Initialize the exception with no message and with a cause.
	 *
	 * @param __c The cause.
	 * @since 2017/02/14
	 */
	public UnitDeletedException(Throwable __c)
	{
		super(__c);
	}
}

