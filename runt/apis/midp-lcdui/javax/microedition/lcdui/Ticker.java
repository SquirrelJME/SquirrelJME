// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

import cc.squirreljme.runtime.lcdui.SerializedEvent;
import cc.squirreljme.runtime.lcdui.ui.UIStack;

/**
 * A ticker contains an infinitely scrolling message.
 *
 * Any {@link Displayable} may have tickers associated with them in which
 * they will be shown at the top of the screen accordingly. As such these can
 * be used to convey information as needed.
 *
 * @since 2018/03/26
 */
public class Ticker
{
	/**
	 * Initializes the ticker with the given string.
	 *
	 * @param __s The string to use for the ticker.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/26
	 */
	public Ticker(String __s)
		throws NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Use internal title set
		this.setString(__s);
	}
	
	/**
	 * Returns the string which is currently being displayed in the ticker.
	 *
	 * @return The string contained within the ticker.
	 * @since 2018/03/26
	 */
	public String getString()
	{
		throw new todo.TODO();
		/*
		return LcdServiceCall.<String>call(String.class,
			LcdFunction.TICKER_GET_STRING, this._handle);*/
	}
	
	/**
	 * Sets the string that is displayed within the ticker.
	 *
	 * @param __s The string to use for the ticker.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/26
	 */
	public void setString(String __s)
		throws NullPointerException
	{
		throw new todo.TODO();
		/*LcdServiceCall.<VoidType>call(VoidType.class,
			LcdFunction.TICKER_SET_STRING, this._handle, __s);*/
	}
}


