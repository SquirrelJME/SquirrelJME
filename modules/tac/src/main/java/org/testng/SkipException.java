// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package org.testng;

/**
 * This is a virtual class for TestNG's {@code SkipException} and as such this
 * is ignored.
 *
 * @since 2020/06/16
 */
public class SkipException
	extends RuntimeException
{
	/**
	 * Initializes the exception.
	 *
	 * @param __m The message.
	 * @since 2020/06/15
	 */
	public SkipException(String __m)
	{
		super(__m);
	}
	
	/**
	 * Initializes the exception.
	 *
	 * @param __m The message.
	 * @param __c The cause.
	 * @since 2020/06/15
	 */
	public SkipException(String __m, Throwable __c)
	{
		super(__m, __c);
	}
	
	/**
	 * If this was skipped.
	 *
	 * @return Always {@code true}.
	 * @since 2020/06/16
	 */
	public boolean isSkip()
	{
		return true;
	}
	
	/**
	 * Not implemented.
	 *
	 * @since 2020/06/16
	 */
	protected void reduceStackTrace()
	{
	}
	
	/**
	 * Not implemented.
	 *
	 * @since 2020/06/16
	 */
	protected void restoreStackTrace()
	{
	}
}
