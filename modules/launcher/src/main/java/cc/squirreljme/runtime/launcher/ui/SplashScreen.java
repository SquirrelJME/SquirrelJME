// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.launcher.ui;

import cc.squirreljme.jvm.mle.constants.UIPixelFormat;
import cc.squirreljme.runtime.cldc.SquirrelJME;
import cc.squirreljme.runtime.lcdui.mle.PencilGraphics;
import java.io.IOException;
import java.io.InputStream;
import javax.microedition.lcdui.Canvas;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

/**
 * This is the splash screen for the launcher which always shows.
 *
 * @since 2019/05/19
 */
public final class SplashScreen
	extends Canvas
	implements Runnable
{
	/** The copyright string. */
	public static final String COPYRIGHT =
		"https://squirreljme.cc/\n" +
		"(C) 2013-2022 Stephanie Gawroriski\n" +
		"TM  2016-2022 Stephanie Gawroriski\n" +
		"Licensed under the GPLv3!\n" +
		"Donate to me on Patreon:\n" +
		"*** https://www.patreon.com/SquirrelJME! ***";
	
	/** The splash image width. */
	public static final int WIDTH =
		240;
	
	/** The splash image height. */
	public static final int HEIGHT =
		320;
	
	/** The starting splash color. */
	public static final int START_COLOR =
		0x5BCEFA;
	
	/** The ending splash color. */
	public static final int END_COLOR =
		0xF5A9B8;
	
	/** Loading string X position. */
	private final int _loadingStrX;
	
	/** Loading String Y position. */
	private final int _loadingStrY;
	
	/** The font used for drawing. */
	private final Font _verFont;
	
	/** The height for text. */
	private final int _verHeight;
	
	/** The font used for copyright. */
	private final Font _copyFont;
	
	/** Copyright X position. */
	private final int _copyX;
	
	/** Y position for the copyright. */
	private final int _copyY;
	
	/** Loading bar X position. */
	private final int _loadingBarX;
	
	/** Loading bar Y position. */
	private final int _loadingBarY;
	
	/** Loading bar width. */
	private final int _loadingBarWidth;
	
	/** Loading bar height. */
	private final int _loadingBarHeight;
	
	/** The refresh state. */
	private final __RefreshState__ _refreshState;
	
	/** The image data to draw. */
	volatile int[] _image;
	
	/** Are we in a refresh? */
	volatile boolean _inRefresh;
	
	/**
	 * Initializes the splash screen with a precached image.
	 *
	 * @param __sw The screen width.
	 * @param __sh The screen height.
	 * @param __refreshState The refresh state.
	 * @since 2019/05/19
	 */
	public SplashScreen(int __sw, int __sh, __RefreshState__ __refreshState)
	{
		this._refreshState = __refreshState;
		
		// Set a nice title!
		this.setTitle("SquirrelJME is Loading!");
		
		// Full-screen mode for the entire image
		this.setFullScreenMode(true);
		
		// Add command to exit the VM, so it does something
		this.addCommand(MidletMain.EXIT_COMMAND);
		
		// Need to handle commands and such
		__ExitHandler__ ch = new __ExitHandler__();
		this.setCommandListener(ch);
		
		// Height of the screen
		int width = SplashScreen.WIDTH;
		int height = SplashScreen.HEIGHT;
		int centerX = width / 2;
		int centerY = height / 2;
		
		// Version number font
		Font verFont = Font.getFont("sansserif", 0, 16);
		int verHeight = verFont.getPixelSize();
		this._verFont = verFont;
		this._verHeight = verHeight;
		
		// Copyright font
		this._copyFont = Font.getFont("sansserif", 0, 12);
		this._copyX = 2;
		this._copyY = height - 2;
		
		// Loading
		this._loadingBarX = 0;
		this._loadingBarY = centerY + (height / 8) + (verHeight / 2);
		this._loadingBarWidth = width;
		this._loadingBarHeight = verHeight;
		this._loadingStrX = centerX;
		this._loadingStrY = this._loadingBarY + (verHeight / 2);
		
		// Load the launcher image in a background thread so the splash screen
		// can still display the copyright notice
		new Thread(this, "LauncherImageLoader").start();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/19
	 */
	@Override
	public final void paint(Graphics __g)
	{
		// If we are in a refresh, set the state
		synchronized (this)
		{
			this._inRefresh = true;
		}
		
		// We are not in a refresh
		try
		{
			// Draw the raw image data, is the fastest
			int[] image = this._image;
			if (image != null)
				__g.drawRGB(image, 0, SplashScreen.WIDTH, 0, 0,
					SplashScreen.WIDTH, SplashScreen.HEIGHT, false);
			
			// The image is not fully loaded yet, so draw the copyright
			// at least
			else
				this.__copyright(__g, true);
			
			// Put in the current refresh status
			__RefreshState__ refreshState = this._refreshState;
			String message = refreshState._message;
			if (message != null)
			{
				// How much progress is there?
				int at = refreshState._at;
				int total = refreshState._total;
				
				// Where is the loading bar at?
				int lBX = this._loadingBarX;
				int lBY = this._loadingBarY;
				int lBW = this._loadingBarWidth;
				int lBH = this._loadingBarHeight;
				
				// Draw progress bar?
				if (total != 0)
				{
					float percent = (float)at / (float)total;
					int leftBarPx = (int)((float)lBW * percent);
					int rightBarPx = lBW - leftBarPx;
					
					// Left side of bar
					__g.setColor(0xFF7900);
					__g.fillRect(lBX, lBY,
						leftBarPx, lBH);
					
					// Right side of bar
					if (rightBarPx > 0)
					{
						__g.setColor(0x444444);
						__g.fillRect(lBX + leftBarPx, lBY,
							rightBarPx, lBH);
					}
				}
				
				// Draw message
				__g.setColor(0xFFFFFF);
				__g.drawString(message,
					this._loadingStrX, this._loadingStrY,
					Graphics.VCENTER | Graphics.HCENTER);
			}
		}
		
		// Not refreshing
		finally
		{
			synchronized (this)
			{
				this._inRefresh = false;
			}
		}
	}
	
	/**
	 * Request repaint of the canvas without overloading.
	 * 
	 * @since 2021/12/29
	 */
	public final void requestRepaint()
	{
		// Do not request another repaint if we are in one
		synchronized (this)
		{
			if (this._inRefresh)
				return;
			
			// Request it now	
			this.repaint();
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/06/29
	 */
	@Override
	public final void run()
	{
		// Print copyright to the console as well
		System.err.println(SplashScreen.COPYRIGHT);
		
		// Image is completely operated with using raw data
		int np = SplashScreen.WIDTH * SplashScreen.HEIGHT;
		int[] image = new int[np];
		
		// Text will be drawn using the advanced graphics since it can
		// operate on integer buffers directly
		Graphics g = PencilGraphics.hardwareGraphics(
			UIPixelFormat.INT_RGB888, 
			SplashScreen.WIDTH, SplashScreen.HEIGHT,
			image, 0, null, 0, 0,
			SplashScreen.WIDTH, SplashScreen.HEIGHT);
		
		// Load splash image onto the data
		try (InputStream relaxedIn = SplashScreen.class
				.getResourceAsStream("relaxedpixel.xpm");
			InputStream logoIn = SplashScreen.class
				.getResourceAsStream("logo.xpm"))
		{
			// Load in the relaxed pixel art of Lex and the logo
			Image relaxedPixel = Image.createImage(relaxedIn);
			Image logoPixel = Image.createImage(logoIn);
			
			// Draw gradient
			SplashScreen.__drawGradient(g,
				SplashScreen.START_COLOR, SplashScreen.END_COLOR,
				SplashScreen.WIDTH, SplashScreen.HEIGHT);
			
			// Draw into the pixel buffer
			g.setBlendingMode(Graphics.SRC_OVER);
			g.drawImage(relaxedPixel,
				SplashScreen.WIDTH >> 1, SplashScreen.HEIGHT,
				Graphics.BOTTOM | Graphics.HCENTER);
			
			// Draw the logo on top of everything
			g.drawImage(logoPixel,
				SplashScreen.WIDTH >> 1, 0,
				Graphics.TOP | Graphics.HCENTER);
		}
		catch (IOException e)
		{
			// Print it but otherwise it is ignored
			e.printStackTrace();
		}
		
		// Draw copyright at the bottom
		this.__copyright(g, false);
		
		// Use this image
		this._image = image;
		
		// Perform a repaint before we return so the better looking splash
		// screen is used
		this.repaint();
	}
	
	/**
	 * Draws the copyright text onto the given graphics.
	 *
	 * @param __g The graphics to draw onto.
	 * @param __swip Should "SquirrelJME Loading..." be drawn?
	 * @throws NullPointerException On null arguments.
	 * @since 2019/06/29
	 */
	private void __copyright(Graphics __g, boolean __swip)
		throws NullPointerException
	{
		if (__g == null)
			throw new NullPointerException("NARG");
		
		// Draw version number
		__g.setAlphaColor(255, 0, 0, 0);
		__g.setFont(this._verFont);
		__g.drawString(SquirrelJME.RUNTIME_VERSION, 238, 48,
			Graphics.RIGHT | Graphics.TOP);
		
		// Draw loading and the SquirrelJME string?
		if (__swip)
		{
			// SquirrelJME String
			__g.drawString("SquirrelJME", 2, 2,
				Graphics.TOP | Graphics.LEFT);
			
			// Loading...
			__g.drawString("Loading...",
				this._loadingStrX, this._loadingStrY,
				Graphics.VCENTER | Graphics.HCENTER);
		}
		
		// Set properties
		__g.setFont(this._copyFont);
		
		// Draw a black drop-shadow, this makes the lighter front easier to
		// see regardless of what is on the background
		int copyX = this._copyX;
		int copyY = this._copyY;
		__g.setColor(0x000000);
		__g.drawString(SplashScreen.COPYRIGHT, copyX - 1, copyY,
			Graphics.BOTTOM | Graphics.LEFT);
		__g.drawString(SplashScreen.COPYRIGHT, copyX + 1, copyY,
			Graphics.BOTTOM | Graphics.LEFT);
		__g.drawString(SplashScreen.COPYRIGHT, copyX, copyY - 1,
			Graphics.BOTTOM | Graphics.LEFT);
		__g.drawString(SplashScreen.COPYRIGHT, copyX, copyY + 1,
			Graphics.BOTTOM | Graphics.LEFT);
		
		// Then draw the frontal white text
		__g.setColor(0xFFFFFF);
		__g.drawString(SplashScreen.COPYRIGHT, copyX, copyY,
			Graphics.BOTTOM | Graphics.LEFT);
	}
	
	/**
	 * Draws a gradient.
	 * 
	 * @param __g The graphics to draw into.
	 * @param __startColor The starting color.
	 * @param __endColor The ending color.
	 * @param __width The width.
	 * @param __height The height.
	 * @since 2022/01/26
	 */
	private static void __drawGradient(Graphics __g, int __startColor,
		int __endColor, int __width, int __height)
	{
		// Decompose the starting color
		int atR = ((__startColor >>> 16) & 0xFF) << 8;
		int atG = ((__startColor >>> 8) & 0xFF) << 8;
		int atB = ((__startColor) & 0xFF) << 8;
		
		// Determine the additive for the three colors
		int addR = ((((__endColor >>> 16) & 0xFF) << 8) - atR) /
			__height;
		int addG = ((((__endColor >>> 8) & 0xFF) << 8) - atG) /
			__height;
		int addB = ((((__endColor) & 0xFF) << 8) - atB) /
			__height;
		
		// Draw for each row height
		for (int y = 0; y < __height; y++)
		{
			// Set the next color to use
			__g.setAlphaColor(0xFF,
				atR >>> 8, atG >>> 8, atB >>> 8);
			
			// Draw line
			__g.drawLine(0, y,
				__width, y);
			
			// Determine the next color to use
			atR += addR;
			atG += addG;
			atB += addB;
		}
	}
}
