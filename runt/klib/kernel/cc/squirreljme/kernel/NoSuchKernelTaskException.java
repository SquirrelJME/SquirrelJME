// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.kernel;

/**
 * This is thrown when a request is made to use a task which does not exist.
 *
 * @since 2018/01/02
 */
public class NoSuchKernelTaskException
	extends RuntimeException
{
	/**
	 * Initializes the exception with no message.
	 *
	 * @since 2018/01/02
	 */
	public NoSuchKernelTaskException()
	{
	}
	
	/**
	 * Initializes the exception with the given message.
	 *
	 * @param __m The message to use.
	 * @since 2018/01/02
	 */
	public NoSuchKernelTaskException(String __m)
	{
		super(__m);
	}
}

