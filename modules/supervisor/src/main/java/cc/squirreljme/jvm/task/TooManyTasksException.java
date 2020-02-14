// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.task;

/**
 * This is thrown when there are too many tasks remaining..
 *
 * @since 2019/12/14
 */
public class TooManyTasksException
	extends TaskException
{
	/**
	 * Initializes the exception with no message or cause.
	 *
	 * @since 2019/12/14
	 */
	public TooManyTasksException()
	{
	}
	
	/**
	 * Initializes the exception with the given message and no cause.
	 *
	 * @param __m The message.
	 * @since 2019/12/14
	 */
	public TooManyTasksException(String __m)
	{
		super(__m);
	}
	
	/**
	 * Initializes the exception with the given message and cause.
	 *
	 * @param __m The message.
	 * @param __t The cause.
	 * @since 2019/12/14
	 */
	public TooManyTasksException(String __m, Throwable __t)
	{
		super(__m, __t);
	}
	
	/**
	 * Initializes the exception with the given cause and no message.
	 *
	 * @param __t The cause.
	 * @since 2019/12/14
	 */
	public TooManyTasksException(Throwable __t)
	{
		super(__t);
	}
}

