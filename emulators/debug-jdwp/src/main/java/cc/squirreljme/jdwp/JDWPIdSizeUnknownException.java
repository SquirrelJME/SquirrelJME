// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp;

/**
 * This exception is thrown when there is an attempt to read an ID when the
 * size of IDs is not yet known.
 *
 * @since 2024/01/23
 */
public class JDWPIdSizeUnknownException
	extends JDWPException
{
	/**
	 * Initializes the exception.
	 * 
	 * @param __m The message used.
	 * @since 2024/01/23
	 */
	public JDWPIdSizeUnknownException(String __m)
	{
		super(__m);
	}
	
	/**
	 * Initializes the exception.
	 * 
	 * @param __m The message used.
	 * @param __c The cause of the exception.
	 * @since 2024/01/23
	 */
	public JDWPIdSizeUnknownException(String __m, Throwable __c)
	{
		super(__m, __c);
	}
}
