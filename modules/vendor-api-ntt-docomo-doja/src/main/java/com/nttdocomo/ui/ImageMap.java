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
import cc.squirreljme.runtime.cldc.annotation.ApiDefinedDeprecated;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

/**
 * DoJa ImageMap class, used for drawing multiple images from a single map, by
 * dividing it into cells.
 *
 * @since 2026/10/01
 */
@Api
public class ImageMap
{
	/** The height of each image cell within this map. */
	private int _cellHeight;

	/** The width of each image cell within this map. */
	private int _cellWidth;

	/** Indicates if image data is concatenated or not. */
	private boolean _concatenated;

	/** This image map's height. */
	private int _mapHeight;

	/** This image map's width. */
	private int _mapWidth;

	/** Array containing image data. */
	private int[] _data;

	/** The images contained within this map. */
	private Image[] _images;

	/** The view window's starting X position. */
	private int _windowX;

	/** The view window's starting Y position. */
	private int _windowY;

	/** The view window's height. */
	private int _windowHeight;

	/** The view window's width. */
	private int _windowWidth;

	/**
	 * This constructor is only accessible via class extension and using it in
	 * such a way is heavily discouraged.
	 *
	 * @since 2026/10/01
	 */
	@Api
	protected ImageMap()
	{
	}

	/**
	 * Creates a new empty ImageMap with the specified cell width and height.
	 *
	 * @param __cellWidth The width of each image cell.
	 * @param __cellHeight The width of each image cell.
	 * @throws IllegalArgumentException If the cell width or height are 0 or
	 * less.
	 * @since 2026/10/01
	 */
	@Api
	public ImageMap(@Range(from = 0, to = Integer.MAX_VALUE) int __cellWidth,
		@Range(from = 0, to = Integer.MAX_VALUE) int __cellHeight)
		throws IllegalArgumentException
	{
		/* {@squirreljme.error AH30 Invalid cell width or height.} */
		if (__cellWidth <= 0 || __cellHeight <= 0)
			throw new IllegalArgumentException("AH30");

		this._cellWidth = __cellWidth;
		this._cellHeight = __cellHeight;
		this._windowWidth = 0;
		this._windowHeight = 0;
	}

	/**
	 * Creates a new ImageMap with cell width and height, as well as image
	 * and map data received as references.
	 *
	 * This method has been deprecated in favor of
	 * {@link ImageMap(int, int, int[] Image[])}, which allows for
	 * more than 128 images.
	 *
	 * @param __cellWidth The width of each image cell.
	 * @param __cellHeight The width of each image cell.
	 * @param __mapWidth The width of the map itself.
	 * @param __mapHeight The height of the map itself.
	 * @param __data The array containing references to images on the
	 * {@code __images} array. This element must be at least as big as the
	 * result of {@code __mapWidth} and {@code __mapHeight}, and any indices
	 * beyond said result or beyond 128 are ignored.
	 * @param __images Array of image data, referenced by {@code __data}.
	 * @throws IllegalArgumentException If the cell width, cell height, map
	 * width or map height are 0 or less; or if the length of {@code __data}
	 * is shorter than {@code __mapWidth * __mapHeight}.
	 * @throws NullPointerException On {@code null} arguments.
	 * @since 2026/10/01
	 */
	@Api
	@ApiDefinedDeprecated
	public ImageMap(@Range(from = 0, to = Integer.MAX_VALUE) int __cellWidth,
		@Range(from = 0, to = Integer.MAX_VALUE) int __cellHeight,
		@Range(from = 0, to = Integer.MAX_VALUE) int __mapWidth,
		@Range(from = 0, to = Integer.MAX_VALUE) int __mapHeight,
		@NotNull byte[] __data, @NotNull Image[] __images)
		throws IllegalArgumentException, NullPointerException
	{
		/* {@squirreljme.error AH30 Invalid cell width or height.} */
		if (__cellWidth <= 0 || __cellHeight <= 0)
			throw new IllegalArgumentException("AH30");

		this._cellWidth = __cellWidth;
		this._cellHeight = __cellHeight;
		this._windowWidth = 0;
		this._windowHeight = 0;

		if ( __data == null)
			throw new NullPointerException("NARG");

		int[] data = new int[__data.length];

		for (int i = 0; i < __data.length; i++)
			data[i] = __data[i];

		this.setImageMap(__mapWidth, __mapHeight, data, __images, false);
	}

	/**
	 * Creates a new ImageMap with cell width and height, as well as image
	 * and map data received as references.
	 *
	 * @param __cellWidth The width of each image cell.
	 * @param __cellHeight The width of each image cell.
	 * @param __mapWidth The width of the map itself.
	 * @param __mapHeight The height of the map itself.
	 * @param __data The array containing references to images on the
	 * {@code __images} array. This element must be at least as big as the
	 * result of {@code __mapWidth} and {@code __mapHeight}, and any indices
	 * beyond said result are ignored.
	 * @param __images Array of image data, referenced by {@code __data}.
	 * @throws IllegalArgumentException If the cell width, cell height, map
	 * width or map height are 0 or less; or if the length of {@code __data}
	 * is shorter than {@code __mapWidth * __mapHeight}.
	 * @throws NullPointerException On {@code null} arguments.
	 * @since 2026/10/01
	 */
	@Api
	public ImageMap(@Range(from = 0, to = Integer.MAX_VALUE) int __cellWidth,
		@Range(from = 0, to = Integer.MAX_VALUE) int __cellHeight,
		@Range(from = 0, to = Integer.MAX_VALUE) int __mapWidth,
		@Range(from = 0, to = Integer.MAX_VALUE) int __mapHeight,
		@NotNull int[] __data, @NotNull Image[] __images)
		throws IllegalArgumentException, NullPointerException
	{
		this(__cellWidth, __cellHeight, __mapWidth, __mapHeight, __data,
			__images, false);
	}

	/**
	 * Creates a new ImageMap with cell width and height, as well as image
	 * and map data received as references, accompanied by a boolean that
	 * indicates whether the images are concatenated or standalone.
	 *
	 * The view window is initialized to have the same dimensions as the map.
	 *
	 * @param __cellWidth The width of each image cell.
	 * @param __cellHeight The width of each image cell.
	 * @param __mapWidth The width of the map itself.
	 * @param __mapHeight The height of the map itself.
	 * @param __data The array containing references to images on the
	 * {@code __images} array. This element must be at least as big as the
	 * result of {@code __mapWidth} and {@code __mapHeight}, and any indices
	 * beyond said result are ignored.
	 * @param __images Array of image data, referenced by {@code __data}.
	 * @param __concat True if each element on the {@code __images} array is a
	 * concatenation of images, false if each element is a single image.
	 * @throws IllegalArgumentException If the cell width, cell height, map
	 * width or map height are 0 or less; or if the length of {@code __data}
	 * is shorter than {@code __mapWidth * __mapHeight}.
	 * @throws NullPointerException On {@code null} arguments.
	 * @since 2026/10/01
	 */
	@Api
	public ImageMap(
		@Range(from = 0, to = Integer.MAX_VALUE) int __cellWidth,
		@Range(from = 0, to = Integer.MAX_VALUE) int __cellHeight,
		@Range(from = 0, to = Integer.MAX_VALUE) int __mapWidth,
		@Range(from = 0, to = Integer.MAX_VALUE) int __mapHeight,
		@NotNull int[] __data, @NotNull Image[] __images, boolean __concat)
		throws IllegalArgumentException, NullPointerException
	{
		this.setImageMap(__mapWidth, __mapHeight, __data, __images, false);
		this._cellWidth = __cellWidth;
		this._cellHeight = __cellHeight;
		this._concatenated = __concat;
		this._windowX = 0;
		this._windowY = 0;
	}

	/**
	 * Sets new image and map data into this ImageMap.
	 *
	 * This method has been deprecated in favor of
	 * {@link ImageMap#setImageMap(int, int, int[], Image[])}, which
	 * allows for more than 128 images.
	 *
	 * @param __mapWidth The width of the map itself.
	 * @param __mapHeight The height of the map itself.
	 * @param __data The array containing references to images on the
	 * {@code __images} array. This element must be at least as big as the
	 * result of {@code __mapWidth} and {@code __mapHeight}, and any indices
	 * beyond said result or beyond 128 are ignored.
	 * @param __images Array of image data, referenced by {@code __data}.
	 * @throws IllegalArgumentException If the map width or map height are 0
	 * or less; or if the length of {@code __data} is shorter than
	 * {@code __mapWidth * __mapHeight}.
	 * @throws NullPointerException On {@code null} arguments.
	 * @since 2026/10/01
	 */
	@Api
	@ApiDefinedDeprecated
	public void setImageMap(
		@Range(from = 0, to = Integer.MAX_VALUE) int __mapWidth,
		@Range(from = 0, to = Integer.MAX_VALUE) int __mapHeight,
		@NotNull byte[] __data, @NotNull Image[] __images)
		throws IllegalArgumentException, NullPointerException
	{
		if (__data == null || __images == null)
			throw new NullPointerException("NARG");

		/* {@squirreljme.error AH31 Invalid ImageMap width or height.} */
		if (__mapWidth <= 0 || __mapHeight <= 0)
			throw new IllegalArgumentException("AH31");

		/* {@squirreljme.error AH32 Invalid ImageMap data reference size.} */
		if (__data.length < __mapWidth * __mapHeight)
			throw new IllegalArgumentException("AH32");

		int[] data = new int[__data.length];

		for (int i = 0; i < __data.length; i++)
			data[i] = __data[i];

		this.setImageMap(__mapWidth, __mapHeight, data, __images, 
			false);
	}

	/**
	 * Sets new image and map data into this ImageMap.
	 *
	 * @param __mapWidth The width of the map itself.
	 * @param __mapHeight The height of the map itself.
	 * @param __data The array containing references to images on the
	 * {@code __images} array. This element must be at least as big as the
	 * result of {@code __mapWidth} and {@code __mapHeight}, and any indices
	 * beyond said result are ignored.
	 * @param __images Array of image data, referenced by {@code __data}.
	 * @throws IllegalArgumentException If the map width or map height are 0
	 * or less; or if the length of {@code __data} is shorter than
	 * {@code __mapWidth * __mapHeight}.
	 * @throws NullPointerException On {@code null} arguments.
	 * @since 2026/05/12
	 */
	@Api
	public void setImageMap(
		@Range(from = 0, to = Integer.MAX_VALUE) int __mapWidth,
		@Range(from = 0, to = Integer.MAX_VALUE) int __mapHeight,
		@NotNull int[] __data, @NotNull Image[] __images)
		throws IllegalArgumentException, NullPointerException
	{
		this.setImageMap(__mapWidth, __mapHeight, __data, __images, false);
	}

	/**
	 * Sets new image and map data into this ImageMap, with the possibility of
	 * using concatenated images.
	 *
	 * The view window is initialized to have the same dimensions as the map.
	 *
	 * @param __mapWidth The width of the map itself.
	 * @param __mapHeight The height of the map itself.
	 * @param __data The array containing references to images on the
	 * {@code __images} array. This element must be at least as big as the
	 * result of {@code __mapWidth} and {@code __mapHeight}, and any indices
	 * beyond said result are ignored.
	 * @param __images Array of image data, referenced by {@code __data}.
	 * @param __concat True if each element on the {@code __images} array is a
	 * concatenation of images, false if each element is a single image.
	 * @throws IllegalArgumentException If the cell width, cell height, map
	 * width or map height are 0 or less; or if the length of {@code __data}
	 * is shorter than {@code __mapWidth * __mapHeight}.
	 * @throws NullPointerException On {@code null} arguments.
	 * @since 2026/10/01
	 */
	@Api
	public void setImageMap(
		@Range(from = 0, to = Integer.MAX_VALUE) int __mapWidth,
		@Range(from = 0, to = Integer.MAX_VALUE) int __mapHeight,
		@NotNull int[] __data, @NotNull Image[] __images, boolean __concat)
		throws IllegalArgumentException, NullPointerException
	{
		if (__data == null || __images == null)
			throw new NullPointerException("NARG");

		/* {@squirreljme.error AH31 Invalid ImageMap width or height.} */
		if (__mapWidth <= 0 || __mapHeight <= 0)
			throw new IllegalArgumentException("AH31");

		/* {@squirreljme.error AH32 Invalid ImageMap data reference size.} */
		if (__data.length < __mapWidth * __mapHeight)
			throw new IllegalArgumentException("AH32");

		this._mapWidth = __mapWidth;
		this._mapHeight = __mapHeight;
		this._data = __data;
		this._images = __images;
		this._windowWidth = __mapWidth;
		this._windowHeight = __mapHeight;
		this._concatenated = __concat;
	}

	/**
	 * Translates this ImageMap's view window by the specified X and Y offsets.
	 *
	 * @param __dx The X cell offset to translate the view window.
	 * @param __dy The Y cell offset to translate the view window.
	 * @throws IllegalArgumentException If any arguments are less than zero;
	 * or the resulting window area exceeds the map's bounds.
	 * @since 2026/10/01
	 */
	@Api
	public void moveWindowLocation(int __dx, int __dy)
		throws IllegalArgumentException
	{
		/* {@squirreljme.error AH32 Invalid ImageMap view window area.} */
		if (this._windowX + __dx < 0 || this._windowY + __dy < 0 ||
			this._windowX + __dx + this._windowWidth > this._mapWidth ||
			this._windowY + __dy + this._windowHeight > this._mapHeight)
			throw new IllegalArgumentException("AH33");

		this._windowX += __dx;
		this._windowY += __dy;
	}

	/**
	 * Sets a new view window rectangle for this ImageMap. Note that the
	 * dimensions are in regard to images, not pixels. Thus, a view window of
	 * {@code [0, 0, 2, 2]} means that a call to
	 * {@link Graphics#drawImageMap(ImageMap, int, int)} will draw 4 images,
	 * being 2 in the first cell row in the reference array, and 2 of the next
	 * cell row right below.
	 *
	 * Due to that behavior, X and Y must always be at least zero, as there is
	 * no negative image index.
	 *
	 * @param __x The starting cell position on the X axis.
	 * @param __y The starting cell position on the Y axis.
	 * @param __width The view window's width in cells.
	 * @param __height The view window's height in cells.
	 * @throws IllegalArgumentException If {@code __x} or {@code __y} are
	 * negative, {@code __width} or {@code __height} are zero or less; or
	 * their sum is larger than the map's region.
	 * @since 2026/10/01
	 */
	@Api
	public void setWindow(
		@Range(from = 0, to = Integer.MAX_VALUE) int __x,
		@Range(from = 0, to = Integer.MAX_VALUE) int __y,
		@Range(from = 0, to = Integer.MAX_VALUE) int __width,
		@Range(from = 0, to = Integer.MAX_VALUE) int __height)
	{
		/* {@squirreljme.error AH32 Invalid ImageMap view window area.} */
		if (__x < 0 || __y < 0 || __width <= 0 || __height <= 0 ||
			__x + __width > this._mapWidth || __y + __height > this._mapHeight)
			throw new IllegalArgumentException("AH33");

		this._windowX = __x;
		this._windowY = __y;
		this._windowWidth = __width;
		this._windowHeight = __height;
	}

	/**
	 * Sets this ImageMap's view window at the specified X and Y cell
	 * coordinates.
	 *
	 * @param __x The new starting cell position on the X axis.
	 * @param __y The new starting cell position on the Y axis.
	 * @throws IllegalArgumentException If any arguments are less than zero;
	 * or the resulting window area exceeds the map's bounds.
	 * @since 2026/10/01
	 */
	@Api
	public void setWindowLocation(
		@Range(from = 0, to = Integer.MAX_VALUE) int __x,
		@Range(from = 0, to = Integer.MAX_VALUE) int __y)
		throws IllegalArgumentException
	{
		/* {@squirreljme.error AH32 Invalid ImageMap view window area.} */
		if (__x < 0 || __y < 0 || __x + this._windowWidth > this._mapWidth ||
			__y + this._windowHeight > this._mapHeight)
			throw new IllegalArgumentException("AH33");

		this._windowX = __x;
		this._windowY = __y;
	}

	/**
	 * Draws this image map. This method is intended to be called by
	 * {@link Graphics#drawImageMap(ImageMap, int, int)} directly.
	 *
	 * Drawing takes place in the specified view window bounds, that is to say,
	 * all images currently inside the region specified by
	 * {@code [windowX, windowY, windowWidth, windowHeight] } are considered
	 * for drawing.
	 *
	 * Note that it is possible to define an image array width and height not
	 * evenly divisible by cellWidth and cellHeight, and the data that resides
	 * out of bounds is discarded, resulting in its bottom and right corners
	 * being cut off.
	 *
	 * It is also possible to define a map with a width and height of 0,
	 * although nothing will be drawn in that case.
	 *
	 * Additionally, the following cases result in a cell not being drawn:
	 *  - If the index to draw is negative
	 *  - If the index to draw is larger than the image array, or the map's
	 *    bounds.
	 *  - If the image referenced by the current index is {@code null}.
	 *  - If the image referenced by the current index has been disposed.
	 *    of (and in this case, an {@link UIException} must be thrown).
	 *
	 * @param __g The {@link Graphics} object to use for drawing this ImageMap.
	 * @param __x The offset to start drawing images on the x axis.
	 * @param __x The offset to start drawing images on the y axis.
	 * @throws UIException If at least one of the images that this methods must
	 * draw has already been disposed of.
	 * @since 2026/10/01
	 */
	void __draw(Graphics __g, int __x, int __y)
		throws UIException
	{
		// No idea how concatenated images must be handled yet
		if (this._concatenated)
			throw Debugging.todo("ImageMap draw concatenated images.");

		int cellHeight = this._cellHeight;
		int cellWidth = this._cellWidth;
		Image[] images = this._images;
		int[] data = this._data;
		int mapWidth = this._mapWidth;
		int mapHeight = this._mapHeight;
		int windowHeight = this._windowHeight;
		int windowWidth = this._windowWidth;
		int windowX = this._windowX;
		int windowY = this._windowY;
		int imgIndex, drawX, drawY;
		int mapSize = mapWidth * mapHeight;

		for (int y = windowY; y < windowHeight; y++)
		{
			drawY = y * cellHeight;

			for (int x = windowX; x < windowWidth; x++)
			{
				imgIndex = data[mapWidth * y + x];

				// Ignore null indices and references that go outside the image
				// array and map's bounds, as per the documentation.
				// TODO: Check if image was disposed, and throw an UIException.
				if (imgIndex >= 0 && imgIndex < images.length &&
					imgIndex < mapSize && images[imgIndex] != null)
				{
					drawX = x * cellWidth;
					__g.drawImage(images[imgIndex], __x + drawX, __y + drawY,
						0, 0, cellWidth, cellHeight);
				}
			}
		}
	}
}
