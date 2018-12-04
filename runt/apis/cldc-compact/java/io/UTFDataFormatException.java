// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.io;

/**
 * This is thrown when the format of a UTF string is not correct.
 *
 * @since 2018/12/03
 */
public class UTFDataFormatException
	extends IOException
{
	/**
	 * Initializes the exception with no message or cause.
	 *
	 * @since 2018/12/03
	 */
	public UTFDataFormatException()
	{
	}
	
	/**
	 * Initializes the exception with the given cause.
	 *
	 * @param __m The message to use.
	 * @since 2018/12/03
	 */
	public UTFDataFormatException(String __m)
	{
		super(__m);
	}
}

