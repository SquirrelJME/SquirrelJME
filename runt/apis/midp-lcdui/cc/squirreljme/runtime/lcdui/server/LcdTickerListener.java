// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.server;

/**
 * This interface is used in conjunction with tickers .
 *
 * @since 2018/03/26
 */
public interface LcdTickerListener
{
	/**
	 * This is called when the ticker's text has changed.
	 *
	 * @param __lcd The ticker which has changed.
	 * @param __text The new text for the ticker.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/26
	 */
	public abstract void textChanged(LcdTicker __lcd, String __text)
		throws NullPointerException;
}

