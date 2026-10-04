// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.nttdocomo.ui;

import cc.squirreljme.runtime.cldc.annotation.Api;
import org.intellij.lang.annotations.MagicConstant;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.Range;

/**
 * DoJa Sprite class, used in SpriteSets or for direct drawing. Upon creation,
 * sprites always default to being visible.
 *
 * @since 2026/10/01
 */
@Api
public class Sprite
{
	/** This sprite's flip mode. */
	private int _flipMode;

	/** The bottom corner of this sprite's draw area. */
	private int _height;

	/** This sprite's underlying image data. */
	private Image _image;

	/** The left corner of this sprite's draw area. */
	private int _imageX;

	/** The top corner of this sprite's draw area. */
	private int _imageY;

	/** This sprite's rotation matrix. */
	private int[] _rotationMatrix;

	/** This sprite's visibility status. */
	private boolean _visible;

	/** The right corner of this sprite's draw area. */
	private int _width;

	/** This sprite's display location on the X axis. */
	private int _x;

	/** This sprite's display location on the Y axis. */
	private int _y;

	/**
	 * This constructor is only accessible via class extension and using it in
	 * such a way is heavily discouraged.
	 *
	 * @since 2026/10/01
	 */
	@Api
	protected Sprite()
	{
	}

	/**
	 * Creates a new Sprite with the specified image data. The sprite's x and y
	 * coordinates will be set so that it is visible at {@code [0, 0]}.
	 *
	 * @param __image The image to use for this sprite.
	 * @throws NullPointerException If {@code __image} is {@code null}.
	 * @throws UIException If the received image has already been disposed of.
	 * @since 2026/10/01
	 */
	@Api
	public Sprite(@NotNull Image __image)
		throws NullPointerException, UIException
	{
		if (__image == null)
			throw new NullPointerException("NARG");

		this.setImage(__image);
		this._visible = true;
	}

	/**
	 * Creates a new Sprite with the specified image data, as well as the
	 * rectangular region of the sprite that should be drawn.
	 *
	 * @param __image The image to use for this sprite.
	 * @param __x The left corner of the sprite's rectangle to draw.
	 * @param __y The top corner of the sprite's rectangle to draw.
	 * @param __width The right corner of the sprite's rectangle to draw.
	 * @param __height The bottom corner of the sprite's rectangle to draw.
	 * @throws IllegalArgumentException If {@code __width} or {@code __height}
	 * are invalid; or the resulting region of the received
	 * {@code [__x, __y, __width, __height]} arguments goes beyond the image's
	 * boundaries.
	 * @throws NullPointerException If {@code __image} is {@code null}.
	 * @throws UIException If the received image has already been disposed of.
	 * @since 2026/10/01
	 */
	@Api
	public Sprite(@NotNull Image __image, int __x, int __y,
		@Range(from = 0, to = Integer.MAX_VALUE) int __width,
		@Range(from = 0, to = Integer.MAX_VALUE) int __height)
		throws IllegalArgumentException, NullPointerException, UIException
	{
		if (__image == null)
			throw new NullPointerException("NARG");

		/* {@squirreljme.error AH2s Invalid Sprite draw region.} */
		if (__width < 0 || __height < 0 || __x < 0 || __y < 0 ||
			__width > __image.getWidth() || __height > __image.getHeight() ||
			(__x + __width) > __image.getWidth() ||
			(__y + __height) > __image.getHeight())
			throw new IllegalArgumentException("AH2s");

		this.setImage(__image, __x, __y, __width, __height);
		this._visible = true;
	}

	/**
	 * Retrieves this Sprite's height, which may be either its image height or
	 * an explicitly specified rectangle height.
	 *
	 * @return The sprite's defined height.
	 * @since 2026/10/01
	 */
	@Api
	@Range(from = 0, to = Integer.MAX_VALUE)
	public int getHeight()
	{
		return this._height;
	}

	/**
	 * Retrieves this Sprite's width, which may be either its image width or
	 * an explicitly specified rectangle width.
	 *
	 * @return The sprite's defined width.
	 * @since 2026/10/01
	 */
	@Api
	@Range(from = 0, to = Integer.MAX_VALUE)
	public int getWidth()
	{
		return this._width;
	}

	/**
	 * Retrieves this Sprite's X display coordinate.
	 *
	 * @return The sprite's display coordinate on the X axis.
	 * @since 2026/10/01
	 */
	@Api
	public int getX()
	{
		return this._x;
	}

	/**
	 * Retrieves this Sprite's Y display coordinate.
	 *
	 * @return The sprite's display coordinate on the Y axis.
	 * @since 2026/10/01
	 */
	@Api
	public int getY()
	{
		return this._y;
	}

	/**
	 * Returns whether this sprite is currently visible.
	 *
	 * @return True if this sprite is visible.
	 * @since 2026/10/01
	 */
	@Api
	public boolean isVisible()
	{
		return this._visible;
	}

	/**
	 * Sets a new image flip mode for this sprite. Must be one of the flip
	 * modes defined in {@link Graphics}.
	 *
	 * @param __flipMode This sprite's new flip mode.
	 * @since 2026/10/01
	 */
	@Api
	public void setFlipMode(
		@MagicConstant(valuesFromClass = Graphics.class) int __flipMode)
	{
		/* {@squirreljme.error AH2u Invalid sprite flip mode.} */
		if (__flipMode < Graphics.FLIP_NONE ||
			__flipMode >Graphics.FLIP_ROTATE_RIGHT_VERTICAL)
			throw new IllegalArgumentException("AH2u");

		this._flipMode = __flipMode;
	}

	/**
	 * Sets a new image to be used for this Sprite.
	 *
	 * @param __image The image to use for this sprite.
	 * @throws NullPointerException If {@code __image} is {@code null}.
	 * @throws UIException If the received image has already been disposed of.
	 * @since 2026/10/01
	 */
	@Api
	public void setImage(@NotNull Image __image)
	{
		if (__image == null)
			throw new NullPointerException("NARG");

		this._image = __image;
		this._x = this._imageX = 0;
		this._y = this._imageY = 0;
		this._width = __image.getWidth();
		this._height = __image.getHeight();
	}

	/**
	 * Sets a new image to be used for this Sprite, as well as the rectangular
	 * region of the sprite that should be drawn.
	 *
	 * @param __image The image to use for this sprite.
	 * @param __x The left corner of the sprite's rectangle to draw.
	 * @param __y The top corner of the sprite's rectangle to draw.
	 * @param __width The right corner of the sprite's rectangle to draw.
	 * @param __height The bottom corner of the sprite's rectangle to draw.
	 * @throws IllegalArgumentException If {@code __width} or {@code __height}
	 * are invalid, or the resulting region of the specified
	 * {@code [__x, __y, __width, __height]} arguments goes beyond the image's
	 * boundaries.
	 * @throws NullPointerException If {@code __image} is {@code null}.
	 * @throws UIException If the received image has already been disposed of.
	 * @since 2026/10/01
	 */
	@Api
	public void setImage(@NotNull Image __image, int __x, int __y,
		@Range(from = 0, to = Integer.MAX_VALUE) int __width,
		@Range(from = 0, to = Integer.MAX_VALUE) int __height)
		throws IllegalArgumentException, NullPointerException, UIException
	{
		if (__image == null)
			throw new NullPointerException("NARG");

		/* {@squirreljme.error AH2s Invalid Sprite draw region.} */
		if (__width < 0 || __height < 0 || __x < 0 || __y < 0 ||
			__width > __image.getWidth() || __height > __image.getHeight() ||
			(__x + __width) > __image.getWidth() ||
			(__y + __height) > __image.getHeight())
			throw new IllegalArgumentException("AH2s");

		this._image = __image;
		this._x = 0;
		this._y = 0;
		this._imageX = __x;
		this._imageY = __y;
		this._width = __width;
		this._height = __height;
	}

	/**
	 * Sets this sprite's screen drawing location. Pivots are not specified,
	 * thus it is assumed this location will reflect the sprite's center, as
	 * the center is what's used when transforming this sprite for drawing with
	 * a transform matrix set by {@link Sprite#setRotation(int[])}.
	 *
	 * @param __x The sprite's new display coordinate on the X axis.
	 * @param __y The sprite's new display coordinate on the Y axis.
	 * @since 2026/10/01
	 */
	@Api
	public void setLocation(int __x, int __y)
	{
		this._x = __x;
		this._y = __y;
	}

	/**
	 * Sets a 2D linear transform matrix for use when drawing this sprite.
	 * After setting a rotation matrix, this sprite will have said matrix
	 * applied to its center before being drawn at the specified (X, Y)
	 * location, and due to the nature of using a rotation matrix, flip modes
	 * are disabled. Passing a null argument here will disable the use of
	 * rotation matrices, allowing for the usage of flip modes.
	 *
	 * When drawing a rectangular area whose bounds defined by
	 * {@code [sx, sy, swidth, sheight]}, a pixel located at a given
	 * {@code [x, y]} point will be manipulated by this matrix such that
	 * the resulting {@code [x', y']} drawing coordinates is given by the
	 * following operation:
	 *
	 * {@code [ x']      1   [ m00 m01   0  ] [ x - (sx + swidth  / 2) ]
	 * [ dx + swidth  / 2 ]}
	 *
	 * {@code [ y'] = ------ [ m10 m11   0  ]
	 * [ y - (sy + sheight / 2) ] + [ dy + sheight / 2 ] }
	 *
	 * {@code [ 1 ]    4096  [  0   0  4096 ] [ 1                      ]
	 * [ 0                ]}
	 *
	 * Note that {@code [dx, dy]} are the location currently set by
	 * {@link Sprite#setLocation(int, int)}.
	 *
	 * @param __lt The rotation matrix to use, containing values for m00, m01,
	 * m10, and m11. Any other elements after those are ignored.
	 * @throws ArrayIndexOutOfBoundsException If the rotation matrix has less
	 * than 4 elements.
	 * @since 2026/10/01
	 */
	@Api
	public void setRotation(@Nullable int[] __lt)
		throws ArrayIndexOutOfBoundsException
	{
		if (__lt == null)
		{
			this._rotationMatrix = __lt;
			return;
		}

		/* {@squirreljme.error AH2t Matrix must have at least 4 elements.} */
		if (__lt.length < 4)
			throw new ArrayIndexOutOfBoundsException("AH2t");

		this._rotationMatrix = new int[4];
		System.arraycopy(__lt, 0, this._rotationMatrix, 0, 4);
	}

	/**
	 * Sets this sprite's visibility state. If it's not visible, it will be
	 * skipped over for drawing and collision testing.
	 *
	 * @param __visible The sprite's new visibility state
	 * @since 2026/10/01
	 */
	@Api
	public void setVisible(boolean __visible)
	{
		this._visible = __visible;
	}

	/**
	 * Retrieves this Sprite's flip mode.
	 *
	 * @return This sprite's flip mode.
	 * @since 2026/10/01
	 */
	@MagicConstant(valuesFromClass = Graphics.class)
	int __getFlipMode()
	{
		return this._flipMode;
	}

	/**
	 * Retrieves this Sprite's image data for drawing.
	 *
	 * @return This sprite's underlying image data.
	 * @since 2026/10/01
	 */
	@NotNull
	Image __getImage()
	{
		return this._image;
	}

	/**
	 * Retrieves this Sprite's starting image pixel on the X axis.
	 *
	 * @return This sprite image's left bounds.
	 * @since 2026/10/01
	 */
	int __getImageX()
	{
		return this._imageX;
	}

	/**
	 * Retrieves this Sprite's starting image pixel on the Y axis.
	 *
	 * @return This sprite image's top bounds.
	 * @since 2026/10/01
	 */
	int __getImageY()
	{
		return this._imageY;
	}

	/**
	 * Retrieves this Sprite's rotation matrix. May be null indicating that it
	 * should not be used for drawing.
	 *
	 * @return This sprite's rotation matrix, if any.
	 * @since 2026/10/01
	 */
	@Nullable
	int[] __getRotationMatrix()
	{
		return this._rotationMatrix;
	}
}
