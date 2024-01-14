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
	
	@Api
	public Palette getPalette()
		throws UIException
	{
		throw Debugging.todo();
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
	
	@Api
	public int getTransparentIndex()
		throws UIException
	{
		throw Debugging.todo();
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
	
	@Api
	public void setTransparentIndex(int __color)
		throws UIException
	{
		throw Debugging.todo();
	}
	
	/**
	 * Does this image currently have an alpha color?
	 *
	 * @return If this has an alpha color currently.
	 * @since 2024/01/14
	 */
	boolean __hasAlpha()
	{
		throw Debugging.todo();
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
