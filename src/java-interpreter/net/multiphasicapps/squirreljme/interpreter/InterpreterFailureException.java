// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.interpreter;

/**
 * This is the base class for exceptions which are thrown by the interoreter to
 * indicate that it has an incorrect state or there is a problem with it.
 *
 * @since 2016/03/05
 */
public class InterpreterFailureException
	extends RuntimeException
{
	/**
	 * Initializes the exception with no message.
	 *
	 * @since 2016/03/05
	 */
	public InterpreterFailureException()
	{
		super();
	}
	
	/**
	 * Initializes exception with the given message.
	 *
	 * @param __msg The exception message.
	 * @since 2016/03/02
	 */
	public InterpreterFailureException(String __msg)
	{
		super(__msg);
	}
	
	/**
	 * Initializes exception with the given message and cause.
	 *
	 * @param __msg The exception message.
	 * @param __c The cause.
	 * @since 2016/03/02
	 */
	public InterpreterFailureException(String __msg, Throwable __c)
	{
		super(__msg, __c);
	}
}

