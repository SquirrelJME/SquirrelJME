// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.scritchui;

import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchWindowBracket;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;

/**
 * State for {@link Display}.
 *
 * @since 2024/03/08
 */
public final class DisplayState
{
	/** The display this is linked to. */
	protected final Reference<Display> display;
	
	/** The scritch window which this display represents. */
	protected final ScritchWindowBracket scritchWindow;
	
	/** The displayable currently showing on this. */
	private volatile Reference<DisplayableState> _current;
	
	/** The display to show on exit. */
	private volatile Reference<DisplayableState> _onExit;
	
	/**
	 * Initializes the display state.
	 *
	 * @param __display The display this is linked to.
	 * @param __scritchWindow The scritch window this display represents.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/03/08
	 */
	public DisplayState(Display __display,
		ScritchWindowBracket __scritchWindow)
		throws NullPointerException
	{
		if (__display == null || __scritchWindow == null)
			throw new NullPointerException("NARG");
		
		this.scritchWindow = __scritchWindow;
		this.display = new WeakReference<>(__display);
	}
	
	/**
	 * Returns the associated display.
	 *
	 * @return The associated display.
	 * @since 2024/03/08
	 */
	public final Display display()
	{
		Display result = this.display.get();
		
		if (result == null)
			throw new IllegalStateException("GCGC");
		
		return result;
	}
}
