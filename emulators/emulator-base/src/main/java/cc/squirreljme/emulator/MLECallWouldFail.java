// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator;

/**
 * This indicates that the MLE call if it were to go through would fail.
 *
 * @since 2020/07/06
 */
public class MLECallWouldFail
	extends RuntimeException
{
	/**
	 * Initializes the exception.
	 * 
	 * @param __m The message.
	 * @since 2020/07/06
	 */
	public MLECallWouldFail(String __m)
	{
		super(__m);
	}
	
	/**
	 * Initializes the exception.
	 * 
	 * @param __m The message.
	 * @param __c The cause.
	 * @since 2020/07/06
	 */
	public MLECallWouldFail(String __m, Throwable __c)
	{
		super(__m, __c);
	}
}
