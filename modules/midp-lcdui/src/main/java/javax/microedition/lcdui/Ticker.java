// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

import cc.squirreljme.runtime.cldc.annotation.Api;

/**
 * A ticker contains an infinitely scrolling message.
 *
 * Any {@link Displayable} may have tickers associated with them in which
 * they will be shown at the top of the screen accordingly. As such these can
 * be used to convey information as needed.
 *
 * @since 2018/03/26
 */
@Api
public class Ticker
{
	/** {@code Displayable}s this ticker is attached to. */
	final __VolatileList__<Displayable> _displayables =
		new __VolatileList__<>();
	
	/** The text used. */
	volatile String _text;
	
	/**
	 * Initializes the ticker with the given string.
	 *
	 * @param __s The string to use for the ticker.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/26
	 */
	@Api
	public Ticker(String __s)
		throws NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Use internal title set
		this.__setString(__s);
	}
	
	/**
	 * Returns the string which is currently being displayed in the ticker.
	 *
	 * @return The string contained within the ticker.
	 * @since 2018/03/26
	 */
	@Api
	public String getString()
	{
		return this._text;
	}
	
	/**
	 * Sets the string that is displayed within the ticker.
	 *
	 * @param __s The string to use for the ticker.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/26
	 */
	@Api
	public void setString(String __s)
		throws NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		this.__setString(__s);
	}
	
	/**
	 * Sets the text to be displayed on the ticker and performs updating on
	 * the form and otherwise.
	 * 
	 * @param __s The string to set.
	 * @since 2021/11/27
	 */
	private void __setString(String __s)
	{
		// Set new
		this._text = __s;
		
		// Adjust the ticker item for all the displayables which are showing
		// this ticker
		for (Displayable di : this._displayables)
			di.__updateTicker();
	}
}


