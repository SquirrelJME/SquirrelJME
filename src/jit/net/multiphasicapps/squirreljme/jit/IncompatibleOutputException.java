// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

/**
 * This is thrown when an attempt is made to output to either a compatible
 * executable or a cached output stream which is not supported by the given
 * {@link JITOutput}.
 *
 * @since 2016/07/04
 */
public class IncompatibleOutputException
	extends JITException
{
	/**
	 * Initialize the exception with no message or cause.
	 *
	 * @since 2016/07/04
	 */
	public IncompatibleOutputException()
	{
	}
	
	/**
	 * Initialize the exception with a message and no cause.
	 *
	 * @param __m The message.
	 * @since 2016/07/04
	 */
	public IncompatibleOutputException(String __m)
	{
		super(__m);
	}
	
	/**
	 * Initialize the exception with a message and cause.
	 *
	 * @param __m The message.
	 * @param __c The cause.
	 * @since 2016/07/04
	 */
	public IncompatibleOutputException(String __m, Throwable __c)
	{
		super(__m, __c);
	}
	
	/**
	 * Initialize the exception with no message and with a cause.
	 *
	 * @param __c The cause.
	 * @since 2016/07/04
	 */
	public IncompatibleOutputException(Throwable __c)
	{
		super(__c);
	}
}

