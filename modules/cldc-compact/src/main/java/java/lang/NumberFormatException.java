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
 * This is thrown when the format of a number is not correct.
 *
 * @since 2018/10/12
 */
public class NumberFormatException
	extends IllegalArgumentException
{
	/**
	 * Initializes the exception with no message or cause.
	 *
	 * @since 2018/10/12
	 */
	public NumberFormatException()
	{
	}
	
	/**
	 * Initializes the exception with the given message and no cause.
	 *
	 * @param __m The message.
	 * @since 2018/10/12
	 */
	public NumberFormatException(String __m)
	{
		super(__m);
	}
}

