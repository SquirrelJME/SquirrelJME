// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

import cc.squirreljme.runtime.cldc.debug.Debugging;

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
	public Ticker(String __s)
		throws NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Use internal title set
		this._text = __s;
	}
	
	/**
	 * Returns the string which is currently being displayed in the ticker.
	 *
	 * @return The string contained within the ticker.
	 * @since 2018/03/26
	 */
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
	public void setString(String __s)
		throws NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Set new
		this._text = __s;
		
		// Find a displayable which is showing this
		boolean isshown = false;
		for (Displayable di : this._displayables)
		{
			Display d = di._display;
			if (d != null)
				isshown = true;
		}
		
		// If this is being shown, then force the view to repaint
		throw Debugging.todo();
		/*UIState.getInstance().repaint();*/
	}
}


