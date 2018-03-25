// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.gfx;

import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import javax.microedition.lcdui.Text;

/**
 * This is the base class for each individual array graphics implementation.
 *
 * @since 2018/03/25
 */
public abstract class AbstractArrayGraphics
	extends Graphics
{
	/** The width of the image. */
	protected final int width;
	
	/** The height of the image. */
	protected final int height;
	
	/** The pitch of the image. */
	protected final int pitch;
	
	/** The buffer offset. */
	protected final int offset;
	
	/** The number of elements that consist of pixel data. */
	protected final int numelements;
	
	/** Physical end of the buffer. */
	protected final int lastelement;
	
	/** Is there an alpha channel? */
	protected final boolean hasalphachannel;
	
	/** The current stroke style. */
	protected int strokestyle;
	
	/** Is a dot stroke being used? */
	protected boolean dotstroke;
	
	/** Translated X coordinate. */
	protected int transx;
	
	/** Translated Y coordinate. */
	protected int transy;
	
	/** The starting X clip. */
	protected int clipsx;
	
	/** The starting Y clip. */
	protected int clipsy;
	
	/** The ending X clip. */
	protected int clipex;
	
	/** The ending Y clip. */
	protected int clipey;
	
	/** The clip width. */
	protected int clipw;
	
	/** The clip height. */
	protected int cliph;
	
	/** The current font, null means default. */
	protected Font font;
	
	/** The current blending mode. */
	protected int blendmode;
	
	/** Could blending be done? */
	protected boolean candoblending;
	
	/** Is blending actually going to be done? */
	protected boolean doblending;
	
	/** The current color. */
	protected int color;
	
	/**
	 * Initializes the base graphics.
	 *
	 * @param __w The image width.
	 * @param __h The image height.
	 * @param __p The image pitch.
	 * @param __o The buffer offset.
	 * @param __l The length of the buffer.
	 * @param __ppe Pixels per element.
	 * @param __alpha Is there an alpha channel?
	 * @throws IllegalArgumentException If the width, height, or pitch are
	 * zero or negative; or pitch is lower than the width.
	 * @since 2018/03/25
	 */
	public AbstractArrayGraphics(int __w, int __h, int __p, int __o, int __l,
		int __ppe, boolean __alpha)
		throws IllegalArgumentException
	{
		// {@squirreljme.error EB2d Invalid width and/or height specified.}
		if (__w <= 0 || __h <= 0)
			throw new IllegalArgumentException("EB2d");
		
		// {@squirreljme.error EB2e The pitch is less than the width.}
		if (__p < __w)
			throw new IllegalArgumentException("EB2e");
		
		// {@squirreljme.error EB2g The specified parameters exceed the bounds
		// of the array. (The width; The height; The offset; The pitch;
		// The array length; The number of elements in the image)}
		int numelements = (__p * __h) / __ppe,
			lastelement = __o + __l;
		if (__o < 0 || lastelement > __l)
			throw new ArrayIndexOutOfBoundsException(
				String.format("EB2g %d %d %d %d %d %d", __w, __h,
				__o, __p, __l, numelements));
		
		// Set
		this.width = __w;
		this.height = __h;
		this.pitch = __p;
		this.offset = __o;
		this.numelements = numelements;
		this.lastelement = lastelement;
		this.hasalphachannel = __alpha;
	}
	
	/**
	 * Internal rectangle fill with blending.
	 *
	 * @param __x The X coordinate.
	 * @param __y The Y coordinate.
	 * @param __ex The end X coordinate.
	 * @param __ey The end Y coordinate.
	 * @param __w The width.
	 * @param __h The height.
	 * @since 2018/03/25
	 */
	protected abstract void internalFillRectBlend(int __x, int __y, int __ex,
		int __ey, int __w, int __h);
	
	/**
	 * Internal rectangle fill with blending.
	 *
	 * @param __x The X coordinate.
	 * @param __y The Y coordinate.
	 * @param __ex The end X coordinate.
	 * @param __ey The end Y coordinate.
	 * @param __w The width.
	 * @param __h The height.
	 * @since 2018/03/25
	 */
	protected abstract void internalFillRectSolid(int __x, int __y, int __ex,
		int __ey, int __w, int __h);
	
	/**
	 * Internally sets the color to be used for drawing.
	 *
	 * @param __a The alpha level.
	 * @param __rgb The RGB color.
	 * @param __blend Is blending to be performed?
	 * @since 2018/03/25
	 */
	protected abstract void internalSetColor(int __a, int __rgb,
		boolean __blend);
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final void clipRect(int __x, int __y, int __w, int __h)
	{
		// Translate
		__x += this.transx;
		__y += this.transy;
		
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
		
		// Additionally do not go past the edge ever that way the end
		// clipping point is always valid
		int width = this.width,
			height = this.height;
		if (ex > width)
			ex = width;
		if (ey > height)
			ey = height;
		
		// Get the old clipping bounds
		int oldclipsx = this.clipsx,
			oldclipsy = this.clipsy,
			oldclipex = this.clipex,
			oldclipey = this.clipey;
		
		// Only set the clipping bounds if they exceed the previous ones
		if (__x > oldclipsx)
			this.clipsx = __x;
		if (__y > oldclipsy)
			this.clipsy = __y;
		if (ex < clipex)
			this.clipex = ex;
		if (ey < clipey)
			this.clipey = ey;
		
		// Set width/height
		this.clipw = ex - __x;
		this.cliph = ey - __y;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final void copyArea(int __sx, int __sy, int __w, int __h,
		int __dx, int __dy, int __anchor)
		throws IllegalArgumentException, IllegalStateException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final void drawArc(int __x, int __y, int __w, int __h, int __sa,
		int __aa)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final void drawARGB16(short[] __data, int __off, int __scanlen,
		int __x, int __y, int __w, int __h)
		throws NullPointerException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final void drawChar(char __s, int __x, int __y, int __anchor)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final void drawChars(char[] __s, int __o, int __l, int __x,
		int __y, int __anchor)
		throws NullPointerException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final void drawImage(Image __i, int __x, int __y, int __anchor)
		throws IllegalArgumentException, NullPointerException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final void drawLine(int __x1, int __y1, int __x2, int __y2)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final void drawRGB(int[] __data, int __off, int __scanlen,
		int __x, int __y, int __w, int __h, boolean __alpha)
		throws NullPointerException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final void drawRGB16(short[] __data, int __off, int __scanlen,
		int __x, int __y, int __w, int __h)
		throws NullPointerException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final void drawRect(int __x, int __y, int __w, int __h)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final void drawRegion(Image __src, int __xsrc, int __ysrc,
		int __wsrc, int __hsrc, int __trans, int __xdest, int __ydest,
		int __anch)
		throws IllegalArgumentException, NullPointerException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final void drawRegion(Image __src, int __xsrc, int __ysrc,
		int __wsrc, int __hsrc, int __trans, int __xdest, int __ydest,
		int __anch, int __wdest, int __hdest)
		throws IllegalArgumentException, NullPointerException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final void drawRoundRect(int __x, int __y, int __w, int __h,
		int __aw, int __ah)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final void drawString(String __s, int __x, int __y,
		int __anchor)
		throws NullPointerException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final void drawSubstring(String __s, int __o, int __l, int __x,
		int __y, int __anchor)
		throws NullPointerException, StringIndexOutOfBoundsException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final void drawText(Text __t, int __x, int __y)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final void fillArc(int __x, int __y, int __w, int __h, int __sa,
		int __aa)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final void fillRect(int __x, int __y, int __w, int __h)
	{
		// Get actual end points
		int ex = __x + __w,
			ey = __y + __h;
			
		// Translate all coordinates
		int transx = this.transx,
			transy = this.transy;
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
		int clipsx = this.clipsx,
			clipsy = this.clipsy,
			clipex = this.clipex - 1,
			clipey = this.clipey - 1;
		
		// Never clip past the left/top
		if (__x < clipsx)
			__x = clipsx;
		if (__y < clipsy)
			__y = clipsy;
		
		// Never clip past the right/bottom
		if (ex > clipex)
			ex = clipex;
		if (ey > clipey)
			ey = clipey;
		
		// Calculate actual dimensions used
		__w = ex - __x;
		__h = ey - __y;
		
		// Perform drawing
		if (this.doblending)
			this.internalFillRectBlend(__x, __y, ex, ey, __w, __h);
		else
			this.internalFillRectSolid(__x, __y, ex, ey, __w, __h);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final void fillRoundRect(int __x, int __y, int __w, int __h,
		int __aw, int __ah)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final void fillTriangle(int __x1, int __y1, int __x2, int __y2,
		int __x3, int __y3)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final int getAlpha()
	{
		return (this.color >> 24) & 0xFF;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final int getAlphaColor()
	{
		return this.color;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final int getBlendingMode()
	{
		return this.blendmode;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final int getBlueComponent()
	{
		return (this.color) & 0xFF;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final int getClipHeight()
	{
		return this.cliph;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final int getClipWidth()
	{
		return this.clipw;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final int getClipX()
	{
		return this.clipsx - this.transx;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final int getClipY()
	{
		return this.clipsy - this.transy;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final int getColor()
	{
		return this.color & 0xFFFFFF;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final int getDisplayColor(int __rgb)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final Font getFont()
	{
		return this.font;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final int getGrayScale()
	{
		return (getRedComponent() + getGreenComponent() +
			getBlueComponent()) / 3;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final int getGreenComponent()
	{
		return (this.color >> 8) & 0xFF;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final int getRedComponent()
	{
		return (this.color >> 16) & 0xFF;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final int getStrokeStyle()
	{
		return this.strokestyle;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final int getTranslateX()
	{
		return this.transx;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final int getTranslateY()
	{
		return this.transy;
	}
	
	/**
	 * Resets all parameters of the graphics output.
	 *
	 * @param __clip If {@code true} then the clip is also reset.
	 * @since 2017/02/12
	 */
	public void resetParameters(boolean __clip)
	{
		// Clear translation
		this.transx = 0;
		this.transy = 0;
	
		// Reset clip also
		if (__clip)
		{
			int width = this.width,
				height = this.height;
			
			this.clipsx = 0;
			this.clipsy = 0;
			this.clipex = width;
			this.clipey = height;
			this.clipw = width;
			this.cliph = height;
		}
		
		// Always reset these
		this.setAlphaColor(0xFF000000);
		this.setBlendingMode(SRC_OVER);
		this.setStrokeStyle(SOLID);
		this.setFont(null);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final void setAlpha(int __a)
		throws IllegalArgumentException
	{
		this.setAlphaColor(__a, getRedComponent(), getGreenComponent(),
			getBlueComponent());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final void setAlphaColor(int __argb)
	{
		// Set the original color directly
		this.color = __argb;
		
		// Determine if blending is to be performed or it is just directly
		// setting values, blending is only performed if the alpha channel
		// is not fully opaque and blending is permitted
		int alpha = (__argb >>> 24);
		boolean doblending = (this.candoblending && alpha != 0xFF);
		
		// Set internal blend mode
		this.doblending = doblending;
		
		// Setting the color is internally implemented
		this.internalSetColor(alpha, __argb & 0xFFFFFF, doblending);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final void setAlphaColor(int __a, int __r, int __g, int __b)
		throws IllegalArgumentException
	{
		// {@squirreljme.error EB2h Color out of range. (Alpha; Red; Green;
		// Blue)}
		if (__a < 0 || __a > 255 || __r < 0 || __r > 255 ||
			__g < 0 || __g > 255 || __b < 0 || __b > 255)
			throw new IllegalArgumentException(String.format(
				"EB2h %d %d %d %d", __a, __r, __g, __b));
		
		// Set
		this.setAlphaColor((__a << 24) | (__r << 16) | (__g << 8) | __b);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final void setBlendingMode(int __m)
		throws IllegalArgumentException
	{
		boolean candoblending,
			oldcandoblending = this.candoblending;
		
		// Just use source pixels
		if (__m == SRC)
		{
			// {@squirreljme.error EB2j Cannot set the overlay blending mode
			// because this graphics context does not have the alpha channel.}
			if (!this.hasalphachannel)
				throw new IllegalArgumentException("EB2j");
			
			candoblending = false;
		}
		
		// Perform blending since this is the default mode
		else if (__m == SRC_OVER)
		{
			candoblending = true;
		}
		
		// {@squirreljme.error EB2i Unknown blending mode.}
		else
			throw new IllegalArgumentException("EB2i");
		
		// Set
		this.blendmode = __m;
		this.candoblending = candoblending;
		
		// If the blending mode has changed then possible blending tables
		// need to be recalculated accordingly
		if (candoblending != oldcandoblending)
			this.setAlphaColor(this.getAlphaColor());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final void setClip(int __x, int __y, int __w, int __h)
	{
		// Translate
		__x += this.transx;
		__y += this.transy;
		
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
		
		// Additionally do not go past the edge ever that way the end
		// clipping point is always valid
		int width = this.width,
			height = this.height;
		if (ex > width)
			ex = width;
		if (ey > height)
			ey = height;
		
		// Set
		this.clipsx = __x;
		this.clipsy = __y;
		this.clipex = ex;
		this.clipey = ey;
		
		// Set width/height
		this.clipw = ex - __x;
		this.cliph = ey - __y;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final void setColor(int __rgb)
	{
		this.setAlphaColor((this.getAlphaColor() & 0xFF_000000) |
			(__rgb & 0x00_FFFFFF));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final void setColor(int __r, int __g, int __b)
		throws IllegalArgumentException
	{
		this.setAlphaColor(getAlpha(), __r, __g, __b);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final void setFont(Font __a)
	{
		// Just set it
		this.font = __a;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final void setGrayScale(int __v)
	{
		this.setAlphaColor(getAlpha(), __v, __v, __v);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final void setStrokeStyle(int __a)
		throws IllegalArgumentException
	{
		// {@squirreljme.error EB0g Illegal stroke style.}
		if (__a != SOLID && __a != DOTTED)
			throw new IllegalArgumentException("EB0g");
		
		// Set
		this.strokestyle = __a;
		this.dotstroke = (__a == DOTTED);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final void translate(int __x, int __y)
	{
		this.transx += __x;
		this.transy += __y;
	}
}

