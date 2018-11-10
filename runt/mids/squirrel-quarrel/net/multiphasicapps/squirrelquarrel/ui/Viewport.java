// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirrelquarrel.ui;

import net.multiphasicapps.squirrelquarrel.world.MegaTile;
import net.multiphasicapps.squirrelquarrel.world.Tile;

/**
 * This represents a viewport into the level, it does not contain any
 * rendering information.
 *
 * @since 2018/03/18
 */
public final class Viewport
{
	/** The level width in pixels. */
	protected final int levelwidth;
	
	/** The level height in pixels. */
	protected final int levelheight;
	
	/** The viewport X position. */
	private volatile int _viewx;
	
	/** The viewport Y position. */
	private volatile int _viewy;
	
	/** The view width. */
	private volatile int _vieww;
	
	/** The view height. */
	private volatile int _viewh;
	
	/** The starting X megatile. */
	private volatile int _msx;
	
	/** The starting Y megatile. */
	private volatile int _msy;
	
	/** The ending X megatile. */
	private volatile int _mex;
	
	/** The ending Y megatile. */
	private volatile int _mey;
	
	/**
	 * Initializes the viewport information.
	 *
	 * @param __lw The level width.
	 * @param __lh The level height.
	 * @throws IllegalArgumentException If the width and/or height are zero or
	 * negative.
	 * @since 2018/03/18
	 */
	public Viewport(int __lw, int __lh)
		throws IllegalArgumentException
	{
		// {@squirreljme.error BE07 The viewport cannot have a negative size.}
		if (__lw <= 0 || __lh <= 0)
			throw new IllegalArgumentException("BE07");
		
		this.levelwidth = __lw;
		this.levelheight = __lh;
		
		// Use a default size for the viewport
		this.setSize(1, 1);
	}
	
	/**
	 * Converts a map X coordinate to a screen X coordinate.
	 *
	 * @param __x The input X coordinate.
	 * @return The output X coordinate.
	 * @since 2017/02/12
	 */
	public int mapToScreenX(int __x)
	{
		return __x - this._viewx;
	}
	
	/**
	 * Converts a map Y coordinate to a screen Y coordinate.
	 *
	 * @param __x The input Y coordinate.
	 * @return The output y coordinate.
	 * @since 2017/02/12
	 */
	public int mapToScreenY(int __y)
	{
		return __y - this._viewy;
	}
	
	/**
	 * Sets the viewport position.
	 *
	 * @param __x The absolute X position.
	 * @param __y The absolute Y position.
	 * @since 2017/02/10
	 */
	public void setViewport(int __x, int __y)
	{
		// Translate the viewport
		int viewx = __x,
			viewy = __y,
			vieww = this._vieww,
			viewh = this._viewh,
			levelpxw = this.levelwidth,
			levelpxh = this.levelheight;
		
		// Cap right side
		if (viewx + vieww >= levelpxw - 1)
			viewx = (levelpxw - vieww) - 1;
		
		// Cop bottom side
		if (viewy + viewh >= levelpxh - 1)
			viewy = (levelpxh - viewh) - 1;
		
		// Cap left side
		if (viewx < 0)
			viewx = 0;
		
		// Cap top
		if (viewy < 0)
			viewy = 0;
		
		// Set new viewport
		this._viewx = viewx;
		this._viewy = viewy;
		
		// Recalculate tiles in view
		int msx = this.screenToMapX(0) / MegaTile.PIXEL_SIZE,
			msy = this.screenToMapY(0) / MegaTile.PIXEL_SIZE,
			mex = (this.screenToMapX(vieww) / MegaTile.PIXEL_SIZE) + 1,
			mey = (this.screenToMapY(viewh) / MegaTile.PIXEL_SIZE) + 1;
		
		// Cap
		mex = Math.min(mex, levelpxw / MegaTile.PIXEL_SIZE);
		mey = Math.min(mey, levelpxh / MegaTile.PIXEL_SIZE);
		
		// Set
		this._msx = msx;
		this._msy = msy;
		this._mex = mex;
		this._mey = mey;
	}
	
	/**
	 * Converts a screen X coordinate to a map X coordinate.
	 *
	 * @param __x The input X coordinate.
	 * @return The output X coordinate.
	 * @since 2017/02/12
	 */
	public int screenToMapX(int __x)
	{
		return this._viewx + __x;
	}
	
	/**
	 * Converts a screen Y coordinate to a map Y coordinate.
	 *
	 * @param __x The input Y coordinate.
	 * @return The output y coordinate.
	 * @since 2017/02/12
	 */
	public int screenToMapY(int __y)
	{
		return this._viewy + __y;
	}
	
	/**
	 * Sets the size of the viewport
	 *
	 * @param __w The new width.
	 * @param __h The new height.
	 * @throws IllegalArgumentException If the width and/or height is zero
	 * or negative.
	 * @since 2017/02/10
	 */
	public void setSize(int __w, int __h)
		throws IllegalArgumentException
	{
		// {@squirreljme.error BE08 Cannot set the viewport size to be zero
		// or negative.}
		if (__w <= 0 || __h <= 0)
			throw new IllegalArgumentException("BE08");
		
		// Correct the viewport
		this._vieww = __w;
		this._viewh = __h;
		this.translateViewport(0, 0);
	}
	
	/**
	 * Translates the viewport.
	 *
	 * @param __x The relative X translation.
	 * @param __y The relative Y translation.
	 * @since 2017/02/10
	 */
	public void translateViewport(int __x, int __y)
	{
		this.setViewport(this._viewx + __x, this._viewy + __y);
	}
	
	/**
	 * Returns the height of the viewport.
	 *
	 * @return The viewport height.
	 * @since 2017/02/12
	 */
	public int viewportHeight()
	{
		return this._viewh;
	}
	
	/**
	 * Returns the width of the viewport.
	 *
	 * @return The viewport width.
	 * @since 2017/02/12
	 */
	public int viewportWidth()
	{
		return this._vieww;
	}
	
	/**
	 * Returns the X position of the viewport.
	 *
	 * @return The viewport X position.
	 * @since 2017/02/12
	 */
	public int viewportX()
	{
		return this._viewx;
	}
	
	/**
	 * Returns the Y position of the viewport.
	 *
	 * @return The viewport Y position.
	 * @since 2017/02/12
	 */
	public int viewportY()
	{
		return this._viewy;
	}
}

