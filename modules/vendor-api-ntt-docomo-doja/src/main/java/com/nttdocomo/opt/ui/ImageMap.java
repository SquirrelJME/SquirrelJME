// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.nttdocomo.opt.ui;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.annotation.ApiDefinedDeprecated;
import com.nttdocomo.ui.Image;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

/**
 * Use {@link com.nttdocomo.ui.ImageMap} instead.
 *
 * @deprecated Use {@link com.nttdocomo.ui.ImageMap} instead.
 * @since 2026/10/01
 */
@Api
@ApiDefinedDeprecated
public class ImageMap
	extends com.nttdocomo.ui.ImageMap
{
	/**
	 * This constructor is only accessible via class extension and using it in
	 * such a way is heavily discouraged.
	 *
	 * @since 2026/10/01
	 */
	@Api
	@ApiDefinedDeprecated
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
	@ApiDefinedDeprecated
	public ImageMap(@Range(from = 0, to = Integer.MAX_VALUE) int __cellWidth,
		@Range(from = 0, to = Integer.MAX_VALUE) int __cellHeight)
		throws IllegalArgumentException
	{
		super(__cellWidth, __cellHeight);
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
		super(__cellWidth, __cellHeight, __mapWidth, __mapHeight, __data,
			__images);
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
	@ApiDefinedDeprecated
	public ImageMap(@Range(from = 0, to = Integer.MAX_VALUE) int __cellWidth,
		@Range(from = 0, to = Integer.MAX_VALUE) int __cellHeight,
		@Range(from = 0, to = Integer.MAX_VALUE) int __mapWidth,
		@Range(from = 0, to = Integer.MAX_VALUE) int __mapHeight,
		@NotNull int[] __data, @NotNull Image[] __images)
		throws IllegalArgumentException, NullPointerException
	{
		super(__cellWidth, __cellHeight, __mapWidth, __mapHeight, __data,
			__images);
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
	@ApiDefinedDeprecated
	public ImageMap(@Range(from = 0, to = Integer.MAX_VALUE) int __cellWidth,
		@Range(from = 0, to = Integer.MAX_VALUE) int __cellHeight,
		@Range(from = 0, to = Integer.MAX_VALUE) int __mapWidth,
		@Range(from = 0, to = Integer.MAX_VALUE) int __mapHeight,
		@NotNull int[] __data, @NotNull Image[] __images, boolean __concat)
		throws IllegalArgumentException, NullPointerException
	{
		super(__cellWidth, __cellHeight, __mapWidth, __mapHeight, __data,
			__images, __concat);
	}
}
