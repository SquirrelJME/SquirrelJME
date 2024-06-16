// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.classfile;

/**
 * This is thrown when the format of a class is not valid.
 *
 * @since 2017/09/27
 */
public class InvalidClassFormatException
	extends RuntimeException
{
	/** Context for this exception. */
	private final Contexual[] _context;
	
	/**
	 * Initialize the exception with no message or cause.
	 *
	 * @since 2016/09/27
	 */
	public InvalidClassFormatException()
	{
		this((Contexual[])null);
	}
	
	/**
	 * Initialize the exception with a message and no cause.
	 *
	 * @param __m The message.
	 * @since 2016/09/27
	 */
	public InvalidClassFormatException(String __m)
	{
		this(__m, (Contexual[])null);
	}
	
	/**
	 * Initialize the exception with a message and cause.
	 *
	 * @param __m The message.
	 * @param __c The cause.
	 * @since 2016/09/27
	 */
	public InvalidClassFormatException(String __m, Throwable __c)
	{
		this(__m, __c, (Contexual[])null);
	}
	
	/**
	 * Initialize the exception with no message and with a cause.
	 *
	 * @param __c The cause.
	 * @since 2016/09/27
	 */
	public InvalidClassFormatException(Throwable __c)
	{
		this(__c, (Contexual[])null);
	}
	
	/**
	 * Initialize the exception with no message or cause.
	 *
	 * @since 2024/06/09
	 */
	public InvalidClassFormatException(Contexual... __context)
	{
		this._context = (__context == null ? null : __context.clone());
	}
	
	/**
	 * Initialize the exception with a message and no cause.
	 *
	 * @param __m The message.
	 * @since 2024/06/09
	 */
	public InvalidClassFormatException(String __m, Contexual... __context)
	{
		super(__m);
		
		this._context = (__context == null ? null : __context.clone());
	}
	
	/**
	 * Initialize the exception with a message and cause.
	 *
	 * @param __m The message.
	 * @param __c The cause.
	 * @since 2024/06/09
	 */
	public InvalidClassFormatException(String __m, Throwable __c,
		Contexual... __context)
	{
		super(__m, __c);
		
		this._context = (__context == null ? null : __context.clone());
	}
	
	/**
	 * Initialize the exception with no message and with a cause.
	 *
	 * @param __c The cause.
	 * @since 2024/06/09
	 */
	public InvalidClassFormatException(Throwable __c, Contexual... __context)
	{
		super(__c);
		
		this._context = (__context == null ? null : __context.clone());
	}
	
	/**
	 * Returns the exception context.
	 *
	 * @return The exception context.
	 * @since 2024/06/09
	 */
	public final Contexual[] context()
	{
		if (this._context == null)
			return new Contexual[0];
		return this._context.clone();
	}
}

