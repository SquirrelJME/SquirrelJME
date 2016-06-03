// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.terp;

/**
 * This exception is thrown when there is a problem with the interpreter.
 *
 * @since 2016/06/03
 */
public class InterpreterException
	extends RuntimeException
{
	/**
	 * Initializes the exception with no message or cause.
	 *
	 * @since 2016/06/03
	 */
	public InterpreterException()
	{
	}
	
	/**
	 * Initializes the exception with the given message and no cause.
	 *
	 * @param __m The message to use.
	 * @since 2016/06/03
	 */
	public InterpreterException(String __m)
	{
		super(__m);
	}
	
	/**
	 * Initializes the exception with given message and cause.
	 *
	 * @param __m The message to use.
	 * @param __c The cause of the exception.
	 * @since 2016/06/03
	 */
	public InterpreterException(String __m, Throwable __c)
	{
		super(__m, __c);
	}
	
	/**
	 * Initializes the exception with no message and the given cause.
	 *
	 * @param __c The cause of the exception.
	 * @since 2016/06/03
	 */
	public InterpreterException(Throwable __c)
	{
		super(__c);
	}
}

