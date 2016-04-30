// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.io.inflate;

import java.io.IOException;

/**
 * This is the base class for any exceptions the inflater may thrown when
 * reading bytes.
 *
 * @since 2016/03/10
 */
public class InflaterException
	extends IOException
{
	/**
	 * Initializes the exception with no message or cause.
	 *
	 * @since 2016/03/12
	 */
	public InflaterException()
	{
	}
	
	/**
	 * Initializes the exception with the given message.
	 *
	 * @param __m The message to use.
	 * @since 2016/03/12
	 */
	public InflaterException(String __m)
	{
		super(__m);
	}
	
	/**
	 * Initializes the exception with the given message and cause.
	 *
	 * @param __m The message to use.
	 * @param __t The cause of this exception.
	 * @since 2016/03/17
	 */
	public InflaterException(String __m, Throwable __t)
	{
		super(__m, __t);
	}
	
	/**
	 * Initializes the exception with the given cause.
	 *
	 * @param __t The cause of this exception.
	 * @since 2016/03/12
	 */
	public InflaterException(Throwable __t)
	{
		super(__t);
	}
}

