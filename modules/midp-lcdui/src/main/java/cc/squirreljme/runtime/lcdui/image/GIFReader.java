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
import cc.squirreljme.runtime.cldc.util.IntegerList;
import cc.squirreljme.runtime.cldc.util.StreamUtils;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import javax.microedition.lcdui.Image;
import net.multiphasicapps.io.DataEndianess;
import net.multiphasicapps.io.ExtendedDataInputStream;

/**
 * This class is used to read and parse GIF images.
 *
 * @since 2021/12/04
 */
public class GIFReader
{
	/** Extension. */
	public static final byte BLOCK_EXTENSION =
		0x21;
	
	/** Image. */
	public static final byte BLOCK_IMAGE =
		0x2C;
	
	/** End of GIF. */
	public static final byte BLOCK_FILE_TERMINATION =
		0x3B;
	
	/** Application extension. */
	public static final byte EXTENSION_APPLICATION =
		(byte)0xFF;
	
	/** Comment extension. */
	public static final byte EXTENSION_COMMENT =
		(byte)0xFE;
	
	/** Graphics control extension. */
	public static final byte EXTENSION_GRAPHICS_CONTROL =
		(byte)0xF9;
	
	/** The source data stream. */
	protected final ExtendedDataInputStream in;
	
	/** The factory used to create the final images. */
	protected final ImageFactory factory;
	
	/** The number of images available. */
	protected final List<Image> subImages =
		new ArrayList<>();
	
	/** Sub-delay times for images. */
	protected final IntegerList subFrameTimes =
		new IntegerList();
	
	/** The global color table. */
	private __GIFPalette__ _gct;
	
	/** The number of times to loop through the image. */
	private int _loopCount =
		-1;
	
	/** The current frame control, if any. */
	private __GIFFrameControl__ _frameControl;
	
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
		this._gct = globalPalette;
		
		// Image parsing loop, stop when termination was reached
		for (int lastId = -1; lastId != GIFReader.BLOCK_FILE_TERMINATION;)
			lastId = this.__decodeBlock();
		
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
		IntegerList subFrameTimes = this.subFrameTimes;
		return imageFactory.animatedImage(
			subImages.toArray(new Image[subImages.size()]),
			subFrameTimes.toIntegerArray(),
			Math.max(1, this._loopCount));
	}
	
	/**
	 * Decodes a GIF block.
	 * 
	 * @return The read block ID.
	 * @throws IOException On read errors.
	 * @since 2022/07/02
	 */
	private byte __decodeBlock()
		throws IOException
	{
		byte blockId = this.in.readByte();
		switch (blockId)
		{
				// Extension
			case GIFReader.BLOCK_EXTENSION:
				this.__decodeExtension();
				break;
			
				// Image
			case GIFReader.BLOCK_IMAGE:
				throw Debugging.todo();
			
				// End of GIF
			case GIFReader.BLOCK_FILE_TERMINATION:
				break;
			
				// {@squirreljme.error EB3a Unknown GIF block type.
				// (The block)}
			default:
				throw new IOException("EB3a " + blockId);
		}
		
		return blockId;
	}
	
	/**
	 * Decodes an extension block.
	 * 
	 * @throws IOException On read errors.
	 * @since 2022/07/02
	 */
	private void __decodeExtension()
		throws IOException
	{
		byte blockId = this.in.readByte();
		switch (blockId)
		{
				// Application extension, this is ignored
			case GIFReader.EXTENSION_APPLICATION:
				this.__handleApplicationExtension();
				break;
			
				// Comment, this is completely ignored
			case GIFReader.EXTENSION_COMMENT:
				this.__handleComment();
				break;
			
				// Graphics control
			case GIFReader.EXTENSION_GRAPHICS_CONTROL:
				this.__handleExtensionGraphicsControl();
				break;
			
				// {@squirreljme.error EB3o Unknown GIF block type.
				// (The block)}
			default:
				throw new IOException(
					"EB3o 0x" + (blockId & 0xFF));
		}
	}
	
	/**
	 * Handles the application extension, which is ignored by SquirrelJME.
	 * 
	 * @throws IOException On read errors.
	 * @since 2022/07/03
	 */
	private void __handleApplicationExtension()
		throws IOException
	{
		ExtendedDataInputStream in = this.in;
		
		// The next field just contains the number of bytes in the application
		// ID and otherwise, should always be 11 but could be more?
		// {@squirreljme.error EB3s Malformed application extension.}
		int preDataBlockSize = in.readUnsignedByte();
		if (preDataBlockSize != in.skipBytes(preDataBlockSize))
			throw new IOException("EB3s");
		
		// Read the application data, but just ignore it
		new __GIFDataSubBlockInputStream__(this.in).close();
	}
	
	/**
	 * Handles Graphics Extension Control.
	 * 
	 * @throws IOException On read errors.
	 * @since 2022/07/03
	 */
	private void __handleExtensionGraphicsControl()
		throws IOException
	{
		ExtendedDataInputStream in = this.in;
		
		// Read in the data block
		int blockSize = in.readUnsignedByte();
		byte[] blockData = new byte[blockSize];
		in.readFully(blockData);
		
		// Handle the data within and parse 
		try (ExtendedDataInputStream data = new ExtendedDataInputStream(
			new ByteArrayInputStream(blockData), DataEndianess.LITTLE))
		{
			// Read the frame control information
			__GIFFrameControl__ frameControl = new __GIFFrameControl__(
				data.readUnsignedByte(),
				data.readUnsignedShort(),
				data.readUnsignedByte());
			this._frameControl = frameControl;
			
			// Set the last frame's delay to the delay specified in this one
			// since this indicates the amount of time to wait _before_
			// showing the current frame
			IntegerList subFrameTimes = this.subFrameTimes;
			if (!subFrameTimes.isEmpty())
				subFrameTimes.setInteger(
					subFrameTimes.size() - 1, frameControl.delayMilli);
		}
		
		// There is technically a data block here, but we just ignore it
		new __GIFDataSubBlockInputStream__(in).close();
	}
	
	/**
	 * Handles GIF comments, essentially they are ignored.
	 * 
	 * @throws IOException On read errors.
	 * @since 2022/07/03
	 */
	private void __handleComment()
		throws IOException
	{
		// Closing will automatically advance the stream
		try (InputStream in =
			new __GIFDataSubBlockInputStream__(this.in))
		{
			Debugging.debugNote("GIF Comment: %s",
				new String(StreamUtils.readAll(in),
					"utf-8"));
		}
	}
}
