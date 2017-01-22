// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.build.base;

import java.io.IOException;

/**
 * This is thrown when an entry within a file directory is not found.
 *
 * @since 2017/01/22
 */
public class FileEntryNotFoundException
	extends IOException
{
	/**
	 * Initialize the exception with no message or cause.
	 *
	 * @since 2017/01/22
	 */
	public FileEntryNotFoundException()
	{
	}
	
	/**
	 * Initialize the exception with a message and no cause.
	 *
	 * @param __m The message.
	 * @since 2017/01/22
	 */
	public FileEntryNotFoundException(String __m)
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
	public FileEntryNotFoundException(String __m, Throwable __c)
	{
		super(__m, __c);
	}
	
	/**
	 * Initialize the exception with no message and with a cause.
	 *
	 * @param __c The cause.
	 * @since 2017/01/22
	 */
	public FileEntryNotFoundException(Throwable __c)
	{
		super(__c);
	}
}

