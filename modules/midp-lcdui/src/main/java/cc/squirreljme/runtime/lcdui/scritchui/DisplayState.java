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
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import javax.microedition.lcdui.Display;

/**
 * State for {@link Display}.
 *
 * @since 2024/03/08
 */
public final class DisplayState
{
	/** The display this is linked to. */
	protected final Reference<Display> lcduiDisplay;
	
	/** The scritch window which this display represents. */
	protected final ScritchWindowBracket scritchWindow;
	
	/** The screen this represents. */
	protected final ScritchScreenBracket scritchScreen;
	
	/** The displayable currently showing on this. */
	private volatile Reference<DisplayableState> _current;
	
	/** The display to show on exit. */
	private volatile Reference<DisplayableState> _onExit;
	
	/**
	 * Initializes the display state.
	 *
	 * @param __display The display this is linked to.
	 * @param __window The scritch window this display represents.
	 * @param __screen The scritch screen this display represents.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/03/08
	 */
	public DisplayState(Display __display,
		ScritchWindowBracket __window, ScritchScreenBracket __screen)
		throws NullPointerException
	{
		if (__display == null || __window == null || __screen == null)
			throw new NullPointerException("NARG");
		
		this.scritchWindow = __window;
		this.scritchScreen = __screen;
		this.lcduiDisplay = new WeakReference<>(__display);
	}
	
	/**
	 * Returns the associated display.
	 *
	 * @return The associated display.
	 * @since 2024/03/08
	 */
	public final Display display()
	{
		Display result = this.lcduiDisplay.get();
		
		if (result == null)
			throw new IllegalStateException("GCGC");
		
		return result;
	}
	
	/**
	 * Returns the ScritchUI window in use.
	 *
	 * @return The ScritchUI window.
	 * @since 2024/03/17
	 */
	public final ScritchWindowBracket scritchWindow()
	{
		return this.scritchWindow;
	}
}
