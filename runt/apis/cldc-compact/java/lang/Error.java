// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.lang;

/**
 * This is the base class for non-checked errors.
 *
 * @since 2016/02/26
 */
public class Error
	extends Throwable
{
	/**
	 * Initializes the error with no message or cause.
	 *
	 * @since 2018/09/16
	 */
	public Error()
	{
	}
	
	/**
	 * Initializes the error with a message and no cause.
	 *
	 * @param __m The message to use.
	 * @since 2018/09/16
	 */
	public Error(String __m)
	{
		super(__m);
	}
	
	/**
	 * Initializes the error with a message and the given cause.
	 *
	 * @param __m The message to use.
	 * @param __t The cause of this error.
	 * @since 2018/09/16
	 */
	public Error(String __m, Throwable __t)
	{
		super(__m);
	}
	
	/**
	 * Initializes the error with no message and the given cause.
	 *
	 * @param __m The message to use.
	 * @param __t The cause of this error.
	 * @since 2018/09/16
	 */
	public Error(Throwable __t)
	{
		super(__t);
	}
}

