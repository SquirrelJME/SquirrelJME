// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.image;

import cc.squirreljme.jvm.mle.callbacks.NativeImageLoadCallback;
import java.io.IOException;
import javax.microedition.lcdui.Image;
import net.multiphasicapps.io.ExtendedDataInputStream;

/**
 * This class is used to read and parse GIF images.
 *
 * @since 2021/12/04
 */
public class GIFReader
	implements ImageReader
{
	/** The source data stream. */
	protected final ExtendedDataInputStream in;
	
	/** The image loader to use. */
	protected final NativeImageLoadCallback loader;
	
	/**
	 * Initializes the GIF reader.
	 *
	 * @param __in The stream to read from.
	 * @param __loader The native loader to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/12/04
	 */
	public GIFReader(ExtendedDataInputStream __in,
		NativeImageLoadCallback __loader)
		throws NullPointerException
	{
		if (__in == null || __loader == null)
			throw new NullPointerException("NARG");
		
		this.in = __in;
		this.loader = __loader;
	}
	
	/**
	 * Parses the image.
	 * 
	 * @throws IOException On read errors.
	 * @since 2021/12/04
	 */
	@Override
	public void parse()
		throws IOException
	{
		NativeImageLoadCallback loader = this.loader;
		
		// Skip header
		ExtendedDataInputStream in = this.in;
		for (int i = 0; i < 6; i++)
			in.readByte();
		
		// Read "screen" size
		int screenWidth = in.readUnsignedShort();
		int screenHeight = in.readUnsignedShort();
		
		// Build image
		loader.initialize(screenWidth, screenHeight,
			false, false);
		loader.addImage(new int[screenWidth * screenHeight], 0,
			screenWidth * screenHeight, 0, false);
	}
}
