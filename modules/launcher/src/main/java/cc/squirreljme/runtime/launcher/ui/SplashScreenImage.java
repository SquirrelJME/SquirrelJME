// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.launcher.ui;

import cc.squirreljme.jvm.mle.constants.UIPixelFormat;
import cc.squirreljme.runtime.cldc.SquirrelJME;
import cc.squirreljme.runtime.lcdui.mle.PencilGraphics;
import java.io.IOException;
import java.io.InputStream;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;

/**
 * Handles the splash screen image.
 *
 * @since 2024/01/28
 */
public class SplashScreenImage
{
	/** The copyright string. */
	public static final String COPYRIGHT =
		"https://squirreljme.cc/\n" +
		"(C) 2013-2024 Stephanie Gawroriski\n" +
		"TM  2016-2024 Stephanie Gawroriski\n" +
		"License: Mozilla Public License 2.0!\n" +
		"Donate to me on Patreon:\n" +
		"*** https://www.patreon.com/SquirrelJME! ***";
	
	/** The ending splash color. */
	public static final int END_COLOR =
		0xF5A9B8;
	
	/** The splash image height. */
	public static final int HEIGHT =
		320;
	
	/** The starting splash color. */
	public static final int START_COLOR =
		0x5BCEFA;
	
	/** The splash image width. */
	public static final int WIDTH =
		240;
	
	/** The font used for drawing. */
	private final Font _verFont;
	
	/** The height for text. */
	final int _verHeight;
	
	/** The font used for copyright. */
	private final Font _copyFont;
	
	/** Copyright X position. */
	private final int _copyX;
	
	/** Y position for the copyright. */
	private final int _copyY;
	
	/** Loading bar Y position. */
	private final int _loadingBarY;
	
	/** Loading string X position. */
	private final int _loadingStrX;
	
	/** Loading String Y position. */
	private final int _loadingStrY;
	
	/**
	 * Initializes the base splash information.
	 *
	 * @since 2024/01/28
	 */
	public SplashScreenImage()
	{
		// Height of the screen
		int width = SplashScreenImage.WIDTH;
		int height = SplashScreenImage.HEIGHT;
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
		this._loadingBarY = centerY + (height / 8) + (verHeight / 2);
		this._loadingStrX = centerX;
		this._loadingStrY = this._loadingBarY + (verHeight / 2);
	}
	
	/**
	 * Generates the splash screen.
	 *
	 * @return The resultant image data.
	 * @since 2024/01/28
	 */
	public int[] generateSplash()
	{
		// Image is completely operated with using raw data
		int np = SplashScreenImage.WIDTH * SplashScreenImage.HEIGHT;
		int[] image = new int[np];
		
		// Text will be drawn using the advanced graphics since it can
		// operate on integer buffers directly
		Graphics g = PencilGraphics.hardwareGraphics(
			UIPixelFormat.INT_RGB888, 
			SplashScreenImage.WIDTH, SplashScreenImage.HEIGHT,
			image, 0, null, 0, 0,
			SplashScreenImage.WIDTH, SplashScreenImage.HEIGHT);
		
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
			SplashScreenImage.drawGradient(g,
				SplashScreenImage.START_COLOR, SplashScreenImage.END_COLOR,
				SplashScreenImage.WIDTH, SplashScreenImage.HEIGHT);
			
			// Draw into the pixel buffer
			g.setBlendingMode(Graphics.SRC_OVER);
			g.drawImage(relaxedPixel,
				SplashScreenImage.WIDTH >> 1, SplashScreenImage.HEIGHT,
				Graphics.BOTTOM | Graphics.HCENTER);
			
			// Draw the logo on top of everything
			g.drawImage(logoPixel,
				SplashScreenImage.WIDTH >> 1, 0,
				Graphics.TOP | Graphics.HCENTER);
		}
		catch (IOException e)
		{
			// Print it but otherwise it is ignored
			e.printStackTrace();
		}
		
		// Draw copyright at the bottom
		this.drawCopyright(g, false);
		
		// Return the resultant image
		return image;
	}
	
	/**
	 * Draws the copyright text onto the given graphics.
	 *
	 * @param __g The graphics to draw onto.
	 * @param __swip Should "SquirrelJME Loading..." be drawn?
	 * @throws NullPointerException On null arguments.
	 * @since 2019/06/29
	 */
	public void drawCopyright(Graphics __g, boolean __swip)
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
		__g.drawString(SplashScreenImage.COPYRIGHT, copyX - 1, copyY,
			Graphics.BOTTOM | Graphics.LEFT);
		__g.drawString(SplashScreenImage.COPYRIGHT, copyX + 1, copyY,
			Graphics.BOTTOM | Graphics.LEFT);
		__g.drawString(SplashScreenImage.COPYRIGHT, copyX, copyY - 1,
			Graphics.BOTTOM | Graphics.LEFT);
		__g.drawString(SplashScreenImage.COPYRIGHT, copyX, copyY + 1,
			Graphics.BOTTOM | Graphics.LEFT);
		
		// Then draw the frontal white text
		__g.setColor(0xFFFFFF);
		__g.drawString(SplashScreenImage.COPYRIGHT, copyX, copyY,
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
	public static void drawGradient(Graphics __g, int __startColor,
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
