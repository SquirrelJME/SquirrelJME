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
 * This is thrown when the test cannot be ran perhaps due to lack of support
 * or otherwise a virtual machine which does not support things.
 *
 * @since 2019/03/04
 */
@SquirrelJMEVendorApi
public class UntestableException
	extends RuntimeException
{
	/**
	 * Initialize the exception with no message or cause.
	 *
	 * @since 2019/03/04
	 */
	@SquirrelJMEVendorApi
	public UntestableException()
	{
	}
	
	/**
	 * Initialize the exception with a message and no cause.
	 *
	 * @param __m The message.
	 * @since 2019/03/04
	 */
	@SquirrelJMEVendorApi
	public UntestableException(String __m)
	{
		super(__m);
	}
	
	/**
	 * Initialize the exception with a message and cause.
	 *
	 * @param __m The message.
	 * @param __c The cause.
	 * @since 2019/03/04
	 */
	@SquirrelJMEVendorApi
	public UntestableException(String __m, Throwable __c)
	{
		super(__m, __c);
	}
	
	/**
	 * Initialize the exception with no message and with a cause.
	 *
	 * @param __c The cause.
	 * @since 2019/03/04
	 */
	@SquirrelJMEVendorApi
	public UntestableException(Throwable __c)
	{
		super(__c);
	}
}

