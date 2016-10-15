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

import java.awt.Color;
import java.awt.DisplayMode;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.SystemColor;
import java.awt.Toolkit;
import java.io.IOException;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Graphics;
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
	/** The size to use for icon images. */
	public static final int IMAGE_SIZE =
		(16 << 16) | 16;
	
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
	 * @since 2016/10/15
	 */
	@Override
	public void outputLoop(DataOutputStream __out)
		throws IOException, NullPointerException
	{
		// Check
		if (__out == null)
			throw new NullPointerException("NARG");
		
		// Setup 
		try
		{
			for (;;)
				Thread.yield();
		}
		
		// Cleanup
		finally
		{
		}
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
		
		// May need the graphics environment
		GraphicsEnvironment ge = GraphicsEnvironment.
			getLocalGraphicsEnvironment();
		GraphicsDevice gd = ge.getDefaultScreenDevice();
		DisplayMode dm = gd.getDisplayMode();
		Toolkit tk = Toolkit.getDefaultToolkit();
		
		// Depends
		switch (__dp)
		{
				// Supports input
			case CAPABILITIES:
				return Display.SUPPORTS_INPUT_EVENTS;
	
			// The dor pitch in micrometers.
			case DOT_PITCH:
				return (int)((1.0D / (double)tk.getScreenResolution()) *
					25_400.0D);

			// Has pointer/stylus press/release events?
			case POINTER_PRESS_RELEASE:
				return 1;

			// Has pointer/sytlus move/drag events?
			case POINTER_DRAG_MOVE:
				return 1;

			// The size of the display (w16h16).
			case DISPLAY_SIZE:
				return ((dm.getWidth() & 0xFFFF) << 16) |
					(dm.getHeight() & 0xFFFF);

			// Best image size for list elements (w16h16).
			case BEST_IMAGE_SIZE_LIST_ELEMENT:
				return IMAGE_SIZE;

			// Best image size for choice group elements (w16h16).
			case BEST_IMAGE_SIZE_CHOICE_GROUP_ELEMENT:
				return IMAGE_SIZE;

			// Best image size for alerts (w16h16).
			case BEST_IMAGE_SIZE_ALERT:
				return IMAGE_SIZE;

			// Best image size for tabs (w16h16).
			case BEST_IMAGE_SIZE_TAB:
				return IMAGE_SIZE;

			// Best image size for commands (w16h16).
			case BEST_IMAGE_SIZE_COMMAND:
				return IMAGE_SIZE;

			// Best image size for notifications (w16h16).
			case BEST_IMAGE_SIZE_NOTIFICATION:
				return IMAGE_SIZE;

			// Best image size for menus (w16h16).
			case BEST_IMAGE_SIZE_MENU:
				return IMAGE_SIZE;

			// The background color.
			case COLOR_BACKGROUND:
				return SystemColor.control.getRGB() & 0xFFFFFF;

			// The foreground color.
			case COLOR_FOREGROUND:
				return SystemColor.controlText.getRGB() & 0xFFFFFF;

			// The highlighted background color.
			case COLOR_HIGHLIGHTED_BACKGROUND:
				return SystemColor.controlHighlight.getRGB() & 0xFFFFFF;

			// The highlighted foreground color.
			case COLOR_HIGHLIGHTED_FOREGROUND:
				return SystemColor.controlText.getRGB() & 0xFFFFFF;

			// The border color.
			case COLOR_BORDER:
				return SystemColor.windowBorder.getRGB() & 0xFFFFFF;

			// The highlighted border color.
			case COLOR_HIGHLIGHTED_BORDER:
				return SystemColor.windowBorder.getRGB() & 0xFFFFFF;

			// The idle background color.
			case COLOR_IDLE_BACKGROUND:
				return SystemColor.control.getRGB() & 0xFFFFFF;

			// The idle foreground color.
			case COLOR_IDLE_FOREGROUND:
				return SystemColor.controlText.getRGB() & 0xFFFFFF;

			// The highlighted idle background color.
			case COLOR_IDLE_HIGHLIGHTED_BACKGROUND:
				return SystemColor.controlHighlight.getRGB() & 0xFFFFFF;

			// The highlighted idle foreground color.
			case COLOR_IDLE_HIGHLIGHTED_FOREGROUND:
				return SystemColor.controlText.getRGB() & 0xFFFFFF;

			// The border style for non-highlights.
			case BORDER_STYLE_NORMAL:
				return Graphics.SOLID;

			// The border style for highlights.
			case BORDER_STYLE_HIGHLIGHT:
				return Graphics.SOLID;

			// Is the display color?
			case IS_COLOR:
				return 1;

			// The number of supported colors.
			case NUM_COLORS:
				return (int)Math.max(16777216L, 1L << dm.getBitDepth());

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

