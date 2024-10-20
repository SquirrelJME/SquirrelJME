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
import cc.squirreljme.runtime.nttdocomo.ui.EightBitImageStore;
import com.nttdocomo.opt.ui.Graphics2;
import javax.microedition.lcdui.game.Sprite;

/**
 * This is used for drawing graphics onto a raster surface.
 *
 * @see javax.microedition.lcdui.Graphics
 * @since 2021/11/30
 */
@Api
public class Graphics
	extends Graphics2
{
	/** {@code #00FF00} via {@link #getColorOfName(int)}. */
	@Api
	public static final int AQUA = 3;
	
	/** {@code #000000} via {@link #getColorOfName(int)}. */
	@Api
	public static final int BLACK = 0;
	
	/** {@code #0000FF} via {@link #getColorOfName(int)}. */
	@Api
	public static final int BLUE = 1;
	
	/** Flip horizontal. */
	@Api
	public static final int	FLIP_HORIZONTAL = 1;
	
	/** No flipping. */
	@Api
	public static final int	FLIP_NONE = 0;
	
	/** Rotate 180 degrees . */
	@Api
	public static final int	FLIP_ROTATE = 3;
	
	/** Rotate left. */
	@Api
	public static final int	FLIP_ROTATE_LEFT = 4;
	
	/** Rotate Right . */
	@Api
	public static final int	FLIP_ROTATE_RIGHT = 5;
	
	/** Rotate right, flip horizontal . */
	@Api
	public static final int	FLIP_ROTATE_RIGHT_HORIZONTAL = 6;
	
	/** Rotate right, flip vertical . */
	@Api
	public static final int	FLIP_ROTATE_RIGHT_VERTICAL = 7;
	
	/** Flip vertically. */
	@Api
	public static final int	FLIP_VERTICAL = 2;
		
	/** {@code #FF0000} via {@link #getColorOfName(int)}. */
	@Api
	public static final int FUCHSIA = 5;
	
	/** {@code #FFFFFF} via {@link #getColorOfName(int)}. */
	@Api
	public static final int GRAY = 8;
	
	/** {@code #000080} via {@link #getColorOfName(int)}. */
	@Api
	public static final int GREEN = 10;
	
	/** {@code #0000FF} via {@link #getColorOfName(int)}. */
	@Api
	public static final int LIME = 2;
	
	/** {@code #008080} via {@link #getColorOfName(int)}. */
	@Api
	public static final int MAROON = 12;
	
	/** {@code #808080} via {@link #getColorOfName(int)}. */
	@Api
	public static final int NAVY = 9;
	
	/** {@code #808000} via {@link #getColorOfName(int)}. */
	@Api
	public static final int OLIVE = 14;
	
	/** {@code #800080} via {@link #getColorOfName(int)}. */
	@Api
	public static final int PURPLE = 13;
	
	/** {@code #00FFFF} via {@link #getColorOfName(int)}. */
	@Api
	public static final int RED = 4;
	
	/** {@code #C0C0C0} via {@link #getColorOfName(int)}. */
	@Api
	public static final int SILVER = 15;
	
	/** {@code #008000} via {@link #getColorOfName(int)}. */
	@Api
	public static final int TEAL = 11;
	
	/** {@code #FFFF00} via {@link #getColorOfName(int)}. */
	@Api
	public static final int WHITE = 7;
	
	/** {@code #FF00FF} via {@link #getColorOfName(int)}. */
	@Api
	public static final int YELLOW = 6;
	
	/** The background color for {@link #clearRect(int, int, int, int)}. */
	private final __BGColor__ _bgColor;
	
	/** The flush handler, which is optional. */
	private final __LockFlush__ _lockFlush;
	
	/** The base graphics to forward to. */
	private final javax.microedition.lcdui.Graphics _graphics;
	
	/** The default image flip mode. */
	private volatile int _flipMode =
		Graphics.FLIP_NONE;
	
	/**
	 * Wraps the given graphics object.
	 *
	 * @param __g The graphics to wrap.
	 * @param __bgColor The background color for
	 * {@link #clearRect(int, int, int, int)}.
	 * @param __flush Optional flush callback to be executed when this
	 * occurs.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/02/14
	 */
	Graphics(javax.microedition.lcdui.Graphics __g, __BGColor__ __bgColor,
		__LockFlush__ __flush)
		throws NullPointerException
	{
		if (__g == null)
			throw new NullPointerException("NARG");
		
		this._graphics = __g;
		this._bgColor = __bgColor;
		this._lockFlush = __flush;
		
		// Default to the default font to use
		__g.setFont(Font.getDefaultFont()._midpFont);
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
	
	/**
	 * This draws the outer edge of the ellipse from the given angles using
	 * the color, alpha, and stroke style.
	 *
	 * The coordinates are treated as if they were in a rectangular region. As
	 * such the center of the ellipse to draw the outline of is in the center
	 * of the specified rectangle.
	 *
	 * Note that no lines are drawn to the center point, so the shape does not
	 * result in a pie slice.
	 *
	 * The angles are in degrees and visually the angles match those of the
	 * unit circle correctly transformed to the output surface. As such, zero
	 * degrees has the point of {@code (__w, __h / 2)}, that is it points to
	 * the right. An angle at 45 degrees will always point to the top right
	 * corner.
	 *
	 * If the width or height are zero, then nothing is drawn. The arc will
	 * cover an area of {@code __w + 1} and {@code __h + 1}.
	 *
	 * @param __x The X position of the upper left corner, will be translated.
	 * @param __y The Y position of the upper left corner, will be translated.
	 * @param __w The width of the arc.
	 * @param __h The height of the arc.
	 * @param __startAngle The starting angle in degrees, 
	 * @param __arcAngle The offset from the starting angle, negative values
	 * indicate clockwise direction while positive values are counterclockwise.
	 * @since 2022/10/07
	 */
	@Api
	public void drawArc(int __x, int __y, int __w, int __h,
		int __startAngle, int __arcAngle)
	{
		this._graphics.drawArc(__x, __y, __w, __h, __startAngle, __arcAngle);
	}
	
	@Api
	public void drawChars(char[] __c, int __x, int __y, int __off, int __len)
		throws IllegalArgumentException
	{
		throw Debugging.todo();
	}
	
	@Api
	public void drawImage(Image __i, int __x, int __y)
		throws NullPointerException
	{
		if (__i == null)
			throw new NullPointerException("NARG");
		
		this.drawImage(__i, __x, __y, 0, 0,
			__i.getWidth(), __i.getHeight());
	}
	
	@Api
	public void drawImage(Image __i, int __dx, int __dy, int __sx, int __sy,
		int __w, int __h)
		throws IllegalArgumentException, NullPointerException, UIException
	{
		if (__i == null)
			throw new NullPointerException("NARG");
		if (__w < 0 || __h < 0)
			throw new IllegalArgumentException("ILLA");
		
		// Which image is being drawn?
		javax.microedition.lcdui.Image target;
		target = Graphics.__recoverImage(__i);
		
		// Which flip mode
		int trans = this.__mapFlip();
		
		// DoJa is more lenient when drawing out of range graphics, it just
		// gets clipped into range
		if (__sx < 0)
		{
			__w -= __sx;
			__sx = 0;
		}
		
		if (__sy < 0)
		{
			__h -= __sy;
			__sy = 0;
		}
		
		int ex = __sx + __w;
		int ey = __sy + __h;
		if (ex > target.getWidth())
			ex = target.getWidth();
		if (ey > target.getHeight())
			ey = target.getHeight();
		
		// Get corrected size
		__w = ex - __sx;
		__h = ey - __sy;
		
		// Not drawing anything after correcting?
		if (__w <= 0 || __h <= 0)
			return;
		
		// Draw it
		this._graphics.drawRegion(target, __sx, __sy,
			__w, __h, trans, __dx, __dy,
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
	public void drawScaledImage(Image __i, int __dx, int __dy,
		int __dw, int __dh, int __sx, int __sy, int __sw, int __sh)
		throws IllegalArgumentException, UIException, NullPointerException
	{
		if (__i == null)
			throw new NullPointerException("NARG");
		if (__dw < 0 || __dh < 0 || __sw < 0 || __sh < 0)
			throw new IllegalArgumentException("ILLA");
		
		throw Debugging.todo();
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
	
	/**
	 * This draws the filled slice of an ellipse (like a pie slice) from the
	 * given angles using the color, alpha, and stroke style.
	 *
	 * Unlike {@link #drawArc(int, int, int, int, int, int)}, the width and
	 * height are not increased by a single pixel.
	 *
	 * Otherwise, this follows the same set of rules as
	 * {@link #drawArc(int, int, int, int, int, int)}.
	 *
	 * @param __x The X position of the upper left corner, will be translated.
	 * @param __y The Y position of the upper left corner, will be translated.
	 * @param __w The width of the arc.
	 * @param __h The height of the arc.
	 * @param __startAngle The starting angle in degrees, 
	 * @param __arcAngle The offset from the starting angle, negative values
	 * indicate clockwise direction while positive values are counterclockwise.
	 * @see #drawArc(int, int, int, int, int, int)
	 * @since 2022/10/07
	 */
	@Api
	public void fillArc(int __x, int __y, int __w, int __h,
		int __startAngle, int __arcAngle)
	{
		this._graphics.fillArc(__x, __y, __w, __h, __startAngle, __arcAngle);
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
	
	/**
	 * Specifies that a double buffered draw operation has started. If
	 * double buffering is not supported, this does nothing.
	 *
	 * @since 2024/06/24
	 */
	@Api
	public void lock()
	{
		__LockFlush__ lockFlush = this._lockFlush;
		if (lockFlush != null)
			lockFlush.__lock();
	}
	
	/**
	 * Sets the new clipping area of the destination image. The previous
	 * clipping area is replaced.
	 *
	 * @param __x The X coordinate, will be translated.
	 * @param __y The Y coordinate, will be translated.
	 * @param __w The width.
	 * @param __h The height.
	 * @since 2022/10/07
	 */
	@Api
	public void setClip(int __x, int __y, int __w, int __h)
	{
		this._graphics.setClip(__x, __y, __w, __h);
	}
	
	/**
	 * Sets the given color.
	 * 
	 * @param __c The color to use.
	 * @throws IllegalArgumentException If the color is not valid for this
	 * device.
	 * @since 202/10/07
	 */
	@Api
	public void setColor(int __c)
		throws IllegalArgumentException
	{
		this._graphics.setColor(__c);
	}
	
	/**
	 * Sets the default flip mode to use when drawing images.
	 *
	 * @param __mode The flip mode to use.
	 * @throws IllegalArgumentException If the flip mode is not valid.
	 * @since 2024/08/11
	 */
	@Api
	public void setFlipMode(int __mode)
		throws IllegalArgumentException
	{
		/** {@squirreljme.error AH1f Invalid flip mode. (The mode)} */
		if (__mode != Graphics.FLIP_HORIZONTAL &&
			__mode != Graphics.FLIP_NONE &&
			__mode != Graphics.FLIP_ROTATE &&
			__mode != Graphics.FLIP_ROTATE_LEFT &&
			__mode != Graphics.FLIP_ROTATE_RIGHT &&
			__mode != Graphics.FLIP_ROTATE_RIGHT_HORIZONTAL &&
			__mode != Graphics.FLIP_ROTATE_RIGHT_VERTICAL &&
			__mode != Graphics.FLIP_VERTICAL)
			throw new IllegalArgumentException("AH1f " + __mode);
		
		// Set it
		this._flipMode = __mode;
	}
	
	/**
	 * Sets the font to use for drawing.
	 * 
	 * @param __f The font to use.
	 * @throws NullPointerException If no font was specified.
	 * @since 2022/10/07
	 */
	@Api
	public void setFont(Font __f)
		throws NullPointerException
	{
		if (__f == null)
			throw new NullPointerException("NARG");
		
		this._graphics.setFont(__f._midpFont);
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
		graphics.translate(__x - graphics.getTranslateX(),
			__y - graphics.getTranslateY());
	}
	
	/**
	 * Unlocks the double buffering operation.
	 *
	 * @param __forced If the operation is forced
	 * then the buffer is immediately drawn and the lock count is set to
	 * zero, otherwise this will only draw when the lock count is zero. 
	 * @since 2024/06/24
	 */
	@Api
	public void unlock(boolean __forced)
	{
		__LockFlush__ lockFlush = this._lockFlush;
		if (lockFlush != null)
			lockFlush.__unlock(__forced);
	}
	
	/**
	 * Maps the flip mode.
	 *
	 * @return The flip mode.
	 * @since 2024/08/13
	 */
	private int __mapFlip()
	{
		switch (this._flipMode)
		{
			case Graphics.FLIP_HORIZONTAL:
				return Sprite.TRANS_MIRROR;
			
			case Graphics.FLIP_ROTATE:
				return Sprite.TRANS_ROT180;
			
			case Graphics.FLIP_ROTATE_LEFT:
				return Sprite.TRANS_ROT270;
			
			case Graphics.FLIP_ROTATE_RIGHT:
				return Sprite.TRANS_ROT90;
			
			case Graphics.FLIP_ROTATE_RIGHT_HORIZONTAL:
				return Sprite.TRANS_MIRROR_ROT270;
			
			case Graphics.FLIP_ROTATE_RIGHT_VERTICAL:
				return Sprite.TRANS_MIRROR_ROT90;
				
			case Graphics.FLIP_VERTICAL:
				return Sprite.TRANS_MIRROR_ROT180;
				
			case Graphics.FLIP_NONE:
			default:
				return Sprite.TRANS_NONE;
		}
	}
	
	@SuppressWarnings("MagicNumber")
	@Api
	public static int getColorOfName(int __name)
		throws IllegalArgumentException
	{
		switch (__name)
		{
			case Graphics.AQUA:
				return 0x00FFFF;
			case Graphics.BLACK:
				return 0x000000;
			case Graphics.BLUE:
				return 0x0000FF;
			case Graphics.FUCHSIA:
				return 0xFF00FF;
			case Graphics.GRAY:
				return 0x808080;
			case Graphics.GREEN:
				return 0x008000;
			case Graphics.LIME:
				return 0x00FF00;
			case Graphics.MAROON:
				return 0x800000;
			case Graphics.NAVY:
				return 0x000080;
			case Graphics.OLIVE:
				return 0x808000;
			case Graphics.PURPLE:
				return 0x800080;
			case Graphics.RED:
				return 0xFF0000;
			case Graphics.SILVER:
				return 0xC0C0C0;
			case Graphics.TEAL:
				return 0x008080;
			case Graphics.WHITE:
				return 0xFFFFFF;
			case Graphics.YELLOW:
				return 0xFFFF00;
		}
		
		// {@squirreljme.error AH0r Invalid color. (The color)}
		throw new IllegalArgumentException("AH0r " + __name);
	}
	
	@Api
	public static int getColorOfRGB(int __r, int __g, int __b)
	{
		return Graphics.getColorOfRGB(__r, __g, __b, 255);
	}
	
	/**
	 * Returns the color code for the given RGBA color.
	 * 
	 * @param __r The red color.
	 * @param __g The green color.
	 * @param __b The blue color.
	 * @param __a The alpha level.
	 * @return The color code.
	 * @throws IllegalArgumentException If the values are out of range.
	 * @since 2022/10/07
	 */
	@Api
	public static int getColorOfRGB(int __r, int __g, int __b, int __a)
		throws IllegalArgumentException
	{
		// {@squirreljme.error AH0t Color out of range.}
		if (__r < 0 || __r > 255 ||
			__g < 0 || __g > 255 ||
			__b < 0 || __b > 255 ||
			__a < 0 || __a > 255)
			throw new IllegalArgumentException("AH0t");
		
		return (__a << 24) |
			(__r << 16) |
			(__g << 8) |
			__b;
	}
	
	
	/**
	 * Recovers the image to draw.
	 *
	 * @param __i The source image.
	 * @return The resultant image.
	 * @throws UIException If the image has been disposed of or is otherwise
	 * invalid.
	 * @since 2024/08/13
	 */
	private static javax.microedition.lcdui.Image __recoverImage(Image __i)
		throws UIException
	{
		if (__i instanceof __MIDPImage__)
			return ((__MIDPImage__)__i).__midpImage();
		
		// Mutable image
		else if (__i instanceof __MutableImage__)
			return __i._midpImage;
		
		// 8-bit image
		else if (__i instanceof __8BitImage__)
		{
			// Get the actual image to be drawn
			__8BitImage__ bitImage = (__8BitImage__)__i;
			EightBitImageStore store = bitImage._store;
			
			// Disposed?
			if (store == null)
				throw new UIException(UIException.ILLEGAL_STATE);
			
			// Get realized image
			return store.midpImage();
		}
		
		// Not supported at all
		else
		{
			// Debug
			Debugging.todoNote("Unsupported image %s", __i.getClass());
			
			throw new UIException(UIException.UNSUPPORTED_FORMAT);
		}
	}
}
