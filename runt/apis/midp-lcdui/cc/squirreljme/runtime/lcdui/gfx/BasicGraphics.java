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
 * This class implements a large portion of the drawing operations and having
 * abstract methods for primitive drawing operations.
 *
 * @since 2017/02/10
 */
public abstract class BasicGraphics
	extends Graphics
{
	/** The RGB slice size. */
	public static final int PIXEL_SLICE =
		4096;
	
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
	
	/** Is there an alpha channel? */
	protected final boolean hasalpha;
	
	/** The current blending mode. */
	protected int blendmode =
		SRC_OVER;
	
	/** The current color. */
	protected int color =
		0xFF_000000;
	
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
	protected int clipex =
		Integer.MAX_VALUE;
	
	/** The ending Y clip. */
	protected int clipey =
		Integer.MAX_VALUE;
	
	/** The current font, null means default. */
	protected Font font;
	
	/** Pixel slice which is used for RGB-based drawing operations. */
	private final int[] _slice =
		new int[PIXEL_SLICE];
	
	/**
	 * Returns the height of the surface.
	 *
	 * @return The height width.
	 * @since 2017/02/11
	 */
	protected abstract int primitiveImageHeight();
	
	/**
	 * Returns the width of the surface.
	 *
	 * @return The surface width.
	 * @since 2017/02/11
	 */
	protected abstract int primitiveImageWidth();
	
	/**
	 * Initializes the base graphics.
	 *
	 * @param __alpha Is there an alpha channel?
	 * @since 2017/10/16
	 */
	public BasicGraphics(boolean __alpha)
	{
		this.hasalpha = __alpha;
	}
	
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
	 * @param _color The color to draw as, includes alpha.
	 * @param __dotted If {@code true} then the line should be drawn dotted.
	 * @param __blend If {@code true} then the {@link #SRC_OVER} blending mode
	 * is to be used.
	 * @param __bor The blended OR value on the destination.
	 * @since 2017/02/10
	 */
	protected abstract void primitiveLine(int __x1, int __y1, int __x2,
		int __y2, int _color, boolean __dotted, boolean __blend, int __bor);
	
	/**
	 * Draws a primitive RGB slice.
	 *
	 * @param __b The source buffer containing RGB data.
	 * @param __o The offset into the buffer.
	 * @param __sl The scanline length in the source buffer.
	 * @param __x The destination X position.
	 * @param __y The destination Y position.
	 * @param __w The width of the tile.
	 * @param __h The height of the tile.
	 * @param __blend If {@code true} then the {@link #SRC_OVER} blending mode
	 * is to be used.
	 * @param __bor The blended OR value on the destination.
	 * @param __alpha The alpha value, if applicable.
	 * @since 2017/02/11
	 */
	protected abstract void primitiveRGBTile(int[] __b, int __o, int __l,
		int __x, int __y, int __w, int __h, boolean __blend, int __bor,
		int __alpha);
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	public final void clipRect(int __x, int __y, int __w, int __h)
	{
		throw new todo.TODO();
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
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	public final void drawArc(int __x, int __y, int __w, int __h, int __sa,
		int __aa)
	{
		throw new todo.TODO();
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
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	public final void drawChar(char __s, int __x, int __y, int __anchor)
	{
		throw new todo.TODO();
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
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	public final void drawImage(Image __i, int __x, int __y, int __anchor)
		throws IllegalArgumentException, NullPointerException
	{
		// Check
		if (__i == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error EB0a Images cannot have the baseline anchor
		// point.}
		if (__anchor == BASELINE)
			throw new IllegalArgumentException("EB0a");
		
		// Transform
		__x += this.transx;
		__y += this.transy;
		
		// Get image dimensions
		int iw = __i.getWidth(),
			ih = __i.getHeight();
		
		// Modify the coordinates for anchoring
		__x = __anchorX(__x, iw, __anchor);
		__y = __anchorY(__y, ih, __anchor);
		
		// Ending X/Y positions
		int ex = __x + iw,
			ey = __y + ih;
		
		// Get clipping region
		int clipsx = this.clipsx, clipsy = this.clipsy,
			clipex = Math.min(primitiveImageWidth(), this.clipex),
			clipey = Math.min(primitiveImageHeight(), this.clipey);
		
		// Box is completely outside the bounds of the clip, do not draw
		if (ex < clipsx || __x >= clipex || ey < clipsy || __y >= clipey)
			return;
		
		// The base X source of the image
		int bsx;
		if (__x < clipsx)
		{
			bsx = (clipsx - __x);
			__x = clipsx;
		}
		else
			bsx = 0;
		
		// The base Y source of the image
		int bsy;
		if (__y < clipsy)
		{
			bsy = (clipsy - __y);
			__y = clipsy;
		}
		else
			bsy = 0;
		
		// Clip the ending X position
		if (ex > clipex)
			ex = clipex;
		
		// And the Y position
		if (ey > clipey)
			ey = clipey;
		
		// Need slice for rendering
		int[] slice = this._slice;
		int slicelen = slice.length;
		
		// If the destination image does not have alpha then pixels will just
		// be drawn or not drawn, so in this event just ignore blend if there
		// is not alpha.
		int alpha = getAlpha();
		boolean blend = (__blend() && (alpha < 255 || __i.hasAlpha()));
		int bor = __blendOr();
		
		// Divide the source image into tiles so that drawing is done using
		// these tiles rather than by scanlines. This would produce faster
		// code because there would be less loop iterations along with less
		// method calls being performed.
		// The tile width stretches up to the slice length as much as
		// possible.
		int dw = ex - __x;
		int tilew = (dw < slicelen ? dw : slicelen);
		
		// The tile height is just the number of rows that are possible to
		// be filled. Although a division is here, it will still be faster
		// than multiple method calls.
		int dh = ey - __y;
		int tileh = slicelen / tilew;
		
		// Draw tiles
		for (int disty = dh; __y < ey; bsy += tileh, disty -= tileh,
			__y += tileh)
		{
			// Limit for the Y coordinate
			int limity = (disty < tileh ? disty : tileh);
			
			// Draw horizontal tiles
			for (int sx = bsx, dx = __x, distx = dw; dx < ex;
				sx += tilew, dx += tilew, distx -= tilew)
			{
				// Tiles may be clipped on the ride side
				int limitx = (distx < tilew ? distx : tilew);
				
				// Read image
				__i.getRGB(slice, 0, limitx, sx, bsy, limitx, limity);
				
				// Draw tile
				primitiveRGBTile(slice, 0, limitx, dx, __y, limitx, limity,
					blend, bor, alpha);
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	public final void drawLine(int __x1, int __y1, int __x2, int __y2)
	{
		// Translate all coordinates
		int transx = this.transx,
			transy = this.transy;
		__x1 += transx;
		__y1 += transy;
		__x2 += transx;
		__y2 += transy;
		
		// Get clipping region
		int clipsx = this.clipsx, clipsy = this.clipsy,
			clipex = Math.min(primitiveImageWidth(), this.clipex),
			clipey = Math.min(primitiveImageHeight(), this.clipey);
		
		// Perform Cohen-Sutherland line clipping
		for (;;)
		{
			// Determine points that lie outside the box
			int outa = __csOut(__x1, __y1,
					clipsx, clipsy, clipex - 1,clipey - 1),
				outb = __csOut(__x2, __y2, clipsx,
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
		
		// Draw it
		boolean dotted = (this.strokestyle == DOTTED);
		boolean blended = __blend();
		int bor = __blendOr();
		if (__y1 == __y2)
			primitiveHorizontalLine(__x1, __y1, __x2 - __x1, this.color,
				dotted, blended, bor);
		else if (__x1 == __x2)
		{
			// Lines have a right facing direction, but they may also face up
			// so handle this case
			if (__y2 < __y1)
				primitiveVerticalLine(__x1, __y2, __y1 - __y2, this.color,
					dotted, blended, bor);
			else
				primitiveVerticalLine(__x1, __y1, __y2 - __y1, this.color,
					dotted, blended, bor);
		}
		else
			primitiveLine(__x1, __y1, __x2, __y2, this.color,
				dotted, blended, bor);
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
		throw new todo.TODO();
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
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	public final void drawRect(int __x, int __y, int __w, int __h)
	{
		// The width and height are increased by a single pixel
		__w += 1;
		__h += 1;
		
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
			ex = clipex;
		
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
			ey = clipey;
		
		// Calculate new height
		if (bhs || ths)
			__h = ey - __y;
		
		// Calculate line properties
		int color = this.color;
		boolean dotted = (this.strokestyle == DOTTED);
		boolean blend = __blend();
		int bor = __blendOr();
		
		// Draw the horizontal
		if (!bhs)
			primitiveHorizontalLine(__x, __y, __w, color, dotted, blend, bor);
		if (!ths)
			primitiveHorizontalLine(__x, ey, __w, color, dotted, blend, bor);
		
		// And the vertical
		if (!lvs)
			primitiveVerticalLine(__x, __y, __h, color, dotted, blend, bor);
		if (!rvs)
			primitiveVerticalLine(ex, __y, __h, color, dotted, blend, bor);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	public final void drawRegion(Image __src, int __xsrc, int __ysrc,
		int __wsrc, int __hsrc, int __trans, int __xdest, int __ydest,
		int __anch)
		throws IllegalArgumentException, NullPointerException
	{
		drawRegion(__src, __xsrc, __ysrc, __wsrc, __hsrc, __trans,
			__xdest, __ydest, __anch, __wsrc, __hsrc);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	public final void drawRegion(Image __src, int __xsrc, int __ysrc,
		int __wsrc, int __hsrc, int __trans, int __xdest, int __ydest,
		int __anchor, int __wdest, int __hdest)
		throws IllegalArgumentException, NullPointerException
	{
		// Check
		if (__src == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error EB0b Images cannot have the baseline anchor
		// point.}
		if (__anchor == BASELINE)
			throw new IllegalArgumentException("EB0b");
		
		// Calculate source image coordinates
		int iw = __src.getWidth(),
			ih = __src.getHeight(),
			exsrc = __xsrc + __wsrc,
			eysrc = __ysrc + __hsrc;
		
		// {@squirreljme.error EB0c Source rectangle from an image exceeds the
		// bounds of the image.}
		if (__xsrc < 0 || __ysrc < 0 || exsrc > iw || eysrc > ih)
			throw new IllegalArgumentException("EB0c");
		
		// Transform
		__xdest += this.transx;
		__ydest += this.transy;
		
		// The destination is anchored
		__xdest = __anchorX(__xdest, __wdest, __anchor);
		__ydest = __anchorY(__ydest, __hdest, __anchor);
		
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	public final void drawRoundRect(int __x, int __y, int __w, int __h,
		int __aw, int __ah)
	{
		throw new todo.TODO();
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
		if (__s == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	public final void drawSubstring(String __s, int __o, int __l, int __x,
		int __y, int __anchor)
		throws NullPointerException, StringIndexOutOfBoundsException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	public final void drawText(Text __t, int __x, int __y)
		throws NullPointerException
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	public final void fillArc(int __x, int __y, int __w, int __h, int __sa,
		int __aa)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
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
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	public final void fillRoundRect(int __x, int __y, int __w, int __h,
		int __aw, int __ah)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	public final void fillTriangle(int __x1, int __y1, int __x2, int __y2,
		int __x3, int __y3)
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	public final int getAlpha()
	{
		return (this.color >> 24) & 0xFF;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	public final int getAlphaColor()
	{
		return this.color;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	public final int getBlendingMode()
	{
		return this.blendmode;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	public final int getBlueComponent()
	{
		return (this.color) & 0xFF;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	public final int getClipHeight()
	{
		return this.clipey - this.clipsy;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	public final int getClipWidth()
	{
		return this.clipex - this.clipsx;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	public final int getClipX()
	{
		return this.clipsx - this.transx;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	public final int getClipY()
	{
		return this.clipsy - this.transy;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	public final int getColor()
	{
		return this.color & 0xFFFFFF;
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
		return this.font;
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
		return (this.color >> 8) & 0xFF;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	public final int getRedComponent()
	{
		return (this.color >> 16) & 0xFF;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	public final int getStrokeStyle()
	{
		return this.strokestyle;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	public final int getTranslateX()
	{
		return this.transx;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	public final int getTranslateY()
	{
		return this.transy;
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
	 * @param _color The color to draw as, includes alpha.
	 * @param __dotted If {@code true} then the line should be drawn dotted.
	 * @param __blend If {@code true} then the {@link #SRC_OVER} blending mode
	 * is to be used.
	 * @param __bor The blended OR value for the destination.
	 * @since 2017/02/10
	 */
	protected void primitiveHorizontalLine(int __x, int __y,
		int __w, int _color, boolean __dotted, boolean __blend, int __bor)
	{
		primitiveLine(__x, __y, __x + __w, __y, _color, __dotted, __blend,
			__bor);
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
	 * @param _color The color to draw as, includes alpha.
	 * @param __dotted If {@code true} then the line should be drawn dotted.
	 * @param __blend If {@code true} then the {@link #SRC_OVER} blending mode
	 * is to be used.
	 * @param __bor The blended OR value for the destination.
	 * @since 2017/02/10
	 */
	protected void primitiveVerticalLine(int __x, int __y,
		int __h, int _color, boolean __dotted, boolean __blend, int __bor)
	{
		primitiveLine(__x, __y, __x, __y + __h, _color, __dotted, __blend,
			__bor);
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
	 * Resets all parameters of the graphics output.
	 *
	 * @param __clip If {@code true} then the clip is also reset.
	 * @since 2017/02/12
	 */
	public void resetParameters(boolean __clip)
	{
		// Always reset these
		this.blendmode = SRC_OVER;
		this.color = 0xFF000000;
		this.strokestyle = SOLID;
		this.transx = 0;
		this.transy = 0;
		this.font = null;
	
		// Reset clip also
		if (__clip)
		{
			this.clipsx = 0;
			this.clipsy = 0;
			this.clipex = Integer.MAX_VALUE;
			this.clipey = Integer.MAX_VALUE;
		}
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
		// {@squirreljme.error EB0d Color out of range. (Alpha; Red; Green;
		// Blue)}
		if (__a < 0 || __a > 255 || __r < 0 || __r > 255 ||
			__g < 0 || __g > 255 || __b < 0 || __b > 255)
			throw new IllegalArgumentException(String.format(
				"EB0d %d %d %d %d", __a, __r, __g, __b));
		
		// Set
		this.color = (__a << 24) | (__r << 16) | (__g << 8) | __b;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	public final void setBlendingMode(int __m)
		throws IllegalArgumentException
	{
		// {@squirreljme.error EB0e Unknown blending mode.}
		if (__m != SRC_OVER && __m != SRC)
			throw new IllegalArgumentException("EB0e");
		
		// {@squirreljme.error EB0f Cannot set the overlay blending mode
		// because this graphics context does not have the alpha channel.}
		if (__m == SRC && !this.hasalpha)
			throw new IllegalArgumentException("EB0f");
		
		// Set
		this.blendmode = __m;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
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
		
		// Set
		this.clipsx = __x;
		this.clipsy = __y;
		this.clipex = ex;
		this.clipey = ey;
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
		// Just set it
		this.font = __a;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	public final void setGrayScale(int __v)
	{
		setAlphaColor(getAlpha(), __v, __v, __v);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
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
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	public final void translate(int __x, int __y)
	{
		this.transx += __x;
		this.transy += __y;
	}
	
	/**
	 * Calculates the X adjustment to the anchor point.
	 *
	 * @param __x The X position.
	 * @param __w The width.
	 * @param __anchor The anchor to use.
	 * @return The adjusted position.
	 * @throws IllegalArgumentException If the anchor is not valid.
	 * @since 2017/02/11
	 */
	private final int __anchorX(int __x, int __w, int __anchor)
	{
		// Mask out Y anchors
		__anchor &= ~(BASELINE | BOTTOM | TOP | VCENTER);
		
		// Depends on the anchor point
		switch (__anchor)
		{
				// Non-transformed
			case 0:
			case LEFT:
				return __x;
				
				// Right
			case RIGHT:
				return __x - __w;
			
				// Center
			case HCENTER:
				return __x - (__w >>> 1);
				
				// {@squirreljme.error EB0h Invalid anchor point. (The anchor
				// point)}
			default:
				throw new IllegalArgumentException(String.format("EB0h %d",
					__anchor));
		}
	}
	
	/**
	 * Calculates the Y adjustment to the anchor point.
	 *
	 * @param __y The Y position.
	 * @param __h The height.
	 * @param __anchor The anchor to use.
	 * @return The adjusted position.
	 * @throws IllegalArgumentException If the anchor is not valid.
	 * @since 2017/02/11
	 */
	private final int __anchorY(int __y, int __h, int __anchor)
	{
		// Mask out X anchors
		__anchor &= ~(LEFT | RIGHT | HCENTER);
		
		// Depends on the anchor point
		switch (__anchor)
		{
				// Non-transformed
			case 0:
			case TOP:
				return __y;
				
				// Baseline
			case BASELINE:
				throw new todo.TODO();
				
				// Bottom
			case BOTTOM:
				return __y - __h;
				
				// Centered
			case VCENTER:
				return __y - (__h >>> 1);
				
				// {@squirreljme.error EB0i Invalid anchor point. (The anchor
				// point)}
			default:
				throw new IllegalArgumentException(String.format("EB0i %d",
					__anchor));
		}
	}
	
	/**
	 * Checks if blending is to be done or if direct overwrite is used.
	 *
	 * @return {@code true} if alpha blending is to be performed.
	 * @since 2017/02/12
	 */
	private final boolean __blend()
	{
		// There must be an alpha channel along with the blend mode being
		// actual blending
		return /*this.hasalpha &&*/ (blendmode == SRC_OVER);
	}
	
	/**
	 * This returns the value which is ORed to the destination to force it to
	 * have a higher alpha value.
	 *
	 * @return The ORed blending value used for blended draws.
	 * @since 2017/02/12
	 */
	private final int __blendOr()
	{
		return (this.hasalpha ? 0 : 0xFF);
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

