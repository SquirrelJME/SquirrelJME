// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.rts.ui;

import cc.squirreljme.jvm.mle.scritchui.ScritchInterface;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchPanelBracket;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchScreenBracket;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchWindowBracket;
import cc.squirreljme.rts.rate.RateController;
import cc.squirreljme.rts.rate.ScreenRunnable;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.lang.ref.Reference;
import org.jetbrains.annotations.NotNull;

/**
 * This handles the initialization of the game and the main game screen
 * event loop, at least for rendering.
 *
 * @since 2026/06/10
 */
public class FullScreenLoop
	implements ScreenRunnable
{
	/** The ScritchUI interface used. */
	protected final ScritchInterface scritch;
	
	/** The rate controller. */
	protected final Reference<RateController> rate;
	
	/** The game window. */
	private final ScritchWindowBracket winGame;
	
	/** The game panel. */
	private final ScritchPanelBracket panelGame;
	
	/** Has the game been set to visible? */
	private volatile boolean _madeVisible;
	
	/** Has the game been set to be fullscreen? */
	private volatile boolean _madeFullscreen;
	
	/**
	 * Initializes the fullscreen loop.
	 *
	 * @param __scritch The ScritchUI interface used.
	 * @param __rate The framerate controller.
	 * @throws NullPointerException On null arguments.
	 * @since 2026/06/10
	 */
	public FullScreenLoop(ScritchInterface __scritch,
		Reference<RateController> __rate)
		throws NullPointerException
	{
		if (__scritch == null || __rate == null)
			throw new NullPointerException("NARG");
		
		// Keep these for later
		this.scritch = __scritch;
		this.rate = __rate;
		
		// Setup game window
		ScritchWindowBracket winGame = __scritch.window().windowNew();
		this.winGame = winGame;
		
		// Setup game panel
		ScritchPanelBracket panelGame = __scritch.panel().panelNew();
		this.panelGame = panelGame;
		
		// Set a proper title
		__scritch.label().labelSetString(winGame, "Strategy Game");
		
		// Put the panel in the window
		__scritch.container().containerAdd(winGame, panelGame);
		
		// Set the renderer for the panel
		FullScreenDrawer drawer = new FullScreenDrawer(this.rate);
		__scritch.paintable().componentSetPaintListener(panelGame,
			drawer);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2026/06/10
	 */
	@Override
	public void run()
	{
		// Do we need to make this visible?
		if (this.__latchVisible())
			this.scritch.window().windowSetVisible(this.winGame,
				true);
		
		// Do we need to set the proper screen size?
		else if (this.__latchFullscreen())
			this.__makeFullscreen();
		
		// Tell the panel to repaint itself
		else if (this._madeVisible && this._madeFullscreen)
			this.scritch.paintable().componentRepaint(this.panelGame);
	}
	
	/**
	 * Latches setting fullscreen.
	 *
	 * @return If the latch should set fullscreen.
	 * @since 2026/06/10
	 */
	private boolean __latchFullscreen()
	{
		// If not visible or already made fullscreen, do nothing
		if (!this._madeVisible || this._madeFullscreen)
			return false;
		
		// Should we try to latch?
		synchronized (this)
		{
			// Double check
			if (!this._madeVisible || this._madeFullscreen)
				return false;
			
			// If ScritchUI does not consider the window visible yet, then
			// we do not latch until it feels it is ready
			if (!this.scritch.window().windowIsVisible(this.winGame))
				return false;
			
			// Set new state
			this._madeFullscreen = true;
			return true;
		}
	}
	
	/**
	 * Visibility latch.
	 *
	 * @return If the latch should make the window visible.
	 * @since 2026/06/10
	 */
	private boolean __latchVisible()
	{
		// Make the game visible?
		if (!this._madeVisible)
			synchronized (this)
			{
				// Double check again
				if (!this._madeVisible)
				{
					this._madeVisible = true;
					return true;
				}
			}
		
		return false;
	}
	
	/**
	 * Makes the game fullscreen.
	 *
	 * @since 2026/06/10
	 */
	private void __makeFullscreen()
	{
		ScritchInterface scritch = this.scritch;
		ScritchWindowBracket winGame = this.winGame;
		ScritchPanelBracket panelGame = this.panelGame;
		
		// The discovered resolution of the user's screen
		int w = 640;
		int h = 480;
		
		// Try to determine the best initial resolution of the game
		ScritchScreenBracket[] screens = scritch.environment().screens();
		if (screens != null && screens.length > 0)
		{
			// Just use the first screen
			ScritchScreenBracket screen =  screens[0];
			
			// Determine bounds
			int[] pixels = new int[4];
			scritch.screen().screenGetBounds(screen, winGame,
				pixels, null);
			
			// Is this actually valid?
			if (pixels[2] > 0)
				w = pixels[2];
			if (pixels[3] > 0)
				h = pixels[3];
		}
		
		// Set the discovered bounds
		scritch.container().containerSetBounds(winGame,
			panelGame, 0, 0, w, h);
		
		// Revalidate both
		scritch.component().componentRevalidate(winGame);
		scritch.component().componentRevalidate(panelGame);
	}
}
