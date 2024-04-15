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
 * This is thrown when a thread is interrupted.
 *
 * Note that this does not clear the interrupt status of a thread.
 *
 * @since 2018/11/21
 */
@Api
public class InterruptedException
	extends Exception
{
	/**
	 * Initializes the exception with no message or cause.
	 *
	 * @since 2018/11/21
	 */
	@Api
	public InterruptedException()
	{
	}
	
	/**
	 * Initializes the exception with the given message and no cause.
	 *
	 * @param __m The message.
	 * @since 2018/11/21
	 */
	@Api
	public InterruptedException(String __m)
	{
		super(__m);
	}
}

