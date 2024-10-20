// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.scritchui;

import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchScreenBracket;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchWindowBracket;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;

/**
 * State for {@link Display}.
 *
 * @since 2024/03/08
 */
@SquirrelJMEVendorApi
public final class DisplayState
{
	/** The display this is linked to. */
	@SquirrelJMEVendorApi
	protected final Display lcduiDisplay;
	
	/** The scritch window which this display represents. */
	@SquirrelJMEVendorApi
	protected final ScritchWindowBracket scritchWindow;
	
	/** The screen this represents. */
	@SquirrelJMEVendorApi
	protected final ScritchScreenBracket scritchScreen;
	
	/** The displayable currently showing on this. */
	@SquirrelJMEVendorApi
	volatile DisplayableState _current;
	
	/** The display to show on exit. */
	@SquirrelJMEVendorApi
	private volatile DisplayableState _onExit;
	
	/** The current displayable hold. */
	@SquirrelJMEVendorApi
	private volatile Displayable _currentHold;
	
	/**
	 * Initializes the display state.
	 *
	 * @param __display The display this is linked to.
	 * @param __window The scritch window this display represents.
	 * @param __screen The scritch screen this display represents.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/03/08
	 */
	@SquirrelJMEVendorApi
	public DisplayState(Display __display,
		ScritchWindowBracket __window, ScritchScreenBracket __screen)
		throws NullPointerException
	{
		if (__display == null || __window == null || __screen == null)
			throw new NullPointerException("NARG");
		
		this.scritchWindow = __window;
		this.scritchScreen = __screen;
		this.lcduiDisplay = __display;
	}
	
	/**
	 * Returns the current displayable.
	 *
	 * @return The current displayable.
	 * @since 2024/07/27
	 */
	@SquirrelJMEVendorApi
	public final DisplayableState current()
	{
		synchronized (this)
		{
			return this._current;
		}
	}
	
	/**
	 * Returns the associated display.
	 *
	 * @return The associated display.
	 * @since 2024/03/08
	 */
	@SquirrelJMEVendorApi
	public final Display display()
	{
		return this.lcduiDisplay;
	}
	
	/**
	 * Returns the ScritchUI window in use.
	 *
	 * @return The ScritchUI window.
	 * @since 2024/03/17
	 */
	@SquirrelJMEVendorApi
	public final ScritchWindowBracket scritchWindow()
	{
		return this.scritchWindow;
	}
	
	/**
	 * Sets the current displayable.
	 *
	 * @param __displayable The current displayable to set.
	 * @since 2024/07/27
	 */
	@SquirrelJMEVendorApi
	void __setCurrent(DisplayableState __displayable)
	{
		synchronized (this)
		{
			this._current = __displayable;
			
			if (__displayable != null)
				this._currentHold = __displayable.displayable();
		}
	}
}
