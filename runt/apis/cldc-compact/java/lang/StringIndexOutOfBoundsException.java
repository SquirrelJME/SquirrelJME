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
 * This is thrown when an attempt is made to read from an index within a string
 * which is not within bounds.
 *
 * @since 2018/09/16
 */
public class StringIndexOutOfBoundsException
	extends IndexOutOfBoundsException
{
	/**
	 * Initializes the exception with no message and no cause.
	 *
	 * @since 2018/09/16
	 */
	public StringIndexOutOfBoundsException()
	{
	}
	
	/**
	 * Initailizes the exception with the given index with no cause.
	 *
	 * @param __dx The index to reference.
	 * @since 2018/09/16
	 */
	public StringIndexOutOfBoundsException(int __dx)
	{
		// {@squirreljme.error ZZ10 String index out of bounds. (The index)}
		super("ZZ10 " + __dx);
	}
	
	/**
	 * Initializes the exception with given message and no cause.
	 *
	 * @param __m The message used.
	 * @since 2018/09/16
	 */
	public StringIndexOutOfBoundsException(String __m)
	{
		super(__m);
	}
}

