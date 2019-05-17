// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.phoneui;

import cc.squirreljme.runtime.cldc.SquirrelJME;
import cc.squirreljme.runtime.lcdui.gfx.EnforcedDrawingAreaGraphics;
import javax.microedition.lcdui.Command;
import javax.microedition.lcdui.Displayable;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import java.util.Arrays;

/**
 * This contains the display display along with the internal image buffer.
 *
 * @since 2019/05/16
 */
public final class ActiveDisplay
{
	/** The width of the display. */
	protected final int width;
	
	/** The height of the display. */
	protected final int height;
	
	/** The backing buffer image. */
	protected final Image image;
	
	/** The displayable to draw. */
	private volatile Displayable _current;
	
	/** Vibrate the display? */
	private volatile boolean _invibrate;
	
	/** Vibration end time. */
	private volatile long _vibrateend;
	
	/** Vibration cycle. */
	private volatile boolean _vibratecycle;
	
	/** Width of the content area. */
	int _contentwidth;
	
	/** Height of the content area. */
	int _contentheight;
	
	/** The title to use. */
	String _title;
	
	/**
	 * Initializes the active display.
	 *
	 * @param __w The width.
	 * @param __h The height.
	 * @since 2019/05/16
	 */
	public ActiveDisplay(int __w, int __h)
	{
		// Set sizes
		this.width = __w;
		this.height = __h;
		
		// Setup buffer
		this.image = Image.createImage(__w, __h);
		
		// Default content area size
		this._contentwidth = __w;
		this._contentheight = __h - (StandardMetrics.TITLE_BAR_HEIGHT +
			StandardMetrics.COMMAND_BAR_HEIGHT);
	}
	
	/**
	 * Activates the display.
	 *
	 * @param __d The displayable to be drawn when requested.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/16
	 */
	public final void activate(Displayable __d)
		throws NullPointerException
	{
		if (__d == null)
			throw new NullPointerException("NARG");
		
		this._current = __d;
	}
	
	/**
	 * Paints whatever is in the active display.
	 *
	 * @param __x The X coordinate.
	 * @param __y The Y coordinate.
	 * @param __w The width.
	 * @param __h The height.
	 * @since 2019/05/16
	 */
	public final void paint(int __x, int __y, int __w, int __h)
	{
		// Get display details
		Graphics dg = this.image.getGraphics();
		int dw = this.width,
			dh = this.height;
		
		// Current displayable to draw
		Displayable current = this._current;
		
		// Get commands that are used, this is used to figure out if the
		// command bar needs to be drawn
		Command[] commands = (current == null ? new Command[0] :
			current.getCommands());
		int numcommands = commands.length;
		
		// Is the command bar and title bar to be drawn maybe?
		boolean drawcommandbar,
			drawtitlebar;
		
		// Drawing full-screen graphics so do not draw the title bar or the
		// command bar at all. But since it is full-screen we can just draw
		// directly on the image without using a wrapper (is faster)
		// This becomes the user drawing area
		Graphics ug;
		int uw, uh;
		if (current != null && current.isFullscreen())
		{
			// Display is the whole screen
			ug = dg;
			uw = dw;
			uh = dh;
			
			// These are never drawn
			drawtitlebar = false;
			drawcommandbar = false;
		}
		
		// Otherwise, we draw the title bar and the command bar
		else
		{
			// Title bar is always drawn
			drawtitlebar = true;
			
			// The command bar is only drawn if we have actual commands
			drawcommandbar = (numcommands > 0);
			
			// Draw area is shortened in height
			uw = dw;
			uh = (dh - StandardMetrics.TITLE_BAR_HEIGHT) -
				(drawcommandbar ? StandardMetrics.COMMAND_BAR_HEIGHT : 0);
			
			// Use 
			ug = new EnforcedDrawingAreaGraphics(dg,
				0, StandardMetrics.TITLE_BAR_HEIGHT,
				uw, uh);
		}
		
		// Remember default parameters
		Font oldfont = dg.getFont();
		int oldcolor = dg.getColor();
		
		// Draw title bar
		if (drawtitlebar)
		{
			// Draw background
			dg.setColor(StandardMetrics.BACKGROUND_BAR_COLOR);
			dg.fillRect(0, 0, dw, StandardMetrics.TITLE_BAR_HEIGHT);
			
			// Set font
			dg.setFont(Font.getFont("sansserif", 0,
				StandardMetrics.TITLE_BAR_HEIGHT));
			
			// Draw title text
			String title = this._title;
			dg.setColor(StandardMetrics.FOREGROUND_BAR_COLOR);
			dg.drawString(title, 0, 0, Graphics.TOP | Graphics.LEFT);
			dg.drawString(title, 1, 0, Graphics.TOP | Graphics.LEFT);
		}
		
		// Draw the command bar?
		if (drawcommandbar)
		{
			// Base Y position
			int cy = dh - StandardMetrics.COMMAND_BAR_HEIGHT;
			
			// Draw background
			dg.setColor(StandardMetrics.BACKGROUND_BAR_COLOR);
			dg.fillRect(0, cy, dw, StandardMetrics.COMMAND_BAR_HEIGHT);
		}
		
		// Restore parameters
		dg.setFont(oldfont);
		dg.setColor(oldcolor);
		
		// If nothing is being shown, just show the version info
		if (current == null)
		{
			// Draw box
			ug.setColor(0x0000FF);
			ug.fillRect(0, 0, uw, uh);
			
			// Draw some layout text
			ug.setColor(0xFFFF00);
			ug.setFont(Font.getFont("sansserif", 0, 16));
			ug.drawString("SquirrelJME " + SquirrelJME.RUNTIME_VERSION + "\n" +
				"(C) Stephanie Gawroriski\n" +
				"https://squirreljme.cc/\nLicensed w/ the GPLv3!", 0, 0, 0);
			ug.drawString("SquirrelJME", 1, 0, 0);
		}
		
		// Normal painting
		else
		{
			// If the displayable is transparent then, we fill in the
			// background for the application
			if (current.isTransparent())
			{
				// Use background color instead
				oldcolor = ug.getColor();
				ug.setColor(StandardMetrics.TRANSPARENT_COLOR);
				
				// Fill
				ug.fillRect(0, 0, uw, uh);
				
				// Restore
				ug.setColor(oldcolor);
			}
			
			// Paint
			current.paint(ug);
		}
		
		// Clear clip for status symbols
		dg.setClip(0, 0, dw, dh);
		
		// Switch to the symbol font
		Font sf = Font.getFont("symbol", 0, StandardMetrics.TITLE_BAR_HEIGHT);
		dg.setFont(sf);
		int xa = sf.charWidth('#'),
			sx = dw - 2;
		
		// Currently vibrating the display?
		if (this._invibrate)
		{
			// Stop vibrating?
			long nowtime = System.nanoTime();
			if (nowtime >= this._vibrateend)
				this._invibrate = false;
			
			// Visual vibration, just a basic symbol
			else
			{
				// Switch to symbol font
				dg.setFont(Font.getFont("symbol", 0, 
					StandardMetrics.TITLE_BAR_HEIGHT));
				
				// Draw vibrate symbol
				dg.setColor(StandardMetrics.VIBRATE_COLOR);
				dg.drawChar('#', sx, 0, Graphics.RIGHT | Graphics.TOP);
				sx -= xa;
			}
		}
	}
	
	/**
	 * Vibrates the display for the given number of milliseconds.
	 *
	 * @param __ms The number of milliseconds to vibrate for.
	 * @since 2019/05/17
	 */
	public final void vibrate(int __ms)
	{
		// Stop vibrating
		if (__ms <= 0)
			this._invibrate = false;
		
		// Otherwise vibrate
		else
		{
			this._invibrate = true;
			this._vibrateend = System.nanoTime() + (__ms * 1000000L);
		}
	}
}

