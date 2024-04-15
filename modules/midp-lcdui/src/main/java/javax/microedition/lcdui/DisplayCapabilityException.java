// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

import cc.squirreljme.runtime.cldc.annotation.Api;

/**
 * This is thrown when an attempt is made to do something on a display which
 * does not support a given capability.
 *
 * @since 2018/11/17
 */
@Api
public class DisplayCapabilityException
	extends RuntimeException
{
	/**
	 * Initializes the exception with no message or cause.
	 *
	 * @since 2018/11/17
	 */
	@Api
	public DisplayCapabilityException()
	{
	}
	
	/**
	 * Initializes the exception with the given message and no cause.
	 *
	 * @param __m The message.
	 * @since 2018/11/17
	 */
	@Api
	public DisplayCapabilityException(String __m)
	{
		super(__m);
	}
}

