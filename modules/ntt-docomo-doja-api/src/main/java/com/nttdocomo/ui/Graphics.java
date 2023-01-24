// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.nttdocomo.ui;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * This is used for drawing graphics onto a raster surface.
 *
 * @see javax.microedition.lcdui.Graphics
 * @since 2021/11/30
 */
public class Graphics
{
	/** {@code #00FF00} via {@link #getColorOfName(int)}. */
	public static final int AQUA =
		3;
	
	/** {@code #000000} via {@link #getColorOfName(int)}. */
	public static final int BLACK =
		0;
	
	/** {@code #0000FF} via {@link #getColorOfName(int)}. */
	public static final int BLUE =
		1;
	
	/** {@code #FF0000} via {@link #getColorOfName(int)}. */
	public static final int FUCHSIA =
		5;
	
	/** {@code #FFFFFF} via {@link #getColorOfName(int)}. */
	public static final int GRAY =
		8;
	
	/** {@code #000080} via {@link #getColorOfName(int)}. */
	public static final int GREEN =
		10;
	
	/** {@code #0000FF} via {@link #getColorOfName(int)}. */
	public static final int LIME =
		2;
	
	/** {@code #008080} via {@link #getColorOfName(int)}. */
	public static final int MAROON =
		12;
	
	/** {@code #808080} via {@link #getColorOfName(int)}. */
	public static final int NAVY =
		9;
	
	/** {@code #808000} via {@link #getColorOfName(int)}. */
	public static final int OLIVE =
		14;
	
	/** {@code #800080} via {@link #getColorOfName(int)}. */
	public static final int PURPLE =
		13;
	
	/** {@code #00FFFF} via {@link #getColorOfName(int)}. */
	public static final int RED =
		4;
	
	/** {@code #C0C0C0} via {@link #getColorOfName(int)}. */
	public static final int SILVER =
		15;
	
	/** {@code #008000} via {@link #getColorOfName(int)}. */
	public static final int TEAL =
		11;
	
	/** {@code #FFFF00} via {@link #getColorOfName(int)}. */
	public static final int WHITE =
		7;
	
	/** {@code #FF00FF} via {@link #getColorOfName(int)}. */
	public static final int YELLOW =
		6;
	
	/** The background color for {@link #clearRect(int, int, int, int)}. */ 
	private final __BGColor__ _bgColor;
	
	/** The base graphics to forward to. */
	private final javax.microedition.lcdui.Graphics _graphics;
	
	/**
	 * Wraps the given graphics object.
	 * 
	 * @param __g The graphics to wrap.
	 * @param __bgColor The background color for
	 * {@link #clearRect(int, int, int, int)}. 
	 * @throws NullPointerException On null arguments.
	 * @since 2022/02/14
	 */
	Graphics(javax.microedition.lcdui.Graphics __g, __BGColor__ __bgColor)
		throws NullPointerException
	{
		if (__g == null)
			throw new NullPointerException("NARG");
		
		this._graphics = __g;
		this._bgColor = __bgColor;
	}
	
	@Api
	public void clearRect(int __x, int __y, int __w, int __h)
		throws IllegalArgumentException
	{
		// {@squirreljme.error AH0o Invalid rectangle size.}
		if (__w < 0 || __h < 0)
			throw new IllegalArgumentException("AH0o");
		
		// Pointless draw?
		if (__w == 0 || __h == 0)
			return;
		
		javax.microedition.lcdui.Graphics graphics = this._graphics;
		
		// The clearing is just drawing the standard background color over
		// the image
		int oldColor = graphics.getAlphaColor();
		try
		{
			// Use background color of the display
			graphics.setAlphaColor(this._bgColor._bgColor);
			
			// Use standard rectangular draw
			graphics.fillRect(__x, __y, __w, __h);
		}
		
		// Restore the old color
		finally
		{
			graphics.setAlphaColor(oldColor);
		}
	}
	
	@Api
	public Graphics copy()
	{
		throw Debugging.todo();
	}
	
	@Api
	public void dispose()
	{
		throw Debugging.todo();
	}
	
	@Api
	public void drawChars(char[] __c, int __x, int __y, int __off, int __len)
		throws IllegalArgumentException, NullPointerException
	{
		throw Debugging.todo();
	}
	
	@Api
	public void drawImage(Image __i, int __x, int __y)
		throws NullPointerException
	{
		if (__i == null)
			throw new NullPointerException("NARG");
		
		// What can even be done here?
		if (!(__i instanceof __MIDPImage__))
			throw new UIException(UIException.UNSUPPORTED_FORMAT);
		
		// Forward base image
		__MIDPImage__ midpImage = (__MIDPImage__)__i;
		this._graphics.drawImage(midpImage.__midpImage(), __x, __y,
			javax.microedition.lcdui.Graphics.TOP |
			javax.microedition.lcdui.Graphics.LEFT);
	}
	
	@Api
	public void drawLine(int __x1, int __y1, int __x2, int __y2)
	{
		throw Debugging.todo();
	}
	
	@Api
	public void drawPolyline(int[] __x, int[] __y, int __n)
		throws IllegalArgumentException
	{
		throw Debugging.todo();
	}
	
	@Api
	public void drawRect(int __x, int __y, int __w, int __h)
		throws IllegalArgumentException
	{
		// {@squirreljme.error AH0p Invalid rectangle size.}
		if (__w < 0 || __h < 0)
			throw new IllegalArgumentException("AH0p");
			
		// Pointless draw?
		if (__w == 0 || __h == 0)
			return;
		
		this._graphics.drawRect(__x, __y, __w, __h);
	}
	
	@Api
	public void drawString(String __s, int __x, int __y)
		throws NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		this._graphics.drawString(__s, __x, __y,
			javax.microedition.lcdui.Graphics.BASELINE);
	}
	
	@Api
	public void fillPolygon(int[] __x, int[] __y, int __n)
		throws IllegalArgumentException
	{
		throw Debugging.todo();
	}
	
	@Api
	public void fillRect(int __x, int __y, int __w, int __h)
		throws IllegalArgumentException
	{
		// {@squirreljme.error AH0q Invalid rectangle size.}
		if (__w < 0 || __h < 0)
			throw new IllegalArgumentException("AH0q");
			
		// Pointless draw?
		if (__w == 0 || __h == 0)
			return;
		
		this._graphics.fillRect(__x, __y, __w, __h);
	}
	
	@Api
	public void lock()
	{
		// Has no effect on SquirrelJME
	}
	
	@Api
	public void setColor(int __c)
		throws IllegalArgumentException
	{
		if ((__c & 0xFF_000000) != 0)
			throw Debugging.todo("Invalid color? %08x", __c);
		
		this._graphics.setColor(__c);
	}
	
	@Api
	public void setFont(Font __f)
	{
		throw Debugging.todo();
	}
	
	/**
	 * Sets the origin of graphics drawing operations.
	 * 
	 * @param __x The X position.
	 * @param __y The Y position.
	 * @since 2022/02/14
	 */
	@Api
	public void setOrigin(int __x, int __y)
	{
		javax.microedition.lcdui.Graphics graphics = this._graphics;
		graphics.translate(
			__x - graphics.getTranslateX(),
			__y - graphics.getTranslateY());
	}
	
	@Api
	public void unlock(boolean __forced)
	{
		// Has no effect on SquirrelJME
	}
	
	@SuppressWarnings({"MagicNumber", "unused"})
	public static int getColorOfName(int __name)
		throws IllegalArgumentException
	{
		switch (__name)
		{
			case Graphics.AQUA:		return 0x00FFFF;
			case Graphics.BLACK:	return 0x000000;
			case Graphics.BLUE:		return 0x0000FF;
			case Graphics.FUCHSIA:	return 0xFF00FF;
			case Graphics.GRAY:		return 0x808080;
			case Graphics.GREEN:	return 0x008000;
			case Graphics.LIME:		return 0x00FF00;
			case Graphics.MAROON:	return 0x800000;
			case Graphics.NAVY:		return 0x000080;
			case Graphics.OLIVE:	return 0x808000;
			case Graphics.PURPLE:	return 0x800080;
			case Graphics.RED:		return 0xFF0000;
			case Graphics.SILVER:	return 0xC0C0C0;
			case Graphics.TEAL:		return 0x008080;
			case Graphics.WHITE:	return 0xFFFFFF;
			case Graphics.YELLOW:	return 0xFFFF00;
		}
		
		// {@squirreljme.error AH0r Invalid color. (The color)}
		throw new IllegalArgumentException("AH0r " + __name);
	}
	
	@Api
	public static int getColorOfRGB(int __r, int __g, int __b)
	{
		throw Debugging.todo();
	}
}
