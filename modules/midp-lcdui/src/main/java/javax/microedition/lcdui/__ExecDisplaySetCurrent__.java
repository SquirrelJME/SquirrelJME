// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

import cc.squirreljme.jvm.mle.scritchui.ScritchComponentInterface;
import cc.squirreljme.jvm.mle.scritchui.ScritchContainerInterface;
import cc.squirreljme.jvm.mle.scritchui.ScritchInterface;
import cc.squirreljme.jvm.mle.scritchui.ScritchPanelInterface;
import cc.squirreljme.jvm.mle.scritchui.ScritchWindowInterface;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchPanelBracket;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchWindowBracket;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.lcdui.scritchui.DisplayScale;
import cc.squirreljme.runtime.lcdui.scritchui.DisplayState;
import cc.squirreljme.runtime.lcdui.scritchui.DisplayableState;

/**
 * Handler for showing/removing a {@link Displayable}.
 *
 * @since 2024/03/16
 */
class __ExecDisplaySetCurrent__
	implements Runnable
{
	/** The displayable to show when the displayable is removed. */
	protected final Displayable onExit;
	
	/** The displayable to show immediately. */
	protected final Displayable showNow;
	
	/** The ScritchUI interface. */
	protected final ScritchInterface scritchApi;
	
	/** The display to call this on. */
	protected final Display display;
	
	/**
	 * Initializes the updater for showing or removing the current displayable.
	 *
	 * @param __scritchApi The API used.
	 * @param __display The display to call this on.
	 * @param __showNow The displayable to show now, if {@code null} then it
	 * is cleared.
	 * @param __onExit The displayable to show when the currently shown item
	 * is removed.
	 * @throws NullPointerException If {@code __showNow} is {@code null}
	 * and {@code __onExit} is not {@code null}; or {@code __scritchApi}
	 * or {@code __on} are {@code null}.
	 * @since 2024/03/17
	 */
	__ExecDisplaySetCurrent__(ScritchInterface __scritchApi, Display __display,
		Displayable __showNow, Displayable __onExit)
		throws NullPointerException
	{
		if (__scritchApi == null || __display == null)
			throw new NullPointerException("NARG");
		
		// If we have an exit but are not showing something then this is
		// incorrect, we cannot just go into an exit directly
		if (__showNow == null && __onExit != null)
			throw new NullPointerException("NARG");
		
		this.scritchApi = __scritchApi;
		this.display = __display;
		this.showNow = __showNow;
		this.onExit = __onExit;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/17
	 */
	@Override
	public void run()
	{
		// Get the container API since we will have to clear and add it to
		// the Display's frame...
		ScritchInterface scritchApi = this.scritchApi;
		ScritchContainerInterface containerApi = scritchApi.container();
		ScritchComponentInterface componentApi = scritchApi.component();
		ScritchPanelInterface panelApi = scritchApi.panel();
		ScritchWindowInterface windowApi = scritchApi.window();
		
		// Target panel may be set later
		ScritchPanelBracket panel;
		
		// Get the ScritchUI window
		Display display = this.display;
		DisplayState displayState = display.__state();
		ScritchWindowBracket window =
			displayState.scritchWindow();
		
		// Debug
		Debugging.debugNote("setCurrent(%p)", this.showNow);
		
		// Add in the panel, if there is one
		Displayable showNow = this.showNow;
		if (showNow != null)
		{
			// Remove everything from the window
			// TODO FIXME: For some reason removeAll happens after the add??
			/*containerApi.removeAll(window);*/
			
			// Get the needed panel and add it in
			DisplayableState ourState = this.showNow._state;
			panel = ourState.scritchPanel();
			containerApi.add(window, panel);
			
			// Set the frame's preferred and minimum sizes for the content area
			DisplayScale scale = display._scale;
			windowApi.contentMinimumSize(window,
				scale.textureMaxW(), scale.textureMaxH());
			
			// Make sure the parent is set
			ourState.setParent(displayState);
			
			// Revalidate so it gets updated
			componentApi.revalidate(panel);
			
			// Show the display window
			windowApi.setVisible(window, true);
			
			// Internal revalidation logic
			showNow.__execRevalidate(displayState);
			
			// Force it to be painted
			panelApi.repaint(panel);
		}
		
		// Hide the window
		else
			windowApi.setVisible(window, false);
	}
}
