// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.ui;

/**
 * This exception is thrown when there is a problem with the user interface.
 *
 * @since 2016/05/21
 */
public final class UIException
	extends RuntimeException
{
	/**
	 * Intializes the exception with no message or cause.
	 *
	 * @since 2016/05/22
	 */
	public UIException()
	{
	}
	
	/**
	 * Intializes the exception with the given message.
	 *
	 * @param __m The message to use.
	 * @since 2016/05/22
	 */
	public UIException(String __m)
	{
		super(__m);
	}
	
	/**
	 * Intializes the exception with the given message and cause.
	 *
	 * @param __m The message to use.
	 * @param __c The cause to use.
	 * @since 2016/05/22
	 */
	public UIException(String __m, Throwable __c)
	{
		super(__m, __c);
	}
	
	/**
	 * Intializes the exception with the given cause.
	 *
	 * @param __c The cause to use.
	 * @since 2016/05/22
	 */
	public UIException(Throwable __c)
	{
		super(__c);
	}
}

