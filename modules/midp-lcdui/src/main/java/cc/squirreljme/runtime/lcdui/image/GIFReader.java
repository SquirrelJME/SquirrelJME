// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
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
	
	/** The background color used. */
	private short _bgColor;
	
	/** The global color table. */
	private __GIFPalette__ _globalPalette;
	
	/** The number of times to loop through the image. */
	private int _loopCount =
		-1;
		
	/** Has this had transparency? */
	private boolean _hadTransparency;
	
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
		this._bgColor = (short)in.readUnsignedByte();
		
		// Pixel aspect ratio, ignored
		in.readUnsignedByte();
		
		// Read in the global color table
		__GIFPalette__ globalPalette = null;
		if (imageFlags.hasGlobalColorTable)
			globalPalette = __GIFPalette__.__parseGlobal(
				in, imageFlags.globalColorTableSize);
		this._globalPalette = globalPalette;
		
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
				false, this._hadTransparency,
				screenWidth, screenHeight);
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
				this.__handleImage();
				break;
			
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
	 * Handles the application extension, the only really useful one is the
	 * Netscape extension which allows for looping.
	 * 
	 * @throws IOException On read errors.
	 * @since 2022/07/03
	 */
	private void __handleApplicationExtension()
		throws IOException
	{
		ExtendedDataInputStream in = this.in;
		
		// The next field just contains the number of bytes in the application
		// ID and otherwise, should always be 11.
		// {@squirreljme.error EB3s Malformed application extension.}
		if (in.readUnsignedByte() != 11)
			throw new IOException("EB3s");
		
		// Read the application ID and authentication code as one chunk
		byte[] rawApp = new byte[11];
		in.readFully(rawApp);
		String app = new String(rawApp, "ascii");
		
		// Action depends on the application ID
		try (ExtendedDataInputStream appData = new ExtendedDataInputStream(
			new __GIFDataSubBlockInputStream__(this.in), DataEndianess.LITTLE))
		{
			switch (app)
			{
					// Netscape/Animation extension, these are the same, and
					// I am assuming ANIMEXTS probably comes from someone
					// or something hating Netscape being in there.
				case "NETSCAPE2.0":
				case "ANIMEXTS1.0":
					// Ignored, always 1??
					appData.readByte();
					
					// Loop count
					this._loopCount = appData.readUnsignedShort();
					break;
				
					// Unknown, ignore
				default:
					break;
			}
		}
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
			
			// Has this had transparency?
			this._hadTransparency |= frameControl.hasTransColor;
		}
		
		// There is technically a data block here, but we just ignore it
		new __GIFDataSubBlockInputStream__(in).close();
	}
	
	/**
	 * Handles the loading of images.
	 * 
	 * @throws IOException On read errors.
	 * @since 2022/07/09
	 */
	private void __handleImage()
		throws IOException
	{
		ExtendedDataInputStream in = this.in;
		__GIFFrameControl__ frameControl = this._frameControl;
		
		// Image position
		int imgX = in.readUnsignedShort();
		int imgY = in.readUnsignedShort();
		
		// Image size
		int imgW = in.readUnsignedShort();
		int imgH = in.readUnsignedShort();
		
		// Image flags
		int flags = in.readUnsignedByte();
		
		throw Debugging.todo();
	}
}
