// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.lang;

/**
 * This is usually thrown when an attempt is made to use a monitor which is
 * not owned by the current thread.
 *
 * @since 2018/12/04
 */
public class IllegalMonitorStateException
	extends RuntimeException
{
	/**
	 * Initializes the exception with no message or cause.
	 *
	 * @since 2018/12/04
	 */
	public IllegalMonitorStateException()
	{
	}
	
	/**
	 * Initializes the exception with the given message and no cause.
	 *
	 * @since 2018/12/04
	 */
	public IllegalMonitorStateException(String __m)
	{
		super(__m);
	}
}

