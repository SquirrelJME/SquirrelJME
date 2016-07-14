// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.java.bytecode;

/**
 * This represents an exception which may be thrown if the Java byte code is
 * not valid or does not verify correctly.
 *
 * @since 2016/05/11
 */
public class BCException
	extends RuntimeException
{
	/**
	 * Initializes the exception with no message or cause.
	 *
	 * @since 2016/05/11
	 */
	public BCException()
	{
		super();
	}
	
	/**
	 * Initializes the exception with the given message.
	 *
	 * @param __msg The exception message.
	 * @since 2016/05/11
	 */
	public BCException(String __msg)
	{
		super(__msg);
	}
	
	/**
	 * Initializes the exception with the given message and cause.
	 *
	 * @param __msg The exception message.
	 * @param __c The cause.
	 * @since 2016/05/11
	 */
	public BCException(String __msg, Throwable __c)
	{
		super(__msg, __c);
	}
	
	/**
	 * Initializes the exception with the given cause and no message.
	 *
	 * @param __c The cause of the exception.
	 * @since 2016/05/11
	 */
	public BCException(Throwable __c)
	{
		super(__c);
	}
}

