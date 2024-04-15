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
 * This is thrown when a class has not been found.
 *
 * @since 2018/12/04
 */
@Api
public class ClassNotFoundException
	extends Exception
{
	/**
	 * Initializes the exception with no message or cause.
	 *
	 * @since 2018/12/04
	 */
	@Api
	public ClassNotFoundException()
	{
	}
	
	/**
	 * Initializes the exception with the given message and no cause.
	 *
	 * @param __m The message.
	 * @since 2018/09/16
	 */
	@Api
	public ClassNotFoundException(String __m)
	{
		super(__m);
	}
	
	/**
	 * Initializes the exception with the given message and cause.
	 *
	 * @param __m The message.
	 * @param __t The cause.
	 * @since 2018/09/16
	 */
	@Api
	public ClassNotFoundException(String __m, Throwable __t)
	{
		super(__m, __t);
	}
	
	/**
	 * This returns the cause of the exception.
	 *
	 * @return The cause of the exception.
	 * @since 2018/12/04
	 */
	@Api
	public Throwable getException()
	{
		return this.getCause();
	}
}

