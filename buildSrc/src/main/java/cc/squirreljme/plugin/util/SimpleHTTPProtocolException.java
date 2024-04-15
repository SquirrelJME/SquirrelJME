// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.util;

import java.io.IOException;

/**
 * This is thrown when there is a HTTP protocol error.
 *
 * @since 2020/06/27
 */
public class SimpleHTTPProtocolException
	extends IOException
{
	/**
	 * Initializes the exception.
	 * 
	 * @param __m The message.
	 * @since 2020/06/27
	 */
	public SimpleHTTPProtocolException(String __m)
	{
		super(__m);
	}
	
	/**
	 * Initializes the exception.
	 * 
	 * @param __m The message.
	 * @param __c The cause.
	 * @since 2020/06/27
	 */
	public SimpleHTTPProtocolException(String __m, Throwable __c)
	{
		super(__m, __c);
	}
}
