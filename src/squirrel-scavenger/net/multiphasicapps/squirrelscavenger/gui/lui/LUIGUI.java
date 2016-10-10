// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirrelscavenger.gui.lui;

import java.util.Iterator;
import javax.microedition.key.InputDevice;
import javax.microedition.lui.Display;
import net.multiphasicapps.squirrelscavenger.game.Game;
import net.multiphasicapps.squirrelscavenger.game.player.Controller;
import net.multiphasicapps.squirrelscavenger.game.player.NullController;
import net.multiphasicapps.squirrelscavenger.gui.GUI;

/**
 * This is the interface which uses the LUI (text based) interface to display
 * the game.
 *
 * This utilizes the software rasterizer and uses a very low resolution which
 * is turned into ASCII art essentially so that the game is made visible.
 *
 * @since 2016/10/06
 */
public class LUIGUI
	implements GUI
{
	/** The display to draw into. */
	protected final Display display;
	
	/** The controller used. */
	protected final Controller controller;
	
	/**
	 * Initializes the line based GUI using the default display.
	 *
	 * @since 2016/10/06
	 */
	public LUIGUI()
	{
		this(__getDefaultDisplay());
	}
	
	/**
	 * Initializes the line based GUI using the given display.
	 *
	 * @param __d The display to draw into.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/06
	 */
	public LUIGUI(Display __d)
		throws NullPointerException
	{
		// Check
		if (__d == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.display = __d;
		
		// If the display has keyboard input then allow for it to be handled
		if (__d instanceof InputDevice)
			this.controller = new LUIController((InputDevice)__d);
		
		// Use null controller (idle)
		else
			this.controller = new NullController();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/06
	 */
	@Override
	public Controller localController(int __id)
		throws IndexOutOfBoundsException
	{
		// {@squirreljme.error BA02 Request for a negative local player ID.}
		if (__id < 0)
			throw new IndexOutOfBoundsException("BA02");
		
		// Only the first player is valid
		if (__id != 0)
			return null;
		
		// Return the controller
		return this.controller;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/06
	 */
	@Override
	public void renderGame(Game __g)
		throws NullPointerException
	{
		throw new Error("TODO");
	}
	
	/**
	 * Returns the default display which is used to render the game and
	 * handle the event loop.
	 *
	 * @return The default display.
	 * @throws RuntimeException If there is none.
	 * @since 2016/10/06
	 */
	private static Display __getDefaultDisplay()
		throws RuntimeException
	{
		// Find displays, use ones that provide keys first (if any)
		Display use = null;
		for (boolean keys = true;; keys = false)
		{
			Iterator<Display> it = Display.getDisplays(keys);
			while (it.hasNext())
			{
				// Get
				Display d = it.next();
				
				// Try to claim the display
				d.setHardwareAssigned(true);
				if (d.isHardwareAssigned())
					return d;
			}
			
			// Stop loop
			if (!keys)
				break;
		}
		
		// {@squirreljme.error BA01 No line based displays found.}
		throw new RuntimeException("BA01");
	}
}

