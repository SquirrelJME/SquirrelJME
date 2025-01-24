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
import cc.squirreljme.jvm.mle.scritchui.ScritchPaintableInterface;
import cc.squirreljme.jvm.mle.scritchui.ScritchWindowInterface;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchMenuBarBracket;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchPanelBracket;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchWindowBracket;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.lcdui.scritchui.DisplayScale;
import cc.squirreljme.runtime.lcdui.scritchui.DisplayState;
import cc.squirreljme.runtime.lcdui.scritchui.DisplayableState;
import cc.squirreljme.runtime.lcdui.scritchui.MenuActionNodeOnly;

/**
 * Handler for showing/removing a {@link Displayable}.
 *
 * @since 2024/03/16
 */
@SquirrelJMEVendorApi
class __ExecDisplaySetCurrent__
	implements Runnable
{
	/** The displayable to show when the displayable is removed. */
	@SquirrelJMEVendorApi
	protected final Displayable onExit;
	
	/** The displayable to show immediately. */
	@SquirrelJMEVendorApi
	protected final Displayable showNow;
	
	/** The ScritchUI interface. */
	@SquirrelJMEVendorApi
	protected final ScritchInterface scritchApi;
	
	/** The display to call this on. */
	@SquirrelJMEVendorApi
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
	@SquirrelJMEVendorApi
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
	@SquirrelJMEVendorApi
	public void run()
	{
		// Get the container API since we will have to clear and add it to
		// the Display's frame...
		ScritchInterface scritchApi = this.scritchApi;
		ScritchContainerInterface containerApi = scritchApi.container();
		ScritchComponentInterface componentApi = scritchApi.component();
		ScritchPaintableInterface paintableApi = scritchApi.paintable();
		ScritchWindowInterface windowApi = scritchApi.window();
		
		// Target panel may be set later
		ScritchPanelBracket panel;
		
		// Get the ScritchUI window
		Display display = this.display;
		DisplayState displayState = display.__state();
		ScritchWindowBracket window =
			displayState.scritchWindow();
		
		// The displayable we are showing
		Displayable showNow = this.showNow;
		DisplayableState showNowState = (showNow != null ? showNow.__state() :
			null);
		
		// Debug
		Debugging.debugNote("setCurrent(%p)", this.showNow);
		
		// No-op?
		DisplayableState current = displayState.current();
		if (current == showNowState)
			return;
		
		// Do we need to remove the displayable from its old display?
		if (showNowState != null)
		{
			// Perform the same logic here, just run that first
			DisplayState showParent = showNowState.currentDisplay();
			if (showParent != null)
			{
				new __ExecDisplaySetCurrent__(scritchApi, showParent.display(),
					null, null).run();
			}
		}
		
		// Are we removing a displayable?
		if (showNow == null)
		{
			// If there is no current display, we can just do nothing
			if (current == null)
				return;
			
			// Remove everything from the window, since every display always
			// has just a single displayable, and we do not care what else
			// was even here...
			containerApi.containerRemoveAll(window);
			
			// Disassociate both ends
			current.setParent(null);
		}
		
		// One is being added
		else
		{
			// Remove old displayable first
			if (current != showNowState && current != null)
				new __ExecDisplaySetCurrent__(scritchApi,
					current.currentDisplay().display(),
					null, null).run();
			
			// Get the needed panel and add it in
			panel = showNowState.scritchPanel();
			containerApi.containerAdd(window, panel);
			
			// Set the frame's preferred and minimum sizes for the content area
			DisplayScale scale = display._scale;
			windowApi.windowContentMinimumSize(window,
				scale.textureMaxW(), scale.textureMaxH());
			
			// Make sure the parent is set
			showNowState.setParent(displayState);
			
			// Revalidate so it gets updated
			componentApi.componentRevalidate(panel);
			
			// Update text tracker of displayable to use the one that is
			// attached to the display
			showNow._trackerTitle.connect(display._listenerTitle);
			
			// Set the menu bar of the window to that of this displayable
			windowApi.windowSetMenuBar(window,
				MenuActionNodeOnly.rootTree(showNow)
					.map(MenuActionNodeOnly.node(showNow))
					.scritchWidget(ScritchMenuBarBracket.class));
			
			// Show the display window
			windowApi.windowSetVisible(window, true);
			
			// Internal revalidation logic
			showNow.__execRevalidate(displayState);
			
			// Force it to be painted
			paintableApi.componentRepaint(panel);
		}
	}
}
