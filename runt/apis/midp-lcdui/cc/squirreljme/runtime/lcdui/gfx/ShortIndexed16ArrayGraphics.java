// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
// Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
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
 * This class is automatically generated to from a template to support
 * multiple pixel formats which are backed by arrays.
 *
 * @since 2018/03/22
 */
public final class ShortIndexed16ArrayGraphics
	extends Graphics
{
	/** The width of the image. */
	protected final int width;

	/** The height of the image. */
	protected final int height;

	/** The pitch of the image. */
	protected final int pitch;

	/** The offset into the buffer data. */
	protected final int offset;

	/** The number of elements that consist of pixel data. */
	protected final int numelements;

	/** Physical end of the buffer. */
	protected final int lastelement;

	/** The array containing the buffer data. */
	private final short[] _buffer;


	/** Number of colors in the palette. */
	protected final int numcolors;

	/** The palette used for drawing. */
	private final int[] _palette;

	/** The scores for each palettized item. */
	private final int[] _palscores;

	/** The colors which are pre-calculated for blending colors. */
	private final short[] _blendcolors;


	/** The current blending mode. */
	protected int blendmode =
		SRC_OVER;

	/** The current color. */
	protected int color;

	/** The color to paint. */
	protected int paintcolor;

	/** The current stroke style. */
	protected int strokestyle =
		SOLID;

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

	/** The current font, null means default. */
	protected Font font;

	/**
	 * Initializes the graphics drawer which draws into the given array.
	 *
	 * @param __buf The buffer to draw into.

	 * @param __pal The palette data.

	 * @param __width The width of the image.
	 * @param __height The height of the image.
	 * @param __pitch The image pitch.
	 * @param __offset The data buffer offset.
	 * @throws ArrayIndexOutOfBoundsException If the image dimensions exceeds
	 * the array bounds.
	 * @throws IllegalArgumentException If the width or height is negative.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/24
	 */
	public ShortIndexed16ArrayGraphics(short[] __buf,

		int[] __pal,

		int __width, int __height, int __pitch, int __offset)
	throws ArrayIndexOutOfBoundsException, IllegalArgumentException,
		   NullPointerException
	{
		if (__buf == null)
			throw new NullPointerException("NARG");


		if (__pal == null)
			throw new NullPointerException("NARG");

		// {@squirreljme.error EBT7 The input palette does not have enough
		// entries to store color information.}
		int numcolors = __pal.length;
		if (numcolors < 65536)
			throw new IllegalArgumentException("EBT7");


		// {@squirreljme.error EBT0 Invalid width and/or height specified.}
		if (__width <= 0 || __height <= 0)
			throw new IllegalArgumentException("EBT0");

		// {@squirreljme.error EBT1 The pitch is less than the width.}
		if (__pitch < __width)
			throw new IllegalArgumentException("EBT1");

		// Count the number of actual elements which may be shifted down
		// if there are more pixels per element
		// {@squirreljme.error EBT2 The specified parameters exceed the bounds
		// of the array. (The pitch; The height; The offset; The pitch;
		// The array length; The number of elements in the image)}
		int numelements = (__pitch * __height) >>> 0,
		lastelement = __offset + numelements;
		if (__offset < 0 || lastelement > __buf.length)
			throw new ArrayIndexOutOfBoundsException(
				String.format("EBT2 %d %d %d %d %d %d", __pitch, __height,
					__offset, __pitch, __buf.length, numelements));

		// Set parameters
		this._buffer = __buf;
		this.width = __width;
		this.height = __height;
		this.pitch = __pitch;
		this.offset = __offset;
		this.numelements = numelements;
		this.lastelement = lastelement;

		// Initially clip to the image bounds
		this.clipex = __width;
		this.clipey = __height;


		// The palette is directly used and may change, although it will
		// result in undefined behavior if it is changed and a graphics object
		// is still valid
		this._palette = __pal;
		this.numcolors = numcolors;

		// Initialize the score for each color in the palette
		int[] palscores = new int[numcolors];
		for (int i = 0; i < numcolors; i++)
		{
			int v = __pal[i] & 0xFFFFFF;
			palscores[i] = (((((v) >>> 16) ^ ((v) & 0xFFFF)) >> 1) + (((v) >>> 16) & ((
							v) & 0xFFFF)));
		}
		this._palscores = palscores;

		// The blend table is just pre-allocated
		this._blendcolors = new short[numcolors];


		// Initialize the color
		this.setAlphaColor(0xFF000000);
	}

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
		throw new todo.TODO();
		/*
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
		int clipsx = this.clipsx, clipsy = this.clipsy,
		 clipex = Math.min(primitiveImageWidth(), this.clipex),
		 clipey = Math.min(primitiveImageHeight(), this.clipey);

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

		// Calculate line properties
		int color = this.color;
		boolean blend = __blend();
		int bor = __blendOr();

		// Draw horizontal spans
		for (int y = __y; y < ey; y++)
		 primitiveHorizontalLine(__x, y, __w, color, false, blend, bor);
		*/
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
		return this.clipey - this.clipsy;
	}

	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final int getClipWidth()
	{
		return this.clipex - this.clipsx;
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
		throw new todo.TODO();
	}

	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final void setAlphaColor(int __a, int __r, int __g, int __b)
	throws IllegalArgumentException
	{
		// {@squirreljme.error EBT4 Color out of range. (Alpha; Red; Green;
		// Blue)}
		if (__a < 0 || __a > 255 || __r < 0 || __r > 255 ||
			__g < 0 || __g > 255 || __b < 0 || __b > 255)
			throw new IllegalArgumentException(String.format(
					"EBT4 %d %d %d %d", __a, __r, __g, __b));

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
		// {@squirreljme.error EBT5 Unknown blending mode.}
		if (__m != SRC_OVER && __m != SRC)
			throw new IllegalArgumentException("EBT5");


		// {@squirreljme.error EBT6 Cannot set the overlay blending mode
		// because this graphics context does not have the alpha channel.}
		if (__m == SRC)
			throw new IllegalArgumentException("EBT6");


		// Set
		this.blendmode = __m;

		// Calculate some things
		throw new todo.TODO();
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
	}

	/**
	 * {@inheritDoc}
	 * @since 2018/03/23
	 */
	@Override
	public final void setColor(int __rgb)
	{
		this.setAlphaColor(getAlpha(),
			(__rgb >> 16) & 0xFF,
			(__rgb >>> 8) & 0xFF,
			__rgb & 0xFF);
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

		throw new todo.TODO();
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
