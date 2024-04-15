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
 * This is thrown when the argument of a method is not correct.
 *
 * @since 2018/10/12
 */
@Api
public class IllegalArgumentException
	extends RuntimeException
{
	/**
	 * Initializes the exception with no message or cause.
	 *
	 * @since 2018/10/12
	 */
	@Api
	public IllegalArgumentException()
	{
	}
	
	/**
	 * Initializes the exception with the given message and no cause.
	 *
	 * @param __m The message.
	 * @since 2018/10/12
	 */
	@Api
	public IllegalArgumentException(String __m)
	{
		super(__m);
	}
	
	/**
	 * Initializes the exception with the given message and cause.
	 *
	 * @param __m The message.
	 * @param __t The cause.
	 * @since 2018/10/12
	 */
	@Api
	public IllegalArgumentException(String __m, Throwable __t)
	{
		super(__m, __t);
	}
	
	/**
	 * Initializes the exception with the given cause and no message.
	 *
	 * @param __t The cause.
	 * @since 2018/10/12
	 */
	@Api
	public IllegalArgumentException(Throwable __t)
	{
		super(__t);
	}
}

