// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.lang;

import cc.squirreljme.runtime.cldc.annotation.Api;

/**
 * This is thrown when an attempt is made to read from or write to an array
 * index which is out of bounds.
 *
 * @since 2018/12/04
 */
@Api
public class ArrayIndexOutOfBoundsException
	extends IndexOutOfBoundsException
{
	/**
	 * Initializes the exception with no message or cause.
	 *
	 * @since 2018/12/04
	 */
	@Api
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
	@Api
	public ArrayIndexOutOfBoundsException(int __i)
	{
		/* {@squirreljme.error ZZ0r Array index out of bounds. (The index)} */
		super("ZZ0r " + __i);
	}
	
	/**
	 * Initializes the exception with the given message and no cause.
	 *
	 * @param __m The exception message.
	 * @since 2018/12/04
	 */
	@Api
	public ArrayIndexOutOfBoundsException(String __m)
	{
		super(__m);
	}
}

