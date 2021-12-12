// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.image;

import cc.squirreljme.jvm.mle.PencilShelf;
import cc.squirreljme.jvm.mle.constants.NativeImageLoadParameter;
import cc.squirreljme.jvm.mle.constants.NativeImageLoadType;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.io.MarkableInputStream;
import cc.squirreljme.runtime.cldc.util.StreamUtils;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.microedition.lcdui.Graphics;
import javax.microedition.lcdui.Image;
import net.multiphasicapps.io.DataEndianess;
import net.multiphasicapps.io.ExtendedDataInputStream;

/**
 * This is used to dispatch to the correct parser when loading images.
 *
 * @since 2017/02/28
 */
public class ImageReaderDispatcher
{
	/**
	 * Not used.
	 *
	 * @since 2017/02/28
	 */
	private ImageReaderDispatcher()
	{
	}
	
	/**
	 * Parses the image stream.
	 *
	 * @param __is The stream to read from.
	 * @return The parsed image data.
	 * @throws IOException If it could not be parsed.
	 * @since 2021/12/04
	 */
	public static Image parse(InputStream __is)
		throws IOException, NullPointerException
	{
		// Check
		if (__is == null)
			throw new NullPointerException("NARG");
		
		if (__is.markSupported())
			return ImageReaderDispatcher.__parse(__is);
		return ImageReaderDispatcher.__parse(new MarkableInputStream(__is));
	}
	
	/**
	 * Parses the image stream.
	 *
	 * @param __is The stream to read from.
	 * @return The parsed image data.
	 * @throws IOException If it could not be parsed.
	 */
	private static Image __parse(InputStream __is)
		throws IOException, NullPointerException
	{
		// Check
		if (__is == null)
			throw new NullPointerException("NARG");
		
		// Mark header
		__is.mark(4);
		
		// Read in the header magic number
		int first = (__is.read() & 0xFF);
		int magic = (first << 24) |
			((__is.read() & 0xFF) << 16) |
			((__is.read() & 0xFF) << 8) |
			(__is.read() & 0xFF);
		
		// Reset for future read
		__is.reset();
		
		// Which types of images are supported?
		int nativeLoad = PencilShelf.nativeImageLoadTypes();
		
		// GIF? (GIF8)
		if (magic == 0x47494638)
		{
			if ((nativeLoad & NativeImageLoadType.LOAD_GIF) != 0)
				return ImageReaderDispatcher.__native(
					NativeImageLoadType.LOAD_GIF, __is);
			
			ExtendedDataInputStream in =
				new ExtendedDataInputStream(__is);
			in.setEndianess(DataEndianess.LITTLE);
			
			return new GIFReader(in).parse();
		}
		
		// PNG?
		else if (magic == 0x89504E47)
		{
			if ((nativeLoad & NativeImageLoadType.LOAD_PNG) != 0)
				return ImageReaderDispatcher.__native(
					NativeImageLoadType.LOAD_PNG, __is);
			return new PNGReader(__is).parse();
		}
		
		// JPEG?
		else if ((magic & 0xFFFFFF00) == 0xFFD8FF00)
		{
			if ((nativeLoad & NativeImageLoadType.LOAD_JPEG) != 0)
				return ImageReaderDispatcher.__native(
					NativeImageLoadType.LOAD_JPEG, __is);
			return new JPEGReader(__is).parse();
		}
		
		// SVG?
		else if (first == '<')
			throw new todo.TODO();
		
		// XPM?
		else if (first == '/')
		{
			if ((nativeLoad & NativeImageLoadType.LOAD_XPM) != 0)
				return ImageReaderDispatcher.__native(
					NativeImageLoadType.LOAD_XPM, __is);
			return new XPMReader(__is).parse();
		}
		
		// {@squirreljme.error EB0s Could not detect the image format used
		// specified by the starting byte. (The magic number; The first byte)}
		else
			throw new IOException(String.format("EB0s %08x %02x", magic,
				first));
	}
	
	/**
	 * Handles loading of native images.
	 * 
	 * @param __type The type of image to load.
	 * @param __in The stream to read from.
	 * @return The image that came from the native data.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/12/05
	 */
	private static Image __native(int __type, InputStream __in)
		throws IOException, NullPointerException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		// Read in all the image data
		byte[] rawData = StreamUtils.readAll(__in);
		
		// Do the native image load
		int[] imageData;
		try
		{
			imageData = PencilShelf.nativeImageLoadRGBA(__type,
				rawData, 0, rawData.length);
			
			// {@squirreljme.error EB3r Could not load the native image.}
			if (imageData == null)
				throw new IOException("EB3r");
		}
		
		// {@squirreljme.error EB3q Could not load the native image.}
		catch (MLECallError __e)
		{
			throw new IOException("EB3q", __e);
		}
		
		// Load image parameters
		int numParameters = imageData[0];
		boolean useAlpha = false;
		int imageWidth = -1;
		int imageHeight = -1;
		int scanLength = -1;
		for (int i = 0; i < numParameters; i++)
		{
			int value = imageData[i];
			switch (i)
			{
					// Does nothing but is used above
				case NativeImageLoadParameter.STORED_PARAMETER_COUNT:
					break;
				
				case NativeImageLoadParameter.USE_ALPHA:
					useAlpha = (value != 0);
					break;
				
				case NativeImageLoadParameter.WIDTH:
					imageWidth = value;
					break;
				
				case NativeImageLoadParameter.HEIGHT:
					imageHeight = value;
					break;
				
				case NativeImageLoadParameter.SCAN_LENGTH:
					scanLength = value;
					break;
				
				default:
					Debugging.debugNote("Native image param %d = %d?",
						i, value);
					break;
			}
		}
		
		// {@squirreljme.error EB3s Image has invalid parameters.}
		if (imageWidth <= 0 || imageHeight <= 0)
			throw new IOException("EB3s");
		
		// The scan length needs to at least be the image width
		if (scanLength < imageWidth)
			scanLength = imageWidth;
		
		// Copy image data to a new buffer if the scan is the same size
		if (scanLength == imageWidth)
		{
			int rgbArea = scanLength * imageHeight;
			int[] rgbData = new int[rgbArea];
			System.arraycopy(imageData, numParameters,
				rgbData, 0, rgbArea);
			
			return Image.createRGBImage(rgbData,
				imageWidth, imageHeight, useAlpha);
		}
		
		// Otherwise, we need to unscan it so we can use our native drawing
		// functions for this process
		else
		{
			Image mutImage = Image.createImage(imageWidth, imageHeight,
				useAlpha, 0);
				
			// Draw the RGB data directly onto a mutable image
			Graphics g = mutImage.getGraphics();
			g.setBlendingMode(Graphics.SRC);
			g.drawRGB(imageData, numParameters, scanLength,
				0, 0, imageWidth, imageHeight, useAlpha);
			
			// Make it immutable
			return Image.createImage(mutImage);
		}
	}
}

