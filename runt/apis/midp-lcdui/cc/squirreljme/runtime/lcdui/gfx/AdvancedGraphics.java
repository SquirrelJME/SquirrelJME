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

import cc.squirreljme.runtime.lcdui.font.SQFFont;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.game.Sprite;
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
	
	/** Function for drawing characters. */
	protected AdvancedFunction funccharbmp;
	
	/** Function for drawing lines. */
	protected AdvancedFunction funcline;
	
	/** RGB tile. */
	protected AdvancedFunction funcrgbtile;
	
	/** ARGB tile. */
	protected AdvancedFunction funcargbtile;
	
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
		this.__unimplemented(__dx, __dy, "copyArea");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/03/24
	 */
	@Override
	public void drawArc(int __x, int __y, int __w, int __h, int __sa,
		int __aa)
	{
		this.__unimplemented(__x, __y, "drawArc");
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
		this.__unimplemented(__x, __y, "drawARGB16");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/03/24
	 */
	@Override
	public void drawChar(char __s, int __x, int __y, int __anchor)
	{
		// Same as drawing strings
		this.drawString(new String(new char[]{__s}, 0, 1),
			__x, __y, __anchor);
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
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Same as drawing string
		this.drawString(new String(__s, __o, __l),
			__x, __y, __anchor);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/03/24
	 */
	@Override
	public void drawImage(Image __i, int __x, int __y, int __anchor)
		throws IllegalArgumentException, NullPointerException
	{
		if (__i == null)
			throw new NullPointerException("NARG");
		
		this.drawRegion(__i, 0, 0, __i.getWidth(), __i.getHeight(), 0,
			__x, __y, __anchor);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/03/24
	 */
	@Override
	public void drawLine(int __x1, int __y1, int __x2, int __y2)
	{
		// Translate all coordinates
		int transx = this.transx,
			transy = this.transy;
		__x1 += transx;
		__y1 += transy;
		__x2 += transx;
		__y2 += transy;
		
		// Get clipping region
		int clipsx = this.clipsx,
			clipsy = this.clipsy,
			clipex = this.clipex,
			clipey = this.clipey;
		
		// Perform Cohen-Sutherland line clipping
		for (;;)
		{
			// Determine points that lie outside the box
			int outa = AdvancedGraphics.__csOut(__x1, __y1,
					clipsx, clipsy, clipex - 1,clipey - 1),
				outb = AdvancedGraphics.__csOut(__x2, __y2, clipsx,
					clipsy, clipex - 1, clipey - 1);
			
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
					__y1 = clipey - 1;
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
					__x1 = clipex - 1;
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
		
		// The resulting line should never be out of bounds
		if (__x1 < clipsx || __x2 < clipsx || __y1 < clipsy || __y2 < clipsy ||
			__x1 > clipex || __x2 > clipex || __y1 > clipey || __y2 > clipey)
			return;
		
		// Forward depending on blending and/or dots
		try
		{
			this.funcline.function(this,
				new int[]{__x1, __y1, __x2, __y2},
				null);
		}
		
		// Exception happened when drawing a line
		catch (IndexOutOfBoundsException e)
		{
			todo.DEBUG.note("Line (%d, %d) -> (%d, %d)", __x1, __y1,
				__x2, __y2);
			e.printStackTrace();
		}
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
		if (__data == null)
			throw new NullPointerException("NARG");
		
		// Transform
		__x += this.transx;
		__y += this.transy;
		
		// Determine ending position
		int ex = __x + __w,
			ey = __y + __h;
		
		// Get clipping region
		int clipsx = this.clipsx,
			clipsy = this.clipsy,
			clipex = this.clipex,
			clipey = this.clipey;
		
		// Box is completely outside the bounds of the clip, do not draw
		if (ex < clipsx || __x >= clipex || ey < clipsy || __y >= clipey)
			return;
		
		// Determine sub-clipping area
		int subx = __x - clipsx,
			suby = __y - clipsy;
		
		// Clip into bounds
		if (__x < 0)
			__x = 0;
		if (__y < 0)
			__y = 0;
		if (ex >= clipex)
			ex = clipex;
		if (ey >= clipey)
			ey = clipey;
		
		// New tile size
		int tw = ex - __x,
			th = ey - __y;
		
		// We might have multiplied alpha blending, or just normal blending
		// If __alpha is true then this is 32-bit RGBA!
		if (__alpha)
			this.funcargbtile.function(this,
				new int[]{__off, __scanlen, __x, __y, tw, th},
				new Object[]{__data});
		else
			this.funcrgbtile.function(this,
				new int[]{__off, __scanlen, __x, __y, tw, th},
				new Object[]{__data});
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
		this.__unimplemented(__x, __y, "drawRGB16");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/03/24
	 */
	@Override
	public void drawRect(int __x, int __y, int __w, int __h)
	{
		// The width and height are increased by a single pixel
		__w += 1;
		__h += 1;
		
		// For now just cheat and draw four lines
		int ex = __x + __w,
			ey = __y + __h;
		
		this.drawLine(__x, __y, ex, __y);
		this.drawLine(__x, ey, ex, ey);
		this.drawLine(__x, __y, __x, ey);
		this.drawLine(ex, __y, ex, ey);
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
		if (__src == null)
			throw new NullPointerException("NARG");
		
		this.__drawRegion(__src, __xsrc, __ysrc, __wsrc, __hsrc, __trans,
			__xdest, __ydest, __anch, __wsrc, __hsrc, true);
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
		if (__src == null)
			throw new NullPointerException("NARG");
		
		this.__drawRegion(__src, __xsrc, __ysrc, __wsrc, __hsrc, __trans,
			__xdest, __ydest, __anch, __wdest, __hdest, false);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/03/24
	 */
	@Override
	public void drawRoundRect(int __x, int __y, int __w, int __h,
		int __aw, int __ah)
	{
		this.__unimplemented(__x, __y, "drawRoundRect");
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
		if (__s == null)
			throw new NullPointerException("NARG");
		
		this.__drawText(this.__buildText(__s), __x, __y, __anchor);
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
		if (__s == null)
			throw new NullPointerException("NARG");
		if (__o < 0 || __l < 0 || (__o + __l) > __s.length())
			throw new StringIndexOutOfBoundsException("IOOB");
		
		this.__drawText(this.__buildText(__s.substring(__o, __o + __l)),
			__x, __y, __anchor);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/03/24
	 */
	@Override
	public void drawText(Text __t, int __x, int __y)
	{
		this.__drawText(__t, __x, __y, 0);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/03/24
	 */
	@Override
	public void fillArc(int __x, int __y, int __w, int __h, int __sa,
		int __aa)
	{
		this.__unimplemented(__x, __y, "fillArc");
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
		this.__unimplemented(__x, __y, "fillRoundRect");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2019/03/24
	 */
	@Override
	public void fillTriangle(int __x1, int __y1, int __x2, int __y2,
		int __x3, int __y3)
	{
		this.__unimplemented(__x1, __y1, "fillTriangle");
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
		// Just use the original input color
		return __rgb | 0xFF_000000;
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
	 * Builds and returns a text object for usage.
	 *
	 * @param __s The string used.
	 * @return A new text object.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/29
	 */
	private final Text __buildText(String __s)
		throws NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Get the font, or fallback to the default if it was not set
		Font font = this.getFont();
		
		// Setup, use a zero height for now since it will be calculated after
		// the font and such has been set
		Text rv = new Text(__s,
			font.stringWidth(__s), 0);
		
		// Set text properties
		if (font != null)
			rv.setFont(font);
		rv.setForegroundColor(this.color);
		
		// Set the height to the required height of the box now that the
		// parameters have been set
		rv.setHeight(rv.getRequiredHeight());
		
		return rv;
	}
	
	/**
	 * Draws region.
	 *
	 * @param __src Source image.
	 * @param __xsrc X source.
	 * @param __ysrc Y source.
	 * @param __wsrc W source.
	 * @param __hsrc H source.
	 * @param __trans Translation.
	 * @param __xdest X destination.
	 * @param __ydest Y destination.
	 * @param __anch Anchor.
	 * @param __wdest W destination.
	 * @param __hdest H destination.
	 * @param __dswap Swap destinations on rotate?
	 * @throws IllegalArgumentException If the input is not valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/18
	 */
	private final void __drawRegion(Image __src, int __xsrc, int __ysrc,
		int __wsrc, int __hsrc, int __trans, int __xdest, int __ydest,
		int __anch, int __wdest, int __hdest, boolean __dswap)
		throws IllegalArgumentException, NullPointerException
	{
		if (__src == null)
			throw new NullPointerException("NARG");
		
		// Is alpha used?
		boolean alpha = __src.hasAlpha();
		
		// Extract image pixel data
		int numpixels = __wsrc * __hsrc;
		int[] data = new int[numpixels];
		__src.getRGB(data, 0, __wsrc, __xsrc, __ysrc, __wdest, __hdest);
		
		// Perform the transformation, possibly returning a new data buffer
		int[] transdim = new int[]{__wsrc, __hsrc, __wdest, __hdest};
		data = this.__transform(__trans, data, __wsrc, __hsrc, transdim,
			__dswap);
		
		// Re-read the new image sizes!
		__wsrc = transdim[0];
		__hsrc = transdim[1];
		__wdest = transdim[2];
		__hdest = transdim[3];
		
		// Anchor horizontally?
		if ((__anch & HCENTER) == HCENTER)
			__xdest -= __wsrc >>> 1;
		
		// Anchor right?
		else if ((__anch & RIGHT) == RIGHT)
			__xdest -= __wsrc;
		
		// Anchor middle?
		if ((__anch & VCENTER) == VCENTER)
			__ydest -= __hsrc >>> 1;
		
		// Anchor bottom?
		else if ((__anch & BOTTOM) == BOTTOM)
			__ydest -= __hsrc;
		
		// If this is non-stretched we can just use the standard RGB
		// drawing function!
		if (__wsrc == __wdest && __hsrc == __hdest)
			this.drawRGB(data, 0, __wsrc, __xdest, __ydest, __wsrc, __hsrc,
				alpha);
		
		// Use stretchy draw
		else
			this.__drawRGBStretched(data, 0, __wsrc, __xdest, __ydest,
				__wsrc, __hsrc, alpha, __wdest, __hdest);
	}
	
	/**
	 * Draws stretched RGB.
	 *
	 * @param __data The data to draw.
	 * @param __off The offset into the array.
	 * @param __scanlen The scan length.
	 * @param __x X position.
	 * @param __y Y position.
	 * @param __wsrc Width of source.
	 * @param __hsrc Height of source.
	 * @param __alpha Use alpha channel?
	 * @param __wdest Width of destination.
	 * @param __hdest Height of destination.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/15
	 */
	private void __drawRGBStretched(int[] __data, int __off, int __scanlen,
		int __x, int __y, int __wsrc, int __hsrc, boolean __alpha,
		int __wdest, int __hdest)
		throws NullPointerException
	{
		if (__data == null)
			throw new NullPointerException("NARG");
		
		this.__unimplemented(__x, __y, "drawRGBStretched");
	}
	
	/**
	 * Draws the given text object.
	 *
	 * @param __t The text object to draw.
	 * @param __x The X position.
	 * @param __y The Y position.
	 * @param __anchor The, this just adjusts determines how the actual text
	 * box region is drawn. If baseline is used, Y is just offset by the
	 * baseline for the first character and not the entire block size.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/29
	 */
	final void __drawText(Text __t, int __x, int __y, int __anchor)
		throws NullPointerException
	{
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// Translate to the displayed coordinate space
		__x += this.transx;
		__y += this.transy;
		
		// Get clipping region
		int clipsx = this.clipsx,
			clipsy = this.clipsy,
			clipex = this.clipex - 1,
			clipey = this.clipey - 1;
		
		// Wanting to draw a bunch of text completely out of the clip? Ignore
		if (__x >= clipex || __y >= clipey)
			return;
		
		// Trying to draw the text completely out of the clip as well?
		int textw = __t.getWidth(),
			texth = __t.getHeight(),
			tex = __x + textw,
			tey = __y + texth;
		if (tex < clipsx || tey < clipsy)
			return;
		
		// The text box acts as an extra clip, so force everything to clip
		// in there
		if (__x > clipsx)
			clipsx = __x;
		if (tex < clipex)
			clipex = tex;
		if (__y > clipsy)
			clipsy = __y;
		if (tey < clipey)
			clipey = tey;
		
		// Cache the default font in the event it is never changed ever
		Font lastfont = __t.getFont();
		SQFFont sqf = SQFFont.cacheFont(lastfont);
		byte[] bmp = new byte[sqf.charbitmapsize];
		int pixelheight = sqf.pixelheight,
			bitsperscan = sqf.bitsperscan;
		
		// Blending the text?
		boolean doblending = this.doblending;
		
		// Need to store the properties since drawing of the text will
		// change how characters are drawn
		int oldcolor = this.getAlphaColor();
		try
		{
			// Read in all the text characters
			int n = __t.getTextLength();
			String chars = __t.getText(0, n);
			
			// Draw each character according to their metrics
			int[] metrics = new int[4];
			for (int i = 0; i < n; i++)
			{
				// Ignore certain characters
				char c = chars.charAt(i);
				if (c == '\r' || c == '\n')
					continue;
				
				// Set color to the foreground color of this character
				this.setAlphaColor(__t.getForegroundColor(i));
				
				// Need to find the SQF for this font again?
				Font drawfont = __t.getFont(i);
				if (drawfont != lastfont)
				{
					lastfont = drawfont;
					sqf = SQFFont.cacheFont(lastfont);
					bmp = new byte[sqf.charbitmapsize];
					pixelheight = sqf.pixelheight;
					bitsperscan = sqf.bitsperscan;
				}
				
				// Get the metrics for the character
				__t.getCharExtent(i, metrics);
				
				// Calculate the draw position of the character
				int dsx = __x + metrics[0],
					dsy = __y + metrics[1],
					dex = dsx + metrics[2],
					dey = dsy + metrics[3];
				
				// Completely out of bounds, ignore because we cannot draw it
				// anyway
				if (dsx >= clipex || dex <= 0 ||
					dsy >= clipey || dey <= 0)
					continue;
				
				// Map character index to the SQF
				char mc = SQFFont.mapChar(c);
				
				// Base scan offsets and such
				int scanoff = 0,
					scanlen = sqf.charWidth(mc),
					lineoff = 0,
					linelen = pixelheight;
				
				// Off the left side?
				if (dsx < clipsx)
				{
					int diff = clipsx - dsx;
					scanoff += diff;
					scanlen -= diff;
					
					// Reset to clip bound
					dsx = clipsx;
				}
				
				// Off the right side
				if (dex > clipex)
					scanlen -= (dex - clipex);
				
				// Off the top?
				if (dsy < clipsy)
				{
					int diff = clipsy - dsy;
					lineoff += diff;
					linelen -= diff;
					
					// Reset to clip bound
					dsy = clipsy;
				}
				
				// Off the bottom
				if (dey > clipey)
					linelen -= (dey - clipey);
				
				// Draw the bitmap for the character
				int bps = sqf.loadCharBitmap(mc, bmp);
				this.funccharbmp.function(this,
					new int[]{color, dsx, dsy, bps, scanoff, scanlen,
						lineoff, linelen},
					new Object[]{bmp});
			}
		}
		
		// Just in case revert properties
		finally
		{
			this.setAlphaColor(oldcolor);
		}
	}
	
	/**
	 * Draw a string which just says not implemented.
	 *
	 * @param __x X Coordinate.
	 * @param __y Y Coordinate.
	 * @param __txt The message text.
	 * @since 2019/03/25
	 */
	private final void __unimplemented(int __x, int __y, String __txt)
		throws NullPointerException
	{
		if (__txt == null)
			throw new NullPointerException("NARG");
		
		// Just draw crosshair and a string
		this.drawLine(__x - 5, __y, __x + 5, __y);
		this.drawLine(__x, __y - 5, __x, __y + 5);
		this.drawString(__txt, __x, __y, 0);
	}
	
	/**
	 * Updates the graphics drawing functions to what is needed.
	 *
	 * @since 2019/03/24
	 */
	private final void __updateFunctions()
	{
		boolean doblending = this.doblending,
			dotstroke = this.dotstroke;
		int blendmode = this.blendmode;
		
		// Blending
		if (doblending)
		{
			this.funcfillrect = AdvancedFunction.FILLRECT_BLEND;
			this.funccharbmp = AdvancedFunction.CHARBITMAP_BLEND;
			this.funcrgbtile = AdvancedFunction.RGBTILE_BLEND;
			this.funcargbtile = AdvancedFunction.ARGBTILE_BLEND;
			
			// Dotted
			if (dotstroke)
			{
				this.funcline = AdvancedFunction.LINE_BLEND_DOT;
			}
			
			// Not dotted
			else
			{
				this.funcline = AdvancedFunction.LINE_BLEND_NODOT;
			}
		}
		
		// Not blending
		else
		{
			this.funcfillrect = AdvancedFunction.FILLRECT_NOBLEND;
			this.funccharbmp = AdvancedFunction.CHARBITMAP_NOBLEND;
			this.funcrgbtile = AdvancedFunction.RGBTILE_NOBLEND;
			this.funcargbtile = AdvancedFunction.ARGBTILE_NOBLEND;
			
			// Dotted
			if (dotstroke)
			{
				this.funcline = AdvancedFunction.LINE_NOBLEND_DOT;
			}
			
			// Not dotted
			else
			{
				this.funcline = AdvancedFunction.LINE_NOBLEND_NODOT;
			}
		}
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
	
	/**
	 * Transforms the data in the buffer.
	 *
	 * @param __trans The transformation to perform.
	 * @param __data The input data.
	 * @param __wsrc The width of the source.
	 * @param __hsrc The width of the destination.
	 * @param __dimout Output dimensions.
	 * @param __dswap Swap destinations?
	 * @return The resulting data is translated, this may be the same as
	 * {@code __data}.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/15
	 */
	private static final int[] __transform(int __trans, int[] __data,
		int __wsrc, int __hsrc, int[] __dimout, boolean __dswap)
		throws NullPointerException
	{
		if (__data == null || __dimout == null)
			throw new NullPointerException("NARG");
		
		// Destination width and height
		int wdest = __wsrc,
			hdest = __hsrc;
		
		// Determine the transformation functions to use. There are just three
		// primitive transformation functions: flip horizontally, then
		// flip vertically, then rotate 90 degrees clockwise. This handles
		// every transformation which fill every single bit.
		byte xform = 0;
		switch (__trans)
		{
			// These bits represent the stuff to do! == 0b9VH;
			case Sprite.TRANS_NONE:				xform = 0b000; break;
			case Sprite.TRANS_MIRROR:			xform = 0b001; break;
			case Sprite.TRANS_MIRROR_ROT180:	xform = 0b010; break;
			case Sprite.TRANS_ROT180:			xform = 0b011; break;
			case Sprite.TRANS_ROT90:			xform = 0b100; break;
			case Sprite.TRANS_MIRROR_ROT90:		xform = 0b101; break;
			case Sprite.TRANS_MIRROR_ROT270:	xform = 0b110; break;
			case Sprite.TRANS_ROT270:			xform = 0b111; break;
			// These bits represent the stuff to do! == 0b9VH;
		}
		
		// Mirror horizontally?
		if ((xform & 0b001) != 0)
			for (int y = 0; y < hdest; y++)
			{
				int dx = wdest * y,
					de = (dx + wdest) - 1;
				for (int x = 0, n = (wdest >> 1); x < n; x++)
				{
					int t = __data[de];
					__data[de--] = __data[dx];
					__data[dx++] = t;
				}
			}
		
		// Mirror vertically?
		if ((xform & 0b010) != 0)
			for (int ya = 0, yn = __hsrc >> 1; ya < yn; ya++)
			{
				int ra = __wsrc * ya,
					rb = (__wsrc * (__hsrc - ya)) - __wsrc;
				
				// Flip
				for (int x = 0; x < __wsrc; x++)
				{
					int t = __data[rb];
					__data[rb++] = __data[ra];
					__data[ra++] = t;
				}
			}
		
		// Rotate 90 degrees clockwise
		if ((xform & 0b100) != 0)
		{
			// Original copy
			int[] orig = __data.clone();
			
			// Swap the X and Y pixels first
			int ttop = hdest - 1;
			for (int y = 0; y < hdest; y++)
				for (int x = 0; x < wdest; x++)
				{
					int ps = (wdest * y) + x,
						pd = (hdest * x) + (ttop - y);
					
					__data[pd] = orig[ps];
				}
			
			// The output width and height are flipped
			__dimout[0] = hdest;
			__dimout[1] = wdest;
			
			// Swap destinations as well?
			if (__dswap)
			{
				int t = __dimout[2];
				__dimout[2] = __dimout[3];
				__dimout[3] = t;
			}
		}
		
		// Otherwise use the same target dimensions
		else
		{
			__dimout[0] = wdest;
			__dimout[1] = hdest;
		}
		
		// Return the data
		return __data;
	}
}

