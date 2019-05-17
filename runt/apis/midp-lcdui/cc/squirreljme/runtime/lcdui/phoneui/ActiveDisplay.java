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
	volatile Displayable _current;
	
	/** Vibrate the display? */
	private volatile boolean _invibrate;
	
	/** Vibration end time. */
	private volatile long _vibrateend;
	
	/** Vibration cycle. */
	private volatile boolean _vibratecycle;
	
	/** Content X Area. */
	int _contentx;
	
	/** Content Y Area. */
	int _contenty;
	
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
		
		// Set
		this._current = __d;
		
		// Realize the dimensions
		this.realize(new int[4]);
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
		ExposedDisplayable current = (ExposedDisplayable)this._current;
		
		// Realize new content area coordinates
		int[] ua = new int[4];
		if (this.realize(ua))
			current.sizeChanged(ua[2], ua[3]);
		
		// Extract coordinates
		int ux = ua[0],
			uy = ua[1],
			uw = ua[2],
			uh = ua[3];
		
		// If not full-screen, then draw title bar and maybe the command bar
		if (uh != dh)
		{
			// Remember default parameters
			Font oldfont = dg.getFont();
			int oldcolor = dg.getColor();
			
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
			
			// Get commands that are used, this is used to figure out if the
			// command bar needs to be drawn
			Command[] commands = (current == null ? new Command[0] :
				current.getCommands());
			int numcommands = commands.length;
			if (numcommands > 0)
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
		}
		
		// Setup enforced graphics
		Graphics ug = new EnforcedDrawingAreaGraphics(dg,
			ux, uy, uw, uh);
		
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
				int oldcolor = ug.getColor();
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
	 * Realizes the size of the content area.
	 *
	 * @param __dims The output content area dimensions.
	 * @return {@code true} if the area has been resized.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/05/17
	 */
	public final boolean realize(int[] __dims)
		throws NullPointerException
	{
		if (__dims == null)
			throw new NullPointerException("NARG");
		
		// Screen size
		int dw = this.width,
			dh = this.height;
		
		// User area
		int ux, uy, uw, uh;
		
		// Current displayable to draw
		ExposedDisplayable current = (ExposedDisplayable)this._current;
		
		// Full-screen uses the entire screen
		if (current != null && current.isFullscreen())
		{
			ux = 0;
			uy = 0;
			uw = dw;
			uh = dh;
		}
		
		// Otherwise, space is lost to the title bar and commands
		else
		{
			// Get commands that are used, this is used to figure out if the
			// command bar needs to be drawn
			Command[] commands = (current == null ? new Command[0] :
				current.getCommands());
			int numcommands = commands.length;
			
			// Clip dimensions
			ux = 0;
			uy = StandardMetrics.TITLE_BAR_HEIGHT;
			uw = dw;
			uh = (dh - StandardMetrics.TITLE_BAR_HEIGHT) -
				(numcommands > 0 ? StandardMetrics.COMMAND_BAR_HEIGHT : 0);
		}
		
		// Has the area changed?
		boolean rv = false;
		int oldux = this._contentx,
			olduy = this._contenty,
			olduw = this._contentwidth,
			olduh = this._contentheight;
		if (ux != oldux || uy != olduy || uw != olduw || uh != olduh)
		{
			// Did change
			rv = true;
			
			// Set new fields
			this._contentx = ux;
			this._contenty = uy;
			this._contentwidth = uw;
			this._contentheight = uh;
		}
		
		// Set output user area dimensions
		__dims[0] = ux;
		__dims[1] = uy;
		__dims[2] = uw;
		__dims[3] = uh;
		
		// Has this changed at all?
		return rv;
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

