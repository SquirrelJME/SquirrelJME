// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.lang;

/**
 * This is thrown when the state of a thread is not valid.
 *
 * @since 2018/02/21
 */
public class IllegalThreadStateException
	extends RuntimeException
{
	/**
	 * Initializes the exception with no message.
	 *
	 * @since 2018/02/21
	 */
	public IllegalThreadStateException()
	{
	}
	
	/**
	 * Initializes the exception with the specified message.
	 *
	 * @param __m The message to use.
	 * @since 2018/02/21
	 */
	public IllegalThreadStateException(String __m)
	{
		super(__m);
	}
}

