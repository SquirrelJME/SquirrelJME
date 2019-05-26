// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.lang;

/**
 * This is thrown when there is an out of bounds array access.
 *
 * @since 2019/05/26
 */
public class ArrayIndexOutOfBoundsException
	extends IndexOutOfBoundsException
{
	/**
	 * Initializes the exception with no message or cause.
	 *
	 * @since 2019/05/26
	 */
	public ArrayIndexOutOfBoundsException()
	{
	}
	
	/**
	 * Initializes the exception with the given message and no cause.
	 *
	 * @param __m The message.
	 * @since 2019/05/26
	 */
	public ArrayIndexOutOfBoundsException(String __m)
	{
		super(__m);
	}
	
	/**
	 * Initializes the exception with the given message and no cause.
	 *
	 * @param __m The message.
	 * @since 2019/05/26
	 */
	public ArrayIndexOutOfBoundsException(int __m)
	{
		super(Integer.valueOf(__m).toString());
	}
}

