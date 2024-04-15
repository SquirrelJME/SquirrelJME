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
 * This is thrown when an attempt is made to read from an index within a string
 * which is not within bounds.
 *
 * @since 2018/09/16
 */
@Api
public class StringIndexOutOfBoundsException
	extends IndexOutOfBoundsException
{
	/**
	 * Initializes the exception with no message and no cause.
	 *
	 * @since 2018/09/16
	 */
	@Api
	public StringIndexOutOfBoundsException()
	{
	}
	
	/**
	 * Initailizes the exception with the given index with no cause.
	 *
	 * @param __dx The index to reference.
	 * @since 2018/09/16
	 */
	@Api
	public StringIndexOutOfBoundsException(int __dx)
	{
		/* {@squirreljme.error ZZ1v String index out of bounds. (The index)} */
		super("ZZ1v " + __dx);
	}
	
	/**
	 * Initializes the exception with given message and no cause.
	 *
	 * @param __m The message used.
	 * @since 2018/09/16
	 */
	@Api
	public StringIndexOutOfBoundsException(String __m)
	{
		super(__m);
	}
}

