// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

/**
 * This is thrown when the state of a monitor is not valid.
 *
 * @since 2018/09/15
 */
public class SpringIllegalMonitorStateException
	extends SpringException
{
	/**
	 * Initialize the exception with no message or cause.
	 *
	 * @since 2018/09/15
	 */
	public SpringIllegalMonitorStateException()
	{
	}
	
	/**
	 * Initialize the exception with a message and no cause.
	 *
	 * @param __m The message.
	 * @since 2018/09/15
	 */
	public SpringIllegalMonitorStateException(String __m)
	{
		super(__m);
	}
	
	/**
	 * Initialize the exception with a message and cause.
	 *
	 * @param __m The message.
	 * @param __c The cause.
	 * @since 2018/09/15
	 */
	public SpringIllegalMonitorStateException(String __m, Throwable __c)
	{
		super(__m, __c);
	}
	
	/**
	 * Initialize the exception with no message and with a cause.
	 *
	 * @param __c The cause.
	 * @since 2018/09/15
	 */
	public SpringIllegalMonitorStateException(Throwable __c)
	{
		super(__c);
	}
}

