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
 * This class contains the advanced graphics functions which uses
 * pre-determined functions to determine how to draw something.
 *
 * @since 2019/03/24
 */
public class AdvancedGraphics
	extends Graphics
{
	/** The array buffer. */
	protected final int[] buffer;
	
	/** The length of the buffer. */
	protected final int bufferlen;
	
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
	
	/** Absolute translated X coordinate. */
	protected final int abstransx;
	
	/** Absolute translated Y coordinate. */
	protected final int abstransy;
	
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
	
	/** The painting alpha color. */
	protected int paintalpha;
	
	/** The color to use for painting. */
	protected int paintcolor;
	
	/** Paint color with the alpha channel set to the max. */
	protected int paintcolorhigh;
	
	/** The alpha color and normal color for painting. */
	protected int paintalphacolor;
	
	/** Function for filled rectangle. */
	protected AdvancedFunction funcfillrect;
	
	/**
	 * Initializes the graphics.
	 *
	 * @param __buf The buffer.
	 * @param __alpha Does the buffer actually use the alpha channel?
	 * @param __aba Advanced buffer adapter, used to translate the internal
	 * integer based buffer to other formats.
	 * @param __w The image width.
	 * @param __h The image height.
	 * @param __p The image pitch.
	 * @param __o The buffer offset.
	 * @param __atx Absolute X translation.
	 * @param __aty Absolute Y translation.
	 * @throws IllegalArgumentException If the input parameters are not
	 * correct.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/03/24
	 */
	public AdvancedGraphics(int[] __buf, boolean __alpha,
		AdvancedBufferAdapter __aba,
		int __w, int __h, int __p, int __o, int __atx, int __aty)
		throws IllegalArgumentException, NullPointerException
	{
		if (__buf == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error EB2v Invalid width and/or height specified.}
		if (__w <= 0 || __h <= 0)
			throw new IllegalArgumentException("EB2v");
		
		// {@squirreljme.error EB2w The pitch is less than the width.}
		if (__p < __w)
			throw new IllegalArgumentException("EB2w");
		
		// {@squirreljme.error EB2x The specified parameters exceed the bounds
		// of the array. (The width; The height; The offset; The pitch;
		// The array length; The number of elements in the image)}
		int numelements = (__p * __h),
			lastelement = __o + numelements,
			buflen = __buf.length;
		if (__o < 0 || lastelement > buflen)
			throw new ArrayIndexOutOfBoundsException(
				String.format("EB2x %d %d %d %d %d %d", __w, __h,
				__o, __p, buflen, numelements));
		
		// Set
		this.buffer = __buf;
		this.bufferlen = buflen;
		this.width = __w;
		this.height = __h;
		this.pitch = __p;
		this.offset = __o;
		this.numelements = numelements;
		this.lastelement = lastelement;
		this.hasalphachannel = __alpha;
		
		// Setup absolute translation and initialize the base translation
		// which is at the absolute origin
		this.abstransx = __atx;
		this.abstransy = __aty;
		this.transx = __atx;
		this.transy = __aty;
		
		// Initial clipping rectangle has the image bounds
		this.clipex = __w;
		this.clipey = __h;
		
		// Reset all initial functions
		this.resetParameters(true);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/03/24
	 */
	@Override
	public void clipRect(int __x, int __y, int __w, int __h)
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
	 * @since 2019/03/24
	 */
	@Override
	public void copyArea(int __sx, int __sy, int __w, int __h,
		int __dx, int __dy, int __anchor)
		throws IllegalArgumentException, IllegalStateException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/03/24
	 */
	@Override
	public void drawArc(int __x, int __y, int __w, int __h, int __sa,
		int __aa)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/03/24
	 */
	@Override
	public void drawARGB16(short[] __data, int __off, int __scanlen,
		int __x, int __y, int __w, int __h)
		throws NullPointerException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/03/24
	 */
	@Override
	public void drawChar(char __s, int __x, int __y, int __anchor)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/03/24
	 */
	@Override
	public void drawChars(char[] __s, int __o, int __l, int __x,
		int __y, int __anchor)
		throws NullPointerException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/03/24
	 */
	@Override
	public void drawImage(Image __i, int __x, int __y, int __anchor)
		throws IllegalArgumentException, NullPointerException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/03/24
	 */
	@Override
	public void drawLine(int __x1, int __y1, int __x2, int __y2)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/03/24
	 */
	@Override
	public void drawRGB(int[] __data, int __off, int __scanlen,
		int __x, int __y, int __w, int __h, boolean __alpha)
		throws NullPointerException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/03/24
	 */
	@Override
	public void drawRGB16(short[] __data, int __off, int __scanlen,
		int __x, int __y, int __w, int __h)
		throws NullPointerException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/03/24
	 */
	@Override
	public void drawRect(int __x, int __y, int __w, int __h)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/03/24
	 */
	@Override
	public void drawRegion(Image __src, int __xsrc, int __ysrc,
		int __wsrc, int __hsrc, int __trans, int __xdest, int __ydest,
		int __anch)
		throws IllegalArgumentException, NullPointerException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/03/24
	 */
	@Override
	public void drawRegion(Image __src, int __xsrc, int __ysrc,
		int __wsrc, int __hsrc, int __trans, int __xdest, int __ydest,
		int __anch, int __wdest, int __hdest)
		throws IllegalArgumentException, NullPointerException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/03/24
	 */
	@Override
	public void drawRoundRect(int __x, int __y, int __w, int __h,
		int __aw, int __ah)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/03/24
	 */
	@Override
	public void drawString(String __s, int __x, int __y,
		int __anchor)
		throws NullPointerException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/03/24
	 */
	@Override
	public void drawSubstring(String __s, int __o, int __l, int __x,
		int __y, int __anchor)
		throws NullPointerException, StringIndexOutOfBoundsException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/03/24
	 */
	@Override
	public void drawText(Text __t, int __x, int __y)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/03/24
	 */
	@Override
	public void fillArc(int __x, int __y, int __w, int __h, int __sa,
		int __aa)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/03/24
	 */
	@Override
	public void fillRect(int __x, int __y, int __w, int __h)
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
		
		// Call function
		this.funcfillrect.function(this,
			new int[]{__x, __y, ex, ey, __w, __h},
			null);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/03/24
	 */
	@Override
	public void fillRoundRect(int __x, int __y, int __w, int __h,
		int __aw, int __ah)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/03/24
	 */
	@Override
	public void fillTriangle(int __x1, int __y1, int __x2, int __y2,
		int __x3, int __y3)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/03/24
	 */
	@Override
	public int getAlpha()
	{
		return (this.color >> 24) & 0xFF;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/03/24
	 */
	@Override
	public int getAlphaColor()
	{
		return this.color;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/03/24
	 */
	@Override
	public int getBlendingMode()
	{
		return this.blendmode;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/03/24
	 */
	@Override
	public int getBlueComponent()
	{
		return (this.color) & 0xFF;
	}
	
	/**
	 * Returns the element in the input array which represents the end of the
	 * clipping rectangle.
	 *
	 * @return The element which contains the end of the clipping rectangle.
	 * @since 2019/03/24
	 */
	public final int getClipElementEnd()
	{
		// Subtract one from the Y because it is on the next row
		return this.offset + (((this.pitch * (this.clipey - 1)) +
			(this.clipex)));
	}
	
	/**
	 * Returns the element in the input array which represents the start of
	 * the clipping rectangle.
	 *
	 * @return The element which contains the start of the clipping rectangle.
	 * @since 2019/03/24
	 */
	public final int getClipElementStart()
	{
		return this.offset + (((this.pitch * this.clipsy) + this.clipsx));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/03/24
	 */
	@Override
	public int getClipHeight()
	{
		return this.cliph;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/03/24
	 */
	@Override
	public int getClipWidth()
	{
		return this.clipw;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/03/24
	 */
	@Override
	public int getClipX()
	{
		return this.clipsx - this.transx;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/03/24
	 */
	@Override
	public int getClipY()
	{
		return this.clipsy - this.transy;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/03/24
	 */
	@Override
	public int getColor()
	{
		return this.color & 0xFFFFFF;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/03/24
	 */
	@Override
	public int getDisplayColor(int __rgb)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/03/24
	 */
	@Override
	public Font getFont()
	{
		Font rv = this.font;
		if (rv == null)
			rv = Font.getDefaultFont();
		return rv;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/03/24
	 */
	@Override
	public int getGrayScale()
	{
		return (getRedComponent() + getGreenComponent() +
			getBlueComponent()) / 3;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/03/24
	 */
	@Override
	public int getGreenComponent()
	{
		return (this.color >> 8) & 0xFF;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/03/24
	 */
	@Override
	public int getRedComponent()
	{
		return (this.color >> 16) & 0xFF;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/03/24
	 */
	@Override
	public int getStrokeStyle()
	{
		return this.strokestyle;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/03/24
	 */
	@Override
	public int getTranslateX()
	{
		return this.transx - this.abstransx;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/03/24
	 */
	@Override
	public int getTranslateY()
	{
		return this.transy - this.abstransy;
	}
	
	/**
	 * Resets all parameters of the graphics output.
	 *
	 * @param __clip If {@code true} then the clip is also reset.
	 * @since 2019/03/24
	 */
	public void resetParameters(boolean __clip)
	{
		// Clear translation
		this.transx = this.abstransx;
		this.transy = this.abstransy;
	
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
	 * @since 2019/03/24
	 */
	@Override
	public void setAlpha(int __a)
		throws IllegalArgumentException
	{
		this.setAlphaColor(__a, getRedComponent(), getGreenComponent(),
			getBlueComponent());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/03/24
	 */
	@Override
	public void setAlphaColor(int __argb)
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
		
		// Set painting colors
		this.paintalpha = alpha;
		this.paintcolor = __argb & 0xFFFFFF;
		this.paintcolorhigh = __argb | 0xFF000000;
		this.paintalphacolor = __argb;
		
		// Update functions
		this.__updateFunctions();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/03/24
	 */
	@Override
	public void setAlphaColor(int __a, int __r, int __g, int __b)
		throws IllegalArgumentException
	{
		// {@squirreljme.error EB0a Color out of range. (Alpha; Red; Green;
		// Blue)}
		if (__a < 0 || __a > 255 || __r < 0 || __r > 255 ||
			__g < 0 || __g > 255 || __b < 0 || __b > 255)
			throw new IllegalArgumentException(String.format(
				"EB0a %d %d %d %d", __a, __r, __g, __b));
		
		// Set
		this.setAlphaColor((__a << 24) | (__r << 16) | (__g << 8) | __b);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/03/24
	 */
	@Override
	public void setBlendingMode(int __m)
		throws IllegalArgumentException
	{
		boolean candoblending,
			oldcandoblending = this.candoblending;
		
		// Just use source pixels
		if (__m == SRC)
		{
			// {@squirreljme.error EB0b Cannot set the overlay blending mode
			// because this graphics context does not have the alpha channel.}
			if (!this.hasalphachannel)
				throw new IllegalArgumentException("EB0b");
			
			candoblending = false;
		}
		
		// Perform blending since this is the default mode
		else if (__m == SRC_OVER)
		{
			candoblending = true;
		}
		
		// {@squirreljme.error EB0c Unknown blending mode.}
		else
			throw new IllegalArgumentException("EB0c");
		
		// Set
		this.blendmode = __m;
		this.candoblending = candoblending;
		
		// If the blending mode has changed then possible blending tables
		// need to be recalculated accordingly
		if (candoblending != oldcandoblending)
			this.setAlphaColor(this.getAlphaColor());
		
		// Update functions
		this.__updateFunctions();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/03/24
	 */
	@Override
	public void setClip(int __x, int __y, int __w, int __h)
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
	 * @since 2019/03/24
	 */
	@Override
	public void setColor(int __rgb)
	{
		this.setAlphaColor((this.getAlphaColor() & 0xFF_000000) |
			(__rgb & 0x00_FFFFFF));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/03/24
	 */
	@Override
	public void setColor(int __r, int __g, int __b)
		throws IllegalArgumentException
	{
		this.setAlphaColor(getAlpha(), __r, __g, __b);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/03/24
	 */
	@Override
	public void setFont(Font __a)
	{
		// Just set it
		this.font = __a;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/03/24
	 */
	@Override
	public void setGrayScale(int __v)
	{
		this.setAlphaColor(getAlpha(), __v, __v, __v);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/03/24
	 */
	@Override
	public void setStrokeStyle(int __a)
		throws IllegalArgumentException
	{
		// {@squirreljme.error EB0d Illegal stroke style.}
		if (__a != SOLID && __a != DOTTED)
			throw new IllegalArgumentException("EB0d");
		
		// Set
		this.strokestyle = __a;
		this.dotstroke = (__a == DOTTED);
		
		// Update functions
		this.__updateFunctions();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/03/24
	 */
	@Override
	public void translate(int __x, int __y)
	{
		this.transx += __x;
		this.transy += __y;
	}
	
	/**
	 * Updates the graphics drawing functions to what is needed.
	 *
	 * @since 2019/03/24
	 */
	private final void __updateFunctions()
	{
		boolean doblending = this.doblending;
		
		this.funcfillrect = (doblending ? AdvancedFunction.FILLRECT_BLEND :
			AdvancedFunction.FILLRECT_NOBLEND);
	}
}

