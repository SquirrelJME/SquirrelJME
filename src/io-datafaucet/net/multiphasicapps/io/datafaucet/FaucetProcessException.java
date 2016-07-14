// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.io.datafaucet;

/**
 * This is thrown when a processing error occurs while attempting to fill bytes
 * to be output by the drain.
 *
 * @since 2016/04/30
 */
public class FaucetProcessException
	extends RuntimeException
{
	/**
	 * Initializes the exception with no message.
	 *
	 * @since 2016/04/30
	 */
	public FaucetProcessException()
	{
		super();
	}
	
	/**
	 * Initializes the exception with the given message.
	 *
	 * @param __m The message to use.
	 * @since 2016/04/30
	 */
	public FaucetProcessException(String __m)
	{
		super(__m);
	}
	
	/**
	 * Initializes the exception with the given message and cause.
	 *
	 * @param __m The message to use.
	 * @param __c The cause of this exception.
	 * @since 2016/04/30
	 */
	public FaucetProcessException(String __m, Throwable __c)
	{
		super(__m, __c);
	}
	
	/**
	 * Initializes the exception with the given cause.
	 *
	 * @param __c The cause of this exception.
	 * @since 2016/04/30
	 */
	public FaucetProcessException(Throwable __c)
	{
		super(__c);
	}
}

