// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.builder.support;

/**
 * This is thrown when the specified binary is not valid.
 *
 * @since 2017/11/29
 */
public class OutOfDateBinaryException
	extends InvalidBinaryException
{
	/**
	 * Initialize the exception with no message or cause.
	 *
	 * @since 2017/11/29
	 */
	public OutOfDateBinaryException()
	{
	}
	
	/**
	 * Initialize the exception with a message and no cause.
	 *
	 * @param __m The message.
	 * @since 2017/11/29
	 */
	public OutOfDateBinaryException(String __m)
	{
		super(__m);
	}
	
	/**
	 * Initialize the exception with a message and cause.
	 *
	 * @param __m The message.
	 * @param __c The cause.
	 * @since 2017/11/29
	 */
	public OutOfDateBinaryException(String __m, Throwable __c)
	{
		super(__m, __c);
	}
	
	/**
	 * Initialize the exception with no message and with a cause.
	 *
	 * @param __c The cause.
	 * @since 2017/11/29
	 */
	public OutOfDateBinaryException(Throwable __c)
	{
		super(__c);
	}
}

