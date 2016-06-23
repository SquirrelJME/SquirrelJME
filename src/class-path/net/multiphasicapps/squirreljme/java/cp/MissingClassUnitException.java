// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.java.cp;

/**
 * This is thrown when a required class unit is missing.
 *
 * @since 2016/06/20
 */
public class MissingClassUnitException
	extends RuntimeException
{
	/**
	 * Initializes the exception with no message or cause.
	 *
	 * @since 2016/06/20
	 */
	public MissingClassUnitException()
	{
	}
	
	/**
	 * Initializes the exception with the given message.
	 *
	 * @param __s The message to use.
	 * @since 2016/06/20
	 */
	public MissingClassUnitException(String __s)
	{
		super(__s);
	}
	
	/**
	 * Initializes the exception with the given message and cause.
	 *
	 * @param __s The message to use.
	 * @param __c The cause to use.
	 * @since 2016/06/20
	 */
	public MissingClassUnitException(String __s, Throwable __c)
	{
		super(__s, __c);
	}
	
	/**
	 * Initializes the exception with the given cause.
	 *
	 * @param __c The cause to use.
	 * @since 2016/06/20
	 */
	public MissingClassUnitException(Throwable __c)
	{
		super(__c);
	}
}

