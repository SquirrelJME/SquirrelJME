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
import cc.squirreljme.jvm.mle.scritchui.annotation.ScritchEventLoop;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchMenuBarBracket;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchPanelBracket;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchWindowBracket;
import cc.squirreljme.jvm.mle.scritchui.constants.ScritchLAFPlatformFlag;
import cc.squirreljme.runtime.cldc.annotation.KeepWhenCompacting;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.lcdui.scritchui.DisplayIdentityScale;
import cc.squirreljme.runtime.lcdui.scritchui.DisplayScale;
import cc.squirreljme.runtime.lcdui.scritchui.DisplayState;
import cc.squirreljme.runtime.lcdui.scritchui.DisplayableState;
import cc.squirreljme.runtime.lcdui.scritchui.MenuActionNodeOnly;

/**
 * Handler for showing/removing a {@link Displayable}.
 *
 * @since 2024/03/16
 */
@KeepWhenCompacting
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
	@KeepWhenCompacting
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
		
		// Get the ScritchUI window
		Display display = this.display;
		DisplayState displayState = display.__state();
		
		// The displayable we are showing
		Displayable showNow = this.showNow;
		DisplayableState showNowState = (showNow != null ? showNow.__state() :
			null);
		
		// Debug
		Debugging.debugNote("setCurrent(%s (%s))",
			showNow, showNowState);
		
		// What is currently being displayed?
		DisplayableState current = displayState.current();
		
		// Showing an alert?
		Displayable onExit = this.onExit;
		if (showNow instanceof Alert)
		{
			// If the exit was not specified, then switch to the exit handler
			if (onExit == null)
				onExit = current.displayable();
			
			// Showing alerts is natively supported?
			if ((scritchApi.environment().lookAndFeel().lafPlatformFlags() &
				ScritchLAFPlatformFlag.HAS_ALERTS) != 0)
			{
				// Pop up the alert
				if (true)
					Debugging.todo("Pop up alert box");
				
				// Switch to the exit handler instead and perform no other
				// logic at all
				new __ExecDisplaySetCurrent__(scritchApi, display, onExit,
					null).run();
				return;
			}
		}
		
		// No-op?
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
		
		// Use commonized logic for refreshing the displayable on this display
		this.__refresh(false, false);
	}
	
	/**
	 * Removes the old displayable, if any, then shows the new one.
	 *
	 * @param __skipExit If this is a direct call that was not performed
	 * from {@link #run()}, this will not cause any alert boxes or other
	 * on exit handlers to be called.
	 * @param __forceRemove Force remove of the current widget, even if it
	 * is the currently shown one.
	 * @since 2025/12/23
	 */
	@ScritchEventLoop
	@KeepWhenCompacting
	void __refresh(boolean __skipExit, boolean __forceRemove)
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
		ScritchWindowBracket window = displayState.scritchWindow();
		
		// The displayable we are showing
		Displayable showNow = this.showNow;
		DisplayableState showNowState =
			(showNow != null ? showNow.__state() : null);
		
		// What is currently being displayed?
		DisplayableState current = displayState.current();
		
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
			
			// Do no further processing.
			return;
		}
		
		// Remove old displayable first, this can be forced
		if ((__forceRemove || current != showNowState) && current != null)
		{
			// Setup the same logic, however with nothing
			__ExecDisplaySetCurrent__ exec = new __ExecDisplaySetCurrent__(
				scritchApi, current.currentDisplay().display(),
				null, null);
			
			// If we are skipping exit/alert handlers, then we do not want
			// this removal to evoke them. However, they will still remain
			// in effect as the normal run() call sets these!
			if (__skipExit)
				exec.__refresh(__skipExit, __forceRemove);
			else
				exec.run();
		}
		
		// This was externally called from elsewhere
		if (showNow == null || showNowState == null)
			return;
		
		// If full screen is being desired, we only want to set it when the
		// display is in identity scale mode. Otherwise, if we do not then
		// compatibility framing and scaling will break along with providing
		// a very inconsistent experience.
		DisplayScale scale = display._scale;
		boolean isFull = ((scale instanceof DisplayIdentityScale) &&
			showNowState.desireFullScreen());
		
		// Get the needed panel and add it in
		panel = showNowState.scritchPanel();
		containerApi.containerAdd(window, panel);
		
		// Set the frame's preferred and minimum sizes for the content area
		Debugging.debugNote("Screen [%d, %d] <- %s",
			scale.textureMaxW(), scale.textureMaxH(),
			scale.getClass());
		windowApi.windowContentMinimumSize(window,
			scale.screenX(scale.textureMaxW()),
			scale.screenY(scale.textureMaxH()));
		
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
