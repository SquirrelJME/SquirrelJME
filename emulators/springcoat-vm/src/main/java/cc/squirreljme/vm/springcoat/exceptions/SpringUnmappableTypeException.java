// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat.exceptions;

/**
 * This is thrown when a type is not mappable.
 *
 * @since 2024/08/02
 */
public class SpringUnmappableTypeException
	extends SpringFatalException
{
	/**
	 * Initializes the exception.
	 * 
	 * @since 2024/08/02
	 */
	public SpringUnmappableTypeException()
	{
	}
	
	/**
	 * Initializes the exception.
	 * 
	 * @param __m The message.
	 * @since 2024/08/02
	 */
	public SpringUnmappableTypeException(String __m)
	{
		super(__m);
	}
	
	/**
	 * Initializes the exception.
	 * 
	 * @param __m The message.
	 * @param __c The cause.
	 * @since 2024/08/02
	 */
	public SpringUnmappableTypeException(String __m, Throwable __c)
	{
		super(__m, __c);
	}
	
	/**
	 * Initializes the exception.
	 * 
	 * @param __c The cause.
	 * @since 2024/08/02
	 */
	public SpringUnmappableTypeException(Throwable __c)
	{
		super(__c);
	}
}
