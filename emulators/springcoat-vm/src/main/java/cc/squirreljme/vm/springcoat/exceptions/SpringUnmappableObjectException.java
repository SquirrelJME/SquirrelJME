// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat.exceptions;

import cc.squirreljme.vm.springcoat.SpringException;

/**
 * This is thrown when an object cannot be mapped.
 *
 * @since 2024/08/02
 */
public class SpringUnmappableObjectException
	extends SpringFatalException
{
	/**
	 * Initialize the exception with no message or cause.
	 *
	 * @since 2024/08/02
	 */
	public SpringUnmappableObjectException()
	{
	}
	
	/**
	 * Initialize the exception with a message and no cause.
	 *
	 * @param __m The message.
	 * @since 2024/08/02
	 */
	public SpringUnmappableObjectException(String __m)
	{
		super(__m);
	}
	
	/**
	 * Initialize the exception with a message and cause.
	 *
	 * @param __m The message.
	 * @param __c The cause.
	 * @since 2024/08/02
	 */
	public SpringUnmappableObjectException(String __m, Throwable __c)
	{
		super(__m, __c);
	}
	
	/**
	 * Initialize the exception with no message and with a cause.
	 *
	 * @param __c The cause.
	 * @since 2024/08/02
	 */
	public SpringUnmappableObjectException(Throwable __c)
	{
		super(__c);
	}
}

