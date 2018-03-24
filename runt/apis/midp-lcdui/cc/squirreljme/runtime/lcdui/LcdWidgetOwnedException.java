// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui;

/**
 * This is thrown when a widget is already taken by a display or tabbed
 * pane.
 *
 * @since 2018/03/18
 */
public class LcdWidgetOwnedException
	extends LcdException
{
	/**
	 * Initialize the exception with no message or cause.
	 *
	 * @since 2018/03/18
	 */
	public LcdWidgetOwnedException()
	{
	}
	
	/**
	 * Initialize the exception with a message and no cause.
	 *
	 * @param __m The message.
	 * @since 2018/03/18
	 */
	public LcdWidgetOwnedException(String __m)
	{
		super(__m);
	}
	
	/**
	 * Initialize the exception with a message and cause.
	 *
	 * @param __m The message.
	 * @param __c The cause.
	 * @since 2018/03/18
	 */
	public LcdWidgetOwnedException(String __m, Throwable __c)
	{
		super(__m, __c);
	}
	
	/**
	 * Initialize the exception with no message and with a cause.
	 *
	 * @param __c The cause.
	 * @since 2018/03/18
	 */
	public LcdWidgetOwnedException(Throwable __c)
	{
		super(__c);
	}
}

