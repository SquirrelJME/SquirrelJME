// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.ssjit;

/**
 * This is thrown when JIT compilation has failed. This may be for any
 * reason such as if the input class fails to verify or if for example if
 * unsupported features (such as invokedynamic) are used.
 *
 * @since 2016/06/25
 */
public class SSJITException
	extends RuntimeException
{
	/**
	 * Initialize the exception with no message or cause.
	 *
	 * @since 2016/06/25
	 */
	public SSJITException()
	{
	}
	
	/**
	 * Initialize the exception with a message and no cause.
	 *
	 * @param __m The message.
	 * @since 2016/06/25
	 */
	public SSJITException(String __m)
	{
		super(__m);
	}
	
	/**
	 * Initialize the exception with a message and cause.
	 *
	 * @param __m The message.
	 * @param __c The cause.
	 * @since 2016/06/25
	 */
	public SSJITException(String __m, Throwable __c)
	{
		super(__m, __c);
	}
	
	/**
	 * Initialize the exception with no message and with a cause.
	 *
	 * @param __c The cause.
	 * @since 2016/06/25
	 */
	public SSJITException(Throwable __c)
	{
		super(__c);
	}
}

