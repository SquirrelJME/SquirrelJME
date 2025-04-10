package cc.squirreljme.plugin.util;

// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

/**
 * There was an exception with Fossil. 
 *
 * @since 2025/04/10
 */
public class FossilException
	extends RuntimeException
{
	/**
	 * Initializes the exception.
	 *
	 * @param __message The exception message.
	 * @param __cause The cause of the exception.
	 * @since 2025/04/10
	 */
	public FossilException(String __message, Throwable __cause)
	{
		super(__message, __cause);
	}
	
	/**
	 * Initializes the exception.
	 *
	 * @param __cause The cause of the exception.
	 * @since 2025/04/10
	 */
	public FossilException(Throwable __cause)
	{
		super(__cause);
	}
}
