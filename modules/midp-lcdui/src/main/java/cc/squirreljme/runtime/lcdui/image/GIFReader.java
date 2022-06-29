// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.image;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.microedition.lcdui.Image;
import net.multiphasicapps.io.ExtendedDataInputStream;

/**
 * This class is used to read and parse GIF images.
 *
 * @since 2021/12/04
 */
public class GIFReader
{
	/** The source data stream. */
	protected final ExtendedDataInputStream in;
	
	/** The factory used to create the final images. */
	protected final ImageFactory factory;
	
	/** The number of images available. */
	protected final List<Image> subImages =
		new ArrayList<>();
	
	/**
	 * Initializes the GIF reader.
	 * 
	 * @param __in The stream to read from.
	 * @param __factory The factory used for creating images.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/12/04
	 */
	public GIFReader(ExtendedDataInputStream __in, ImageFactory __factory)
		throws NullPointerException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		this.in = __in;
		this.factory = __factory;
	}
	
	/**
	 * Parses the image.
	 * 
	 * @return The resultant image.
	 * @throws IOException On null arguments.
	 * @since 2021/12/04
	 */
	protected Image parse()
		throws IOException
	{
		// Skip header
		ExtendedDataInputStream in = this.in;
		for (int i = 0; i < 6; i++)
			in.readByte();
		
		// Read "screen" size
		int screenWidth = in.readUnsignedShort();
		int screenHeight = in.readUnsignedShort();
		
		// Flags regarding the image
		__GIFFlags__ imageFlags = new __GIFFlags__(in.readUnsignedByte());
		
		// Background color index
		int bgColorIndex = in.readUnsignedByte();
		
		// Pixel aspect ratio
		int aspectRatio = in.readUnsignedByte();
		
		// Read in the global color table
		__GIFPalette__ globalPalette = null;
		if (imageFlags.hasGlobalColorTable)
			globalPalette = __GIFPalette__.__parseGlobal(
				in, imageFlags.globalColorTableSize);
		
		// Image parsing loop
		
		// Build image, which may be animated or not!
		ImageFactory imageFactory = this.factory;
		List<Image> subImages = this.subImages;
		
		// Fallback blank image?
		if (subImages.isEmpty())
		{
			int[] rgb = new int[screenWidth * screenHeight];
			
			return imageFactory.stillImage(rgb, 0, rgb.length,
				false, false, screenWidth, screenHeight);
		}
		
		// Single image, use it directly
		else if (subImages.size() == 1)
			return subImages.get(0);
		
		// Animated images
		throw Debugging.todo();
	}
}
