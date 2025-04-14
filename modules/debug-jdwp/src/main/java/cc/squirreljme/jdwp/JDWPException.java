// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp;

/**
 * This exception is thrown if there is any issue with the JDWP connection.
 *
 * @since 2021/03/10
 */
public class JDWPException
	extends RuntimeException
{
	/**
	 * Initializes the exception.
	 * 
	 * @since 2024/01/26
	 */
	public JDWPException()
	{
	}
	
	/**
	 * Initializes the exception.
	 * 
	 * @param __m The message used.
	 * @since 2021/03/10
	 */
	public JDWPException(String __m)
	{
		super(__m);
	}
	
	/**
	 * Initializes the exception.
	 * 
	 * @param __m The message used.
	 * @param __c The cause of the exception.
	 * @since 2021/03/10
	 */
	public JDWPException(String __m, Throwable __c)
	{
		super(__m, __c);
	}
}
