// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.zip.blockreader;

import net.multiphasicapps.zip.ZipException;

/**
 * This is thrown when an entry within a ZIP file is not found.
 *
 * @since 2017/01/22
 */
public class ZipEntryNotFoundException
	extends ZipException
{
	/**
	 * Initialize the exception with no message or cause.
	 *
	 * @since 2017/01/22
	 */
	public ZipEntryNotFoundException()
	{
	}
	
	/**
	 * Initialize the exception with a message and no cause.
	 *
	 * @param __m The message.
	 * @since 2017/01/22
	 */
	public ZipEntryNotFoundException(String __m)
	{
		super(__m);
	}
	
	/**
	 * Initialize the exception with a message and cause.
	 *
	 * @param __m The message.
	 * @param __c The cause.
	 * @since 2017/01/22
	 */
	public ZipEntryNotFoundException(String __m, Throwable __c)
	{
		super(__m, __c);
	}
	
	/**
	 * Initialize the exception with no message and with a cause.
	 *
	 * @param __c The cause.
	 * @since 2017/01/22
	 */
	public ZipEntryNotFoundException(Throwable __c)
	{
		super(__c);
	}
}

