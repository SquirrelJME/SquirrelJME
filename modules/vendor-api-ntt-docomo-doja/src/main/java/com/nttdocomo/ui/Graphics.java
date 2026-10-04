// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.nttdocomo.ui;

import cc.squirreljme.jvm.mle.ObjectShelf;
import cc.squirreljme.jvm.mle.constants.UIPixelFormat;
import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.lcdui.gfx.ExtraGraphics;
import cc.squirreljme.runtime.midlet.DoJaRuntime;
import cc.squirreljme.runtime.nttdocomo.ui.EightBitImageStore;
import cc.squirreljme.runtime.nttdocomo.ui.BGColor;
import cc.squirreljme.runtime.nttdocomo.ui.LockFlush;

/**
 * This is used for drawing graphics onto a raster surface.
 *
 * @see javax.microedition.lcdui.Graphics
 * @since 2021/11/30
 */
@Api
public class Graphics
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
	private final BGColor _bgColor;

	/** The flush handler, which is optional. */
	private final LockFlush _lockFlush;

	/** The base graphics to forward to. */
	private final javax.microedition.lcdui.Graphics _graphics;

	/** The default image flip mode. */
	private volatile int _flipMode =
		Graphics.FLIP_NONE;

	/** Emoji color determination. */
	private volatile boolean _emojiColor;

	/** Is this disposed? */
	private volatile boolean _disposed;

	/**
	 * Wraps the given graphics object.
	 *
	 * @param __g The graphics to wrap.
	 * @param __bgColor The background color for
	 * {@link #clearRect(int, int, int, int)}.
	 * @param __flush Optional flush callback to be executed when this
	 * occurs.
	 * @throws NullPointerException On {@code null} arguments.
	 * @since 2022/02/14
	 */
	protected Graphics(javax.microedition.lcdui.Graphics __g,
		BGColor __bgColor, LockFlush __flush)
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

	/**
	 * Clears the clip region, effectively making the clip region be the whole
	 * surface area.
	 *
	 * @since 2026/04/09
	 */
	@Api
	public void clearClip()
	{
		this.__checkDispose();

		// ExtraGraphics can give us the surface area.
		ExtraGraphics g = this.__extra();
		this._graphics.setClip(0, 0, g.surfaceWidth(),
			g.surfaceHeight());
	}

	/**
	 * Clears the given rectangle.
	 *
	 * @param __x The X coordinate.
	 * @param __y The Y coordinate.
	 * @param __w The width.
	 * @param __h The height.
	 * @throws IllegalArgumentException If either the width and/or height
	 * are negative.
	 * @since 2025/12/21
	 */
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

		this.__checkDispose();

		javax.microedition.lcdui.Graphics graphics = this._graphics;

		// The clearing is just drawing the standard background color over
		// the image
		int oldColor = graphics.getAlphaColor();
		try
		{
			// Use background color of the display
			graphics.setAlphaColor(this._bgColor.bgColor);

			// Use standard rectangular draw
			graphics.fillRect(__x, __y, __w, __h);
		}

		// Restore the old color
		finally
		{
			graphics.setAlphaColor(oldColor);
		}
	}

	/**
	 * This reduces the clipping area of the drawing so that only the
	 * intersection of the current clip area, and the rectangle specified
	 * in this call is valid for drawing.
	 *
	 * This is only used to reduce the clipping area, to make it larger use
	 * either {@link Graphics#setClip(int, int, int, int)}, or
	 * {@link Graphics#clearClip()}.
	 *
	 * @param __x The X coordinate of the clipping rectangle,
	 * will be translated.
	 * @param __y The Y coordinate of the clipping rectangle,
	 * will be translated.
	 * @param __w The width of the rectangle.
	 * @param __h The height of the rectangle.
	 * @since 2026/10/01
	 */
	@Api
	public void clipRect(int __x, int __y, int __width, int __height)
	{
		this.__checkDispose();

		this._graphics.clipRect(__x, __y, __width, __height);
	}

	/**
	 * Creates a copy of this Graphics context in its current state.
	 *
	 * @return A copy of the graphics object, or {@code null} if a copy could not
	 * be made.
	 * @since 2021/09/30
	 */
	@Api
	public Graphics copy()
	{
		this.__checkDispose();

		throw Debugging.todo();
	}

	/**
	 * Copies a rectangular area of the canvas into another destination area.
	 *
	 * The destination area is positioned according to offsets, which means
	 * that, to copy data into a region above the source area, the {@code __dy}
	 * argument must be negative. Source and destination pixels outside of the
	 * canvas bounds are not processed, although the actual behavior is
	 * platform-dependent for DoJa-2.x.
	 *
	 * @param __sx The source X coordinate.
	 * @param __sy The source Y coordinate.
	 * @param __w The width to copy.
	 * @param __h The height to copy.
	 * @param __dx The X axis offset from the source X coordinate to the
	 * destination coordinate.
	 * @param __dy The Y axis offset from the source Y coordinate to the
	 * destination coordinate.
	 * @throws IllegalArgumentException If the width and/or height are
	 * negative.
	 * @since 2025/12/21
	 */
	@Api
	public void copyArea(int __sx, int __sy, int __w, int __h,
		int __dx, int __dy)
		throws IllegalArgumentException
	{
		if (__w < 0 || __h < 0)
			throw new IllegalArgumentException("NEGV");

		// Nothing to draw?
		if (__w == 0 || __h == 0)
			return;

		// Forward
		this._graphics.copyArea(__sx, __sy, __w, __h, __sx + __dx, __sy + __dy,
			javax.microedition.lcdui.Graphics.TOP |
				javax.microedition.lcdui.Graphics.LEFT);
	}

	/**
	 * Disposes the current graphics context.
	 *
	 * @since 2025/06/15
	 */
	@Api
	public void dispose()
	{
		// Set dispose flag, which is only considered on DoJa 2
		this._disposed = true;
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
	 * @param __startAngle The starting angle in degrees.
	 * @param __arcAngle The offset from the starting angle, negative values
	 * indicate clockwise direction while positive values are counterclockwise.
	 * @since 2022/10/07
	 */
	@Api
	public void drawArc(int __x, int __y, int __w, int __h,
		int __startAngle, int __arcAngle)
	{
		this.__checkDispose();

		this._graphics.drawArc(__x, __y, __w, __h, __startAngle, __arcAngle);
	}

	/**
	 * Draws the given characters at the baseline.
	 *
	 * @param __c The characters to draw.
	 * @param __x The X coordinate.
	 * @param __y The Y coordinate.
	 * @param __off The offset into the array.
	 * @param __n The number of characters to draw.
	 * @throws IllegalArgumentException Before DoJa 2.0, if the offset and/or
	 * length exceed the array bounds.
	 * @throws NullPointerException On {@code null} arguments.
	 * @throws StringIndexOutOfBoundsException DoJa 2.0 and after, if the
	 * offset and/or length exceed the array bounds.
	 * @since 2025/12/21
	 */
	@Api
	public void drawChars(char[] __c, int __x, int __y, int __off, int __n)
		throws IllegalArgumentException, NullPointerException,
			StringIndexOutOfBoundsException
	{
		if (__c == null)
			throw new NullPointerException("NARG");

		// Different DoJa versions throw distinct exceptions
		if (__off < 0 || __n < 0 || (__off + __n) < 0 ||
			(__off + __n) > __c.length)
		{
			if (DoJaRuntime.versionBefore(2, 0))
				throw new IllegalArgumentException("IOOB");
			else if (DoJaRuntime.versionLeast(2, 0))
				throw new StringIndexOutOfBoundsException("IOOB");
		}

		this.__checkDispose();

		// TODO: Support default emoji color
		this._graphics.drawChars(__c, __x, __y, __off, __n,
			javax.microedition.lcdui.Graphics.BASELINE);
	}

	/**
	 * Draws the specified image with the given 2D affine transform matrix.
	 *
	 * @param __i The image to draw.
	 * @param __matrix The affine matrix.
	 * @throws ArrayIndexOutOfBoundsException If {@code __matrix} has less than
	 * 6 elements.
	 * @throws NullPointerException On {@code null} arguments.
	 * @since 2026/10/01
	 */
	@Api
	public void drawImage(Image __i, int[] __matrix)
		throws ArrayIndexOutOfBoundsException, NullPointerException
	{
		if (__i == null || __matrix == null)
			throw new NullPointerException("NARG");

		if (__matrix.length < 6)
			throw new ArrayIndexOutOfBoundsException("IOOB");

		this.__checkDispose();

		throw Debugging.todo();
	}

	/**
	 * Draws the specified rectangle area of an image with the given 2D affine
	 * transform matrix.
	 *
	 * @param __i The image to draw.
	 * @param __matrix The affine matrix.
	 * @param __sx The X coordinate for the top-left corner of the rectangle
	 * to draw.
	 * @param __sy The Y coordinate for the top-left corner of the rectangle
	 * to draw.
	 * @param __width The width of the rectangle area to draw.
	 * @param __height The height of the rectangle area to draw.
	 * @throws ArrayIndexOutOfBoundsException If {@code __matrix} has less than
	 * 6 elements.
	 * @throws IllegalArgumentException If the width and/or height are
	 * negative.
	 * @throws NullPointerException On {@code null} arguments.
	 * @since 2026/10/01
	 */
	@Api
	public void drawImage(Image __i, int[] __matrix, int __sx, int __sy,
		int __width, int __height)
		throws ArrayIndexOutOfBoundsException, IllegalArgumentException,
		NullPointerException
	{
		if (__i == null || __matrix == null)
			throw new NullPointerException("NARG");

		if (__width < 0 || __height < 0)
			throw new IllegalArgumentException("NEGV");

		if (__matrix.length < 6)
			throw new ArrayIndexOutOfBoundsException("IOOB");

		this.__checkDispose();

		throw Debugging.todo();
	}

	/**
	 * Draws the specified image at the given coordinates.
	 *
	 * @param __i The image to draw.
	 * @param __x The X coordinate.
	 * @param __y The Y coordinate.
	 * @throws NullPointerException On {@code null} arguments.
	 * @since 2025/12/21
	 */
	@Api
	public void drawImage(Image __i, int __x, int __y)
		throws NullPointerException
	{
		if (__i == null)
			throw new NullPointerException("NARG");

		this.__checkDispose();

		this.drawImage(__i, __x, __y, 0, 0,
			__i.getWidth(), __i.getHeight());
	}

	/**
	 * Draws a part of the given image at the given coordinates, if the
	 * width and/or height would exceed the image bounds it is not drawn.
	 *
	 * @param __i The image to draw.
	 * @param __dx The destination X coordinate.
	 * @param __dy The destination Y coordinate.
	 * @param __sx The source X coordinate.
	 * @param __sy The source Y coordinate.
	 * @param __w The width of the area to draw.
	 * @param __h The height to draw.
	 * @throws IllegalArgumentException If the width and/or height are
	 * negative.
	 * @throws NullPointerException On {@code null} arguments.
	 * @throws UIException If the image has already been disposed.
	 * @since 2025/12/21
	 */
	@Api
	public void drawImage(Image __i, int __dx, int __dy, int __sx, int __sy,
		int __w, int __h)
		throws IllegalArgumentException, NullPointerException, UIException
	{
		if (__i == null)
			throw new NullPointerException("NARG");

		if (__w < 0 || __h < 0)
			throw new IllegalArgumentException("NEGV");

		this.__checkDispose();

		// Forward to other call, as it simplifies the shared logic
		this.drawScaledImage(__i,
			__dx, __dy, __w, __h,
			__sx, __sy, __w, __h);
	}

	/**
	 * Draws an {@link ImageMap} such that the top left corner of the window
	 * aligns with the received x and y coordinates.
	 *
	 * @param __map The image map to draw.
	 * @param __x The source X coordinate.
	 * @param __y The source Y coordinate.
	 * @throws NullPointerException If the image map is {@code null}.
	 * @throws UIException If the image map holds a reference to an image that
	 * has already been disposed of. In this case, this exception is thrown
	 * after drawing all of the ImageMap's cells.
	 * @since 2026/10/01
	 */
	@Api
	public void drawImageMap(ImageMap __map, int __x, int __y)
		throws NullPointerException, UIException
	{
		if (__map == null)
			throw new NullPointerException("NARG");

		this.__checkDispose();

		__map.__draw(this, __x, __y);
	}

	/**
	 * Draws a line.
	 *
	 * @param __x1 The starting X coordinate.
	 * @param __y1 The starting Y coordinate.
	 * @param __x2 The ending X coordinate.
	 * @param __y2 The ending Y coordinate.
	 * @since 2025/12/21
	 */
	@Api
	public void drawLine(int __x1, int __y1, int __x2, int __y2)
	{
		this.__checkDispose();

		this._graphics.drawLine(__x1, __y1, __x2, __y2);
	}

	/**
	 * Draws the given set of points as a non-filled polygon.
	 *
	 * @param __x The X coordinates.
	 * @param __y The Y coordinates.
	 * @param __n The number of points to draw.
	 * @throws ArrayIndexOutOfBoundsException For DoJa 2.0+, this is thrown
	 * if {@code __n} is negative and/or exceeds the array bounds.
	 * @throws IllegalArgumentException Before DoJa 2.0, this is thrown if
	 * any argument is {@code null} and {@code __n} is positive; if {@code __n}
	 * is negative; or if {@code __n} exceeds array bounds.
	 * @throws NullPointerException For DoJa 2.0+, this is thrown if any
	 * input argument is {@code null}.
	 * @since 2025/12/21
	 */
	@Api
	@SuppressWarnings("DuplicatedCode")
	public void drawPolyline(int[] __x, int[] __y, int __n)
		throws ArrayIndexOutOfBoundsException, IllegalArgumentException,
			NullPointerException
	{
		// DoJa 1.0 just throws IllegalArgumentException if __n is negative
		// or too large for an array, however there is no specific case for
		// an NPE to occur
		if (DoJaRuntime.versionBefore(2, 0))
		{
			// It is assumed that null arrays are zero length
			if (__n < 0 || __n > (__x != null ? __x.length : 0) ||
				__n > (__y != null ? __y.length : 0))
				throw new IllegalArgumentException("NARG");
		}

		// DoJa 2.0+ uses these exceptions instead for consistency with
		// the version with the offset specified
		else if (DoJaRuntime.versionLeast(2, 0))
		{
			if (__x == null || __y == null)
				throw new NullPointerException("NARG");

			if (__n < 0 || __n > __x.length || __n > __y.length)
				throw new ArrayIndexOutOfBoundsException("NARG");
		}

		this.__checkDispose();

		// Not drawing anything?
		if (__n <= 0)
			return;

		// Forward
		this.drawPolyline(__x, __y, 0, __n);
	}

	/**
	 * Draws the given set of points as a non-filled polygon.
	 *
	 * @param __x The X coordinates.
	 * @param __y The Y coordinates.
	 * @param __o The offset into the array.
	 * @param __n The number of points to draw.
	 * @throws ArrayIndexOutOfBoundsException If {@code __n} is negative
	 * and/or exceeds the array bounds.
	 * @throws NullPointerException On {@code null} arguments.
	 * @since 2025/12/21
	 */
	@Api
	@SuppressWarnings("DuplicatedCode")
	public void drawPolyline(int[] __x, int[] __y, int __o, int __n)
		throws ArrayIndexOutOfBoundsException, NullPointerException
	{
		if (__x == null || __y == null)
			throw new NullPointerException("NARG");

		if (__o < 0 || __n < 0 || (__o + __n) > __x.length ||
			(__o + __n) > __y.length)
			throw new ArrayIndexOutOfBoundsException("IOOB");

		// Pointless draw?
		if (__n == 0)
			return;

		this.__checkDispose();

		// ExtraGraphics handles this
		this.__extra().drawPolyline(__x, __o,
			__y, __o, __n);
	}

	/**
	 * Draws a non-filled rectangle.
	 *
	 * @param __x The X coordinate.
	 * @param __y The Y coordinate.
	 * @param __w The width.
	 * @param __h The height.
	 * @throws IllegalArgumentException If any rectangle dimension is
	 * negative.
	 * @since 2025/12/21
	 */
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

		this.__checkDispose();

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

		this.__checkDispose();

		// Which image is being drawn?
		javax.microedition.lcdui.Image target;
		target = Graphics.__recoverImage(__i);

		// Which flip mode
		int trans = this.__mapFlip();

		// DoJa is more lenient when drawing out of range graphics, it just
		// gets clipped into range
		if (__sx < 0)
		{
			// Note sx is negative, so we subtract width
			__sw += __sx;
			__sx = 0;
		}

		if (__sy < 0)
		{
			// Note sy is negative, so we subtract height
			__sh += __sy;
			__sy = 0;
		}

		int ex = __sx + __sw;
		int ey = __sy + __sh;
		if (ex > target.getWidth())
			ex = target.getWidth();
		if (ey > target.getHeight())
			ey = target.getHeight();

		// Get corrected size
		__sw = ex - __sx;
		__sh = ey - __sy;

		// Not drawing anything after correcting?
		if (__sw <= 0 || __sh <= 0)
			return;

		// Draw it
		this._graphics.drawRegion(target, __sx, __sy,
			__sw, __sh, trans, __dx, __dy,
			javax.microedition.lcdui.Graphics.TOP |
			javax.microedition.lcdui.Graphics.LEFT,
			__dw, __dh);
	}

	/**
	 * Draws the specified sprite set on the current graphics origin. Sprites
	 * that are set to not be visible, sprites that do not have an image, and
	 * sprites with disposed images are not drawn.
	 *
	 * @param __sprites The sprite set to draw.
	 * @throws NullPointerException If {@code __sprites} is {@code null},
	 * contains {@code null} elements, or has no sprite objects.
	 * @throws UIException At least one of the sprites of the set has been
	 * unused or disposed of.
	 * @since 2026/10/01
	 */
	@Api
	public void drawSpriteSet(SpriteSet __sprites)
		throws NullPointerException, UIException
	{
		this.drawSpriteSet(__sprites, 0, __sprites.getCount());
	}

	/**
	 * Draws a specific range of the specified sprite set on the current
	 * graphics origin. Sprites that are set to not be visible, sprites that do
	 * not have an image, and sprites with disposed images are not drawn.
	 *
	 * @param __sprites The sprite set to draw.
	 * @param __offset The offset for the first sprite to draw.
	 * @param __count The amount of sprites to draw starting from the offset.
	 * @throws NullPointerException If {@code __sprites} is {@code null},
	 * contains {@code null} elements, or has no sprite objects.
	 * @throws IllegalArgumentException If the offset is less than zero; if the
	 * sprite count is less than zero; if the offset is larger than the amount
	 * of sprites in the set; or if the sum of offset and count are larger than
	 * the amount of sprites in the set.
	 * @throws UIException At least one of the sprites of the set's specified
	 * range has been unused or disposed of.
	 * @since 2026/10/01
	 */
	@Api
	public void drawSpriteSet(SpriteSet __sprites, int __offset, int __count)
		throws IllegalArgumentException, NullPointerException, UIException
	{
		if (__sprites == null)
			throw new NullPointerException("NARG");

		/* {@squirreljme.error AH2a Invalid image index.} */
		if (__offset < 0 || __count < 0 || __offset > __sprites.getCount() ||
			__offset + __count > __sprites.getCount())
			throw new IllegalArgumentException("AH2a");

		this.__checkDispose();

		for (int i = __offset; i < __offset + __count; i++)
		{
			Sprite s = __sprites.getSprite(i);

			this.drawImage(s.__getImage(), s.getX(), s.getY(), s.__getImageX(),
				s.__getImageY(), s.getWidth(), s.getHeight());
		}
	}

	/**
	 * Draws the given string at the baseline.
	 *
	 * @param __s The string to draw.
	 * @param __x The X coordinate.
	 * @param __y The Y coordinate.
	 * @throws NullPointerException On {@code null} arguments.
	 * @since 2025/12/21
	 */
	@Api
	public void drawString(String __s, int __x, int __y)
		throws NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");

		this.__checkDispose();

		// TODO: Support default emoji color
		this._graphics.drawString(__s, __x, __y,
			javax.microedition.lcdui.Graphics.BASELINE);
	}

	/**
	 * Draws the given string at the baseline.
	 *
	 * @param __s The string to draw.
	 * @param __x The X coordinate.
	 * @param __y The Y coordinate.
	 * @param __off The offset into the array.
	 * @param __n The number of characters to draw.
	 * @throws NullPointerException On {@code null} arguments.
	 * @throws StringIndexOutOfBoundsException If the
	 * offset and/or length exceed the array bounds.
	 * @since 2025/12/21
	 */
	@Api
	public void drawString(String __s, int __x, int __y, int __off, int __n)
		throws IllegalArgumentException, NullPointerException,
			StringIndexOutOfBoundsException
	{
		if (__s == null)
			throw new NullPointerException("NARG");

		if (__off < 0 || __n < 0 || (__off + __n) < 0 ||
			(__off + __n) > __s.length())
			throw new StringIndexOutOfBoundsException("IOOB");

		this.__checkDispose();

		// TODO: Support default emoji color
		this._graphics.drawSubstring(__s, __off, __n, __x, __y,
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
	 * @param __startAngle The starting angle in degrees.
	 * @param __arcAngle The offset from the starting angle, negative values
	 * indicate clockwise direction while positive values are counterclockwise.
	 * @see #drawArc(int, int, int, int, int, int)
	 * @since 2022/10/07
	 */
	@Api
	public void fillArc(int __x, int __y, int __w, int __h,
		int __startAngle, int __arcAngle)
	{
		this.__checkDispose();

		this._graphics.fillArc(__x, __y, __w, __h, __startAngle, __arcAngle);
	}

	/**
	 * Draws the given set of points as a filled polygon.
	 *
	 * @param __x The X coordinates.
	 * @param __y The Y coordinates.
	 * @param __n The number of points to draw.
	 * @throws ArrayIndexOutOfBoundsException For DoJa 2.0+, this is thrown
	 * if {@code __n} is negative and/or exceeds the array bounds.
	 * @throws IllegalArgumentException Before DoJa 2.0, this is thrown if
	 * any argument is {@code null} and {@code __n} is positive; if {@code __n}
	 * is negative; or if {@code __n} exceeds array bounds.
	 * @throws NullPointerException For DoJa 2.0+, this is thrown if any
	 * input argument is {@code null}.
	 * @since 2025/12/21
	 */
	@Api
	@SuppressWarnings("DuplicatedCode")
	public void fillPolygon(int[] __x, int[] __y, int __n)
		throws ArrayIndexOutOfBoundsException, IllegalArgumentException,
			NullPointerException
	{
		// DoJa 1.0 just throws IllegalArgumentException if __n is negative
		// or too large for an array, however there is no specific case for
		// an NPE to occur
		if (DoJaRuntime.versionBefore(2, 0))
		{
			// It is assumed that null arrays are zero length
			if (__n < 0 || __n > (__x != null ? __x.length : 0) ||
				__n > (__y != null ? __y.length : 0))
				throw new IllegalArgumentException("NARG");
		}

		// DoJa 2.0+ uses these exceptions instead for consistency with
		// the version with the offset specified
		else if (DoJaRuntime.versionLeast(2, 0))
		{
			if (__x == null || __y == null)
				throw new NullPointerException("NARG");

			if (__n < 0 || __n > __x.length || __n > __y.length)
				throw new ArrayIndexOutOfBoundsException("NARG");
		}

		this.__checkDispose();

		// Not drawing anything?
		if (__n <= 0)
			return;

		// Forward
		this.fillPolygon(__x, __y, 0, __n);
	}

	/**
	 * Draws the given set of points as a non-filled polygon.
	 *
	 * @param __x The X coordinates.
	 * @param __y The Y coordinates.
	 * @param __o The offset into the array.
	 * @param __n The number of points to draw.
	 * @throws ArrayIndexOutOfBoundsException If {@code __n} is negative
	 * and/or exceeds the array bounds.
	 * @throws NullPointerException On {@code null} arguments.
	 * @since 2025/12/21
	 */
	@Api
	@SuppressWarnings("DuplicatedCode")
	public void fillPolygon(int[] __x, int[] __y, int __o, int __n)
		throws ArrayIndexOutOfBoundsException, NullPointerException
	{
		if (__x == null || __y == null)
			throw new NullPointerException("NARG");

		if (__o < 0 || __n < 0 || (__o + __n) > __x.length ||
			(__o + __n) > __y.length)
			throw new ArrayIndexOutOfBoundsException("IOOB");

		// Pointless draw?
		if (__n == 0)
			return;

		this.__checkDispose();

		// ExtraGraphics handles this
		this.__extra().fillPolygon(__x, __o,
			__y, __o, __n);
	}

	/**
	 * Draws a-filled rectangle.
	 *
	 * @param __x The X coordinate.
	 * @param __y The Y coordinate.
	 * @param __w The width.
	 * @param __h The height.
	 * @throws IllegalArgumentException If any rectangle dimension is
	 * negative.
	 * @since 2025/12/21
	 */
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

		this.__checkDispose();

		this._graphics.fillRect(__x, __y, __w, __h);
	}

	/**
	 * Returns the pixel color at the specified [x, y] coordinate. The pixel
	 * can be retrieved from anywhere on the canvas, as clipping is ignored.
	 *
	 * Although not disclosed, the pixel format is assumed to be the device's
	 * native pixel format, so no pixel conversion of any kind is done to the
	 * returned data, especially since said behavior be achieved by calling for
	 * {@link Graphics#getRGBPixel(int, int)} instead.
	 *
	 * If the coordinates point to a pixel outside of the canvas or image
	 * bounds, the canvas or image is not the current {@link Frame}, a dialog
	 * is currently being displayed, or IME is active,
	 * {@link Graphics#getColorOfName(Graphics#BLACK)} is returned.
	 *
	 * When double-buffering is enabled, this method returns the pixel value
	 * from the back buffer.
	 *
	 * If an {@link XString} is being drawn while this is called, it will
	 * constitute a security violation and a forced termination must occur.
	 *
	 * @param __x The X coordinate of the pixel to retrieve.
	 * @param __y The Y coordinate of the pixel to retrieve.
	 * @return The pixel color at the specified coordinates.
	 * @since 2026/06/01
	 */
	@Api
	public int getPixel(int __x, int __y)
	{
		ExtraGraphics g = this.__extra();
		if (__x < 0 || __y < 0 || __x >= g.surfaceWidth() ||
			__y >= g.surfaceHeight())
			return Graphics.getColorOfName(Graphics.BLACK);

		this.__checkDispose();

		// TODO: Add ScritchUI getPixel() and use that for ExtraGraphics
		return this.getPixels(__x, __y, 1, 1, null, 0)[0];
	}

	/**
	 * Returns an array of pixel colors starting at the designated [x, y]
	 * coordinate and extending to the [width, height] rectangle. The pixels
	 * can be retrieved from anywhere on the canvas, as clipping is ignored.
	 *
	 * Although not disclosed, the pixel format is assumed to be the device's
	 * native pixel format, so no pixel conversion of any kind is done to the
	 * returned data, especially since said behavior be achieved by calling for
	 * {@link Graphics#getRGBPixels(int, int, int, int, int[], int)} instead.
	 *
	 * Behavior if the area is out of bounds either completely or partially is
	 * unknown at this moment. Aside from that, this method follows the same
	 * concepts as {@link Graphics#getPixel(int, int)}.
	 *
	 * If the {@code __pixels} argument is not {@code null}, values are stored
	 * directly into it, but if it is {@code null}, this method automatically
	 * creates a properly sized array and returns the data to it.
	 *
	 * The logic behind this function can be explained as:
	 * {@code __pixels[__off + j * width + i] = this.getPixel(x + i, y + j)}
	 * for each pixel within the specified rectangle area.
	 *
	 * @param __x The starting X coordinate of the pixels to retrieve.
	 * @param __y The starting Y coordinate of the pixels to retrieve.
	 * @param __width The rectangle's width.
	 * @param __height The rectangle's height.
	 * @param __pixels The array of pixels that should receive the retrieved
	 * data, if not {@code null}.
	 * @param __off The offset for the starting position in the pixel array,
	 * which applies even if {@code __pixels} is {@code null}.
	 * @return An array of pixels. If {@code __pixels} is not {@code null}, it
	 * will also have the same values.
	 * @throws ArrayIndexOutOfBoundsException If {@code __off} is negative; or
	 * {@code __pixels} is not {@code null} and
	 * {@code (__off + __width * __height)} is larger than the remaining length
	 * of the array.
	 * @throws IllegalArgumentException If either {@code __width} or
	 * {@code __height} are less than 0.
	 * @since 2026/06/01
	 */
	@Api
	public int[] getPixels(int __x, int __y, int __width, int __height,
		int[] __pixels, int __off)
		throws ArrayIndexOutOfBoundsException, IllegalArgumentException
	{
		if (__width < 0 || __height < 0)
			throw new IllegalArgumentException("NEGV");

		if (__pixels != null && __off + __width * __height > __pixels.length)
			throw new IllegalArgumentException("IOOB");

		this.__checkDispose();
		
		if (__width == 0 || __height == 0)
			return __pixels == null ?
				new int[] {Graphics.getColorOfName(Graphics.BLACK)} :
				__pixels;

		// We must include the offset on the newly created array, as apps might
		// want to use that space for something else.
		if (__pixels == null)
			__pixels = new int[__off + (__width * __height)];

		ExtraGraphics g = this.__extra();

		// Fill the specified area with BLACK.
		// TODO: The spec does not specify whether this should be done.
		// TODO: But doing this avoids garbage when/if the area is OOB.
		ObjectShelf.arrayFill(__pixels, __off, __width * __height,
			Graphics.getColorOfName(Graphics.BLACK));

		// If reading outside the surface area, shrink the actual read area to
		// fit.
		int validWidth = (__x + __width > g.surfaceWidth() ?
			g.surfaceWidth() - __x : __width);
		int validHeight = (__y + __height > g.surfaceHeight() ?
			g.surfaceHeight() - __y : __height);

		g.getPfRegion(g.getPixelFormat(), __pixels, __off, __width,
			true, __x, __y, validWidth, validHeight, 0);

		return __pixels;
	}

	/**
	 * Returns the pixel color at the specified [x, y] coordinate in the
	 * 0xRRGGBB format. Aside from the color format, this method behaves just
	 * like {@link Graphics#getPixel(int, int)}.
	 *
	 * @param __x The X coordinate of the pixel to retrieve.
	 * @param __y The Y coordinate of the pixel to retrieve.
	 * @return The 0xRRGGBB pixel color at the specified coordinates, or
	 * {@link Graphics#BLACK} if the coordinates are out of bounds.
	 * @since 2026/06/01
	 */
	@Api
	public int getRGBPixel(int __x, int __y)
	{
		ExtraGraphics g = this.__extra();
		if (__x < 0 || __y < 0 || __x >= g.surfaceWidth() ||
			__y >= g.surfaceHeight())
			return Graphics.getColorOfName(Graphics.BLACK);

		this.__checkDispose();

		// TODO: Add ScritchUI getPixel() and use that for ExtraGraphics
		return this.getRGBPixels(__x, __y, 1, 1, null, 0)[0];
	}

	/**
	 * Returns an array of pixel colors starting at the designated [x, y]
	 * coordinate and extending to the [width, height] rectangle, in the
	 * 0xRRGGBB format.
	 *
	 * Aside from the color format, this method behaves exactly like
	 * {@link Graphics#getPixels(int, int, int, int, int[], int)}.
	 *
	 * @param __x The starting X coordinate of the pixels to retrieve.
	 * @param __y The starting Y coordinate of the pixels to retrieve.
	 * @param __width The rectangle's width.
	 * @param __height The rectangle's height.
	 * @param __pixels The array of pixels that should receive the retrieved
	 * data, if not {@code null}.
	 * @param __off The offset for the starting position in the pixel array,
	 * which applies even if {@code __pixels} is {@code null}.
	 * @return An array of pixel colors in 0xRRGGBB format. If {@code __pixels}
	 * is not {@code null}, it will also have the same values.
	 * @throws ArrayIndexOutOfBoundsException If {@code __off} is negative; or
	 * {@code __pixels} is not {@code null} and
	 * {@code (__off + __width * __height)} is larger than the remaining length
	 * of the array.
	 * @throws IllegalArgumentException If either {@code __width} or
	 * {@code __height} are less than 0.
	 * @since 2026/06/01
	 */
	@Api
	public int[] getRGBPixels(int __x, int __y, int __width, int __height,
		int[] __pixels, int __off)
		throws ArrayIndexOutOfBoundsException, IllegalArgumentException
	{
		if (__width < 0 || __height < 0)
			throw new IllegalArgumentException("NEGV");

		if (__pixels != null && __off + __width * __height > __pixels.length)
			throw new IllegalArgumentException("IOOB");

		this.__checkDispose();
		
		if (__width == 0 || __height == 0)
			return __pixels == null ?
				new int[] {Graphics.getColorOfName(Graphics.BLACK)} :
				__pixels;

		// We must include the offset on the newly created array, as apps might
		// want to use that space for something else.
		if (__pixels == null)
			__pixels = new int[__off + (__width * __height)];

		ExtraGraphics g = this.__extra();

		// Fill the specified area with BLACK.
		// TODO: The spec does not specify whether this should be done.
		// TODO: But doing this avoids garbage when/if the area is OOB.
		ObjectShelf.arrayFill(__pixels, __off, __width * __height,
			Graphics.getColorOfName(Graphics.BLACK));

		// If reading outside the surface area, shrink the actual read area to
		// fit.
		int validWidth = (__x + __width > g.surfaceWidth() ?
			g.surfaceWidth() - __x : __width);
		int validHeight = (__y + __height > g.surfaceHeight() ?
			g.surfaceHeight() - __y : __height);

		g.getPfRegion(UIPixelFormat.INT_RGB888, __pixels, __off, __width,
			true, __x, __y, validWidth, validHeight, 0);

		int alphaMask, i;
		if (DoJaRuntime.versionBefore(4, 0))
			alphaMask = 0;
		else
			alphaMask = 0xFF_000000;

		for (i = __off; i < __off + (__width * __height); i++)
			__pixels[i] |= alphaMask;

		return __pixels;
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
		this.__checkDispose();

		LockFlush lockFlush = this._lockFlush;
		if (lockFlush != null)
			lockFlush.lock();
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
		this.__checkDispose();

		this._graphics.setClip(__x, __y, __w, __h);
	}

	/**
	 * Sets the given color to use for drawing primitives and other non-image
	 * data.
	 *
	 * @param __c The color to use.
	 * @throws IllegalArgumentException If the color is not valid for this
	 * device.
	 * @since 2022/10/07
	 */
	@Api
	public void setColor(int __c)
		throws IllegalArgumentException
	{
		this.__checkDispose();

		// Before 4.0, alpha is completely excluded from the color
		if (DoJaRuntime.versionBefore(4, 0))
			this._graphics.setAlphaColor(__c | 0xFF_000000);
		else
			this._graphics.setAlphaColor(__c);
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
		/* {@squirreljme.error AH1f Invalid flip mode. (The mode)} */
		if (__mode != Graphics.FLIP_HORIZONTAL &&
			__mode != Graphics.FLIP_NONE &&
			__mode != Graphics.FLIP_ROTATE &&
			__mode != Graphics.FLIP_ROTATE_LEFT &&
			__mode != Graphics.FLIP_ROTATE_RIGHT &&
			__mode != Graphics.FLIP_ROTATE_RIGHT_HORIZONTAL &&
			__mode != Graphics.FLIP_ROTATE_RIGHT_VERTICAL &&
			__mode != Graphics.FLIP_VERTICAL)
			throw new IllegalArgumentException("AH1f " + __mode);

		this.__checkDispose();

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

		this.__checkDispose();

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
		this.__checkDispose();

		javax.microedition.lcdui.Graphics graphics = this._graphics;
		graphics.translate(__x - graphics.getTranslateX(),
			__y - graphics.getTranslateY());
	}

	/**
	 * Sets whether emoji colors are determined by {@link #setColor(int)} or
	 * by the system.
	 *
	 * @param __enabled If emoji color is determined by {@link #setColor(int)}.
	 * @since 2025/06/15
	 */
	@Api
	public void setPictoColorEnabled(boolean __enabled)
	{
		this.__checkDispose();

		this._emojiColor = __enabled;

		// TODO: Support default emoji color
		Debugging.todoNote("Support default emoji color.");
	}

	/**
	 * Sets the pixel at the given coordinates with the current color.
	 *
	 * @param __x The X coordinate.
	 * @param __y The Y coordinate.
	 * @since 2025/06/15
	 */
	@Api
	public void setPixel(int __x, int __y)
	{
		this.__checkDispose();

		javax.microedition.lcdui.Graphics graphics = this._graphics;
		graphics.drawLine(__x, __y, __x + 1, __y);
	}

	/**
	 * Sets the pixel at the given coordinates with the given color.
	 *
	 * @param __x The X coordinate.
	 * @param __y The Y coordinate.
	 * @param __rgb The specified color, which may be one returned by
	 * {@link #getColorOfRGB(int, int, int, int)} to set.
	 * @since 2025/06/15
	 */
	@Api
	public void setPixel(int __x, int __y, int __rgb)
	{
		this.__checkDispose();

		javax.microedition.lcdui.Graphics graphics = this._graphics;
		int oldColor = graphics.getAlphaColor();
		try
		{
			// There is no alpha before DoJa 4
			if (DoJaRuntime.versionBefore(4, 0))
				graphics.setAlphaColor(__rgb | 0xFF_000000);
			else
				graphics.setAlphaColor(__rgb);

			graphics.drawLine(__x, __y, __x + 1, __y);
		}
		finally
		{
			graphics.setAlphaColor(oldColor);
		}
	}

	/**
	 * Draws the given buffer as (A)RGB data.
	 *
	 * @param __x The destination X coordinate.
	 * @param __y The destination Y coordinate.
	 * @param __w The buffer width.
	 * @param __h The buffer height.
	 * @param __buf The buffer pixel data.
	 * @param __off The offset into the buffer.
	 * @throws ArrayIndexOutOfBoundsException If the offset and/or calculated
	 * length is outside the buffer bounds.
	 * @throws IllegalArgumentException If the width and/or height are zero
	 * or negative.
	 * @throws NullPointerException On {@code null} arguments.
	 * @since 2025/06/15
	 */
	@Api
	public void setPixels(int __x, int __y, int __w, int __h, int[] __buf,
		int __off)
		throws ArrayIndexOutOfBoundsException, IllegalArgumentException,
			NullPointerException
	{
		if (__buf == null)
			throw new NullPointerException("NARG");
			
		if (__w <= 0 || __h <= 0)
			throw new IllegalArgumentException("NEGV");

		int len = __w * __h;
		if (__off < 0 || (__off + len) > __buf.length || (__off + len) < 0)
			throw new ArrayIndexOutOfBoundsException("IOOB");

		this.__checkDispose();

		// Draw the RGB data, alpha is only considered valid when at least
		// DoJa 4 since this has the same behavior as getColorOfRGB()
		javax.microedition.lcdui.Graphics graphics = this._graphics;
		graphics.drawRGB(__buf, __off, len, __x, __y, __w, __h,
			DoJaRuntime.versionLeast(4, 0));
	}

	/**
	 * Draws the given buffer as 0xRRGGBB data, with the most significant byte
	 * ignored. Aside from pixel format, this method behaves like
	 * {@link Graphics#setPixel(int, int, int)}.
	 *
	 * @param __x The X coordinate.
	 * @param __y The Y coordinate.
	 * @param __rgb The specified color, which may be one returned by
	 * {@link #getColorOfRGB(int, int, int)} to set.
	 * @since 2025/06/15
	 */
	@Api
	public void setRGBPixel(int __x, int __y, int __rgb)
	{
		this.__checkDispose();

		int alphaMask;
		if (DoJaRuntime.versionBefore(4, 0))
			alphaMask = 0;
		else
			alphaMask = 0xFF_000000;

		this.setPixel(__x, __y, __rgb | alphaMask);
	}

	/**
	 * Draws the given buffer as 0xRRGGBB data, with the most significant byte
	 * ignored. Aside from pixel format, this method behaves just like
	 * {@link Graphics#setPixels(int, int, int, int, int[], int)}.
	 *
	 * @param __x The destination X coordinate.
	 * @param __y The destination Y coordinate.
	 * @param __w The buffer width.
	 * @param __h The buffer height.
	 * @param __buf The buffer pixel data in 0xRRGGBB format.
	 * @param __off The offset into the buffer.
	 * @throws ArrayIndexOutOfBoundsException If the offset and/or calculated
	 * length is outside the buffer bounds.
	 * @throws IllegalArgumentException If the width and/or height are zero
	 * or negative.
	 * @throws NullPointerException On {@code null} arguments.
	 * @since 2025/06/15
	 */
	@Api
	public void setRGBPixels(int __x, int __y, int __w, int __h, int[] __buf,
		int __off)
		throws ArrayIndexOutOfBoundsException, IllegalArgumentException,
			NullPointerException
	{
		if (__buf == null)
			throw new NullPointerException("NARG");
			
		if (__w <= 0 || __h <= 0)
			throw new IllegalArgumentException("NEGV");

		int len = __w * __h;
		if (__off < 0 || (__off + len) > __buf.length || (__off + len) < 0)
			throw new ArrayIndexOutOfBoundsException("IOOB");
			
		this.__checkDispose();

		this.setPixels(__x, __y, __w, __h, __buf, __off);
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
		this.__checkDispose();

		LockFlush lockFlush = this._lockFlush;
		if (lockFlush != null)
			lockFlush.unlock(__forced);
	}

	/**
	 * Checks whether the current graphics is disposed, note that this fails
	 * only on DoJa 2 and up.
	 *
	 * @throws UIException If this has been disposed.
	 * @since 2025/06/15
	 */
	private void __checkDispose()
		throws UIException
	{
		if (DoJaRuntime.versionLeast(2, 0))
			if (this._disposed)
				throw new UIException(UIException.ILLEGAL_STATE);
	}

	/**
	 * Returns the graphics instance as an {@link ExtraGraphics}.
	 *
	 * @return The {@link ExtraGraphics}.
	 * @throws UIException If this is not an {@link ExtraGraphics}.
	 * @since 2025/12/21
	 */
	ExtraGraphics __extra()
		throws UIException
	{
		/* {@squirreljme.error AH91 Graphics is not capable of extra
		functions.} */
		javax.microedition.lcdui.Graphics g = this._graphics;
		if (!(g instanceof ExtraGraphics))
			throw new UIException(UIException.ILLEGAL_STATE,
				"AH91");

		// Cast
		return (ExtraGraphics)g;
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
				return javax.microedition.lcdui.game.Sprite.TRANS_MIRROR;

			case Graphics.FLIP_ROTATE:
				return javax.microedition.lcdui.game.Sprite.TRANS_ROT180;

			case Graphics.FLIP_ROTATE_LEFT:
				return javax.microedition.lcdui.game.Sprite.TRANS_ROT270;

			case Graphics.FLIP_ROTATE_RIGHT:
				return javax.microedition.lcdui.game.Sprite.TRANS_ROT90;

			case Graphics.FLIP_ROTATE_RIGHT_HORIZONTAL:
				return javax.microedition.lcdui.game.Sprite.TRANS_MIRROR_ROT270;

			case Graphics.FLIP_ROTATE_RIGHT_VERTICAL:
				return javax.microedition.lcdui.game.Sprite.TRANS_MIRROR_ROT90;

			case Graphics.FLIP_VERTICAL:
				return javax.microedition.lcdui.game.Sprite.TRANS_MIRROR_ROT180;

			case Graphics.FLIP_NONE:
			default:
				return javax.microedition.lcdui.game.Sprite.TRANS_NONE;
		}
	}

	/**
	 * Retrieves an RGB integer representing the color requested by the given
	 * {@code __name} argument.
	 *
	 * @param __name The color name to retrieve the RGB equivalent of.
	 * @throws IllegalArgumentException If the {@code __name} argument does not
	 * match a valid color name in this class.
	 * @since 2025/12/21
	 */
	@SuppressWarnings("MagicNumber")
	@Api
	public static int getColorOfName(int __name)
		throws IllegalArgumentException
	{
		// Before 4.0, negative values are never returned
		int alphaMask;
		if (DoJaRuntime.versionBefore(4, 0))
			alphaMask = 0;
		else
			alphaMask = 0xFF_000000;

		// Depends on the color name
		switch (__name)
		{
			case Graphics.AQUA:
				return 0x00FFFF | alphaMask;
			case Graphics.BLACK:
				return 0x000000 | alphaMask;
			case Graphics.BLUE:
				return 0x0000FF | alphaMask;
			case Graphics.FUCHSIA:
				return 0xFF00FF | alphaMask;
			case Graphics.GRAY:
				return 0x808080 | alphaMask;
			case Graphics.GREEN:
				return 0x008000 | alphaMask;
			case Graphics.LIME:
				return 0x00FF00 | alphaMask;
			case Graphics.MAROON:
				return 0x800000 | alphaMask;
			case Graphics.NAVY:
				return 0x000080 | alphaMask;
			case Graphics.OLIVE:
				return 0x808000 | alphaMask;
			case Graphics.PURPLE:
				return 0x800080 | alphaMask;
			case Graphics.RED:
				return 0xFF0000 | alphaMask;
			case Graphics.SILVER:
				return 0xC0C0C0 | alphaMask;
			case Graphics.TEAL:
				return 0x008080 | alphaMask;
			case Graphics.WHITE:
				return 0xFFFFFF | alphaMask;
			case Graphics.YELLOW:
				return 0xFFFF00 | alphaMask;
		}

		// {@squirreljme.error AH0r Invalid color. (The color)}
		throw new IllegalArgumentException("AH0r " + __name);
	}

	/**
	 * Returns the color code for the given RGB color.
	 *
	 * @param __r The red color.
	 * @param __g The green color.
	 * @param __b The blue color.
	 * @return The color code.
	 * @throws IllegalArgumentException If the values are out of range.
	 * @since 2022/10/07
	 */
	@Api
	public static int getColorOfRGB(int __r, int __g, int __b)
	{
		// Before 4.0, negative values are never returned
		if (DoJaRuntime.versionBefore(4, 0))
			return Graphics.getColorOfRGB(__r, __g, __b, 0);
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
		// Mutable image or base DoJa image
		if ((__i instanceof __MutableImage__) ||
			(__i instanceof __DoJaImage__))
		{
			javax.microedition.lcdui.Image midpImage = __i._midpImage;

			// Disposed?
			if (midpImage == null)
				throw new UIException(UIException.ILLEGAL_STATE);

			return midpImage;
		}

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
			Debugging.todoNote("Unsupported image %s",
				__i.getClass());

			throw new UIException(UIException.UNSUPPORTED_FORMAT);
		}
	}
}
