// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.bootstrap.javase.lcdui;

import javax.microedition.lcdui.Display;
import net.multiphasicapps.squirreljme.midp.lcdui.DisplayProperty;
import net.multiphasicapps.squirreljme.midp.lcdui.DisplayProtocol;
import net.multiphasicapps.squirreljme.midp.lcdui.VirtualDisplay;

/**
 * This is a virtual display used by the Swing display manager.
 *
 * @since 2016/10/14
 */
public class SwingVirtualDisplay
	extends VirtualDisplay
{
	/**
	 * Initilaizes the virtual display.
	 *
	 * @param __ds The owning display server.
	 * @param __id The id of the display.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/14
	 */
	public SwingVirtualDisplay(SwingDisplayServer __ds, byte __id)
	{
		super(__ds, __id);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/14
	 */
	@Override
	public int property(DisplayProperty __dp)
		throws NullPointerException
	{
		// Check
		if (__dp == null)
			throw new NullPointerException("NARG");
		
		// Depends
		switch (__dp)
		{
				// Supports input
			case CAPABILITIES:
				return Display.SUPPORTS_INPUT_EVENTS;
	
			// The dor pitch in micrometers.
			case DOT_PITCH:
				throw new Error("TODO");

			// Has pointer/stylus press/release events?
			case POINTER_PRESS_RELEASE:
				return 1;

			// Has pointer/sytlus move/drag events?
			case POINTER_DRAG_MOVE:
				return 1;

			// The size of the display (w16h16).
			case DISPLAY_SIZE:
				throw new Error("TODO");

			// Best image size for list elements (w16h16).
			case BEST_IMAGE_SIZE_LIST_ELEMENT:
				throw new Error("TODO");

			// Best image size for choice group elements (w16h16).
			case BEST_IMAGE_SIZE_CHOICE_GROUP_ELEMENT:
				throw new Error("TODO");

			// Best image size for alerts (w16h16).
			case BEST_IMAGE_SIZE_ALERT:
				throw new Error("TODO");

			// Best image size for tabs (w16h16).
			case BEST_IMAGE_SIZE_TAB:
				throw new Error("TODO");

			// Best image size for commands (w16h16).
			case BEST_IMAGE_SIZE_COMMAND:
				throw new Error("TODO");

			// Best image size for notifications (w16h16).
			case BEST_IMAGE_SIZE_NOTIFICATION:
				throw new Error("TODO");

			// Best image size for menus (w16h16).
			case BEST_IMAGE_SIZE_MENU:
				throw new Error("TODO");

			// The background color.
			case COLOR_BACKGROUND:
				throw new Error("TODO");

			// The foreground color.
			case COLOR_FOREGROUND:
				throw new Error("TODO");

			// The highlighted background color.
			case COLOR_HIGHLIGHTED_BACKGROUND:
				throw new Error("TODO");

			// The highlighted foreground color.
			case COLOR_HIGHLIGHTED_FOREGROUND:
				throw new Error("TODO");

			// The border color.
			case COLOR_BORDER:
				throw new Error("TODO");

			// The highlighted border color.
			case COLOR_HIGHLIGHTED_BORDER:
				throw new Error("TODO");

			// The idle background color.
			case COLOR_IDLE_BACKGROUND:
				throw new Error("TODO");

			// The idle foreground color.
			case COLOR_IDLE_FOREGROUND:
				throw new Error("TODO");

			// The highlighted idle background color.
			case COLOR_HIGHLIGHTED_IDLE_BACKGROUND:
				throw new Error("TODO");

			// The highlighted idle foreground color.
			case COLOR_HIGHLIGHTED_IDLE_FOREGROUND:
				throw new Error("TODO");

			// The border style for non-highlights.
			case BORDER_STYLE_NORMAL:
				throw new Error("TODO");

			// The border style for highlights.
			case BORDER_STYLE_HIGHLIGHT:
				throw new Error("TODO");

			// Is the display color?
			case IS_COLOR:
				return 1;

			// The number of supported colors.
			case NUM_COLORS:
				return 256 * 256 * 256;

			// The number of alpha levels.
			case NUM_ALPHA_LEVELS:
				return 2;

			// Is the display built-in?
			case BUILT_IN:
				return 1;
			
				// Unknown, use just zero.
			default:
				return 0;
		}
	}
}

