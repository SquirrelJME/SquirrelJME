// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat.exceptions;

/**
 * This is thrown when a method in a class does not exist.
 *
 * @since 2018/09/03
 */
public class SpringNoSuchMethodException
	extends SpringIncompatibleClassChangeException
{
	/**
	 * Initialize the exception with no message or cause.
	 *
	 * @since 2018/09/03
	 */
	public SpringNoSuchMethodException()
	{
	}
	
	/**
	 * Initialize the exception with a message and no cause.
	 *
	 * @param __m The message.
	 * @since 2018/09/03
	 */
	public SpringNoSuchMethodException(String __m)
	{
		super(__m);
	}
	
	/**
	 * Initialize the exception with a message and cause.
	 *
	 * @param __m The message.
	 * @param __c The cause.
	 * @since 2018/09/03
	 */
	public SpringNoSuchMethodException(String __m, Throwable __c)
	{
		super(__m, __c);
	}
	
	/**
	 * Initialize the exception with no message and with a cause.
	 *
	 * @param __c The cause.
	 * @since 2018/09/03
	 */
	public SpringNoSuchMethodException(Throwable __c)
	{
		super(__c);
	}
}

