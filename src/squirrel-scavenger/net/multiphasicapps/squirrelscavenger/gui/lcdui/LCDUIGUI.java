// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirrelscavenger.gui.lcdui;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.game.GameCanvas;
import javax.microedition.lcdui.Graphics;
import javax.microedition.midlet.MIDlet;
import net.multiphasicapps.squirrelscavenger.game.Game;
import net.multiphasicapps.squirrelscavenger.game.player.Controller;
import net.multiphasicapps.squirrelscavenger.game.player.NullController;
import net.multiphasicapps.squirrelscavenger.gui.egl.EGLRenderer;
import net.multiphasicapps.squirrelscavenger.gui.GUI;

/**
 * This implements the GUI on top of the LCD UI.
 *
 * @since 2016/10/08
 */
public class LCDUIGUI
	implements GUI
{
	/** The LCD display being used. */
	protected final Display display;
	
	/** The controller for controlling the game. */
	protected final Controller controller;
	
	/** The canvas used to show the game. */
	protected final LCDCanvas canvas;
	
	/** The renderer. */
	protected final EGLRenderer renderer;
	
	/**
	 * Initializes the LCD UI interface.
	 *
	 * @param __mid The optional midlet that is used to obtain a display,
	 * may be {@code null}.
	 * @since 2016/10/08
	 */
	public LCDUIGUI(MIDlet __mid)
	{
		// If a midlet was specified use that to get the display
		Display d = null;
		if (__mid != null)
			d = Display.getDisplay(__mid);
		
		// Otherwise search for one
		if (d == null)
			d = __getDefaultDisplay();
		
		// Set
		this.display = d;
		
		// Setup controller
		if ((d.getCapabilities() & Display.SUPPORTS_INPUT_EVENTS) != 0)
			this.controller = new LCDUIController(d);
		
		// Just stand still for a long time
		else
			this.controller = new NullController();
		
		// While playing the game, entering sleep mode would be very annoying
		d.setActivityMode(Display.MODE_ACTIVE);
		
		// Setup the initial display with just a Canvas to display an Image
		LCDCanvas canvas;
		this.canvas = (canvas = new LCDCanvas());
		d.setCurrent(canvas);
		
		// Setup renderer
		this.renderer = new EGLRenderer(d, canvas.getGraphics());
		
		// Set title bar
		canvas.setTitle("Squirrel Scavenger");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/08
	 */
	@Override
	public Controller localController(int __id)
		throws IndexOutOfBoundsException
	{
		// {@squirreljme.error BA03 Request for a negative controller.}
		if (__id < 0)
			throw new IndexOutOfBoundsException("BA03");
		
		// All other controllers are null
		if (__id != 0)
			return null;
		
		// Otherwise use the single controller
		return this.controller;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/08
	 */
	@Override
	public void renderGame(Game __g)
		throws NullPointerException
	{
		// Check
		if (__g == null)
			throw new NullPointerException("NARG");
		
		// Render the game into the canvas
		LCDCanvas canvas = this.canvas;
		int w = canvas.getWidth(), h = canvas.getHeight();
		this.renderer.renderGame(__g, w, h);
		
		// Flush drawn graphics
		canvas.flushGraphics();
	}
	
	/**
	 * Find a default LCDUI based display to use.
	 *
	 * @return The display to use.
	 * @throws RuntimeException If one was not found.
	 * @since 2016/10/08
	 */
	private static final Display __getDefaultDisplay()
		throws RuntimeException
	{
		// Just use any display with one that supports input since only a
		// canvas will be used as a screen for the most part
		for (Display d : Display.getDisplays(Display.SUPPORTS_INPUT_EVENTS))
			return d;
		
		// {@squirreljme.error BA04 Could not find a LCDUI display.}
		throw new RuntimeException("BA04");
	}
}

