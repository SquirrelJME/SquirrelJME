// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classprogram;

/**
 * This is thrown when the byte code of a class is badly formatted.
 *
 * @since 2016/03/29
 */
public class CPProgramException
	extends RuntimeException
{
	/**
	 * Initializes the exception with no message or cause.
	 *
	 * @since 2016/03/29
	 */
	public CPProgramException()
	{
	}
	
	/**
	 * Initializes the exception with the given message.
	 *
	 * @param __msg The exception message.
	 * @since 2016/03/29
	 */
	public CPProgramException(String __msg)
	{
		super(__msg);
	}
	
	/**
	 * Initializes the exception with the given message and cause.
	 *
	 * @param __msg The exception message.
	 * @param __c The cause.
	 * @since 2016/03/29
	 */
	public CPProgramException(String __msg, Throwable __c)
	{
		super(__msg, __c);
	}
	
	/**
	 * Initializes the exception with the given cause and no message.
	 *
	 * @param __c The cause of the exception.
	 * @since 2016/03/29
	 */
	public CPProgramException(Throwable __c)
	{
		super(__c);
	}
}

