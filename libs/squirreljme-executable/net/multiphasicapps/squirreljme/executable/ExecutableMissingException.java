// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.executable;

/**
 * This is thrown when the specified class could not be found.
 *
 * @since 2017/01/16
 */
public class ExecutableMissingException
	extends ExecutableLoadException
{
	/**
	 * Initialize the exception with no message or cause.
	 *
	 * @since 2017/04/15
	 */
	public ExecutableMissingException()
	{
	}
	
	/**
	 * Initialize the exception with a message and no cause.
	 *
	 * @param __m The message.
	 * @since 2017/04/15
	 */
	public ExecutableMissingException(String __m)
	{
		super(__m);
	}
	
	/**
	 * Initialize the exception with a message and cause.
	 *
	 * @param __m The message.
	 * @param __c The cause.
	 * @since 2017/04/15
	 */
	public ExecutableMissingException(String __m, Throwable __c)
	{
		super(__m, __c);
	}
	
	/**
	 * Initialize the exception with no message and with a cause.
	 *
	 * @param __c The cause.
	 * @since 2017/04/15
	 */
	public ExecutableMissingException(Throwable __c)
	{
		super(__c);
	}
}

