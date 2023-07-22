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
 * This is thrown when the state of a thread is not valid.
 *
 * @since 2018/02/21
 */
@Api
public class IllegalThreadStateException
	extends RuntimeException
{
	/**
	 * Initializes the exception with no message.
	 *
	 * @since 2018/02/21
	 */
	@Api
	public IllegalThreadStateException()
	{
	}
	
	/**
	 * Initializes the exception with the specified message.
	 *
	 * @param __m The message to use.
	 * @since 2018/02/21
	 */
	@Api
	public IllegalThreadStateException(String __m)
	{
		super(__m);
	}
}

