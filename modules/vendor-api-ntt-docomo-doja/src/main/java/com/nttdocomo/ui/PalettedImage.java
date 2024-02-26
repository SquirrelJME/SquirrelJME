// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.nttdocomo.ui;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.debug.ErrorCode;
import cc.squirreljme.runtime.nttdocomo.ui.EightBitImageStore;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@SuppressWarnings("SpellCheckingInspection")
@Api
public abstract class PalettedImage
	extends Image
{
	/** The overriding palette. */
	volatile Palette _paletteOverride;
	
	/** Override transparent index? */
	volatile int _overrideTransDx =
		-1;
	
	/**
	 * Only initialized by subclasses.
	 * 
	 * @since 2024/01/14
	 */
	protected PalettedImage()
	{
	}
	
	@Api
	public void changeData(byte[] __data)
		throws NullPointerException, UIException
	{
		throw Debugging.todo();
	}
	
	@Api
	public void changeData(InputStream __in)
		throws NullPointerException, UIException
	{
		throw Debugging.todo();
	}
	
	/**
	 * Always throws {@link UnsupportedOperationException}.
	 * 
	 * @throws UnsupportedOperationException Always.
	 * @since 2024/01/14
	 */
	@Api
	@Override
	public final Graphics getGraphics()
		throws UnsupportedOperationException
	{
		/* {@squirreljme.error AH18 Cannot get graphics of a palette based
		image.} */
		throw new UnsupportedOperationException(
			ErrorCode.__error__("AH18"));
	}
	
	/**
	 * Returns the palette for this given image.
	 *
	 * @return The resultant image palette.
	 * @throws UIException If the image has been disposed of.
	 * @since 2024/01/14
	 */
	@Api
	public Palette getPalette()
		throws UIException
	{
		Palette result = this._paletteOverride;
		if (result == null)
			throw new UIException(UIException.ILLEGAL_STATE);
		
		return result;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/14
	 */
	@Override
	@Api
	public final int getTransparentColor()
		throws UnsupportedOperationException
	{
		/* {@squirreljme.error AH19 Cannot get transparent color of a palette
		based image.} */
		throw new UnsupportedOperationException(
			ErrorCode.__error__("AH19"));
	}
	
	/**
	 * Returns the index used for transparency.
	 *
	 * @return The transparency index, if not set then {@code 0} is returned.
	 * @throws UIException If the image has been disposed.
	 * @since 2024/01/15
	 */
	@Api
	public int getTransparentIndex()
		throws UIException
	{
		// Has it been overridden
		int result = this._overrideTransDx;
		if (result >= 0)
			return result;
		
		// Not possible
		throw new UIException(UIException.ILLEGAL_STATE);
	}
	
	/**
	 * Sets the palette of this image.
	 *
	 * @param __palette The palette to set.
	 * @throws IllegalArgumentException If the size of this palette does not
	 * match the size of the existing palette.
	 * @throws NullPointerException On null arguments.
	 * @throws UIException If this has been disposed of.
	 * @since 2024/01/14
	 */
	@Api
	public void setPalette(Palette __palette)
		throws IllegalArgumentException, NullPointerException, UIException
	{
		if (__palette == null)
			throw new NullPointerException("NARG");
		
		// Was this disposed of?
		Palette current = this._paletteOverride;
		if (current == null)
			throw new UIException(UIException.ILLEGAL_STATE);
		
		/* {@squirreljme.error AH1b A palette of the same size must be used
		in order to override an image's existing palette.} */
		if (current.getEntryCount() != __palette.getEntryCount())
			throw new IllegalArgumentException(
				ErrorCode.__error__("AH1b"));
		
		// Set override
		this._paletteOverride = __palette;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/01/14
	 */
	@Api
	@Override
	public final void setTransparentColor(int __color)
		throws UIException
	{
		/* {@squirreljme.error AH1a Cannot set transparent color of a palette
		based image.} */
		throw new UnsupportedOperationException(
			ErrorCode.__error__("AH19a"));
	}
	
	@Api
	@Override
	public final void setTransparentEnabled(boolean __enable)
		throws UIException
	{
		throw Debugging.todo();
	}
	
	/**
	 * Sets the transparent index.
	 *
	 * @param __index The index to set.
	 * @throws ArrayIndexOutOfBoundsException If the index is out of bounds
	 * of the palette.
	 * @throws UIException If the image has been disposed an illegal state is
	 * thrown.
	 * @since 2024/01/15
	 */
	@Api
	public void setTransparentIndex(int __index)
		throws ArrayIndexOutOfBoundsException, UIException
	{
		/* {@squirreljme.error AH1d The transparent index is not valid.} */
		if (__index < 0 || __index >= 256)
			throw new ArrayIndexOutOfBoundsException(
				ErrorCode.__error__("AH1d"));
		
		// Must be within the palette bounds
		Palette palette = this.getPalette();
		if (palette != null)
		{
			/* {@squirreljme.error AH1e The transparent index is not valid.} */
			if (__index >= palette.getEntryCount())
				throw new ArrayIndexOutOfBoundsException(
					ErrorCode.__error__("AH1e"));
		}
		
		// Set
		this._overrideTransDx = __index;
	}
	
	/**
	 * Does this image currently have an alpha color?
	 *
	 * @return If this has an alpha color currently.
	 * @since 2024/01/14
	 */
	boolean __hasAlpha()
	{
		return false;
	}
	
	/**
	 * Reads in the given image data, this only supports GIF and BMP images
	 * up to 8-bits. 
	 *
	 * @param __in Where to read the data from.
	 * @return The resultant image.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/14
	 */
	@Api
	public static PalettedImage createPalettedImage(byte[] __in)
		throws IOException, NullPointerException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		// Forward to stream handler
		try (InputStream in = new ByteArrayInputStream(__in))
		{
			return PalettedImage.createPalettedImage(in);
		}
	}
	
	/**
	 * Reads in the given image data, this only supports GIF and BMP images
	 * up to 8-bits. 
	 *
	 * @param __in Where to read the data from.
	 * @return The resultant image.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/14
	 */
	@Api
	public static PalettedImage createPalettedImage(InputStream __in)
		throws IOException, NullPointerException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		// Just make a generic image and load into it.
		__8BitImage__ bitImage = new __8BitImage__();
		bitImage.changeData(__in);
		
		// Return the resultant image
		return bitImage;
	}
	
	/**
	 * Creates a blank paletted image which may be drawn onto and a palette
	 * set accordingly.
	 *
	 * @param __w The image width.
	 * @param __h The image height.
	 * @return The resultant image.
	 * @throws IllegalArgumentException If the width and/or height are
	 * negative or exceed the array bounds.
	 * @since 2024/01/14
	 */
	@Api
	public static PalettedImage createPalettedImage(int __w, int __h)
		throws IllegalArgumentException
	{
		if (__w <= 0 || __h <= 0)
			throw new IllegalArgumentException("NEGV");
		
		throw Debugging.todo();
	}
}
