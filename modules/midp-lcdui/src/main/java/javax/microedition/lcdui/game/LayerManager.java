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
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

/**
 * Manages a list of Layer objects, as well as their view window for clipping
 * layers outside of the viewable area.
 *
 * @since 2026/08/09
 */
@Api
public class LayerManager
{
	/** List of Layers currently in use. */
	private final List<Layer> _layerList;

	/** The view window's X translation. */
	private int _viewWindowX;

	/** The view window's Y translation. */
	private int _viewWindowY;

	/** The view window's width. */
	private int _viewWindowWidth;

	/** The view window's height. */
	private int _viewWindowHeight;

	/**
	 * Initializes a LayerManager instance.
	 *
	 * @since 2026/08/09
	 */
	@Api
	public LayerManager()
	{
		this._layerList = new ArrayList<Layer>();
		this.setViewWindow(0, 0, Integer.MAX_VALUE, Integer.MAX_VALUE);
	}

	/**
	 * Appends a layer at the end of this {@link LayerManager}.
	 *
	 * Effectively the same as calling {@link LayerManager#insert(Layer, int)}
	 * with the index set as {@link LayerManager#getSize()}.
	 *
	 * @param __layer A layer to be added to this {@link LayerManager}.
	 * @throws NullPointerException If {@code __layer} is {@code null}.
	 * @since 2026/08/09
	 */
	@Api
	public void append(@NotNull Layer __layer)
		throws NullPointerException
	{
		this.insert(__layer, this._layerList.size());
	}

	/**
	 * Gets a layer at the specified index of this {@link LayerManager}.
	 *
	 * @return The layer at the specified index.
	 * @throws IndexOutOfBoundsException if the specified index is invalid.
	 * @since 2026/08/09
	 */
	@Api
	public Layer getLayerAt(
		@Range(from = 0, to = Integer.MAX_VALUE) int __index)
		throws IndexOutOfBoundsException
	{
		synchronized (this)
		{
			if ((__index < 0) || __index >= this._layerList.size())
				throw new IndexOutOfBoundsException("IOOB");

			return this._layerList.get(__index);
		}
	}

	/**
	 * Gets the size of this {@link LayerManager}'s, indicating how many layers
	 * are currently set.
	 *
	 * @return The amount of layers in this {@link LayerManager}.
	 * @since 2026/08/09
	 */
	@Api
	public int getSize()
	{
		return this._layerList.size();
	}

	/**
	 * Inserts a layer at the given index of this {@link LayerManager}'s
	 * {@code _layerList}.
	 *
	 * If the layer already exists, it is first removed from the list, and
	 * then added to the specified index.
	 *
	 * @param __layer A Layer to be added to this {@link LayerManager}.
	 * @param __index The index in which to add {@code __layer} into.
	 * @throws NullPointerException if {@code __layer} is {@code null}.
	 * @throws IndexOutOfBoundsException If the {@code __index} is invalid.
	 * @since 2026/08/09
	 */
	@Api
	public void insert(@NotNull Layer __layer,
		@Range(from = 0, to = Integer.MAX_VALUE) int __index)
		throws NullPointerException, IndexOutOfBoundsException
	{
		if (__layer == null)
			throw new NullPointerException("NARG");

		synchronized (this)
		{
			if ((__index < 0) || __index > this._layerList.size())
				throw new IndexOutOfBoundsException("IOOB");

			this.remove(__layer);

			this._layerList.add(__index, __layer);
		}
	}

	/**
	 * Renders all currently available {@link Layer} objects in this
	 * {@link LayerManager}, clipping to its current view window.
	 *
	 * {@link Layer} objects are rendered in the inverse order of which they
	 * are present in this {@link LayerManager}. This is because a
	 * {@link Layer}'s index is directly related to its z-order, where
	 * {@code LayerManager.getLayerAt(0)} retrieves the layer closest to the
	 * viewer.
	 *
	 * @param __g The {@link Graphics} object to draw with.
	 * @param __dx The destination's X translation.
	 * @param __dy The destination's Y translation.
	 * @throws NullPointerException If {@code __g} is {@code null}.
	 * @since 2026/08/09
	 */
	@Api
	public void paint(@NotNull Graphics __g, int __dx, int __dy)
		throws NullPointerException
	{
		if (__g == null)
			throw new NullPointerException("NARG");

		int clipX = __g.getClipX();
		int clipY = __g.getClipY();
		int clipWidth = __g.getClipWidth();
		int clipHeight = __g.getClipHeight();
		List<Layer> layerList = this._layerList;

		// Translate and set Graphics clip to the current ViewWindow bounds
		__g.translate(__dx - this._viewWindowX, __dy - this._viewWindowY);
		__g.clipRect(this._viewWindowX, this._viewWindowY,
			this._viewWindowWidth, this._viewWindowHeight);

		// Paint the given layer (if visible). The layer's index indicates its
		// z-order, meaning that index 0 is at the front of all others, which
		// means we have to draw from _layerList.size()-1 to 0.
		synchronized (this)
		{
			for (int i = layerList.size() - 1; i >= 0; i--)
			{
				Layer layer = layerList.get(i);
				if (layer.isVisible())
					layer.paint(__g);
			}
		}

		// Restore the original translation and clip bounds after painting
		__g.translate(-__dx + this._viewWindowX, -__dy + this._viewWindowY);
		__g.setClip(clipX, clipY, clipWidth, clipHeight);
	}

	/**
	 * Removes a given layer from this {@link LayerManager}.
	 *
	 * Note that this method will do nothing if {@code __layer} is not present
	 * in this {@link LayerManager}.
	 *
	 * @param __layer A Layer to be removed from this {@link LayerManager}.
	 * @throws NullPointerException if {@code __layer} is {@code null}.
	 * @since 2026/08/09
	 */
	@Api
	public void remove(@NotNull Layer __layer)
		throws NullPointerException
	{
		if (__layer == null)
			throw new NullPointerException("NARG");

		synchronized (this)
		{
			this._layerList.remove(__layer);
		}
	}

	/**
	 * Sets the view window's bounds for this {@link LayerManager}.
	 *
	 * The view window effectively acts as a {@link LayerManager}-specific clip
	 * region, with its own bounds entirely separate from the {@link Graphics}
	 * object passed into {@link LayerManager#paint(Graphics, int, int)}.
	 *
	 * @param __x The view window's X translation.
	 * @param __y The view window's Y translation.
	 * @param __width The view window's width.
	 * @param __height The view window's height.
	 * @throws IllegalArgumentException if {@code __width} or {@code __height}
	 * are negative.
	 * @since 2026/08/09
	 */
	@Api
	public void setViewWindow(int __x, int __y,
		@Range(from = 0, to = Integer.MAX_VALUE) int __width,
		@Range(from = 0, to = Integer.MAX_VALUE) int __height)
		throws IllegalArgumentException
	{
		if (__width < 0 || __height < 0)
			throw new IllegalArgumentException("INVL");

		this._viewWindowX = __x;
		this._viewWindowY = __y;
		this._viewWindowWidth = __width;
		this._viewWindowHeight = __height;
	}
}
