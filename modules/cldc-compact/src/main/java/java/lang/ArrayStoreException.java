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
 * This is thrown when the given value cannot be stored into the given array
 * because it is of the wrong type.
 *
 * @since 2018/12/04
 */
public class ArrayStoreException
	extends RuntimeException
{
	/**
	 * Initializes the exception with no message or cause.
	 *
	 * @since 2018/12/04
	 */
	public ArrayStoreException()
	{
	}
	
	/**
	 * Initializes the exception with the given message and no cause.
	 *
	 * @since 2018/12/04
	 */
	public ArrayStoreException(String __m)
	{
		super(__m);
	}
}

