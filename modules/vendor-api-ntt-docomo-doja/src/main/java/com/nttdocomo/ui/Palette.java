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
import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * This is a mutable palette which can be utilized across multiple instances
 * of {@link PalettedImage} to reflect the given colors. When multiple
 * {@link PalettedImage} refer to a single palette then they all share the
 * same palette with its modifications.
 * 
 * @since 2024/01/14
 */
@Api
public class Palette
{
	/** The set of colors in the palette. */
	private final int[] _colors;
	
	/**
	 * Initializes the palette to entirely black.
	 *
	 * @param __numColors The number of colors in the palette.
	 * @throws IllegalArgumentException If the number of colors is zero or
	 * negative.
	 * @since 2024/01/14
	 */
	@Api
	public Palette(int __numColors)
		throws IllegalArgumentException
	{
		if (__numColors <= 0)
			throw new IllegalArgumentException("NEGV");
		
		// Setup
		this._colors = new int[__numColors];
	}
	
	/**
	 * Initializes the palette with the following colors.
	 *
	 * @param __colors The colors to use, the array is copied so as such any
	 * changes made to it following are ignored.
	 * @throws IllegalArgumentException If there are no colors; or a color is
	 * an invalid value.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/14
	 */
	@Api
	public Palette(int[] __colors)
		throws IllegalArgumentException, NullPointerException
	{
		if (__colors == null)
			throw new NullPointerException("NARG");
		
		// Limit to 256 colors
		int numColors = __colors.length;
		if (numColors > 256)
			numColors = 256;
		
		// Cannot have a zero sized palette
		else if (numColors == 0)
			throw new IllegalArgumentException("NEGV");
		
		// Copy colors
		int[] colors = new int[numColors];
		System.arraycopy(__colors, 0,
			colors, 0, numColors);
		
		// Store colors
		this._colors = colors;
	}
	
	/**
	 * Returns the color at the given index.
	 *
	 * @param __index The index to get.
	 * @return The resultant color.
	 * @throws ArrayIndexOutOfBoundsException If the index is not within the
	 * bounds of the palette.
	 * @since 2024/01/14
	 */
	@Api
	public int getEntry(int __index)
		throws ArrayIndexOutOfBoundsException
	{
		int[] colors = this._colors;
		if (__index < 0 || __index >= colors.length)
			throw new ArrayIndexOutOfBoundsException("IOOB");
		
		return colors[__index] | 0xFF_000000;
	}
	
	/**
	 * Returns the number of colors available in the palette.
	 *
	 * @return The number of available colors.
	 * @since 2024/01/14
	 */
	@Api
	public int getEntryCount()
	{
		return this._colors.length;
	}
	
	/**
	 * Sets the index to the specified color.
	 *
	 * @param __index The index to set.
	 * @param __color The color to set to.
	 * @throws ArrayIndexOutOfBoundsException If the index is not within
	 * the bounds of the palette.
	 * @throws IllegalArgumentException If the color is not valid.
	 * @since 2024/01/14
	 */
	@Api
	public void setEntry(int __index, int __color)
		throws ArrayIndexOutOfBoundsException, IllegalArgumentException
	{
		int[] colors = this._colors;
		if (__index < 0 || __index >= colors.length)
			throw new ArrayIndexOutOfBoundsException("IOOB");
		
		// Replace color
		colors[__index] = __color;
	}
}
