// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.zip;

import java.io.IOException;

/**
 * This is thrown when there is a problem with a ZIP file.
 *
 * @since 2016/08/02
 */
public class ZipException
	extends IOException
{
	/**
	 * Initializes exception with no message.
	 *
	 * @since 2016/08/02
	 */
	public ZipException()
	{
		super();
	}
	
	/**
	 * Initializes exception with the given message.
	 *
	 * @param __msg The exception message.
	 * @since 2016/08/02
	 */
	public ZipException(String __msg)
	{
		super(__msg);
	}
	
	/**
	 * Initializes exception with the given message and cause.
	 *
	 * @param __msg The exception message.
	 * @param __c The cause.
	 * @since 2016/08/02
	 */
	public ZipException(String __msg, Throwable __c)
	{
		super(__msg, __c);
	}
	
	/**
	 * Initialize the exception with no message and with a cause.
	 *
	 * @param __c The cause.
	 * @since 2017/01/22
	 */
	public ZipException(Throwable __c)
	{
		super(__c);
	}
}

