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
 * This is thrown when an attempt is made to read from or write to an array
 * index which is out of bounds.
 *
 * @since 2018/12/04
 */
public class ArrayIndexOutOfBoundsException
	extends IndexOutOfBoundsException
{
	/**
	 * Initializes the exception with no message or cause.
	 *
	 * @since 2018/12/04
	 */
	public ArrayIndexOutOfBoundsException()
	{
	}
	
	/**
	 * Initializes the exception with the index specified as the message and
	 * no cause.
	 *
	 * @param __i The out of bounds index.
	 * @since 2018/12/04
	 */
	public ArrayIndexOutOfBoundsException(int __i)
	{
		// {@squirreljme.error ZZ33 Array index out of bounds. (The index)}
		super("ZZ33 " + __i);
	}
	
	/**
	 * Initializes the exception with the given message and no cause.
	 *
	 * @param __m The exception message.
	 * @since 2018/12/04
	 */
	public ArrayIndexOutOfBoundsException(String __m)
	{
		super(__m);
	}
}

