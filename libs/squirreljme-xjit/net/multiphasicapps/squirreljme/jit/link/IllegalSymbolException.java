// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.link;

/**
 * This is thrown when an input symbol is not valid.
 *
 * @since 2016/03/14
 */
public class IllegalSymbolException
	extends IllegalArgumentException
{
	/**
	 * Initializes exception with the given message.
	 *
	 * @param __msg The exception message.
	 * @since 2016/03/14
	 */
	public IllegalSymbolException(String __msg)
	{
		super(__msg);
	}
	
	/**
	 * Initializes exception with the given message and cause.
	 *
	 * @param __msg The exception message.
	 * @param __c The cause.
	 * @since 2016/03/14
	 */
	public IllegalSymbolException(String __msg, Throwable __c)
	{
		super(__msg, __c);
	}
}

