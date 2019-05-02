// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.builder.support;

/**
 * This is thrown when source code is not available during compilation.
 *
 * @since 2019/05/02
 */
public class NoSourceAvailableException
	extends InvalidBinaryException
{
	/**
	 * Initialize the exception with no message or cause.
	 *
	 * @since 2019/05/02
	 */
	public NoSourceAvailableException()
	{
	}
	
	/**
	 * Initialize the exception with a message and no cause.
	 *
	 * @param __m The message.
	 * @since 2019/05/02
	 */
	public NoSourceAvailableException(String __m)
	{
		super(__m);
	}
	
	/**
	 * Initialize the exception with a message and cause.
	 *
	 * @param __m The message.
	 * @param __c The cause.
	 * @since 2019/05/02
	 */
	public NoSourceAvailableException(String __m, Throwable __c)
	{
		super(__m, __c);
	}
	
	/**
	 * Initialize the exception with no message and with a cause.
	 *
	 * @param __c The cause.
	 * @since 2019/05/02
	 */
	public NoSourceAvailableException(Throwable __c)
	{
		super(__c);
	}
}

