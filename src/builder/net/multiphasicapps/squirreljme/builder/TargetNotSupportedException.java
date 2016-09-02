// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.builder;

/**
 * This is thrown when the specified target is not supported and that the
 * next target should be selected for a build attempt.
 *
 * @since 2016/09/02
 */
public class TargetNotSupportedException
	extends RuntimeException
{
	/**
	 * Initialize the exception with no message or cause.
	 *
	 * @since 2016/09/02
	 */
	public TargetNotSupportedException()
	{
	}
	
	/**
	 * Initialize the exception with a message and no cause.
	 *
	 * @param __m The message.
	 * @since 2016/09/02
	 */
	public TargetNotSupportedException(String __m)
	{
		super(__m);
	}
	
	/**
	 * Initialize the exception with a message and cause.
	 *
	 * @param __m The message.
	 * @param __c The cause.
	 * @since 2016/09/02
	 */
	public TargetNotSupportedException(String __m, Throwable __c)
	{
		super(__m, __c);
	}
	
	/**
	 * Initialize the exception with no message and with a cause.
	 *
	 * @param __c The cause.
	 * @since 2016/09/02
	 */
	public TargetNotSupportedException(Throwable __c)
	{
		super(__c);
	}
}

