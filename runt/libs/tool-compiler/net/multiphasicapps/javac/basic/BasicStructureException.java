// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.javac.basic;

import net.multiphasicapps.javac.CompilerException;
import net.multiphasicapps.javac.LocationAware;

/**
 * This is thrown when the structure of a parsed class is not correct.
 *
 * @since 2018/03/12
 */
public class BasicStructureException
	extends CompilerException
{
	/**
	 * Initialize the exception with no message or cause.
	 *
	 * @since 2018/03/12
	 */
	public BasicStructureException()
	{
	}
	
	/**
	 * Initialize the exception with a message and no cause.
	 *
	 * @param __m The message.
	 * @since 2018/03/12
	 */
	public BasicStructureException(String __m)
	{
		super(__m);
	}
	
	/**
	 * Initialize the exception with a message and cause.
	 *
	 * @param __m The message.
	 * @param __c The cause.
	 * @since 2018/03/12
	 */
	public BasicStructureException(String __m, Throwable __c)
	{
		super(__m, __c);
	}
	
	/**
	 * Initialize the exception with no message and with a cause.
	 *
	 * @param __c The cause.
	 * @since 2018/03/12
	 */
	public BasicStructureException(Throwable __c)
	{
		super(__c);
	}
	
	/**
	 * Initialize the exception with no message or cause.
	 *
	 * @param __la Location awareness information.
	 * @since 2018/03/12
	 */
	public BasicStructureException(LocationAware __la)
	{
		super(__la);
	}
	
	/**
	 * Initialize the exception with a message and no cause.
	 *
	 * @param __la Location awareness information.
	 * @param __m The message.
	 * @since 2018/03/12
	 */
	public BasicStructureException(LocationAware __la, String __m)
	{
		super(__la, __m);
	}
	
	/**
	 * Initialize the exception with a message and cause.
	 *
	 * @param __la Location awareness information.
	 * @param __m The message.
	 * @param __c The cause.
	 * @since 2018/03/12
	 */
	public BasicStructureException(LocationAware __la, String __m,
		Throwable __c)
	{
		super(__la, __m, __c);
	}
	
	/**
	 * Initialize the exception with no message and with a cause.
	 *
	 * @param __la Location awareness information.
	 * @param __c The cause.
	 * @since 2017/03/12
	 */
	public BasicStructureException(LocationAware __la, Throwable __c)
	{
		super(__la, __c);
	}
}

