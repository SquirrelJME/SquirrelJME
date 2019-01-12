// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.scrf.compiler;

import net.multiphasicapps.classfile.InvalidClassFormatException;

/**
 * This is thrown when the class could not be processed due to an invalid
 * format or otherwise.
 *
 * @since 2019/01/11
 */
public class ClassProcessException
	extends InvalidClassFormatException
{
	/**
	 * Initialize the exception with no message or cause.
	 *
	 * @since 2019/01/11
	 */
	public ClassProcessException()
	{
	}
	
	/**
	 * Initialize the exception with a message and no cause.
	 *
	 * @param __m The message.
	 * @since 2019/01/11
	 */
	public ClassProcessException(String __m)
	{
		super(__m);
	}
	
	/**
	 * Initialize the exception with a message and cause.
	 *
	 * @param __m The message.
	 * @param __c The cause.
	 * @since 2019/01/11
	 */
	public ClassProcessException(String __m, Throwable __c)
	{
		super(__m, __c);
	}
	
	/**
	 * Initialize the exception with no message and with a cause.
	 *
	 * @param __c The cause.
	 * @since 2019/01/11
	 */
	public ClassProcessException(Throwable __c)
	{
		super(__c);
	}
}

