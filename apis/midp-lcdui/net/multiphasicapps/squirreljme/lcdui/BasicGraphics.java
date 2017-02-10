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
	/** Clipping above. */
	private static final int _CLIP_ABOVE =
		1;
	
	/** Clipping below. */
	private static final int _CLIP_BELOW =
		2;
	
	/** Clip right. */
	private static final int _CLIP_RIGHT =
		4;
	
	/** Clip left. */
	private static final int _CLIP_LEFT =
		8;
	
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
		// Translate all coordinates
		int transx = this._transx,
			transy = this._transy;
		__x1 += transx;
		__y1 += transy;
		__x2 += transx;
		__y2 += transy;
		
		// Get clipping region
		int clipsx = this._clipsx, clipsy = this._clipsy,
			clipex = this._clipex, clipey = this._clipey;
		
		// Perform Cohen-Sutherland line clipping
		for (;;)
		{
			// Determine points that lie outside the box
			int outa = __csOut(__x1, __y1, clipsx, clipsy, clipex, clipey),
				outb = __csOut(__x2, __y2, clipsx, clipsy, clipex, clipey);
			
			// Both points are outside the box, do nothing
			if ((outa & outb) != 0)
				return;
			
			// Both points are inside the box, use this line
			if (outa == 0 && outb == 0)
				break;
			
			// Only the second point is outside, swap the points so that the
			// first point is outside and the first is not
			if (outa == 0)
			{
				// Swap X
				int boop = __x1;
				__x1 = __x2;
				__x2 = boop;
				
				// Swap Y
				boop = __y1;
				__y1 = __y2;
				__y2 = boop;
				
				// Swap clip flags
				boop = outb;
				outb = outa;
				outa = boop;
			}
			
			// The point is clipped
			if (outa != 0)
			{
				// Differences of points
				int dx = __x2 - __x1,
					dy = __y2 - __y1;
				
				// Clips above the box
				if ((outa & _CLIP_ABOVE) != 0)
				{
					__x1 += dx * (clipey - __y1) / dy;
					__y1 = clipey;
				}
			
				// Clips below
				else if ((outa & _CLIP_BELOW) != 0)
				{
					__x1 += dx * (clipsy - __y1) / dy;
					__y1 = clipsy;
				}
			
				// Clips the right side
				else if ((outa & _CLIP_RIGHT) != 0)
				{
					__y1 += dy * (clipex - __x1) / dx;
					__x1 = clipex;
				}
			
				// Clips the left side
				else if ((outa & _CLIP_LEFT) != 0)
				{
					__y1 += dy * (clipsx - __x1) / dx;
					__x1 = clipsx;
				}
			}
		}
		
		// Have lines which always go to the right
		if (__x2 < __x1)
		{
			int boopx = __x1,
				boopy = __y1;
			__x1 = __x2;
			__y1 = __y2;
			__x2 = boopx;
			__y2 = boopy;
		}
		
		// Draw it
		primitiveLine(__x1, __y1, __x2, __y2, this._color,
			(this._strokestyle == DOTTED), (this._blendmode == SRC_OVER));
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
		// Get actual end points
		int ex = __x + __w,
			ey = __y + __h;
			
		// Translate all coordinates
		int transx = this._transx,
			transy = this._transy;
		__x += transx;
		__y += transy;
		ex += transx;
		ey += transy;
		
		// Force lower X
		if (ex < __x)
		{
			int boop = ex;
			ex = __x;
			__x = boop;
		}
		
		// Force lower Y
		if (ey < __y)
		{
			int boop = ey;
			ey = __y;
			__y = boop;
		}
		
		// Get clipping region
		int clipsx = this._clipsx, clipsy = this._clipsy,
			clipex = this._clipex, clipey = this._clipey;
		
		// Box is completely outside the bounds of the clip, do not draw
		if (ex < clipsx || __x >= clipex || ey < clipsy || __y >= clipey)
			return;
		
		// Left vertical shortening
		boolean lvs = (__x < clipsx);
		if (lvs)
			__x = clipsx;
		
		// Right vertical shortening
		boolean rvs = (ex >= clipex);
		if (rvs)
			ex = clipex - 1;
		
		// Calculate new width
		if (lvs || rvs)
			__w = ex - __x;
		
		// Bottom horizontal shortening
		boolean bhs = (__y < clipsy);
		if (bhs)
			__y = clipsy;
		
		// Top horizontal shortening
		boolean ths = (ey >= clipey);
		if (ths)
			ey = clipey - 1;
		
		// Calculate new height
		if (bhs || ths)
			__h = ey - __y;
		
		// Calculate line properties
		int color = this._color;
		boolean dotted = (this._strokestyle == DOTTED);
		boolean blend = (this._blendmode == SRC_OVER);
		
		// Draw the horizontal
		if (!bhs)
			primitiveHorizontalLine(__x, __y, __w, color, dotted, blend);
		if (!ths)
			primitiveHorizontalLine(__x, ey, __w, color, dotted, blend);
		
		// And the vertical
		if (!lvs)
			primitiveVerticalLine(__x, __y, __h, color, dotted, blend);
		if (!rvs)
			primitiveVerticalLine(ex, __y, __h, color, dotted, blend);
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
	protected void primitiveHorizontalLine(int __x, int __y,
		int __w, int __color, boolean __dotted, boolean __blend)
	{
		primitiveLine(__x, __y, __x + __w, __y, __color, __dotted, __blend);
	}
	
	/**
	 * Draws a primitive vertical line that exists only witin.
	 *
	 * The coordinates will be absolute coordinates after translation and
	 * clipping is performed. The start coordinate will always be the point
	 * closest to the origin.
	 *
	 * @param __x The start X coordinate.
	 * @param __y The start Y coordinate.
	 * @param __h The height of the line.
	 * @param __color The color to draw as, includes alpha.
	 * @param __dotted If {@code true} then the line should be drawn dotted.
	 * @param __blend If {@code true} then the {@link #SRC_OVER} blending mode
	 * is to be used.
	 * @since 2017/02/10
	 */
	protected void primitiveVerticalLine(int __x, int __y,
		int __h, int __color, boolean __dotted, boolean __blend)
	{
		primitiveLine(__x, __y, __x, __y + __h, __color, __dotted, __blend);
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
		
		// Never go past the end of the viewport because pixels will never
		// be drawn in negative regions
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
	
	/**
	 * Determines the Cohen-Sutherland clipping flags.
	 *
	 * @param __x Input X coordinate.
	 * @param __y Input Y coordinate.
	 * @param __csx Clipping box starting X.
	 * @param __csy Clipping box starting Y.
	 * @param __cex Clipping box ending X.
	 * @param __cey Clipping box ending Y.
	 * @return The clipping bit flags.
	 * @since 2017/09/10
	 */
	private static final int __csOut(int __x, int __y, int __csx, int __csy,
		int __cex, int __cey)
	{
		int rv = 0;
		
		// Clips above or below?
		if (__y > __cey)
			rv |= _CLIP_ABOVE;
		else if (__y < __csy)
			rv |= _CLIP_BELOW;
		
		// Clips right or left?
		if (__x > __cex)
			rv |= _CLIP_RIGHT;
		else if (__x < __csx)
			rv |= _CLIP_LEFT;
		
		return rv;
	}
}

