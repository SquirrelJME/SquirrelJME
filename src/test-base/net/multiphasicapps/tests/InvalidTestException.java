// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.tests;

/**
 * This is thrown when the given test is not valid.
 *
 * @since 2016/05/04
 */
public class InvalidTestException
	extends RuntimeException
{
	/**
	 * Initializes the exception.
	 *
	 * @since 2016/05/04
	 */
	public InvalidTestException()
	{
	}
	
	/**
	 * Initializes the exception with the given message.
	 *
	 * @param __m The message to use.
	 * @since 2016/05/05
	 */
	public InvalidTestException(String __m)
	{
		super(__m);
	}
	
	/**
	 * Initializes the exception using the given individual test name.
	 *
	 * @param __t The test to use.
	 * @since 2016/07/12
	 */
	public InvalidTestException(IndividualTest __t)
	{
		super((__t == null ? "null" : __t.toString()));
	}
}

