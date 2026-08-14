// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui.game;

import cc.squirreljme.runtime.cldc.annotation.Api;
import javax.microedition.lcdui.Graphics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

/**
 * Represents a Layer, which by itself is little more than an empty
 * rectangle's size, translation, and visibility flag. Other classes like
 * {@link Sprite} extend from this in order to provide actual image data for
 * drawing as well as additional functionality.
 *
 * The base coordinate translation of this layer is stored internally and
 * represents its offset in regards to the {@link Graphics} context's
 * translation when it is to be drawn.
 *
 * Implementors may override setter methods, but getters are always
 * {@code final} and return values stored internally in this class.
 *
 * @since 2026/08/09
 */
@Api
public abstract class Layer
{
	/** This layer's X translation. */
	int _x;

	/** This layer's Y translation. */
	int _y;

	/** This layer's width. */
	int _width;

	/** This layer's height. */
	int _height;

	/** This layer's visibility state. */
	boolean _visible;

	/**
	 * Initializes a {@link Layer} instance with {@code [x, y]} as
	 * {@code [0, 0]}.
	 *
	 * Despite MIDP documentation not outright stating it, the {@link Layer}'s
	 * visibility defaults to {@code true}.
	 *
	 * @since 2026/08/09
	 */
	@Api
	public Layer()
	{
		this._x = 0;
		this._y = 0;
		this._visible = true;
	}

	/**
	 * Paints this {@link Layer} instance to the given {@link Graphics} context.
	 *
	 * Implementors must check if the passed graphics context is in a valid
	 * state prior to drawing, and the context must be restored to its state
	 * prior to the paint operation should any changes be done when painting,
	 * such as clipping or translation changes.
	 *
	 * This method also only paints this {@link Layer} if its visibility flag is
	 * set to {@code true}.
	 *
	 * @param __g The {@link Graphics} object to draw with.
	 * @throws NullPointerException if {@code __g} is {@code null}.
	 * @since 2026/08/09
	 */
	@Api
	public abstract void paint(@NotNull Graphics __g)
		throws NullPointerException;

	/**
	 * Returns the height of this {@link Layer} instance.
	 *
	 * @return This {@link Layer}'s height.
	 * @since 2026/08/09
	 */
	@Api
	@Range(from = 1, to = Integer.MAX_VALUE)
	public final int getHeight()
	{
		return this._height;
	}

	/**
	 * Returns the width of this {@link Layer} instance.
	 *
	 * @return This {@link Layer}'s width.
	 * @since 2026/08/09
	 */
	@Api
	@Range(from = 1, to = Integer.MAX_VALUE)
	public final int getWidth()
	{
		return this._width;
	}

	/**
	 * Returns the current X translation of this {@link Layer} instance.
	 *
	 * @return This {@link Layer}'s X translation.
	 * @since 2026/08/09
	 */
	@Api
	public final int getX()
	{
		return this._x;
	}

	/**
	 * Returns the current Y translation of this {@link Layer} instance.
	 *
	 * @return This {@link Layer}'s Y translation.
	 * @since 2026/08/09
	 */
	@Api
	public final int getY()
	{
		return this._y;
	}

	/**
	 * Returns whether this {@link Layer} is visible and should be drawn.
	 *
	 * @return Whether this {@link Layer} is visible.
	 * @since 2026/08/09
	 */
	@Api
	public final boolean isVisible()
	{
		return this._visible;
	}

	/**
	 * Translates this {@link Layer}'s base coordinates by the specified
	 * {@code [__dx, __dy]} offset.
	 *
	 * @param __dx The additional X translation to be applied.
	 * @param __dy The additional Y translation to be applied.
	 * @since 2026/08/09
	 */
	@Api
	public void move(int __dx, int __dy)
	{
		this._x += __dx;
		this._y += __dy;
	}

	/**
	 * Sets the {@code [__x, __y]} translation of this {@link Layer}.
	 *
	 * Note that as opposed to {@link Layer#move(int, int)}, this operation
	 * sets this {@link Layer}'s X and Y positions to be identical to the
	 * values received as arguments.
	 *
	 * @param __x The X translation to be set.
	 * @param __y The Y translation to be set.
	 * @since 2026/08/09
	 */
	@Api
	public void setPosition(int __x, int __y)
	{
		this._x = __x;
		this._y = __y;
	}

	/**
	 * Sets this {@link Layer}'s visibility state, indicating whether it should
	 * be considered for {@link Layer#paint(Graphics)} drawing operations.
	 *
	 * @param __visible Argument indicating the visibility state to be set.
	 * @since 2026/08/09
	 */
	@Api
	public void setVisible(boolean __visible)
	{
		this._visible = __visible;
	}

	/**
	 * Sets the height value for this {@link Layer}. This is useful for
	 * {@link TiledLayer} tilesets, or for updating sprite data in
	 * {@link Sprite}.
	 *
	 * @param __height The new height value to be set.
	 * @throws IllegalArgumentException If {@code __height} is {@code null}.
	 * @since 2026/08/09
	 */
	void __setHeight(@Range(from = 1, to = Integer.MAX_VALUE) int __height)
		throws IllegalArgumentException
	{
		if (__height < 1)
			throw new IllegalArgumentException("INVL");

		this._height = __height;
	}

	/**
	 * Sets the width value for this {@link Layer}. This is useful for
	 * {@link TiledLayer} tilesets, or for updating sprite data in
	 * {@link Sprite}.
	 *
	 * @param __width The new width value to be set.
	 * @throws IllegalArgumentException If {@code __width} is {@code null}.
	 * @since 2026/08/09
	 */
	void __setWidth(@Range(from = 1, to = Integer.MAX_VALUE) int __width)
		throws IllegalArgumentException
	{
		if (__width < 1)
			throw new IllegalArgumentException("INVL");

		this._width = __width;
	}
}
