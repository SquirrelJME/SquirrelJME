// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.tac;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;

/**
 * This is thrown when the parameter of a test is not correct.
 *
 * @since 2018/10/06
 */
@SquirrelJMEVendorApi
public class InvalidTestParameterException
	extends InvalidTestException
{
	/**
	 * Initialize the exception with no message or cause.
	 *
	 * @since 2018/10/06
	 */
	@SquirrelJMEVendorApi
	public InvalidTestParameterException()
	{
	}
	
	/**
	 * Initialize the exception with a message and no cause.
	 *
	 * @param __m The message.
	 * @since 2018/10/06
	 */
	@SquirrelJMEVendorApi
	public InvalidTestParameterException(String __m)
	{
		super(__m);
	}
	
	/**
	 * Initialize the exception with a message and cause.
	 *
	 * @param __m The message.
	 * @param __c The cause.
	 * @since 2018/10/06
	 */
	@SquirrelJMEVendorApi
	public InvalidTestParameterException(String __m, Throwable __c)
	{
		super(__m, __c);
	}
	
	/**
	 * Initialize the exception with no message and with a cause.
	 *
	 * @param __c The cause.
	 * @since 2018/10/06
	 */
	@SquirrelJMEVendorApi
	public InvalidTestParameterException(Throwable __c)
	{
		super(__c);
	}
}

