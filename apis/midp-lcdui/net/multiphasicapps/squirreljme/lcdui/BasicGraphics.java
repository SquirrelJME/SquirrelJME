// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.lcdui;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.Text;

/**
 * This class implements a large portion of the drawing operations and having
 * abstract methods for primitive drawing operations.
 *
 * @since 2017/02/10
 */
public abstract class BasicGraphics
	extends Graphics
{
	/** The current blending mode. */
	private volatile int _blendmode =
		SRC_OVER;
	
	/** The current color. */
	private volatile int _color =
		0xFF000000;
	
	/** The current stroke style. */
	private volatile int _strokestyle =
		SOLID;
	
	/** Translated X coordinate. */
	private volatile int _transx;
	
	/** Translated Y coordinate. */
	private volatile int _transy;
	
	/** The starting X clip. */
	private volatile int _clipsx;
	
	/** The starting Y clip. */
	private volatile int _clipsy;
	
	/** The ending X clip. */
	private volatile int _clipex =
		Integer.MAX_VALUE;
	
	/** The ending Y clip. */
	private volatile int _clipey =
		Integer.MAX_VALUE;
	
	/**
	 * Draws a primitive horizontal line that exists only witin.
	 *
	 * The coordinates will be absolute coordinates after translation and
	 * clipping is performed. The start coordinate will always be the point
	 * closest to the origin.
	 *
	 * @param __x The start X coordinate.
	 * @param __y The start Y coordinate.
	 * @param __w The width of the line.
	 * @param __color The color to draw as, includes alpha.
	 * @param __dotted If {@code true} then the line should be drawn dotted.
	 * @param __blend If {@code true} then the {@link #SRC_OVER} blending mode
	 * is to be used.
	 * @since 2017/02/10
	 */
	protected abstract void primitiveHorizontalLine(int __x, int __y,
		int __w, int __color, boolean __dotted, boolean __blend);
		
	/**
	 * Draws a primitive line.
	 *
	 * The coordinates will be absolute coordinates after translation and
	 * clipping is performed. The start coordinate will always be the point
	 * closest to the origin.
	 *
	 * @param __x1 The start X coordinate.
	 * @param __y1 The start Y coordinate.
	 * @param __x2 The end X coordinate.
	 * @param __y2 The end Y coordinate.
	 * @param __color The color to draw as, includes alpha.
	 * @param __dotted If {@code true} then the line should be drawn dotted.
	 * @param __blend If {@code true} then the {@link #SRC_OVER} blending mode
	 * is to be used.
	 * @since 2017/02/10
	 */
	protected abstract void primitiveLine(int __x1, int __y1, int __x2,
		int __y2, int __color, boolean __dotted, boolean __blend);
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	public final void clipRect(int __x, int __y, int __w, int __h)
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	public final void copyArea(int __sx, int __sy, int __w, int __h,
		int __dx, int __dy, int __anchor)
		throws IllegalArgumentException, IllegalStateException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	public final void drawArc(int __x, int __y, int __w, int __h, int __sa,
		int __aa)
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	public final void drawARGB16(short[] __data, int __off, int __scanlen,
		int __x, int __y, int __w, int __h)
		throws NullPointerException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	public final void drawChar(char __s, int __x, int __y, int __anchor)
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	public final void drawChars(char[] __s, int __o, int __l, int __x,
		int __y, int __anchor)
		throws NullPointerException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	public final void drawImage(Image __i, int __x, int __y, int __anchor)
		throws NullPointerException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	public final void drawLine(int __x1, int __y1, int __x2, int __y2)
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	public final void drawRGB(int[] __data, int __off, int __scanlen,
		int __x, int __y, int __w, int __h, boolean __alpha)
		throws NullPointerException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	public final void drawRGB16(short[] __data, int __off, int __scanlen,
		int __x, int __y, int __w, int __h)
		throws NullPointerException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	public final void drawRect(int __x, int __y, int __w, int __h)
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	public final void drawRegion(Image __src, int __xsrc, int __ysrc,
		int __w, int __h, int __trans, int __xdest, int __ydest, int __anch)
		throws NullPointerException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	public final void drawRegion(Image __src, int __xsrc, int __ysrc,
		int __w, int __h, int __trans, int __xdest, int __ydest, int __anch,
		int __wdest, int __hdest)
		throws NullPointerException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	public final void drawRoundRect(int __x, int __y, int __w, int __h,
		int __aw, int __ah)
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	public final void drawString(String __s, int __x, int __y,
		int __anchor)
		throws NullPointerException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	public final void drawSubstring(String __s, int __o, int __l, int __x,
		int __y, int __anchor)
		throws NullPointerException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	public final void drawText(Text __t, int __x, int __y)
		throws NullPointerException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	public final void fillArc(int __x, int __y, int __w, int __h, int __sa,
		int __aa)
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	public final void fillRect(int __x, int __y, int __w, int __h)
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	public final void fillRoundRect(int __x, int __y, int __w, int __h,
		int __aw, int __ah)
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	public final void fillTriangle(int __x1, int __y1, int __x2, int __y2,
		int __x3, int __y3)
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	public final int getAlpha()
	{
		return (this._color >> 24) & 0xFF;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	public final int getAlphaColor()
	{
		return this._color;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	public final int getBlendingMode()
	{
		return this._blendmode;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	public final int getBlueComponent()
	{
		return (this._color) & 0xFF;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	public final int getClipHeight()
	{
		return this._clipey - this._clipsy;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	public final int getClipWidth()
	{
		return this._clipex - this._clipsx;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	public final int getClipX()
	{
		return this._clipsx - this._transx;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	public final int getClipY()
	{
		return this._clipsy - this._transy;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	public final int getColor()
	{
		return this._color & 0xFFFFFF;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	public int getDisplayColor(int __rgb)
	{
		return __rgb;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	public final Font getFont()
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	public final int getGrayScale()
	{
		return (getRedComponent() + getGreenComponent() +
			getBlueComponent()) / 3;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	public final int getGreenComponent()
	{
		return (this._color >> 8) & 0xFF;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	public final int getRedComponent()
	{
		return (this._color >> 16) & 0xFF;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	public final int getStrokeStyle()
	{
		return this._strokestyle;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	public final int getTranslateX()
	{
		return this._transx;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	public final int getTranslateY()
	{
		return this._transy;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	public final void setAlpha(int __a)
		throws IllegalArgumentException
	{
		setAlphaColor(__a, getRedComponent(), getGreenComponent(),
			getBlueComponent());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	public final void setAlphaColor(int __argb)
	{
		setAlphaColor((__argb >> 24) & 0xFF,
			(__argb >> 16) & 0xFF,
			(__argb >>> 8) & 0xFF,
			__argb & 0xFF);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	public final void setAlphaColor(int __a, int __r, int __g, int __b)
		throws IllegalArgumentException
	{
		// {@squirreljme.error EB0b Color out of range. (Alpha; Red; Green;
		// Blue)}
		if (__a < 0 || __a > 255 || __r < 0 || __r > 255 ||
			__g < 0 || __g > 255 || __b < 0 || __b > 255)
			throw new IllegalArgumentException(String.format(
				"EB0b %d %d %d %d", __a, __r, __g, __b));
		
		// Set
		this._color = (__a << 24) | (__r << 16) | (__g << 8) | __b;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	public final void setBlendingMode(int __m)
		throws IllegalArgumentException
	{
		// {@squirreljme.error EB0a Unknown blending mode.}
		if (__m != SRC_OVER && __m != SRC)
			throw new IllegalArgumentException("EB0a");
		
		// Set
		this._blendmode = __m;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	public final void setClip(int __x, int __y, int __w, int __h)
	{
		// Translate
		__x += this._transx;
		__y += this._transy;
		
		// Get right end coordinates
		int ex = __x + __w,
			ey = __y + __h;
		
		// Swap X if lower
		if (ex < __x)
		{
			int boop = __x;
			__x = ex;
			ex = boop;
		}
		
		// Same for Y
		if (ey < __y)
		{
			int boop = __y;
			__y = ey;
			ey = boop;
		}
		
		// Never exceed the lower range
		if (__x < 0)
			__x = 0;
		if (__y < 0)
			__y = 0;
		
		// Set
		this._clipsx = __x;
		this._clipsy = __y;
		this._clipex = ex;
		this._clipey = ey;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	public final void setColor(int __rgb)
	{
		setAlphaColor(getAlpha(),
			(__rgb >> 16) & 0xFF,
			(__rgb >>> 8) & 0xFF,
			__rgb & 0xFF);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	public final void setColor(int __r, int __g, int __b)
		throws IllegalArgumentException
	{
		setAlphaColor(getAlpha(), __r, __g, __b);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	public final void setFont(Font __a)
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	public final void setGrayScale(int __v)
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	public final void setStrokeStyle(int __a)
		throws IllegalArgumentException
	{
		// {@squirreljme.error EB0c Illegal stroke style.}
		if (__a != SOLID && __a != DOTTED)
			throw new IllegalArgumentException("EB0c");
		
		// Set
		this._strokestyle = __a;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	public final void translate(int __x, int __y)
	{
		this._transx += __x;
		this._transy += __y;
	}
}

