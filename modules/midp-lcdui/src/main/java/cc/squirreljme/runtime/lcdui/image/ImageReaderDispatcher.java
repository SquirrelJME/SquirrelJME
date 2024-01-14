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
import cc.squirreljme.jvm.mle.constants.NativeImageLoadType;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.io.MarkableInputStream;
import cc.squirreljme.runtime.cldc.util.StreamUtils;
import java.io.IOException;
import java.io.InputStream;
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
	 * Initializes the base dispatcher.
	 *
	 * @since 2017/02/28
	 */
	public ImageReaderDispatcher()
	{
	}
	
	/**
	 * Parses the image stream.
	 *
	 * @param __is The stream to read from.
	 * @param __factory The factory used to create the final image.
	 * @return The parsed image data.
	 * @throws IOException If it could not be parsed.
	 * @since 2021/12/04
	 */
	public Image parse(InputStream __is, ImageFactory __factory)
		throws IOException, NullPointerException
	{
		// Check
		if (__is == null || __factory == null)
			throw new NullPointerException("NARG");
		
		if (__is.markSupported())
			return this.__parse(__is, __factory);
		return this.__parse(new MarkableInputStream(__is), __factory);
	}
	
	/**
	 * Determines the type of image this is.
	 * 
	 * @param __is The stream to check.
	 * @return The type of image that is here.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/02/10
	 */
	private int __determineType(InputStream __is)
		throws IOException, NullPointerException
	{
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
		
		// GIF? (GIF8)
		if (magic == 0x47494638)
			return NativeImageLoadType.LOAD_GIF;
		
		// PNG?
		else if (magic == 0x89504E47)
			return NativeImageLoadType.LOAD_PNG;
		
		// JPEG?
		else if ((magic & 0xFFFFFF00) == 0xFFD8FF00)
			return NativeImageLoadType.LOAD_JPEG;
		
		// SVG?
		else if (first == '<')
			return NativeImageLoadType.LOAD_SVG;
		
		// XPM?
		else if (first == '/')
			return NativeImageLoadType.LOAD_XPM;
		
		// Unknown
		return 0;
	}
	
	/**
	 * Handles loading of native images.
	 *
	 * @param __type The type of image to load.
	 * @param __in The stream to read from.
	 * @param __factory The factory used for image creation.
	 * @return The image that came from the native data.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/12/05
	 */
	private Image __native(int __type, InputStream __in,
		ImageFactory __factory)
		throws IOException, NullPointerException
	{
		if (__in == null)
			throw new NullPointerException("NARG");
		
		// Read in all the image data
		byte[] rawData = StreamUtils.readAll(__in);
		
		// Do the native image load
		Object result;
		try
		{
			result = PencilShelf.nativeImageLoadRGBA(__type,
				rawData, 0, rawData.length,
				new __NativeLoadHandler__(__factory));
			
			// Cancelled?
			if (result == null)
				throw new __CancelNativeException__();
			
			/* {@squirreljme.error EB3r Could not load the native image.} */
			if (!(result instanceof Image))
				throw new IOException("EB3r");
		}
		
		/* {@squirreljme.error EB3q Could not load the native image.} */
		catch (MLECallError __e)
		{
			throw new IOException("EB3q", __e);
		}
		
		// Use the resultant decoded image
		return (Image)result;
	}
	
	/**
	 * Parses the image stream.
	 *
	 * @param __is The stream to read from.
	 * @param __factory The factory used for creating images.
	 * @return The parsed image data.
	 * @throws IOException If it could not be parsed.
	 */
	private Image __parse(InputStream __is, ImageFactory __factory)
		throws IOException, NullPointerException
	{
		// Check
		if (__is == null || __factory == null)
			throw new NullPointerException("NARG");
		
		// Determine the image type
		int loadType = this.__determineType(__is);
		
		/* {@squirreljme.error EB0k Unsupported image type.} */
		if (loadType == 0 || Integer.bitCount(loadType) != 1)
			throw new IOException("EB0k");
		
		// Native image load is supported for this image type?
		if ((PencilShelf.nativeImageLoadTypes() & loadType) != 0)
			try
			{
				return this.__native(loadType, __is, __factory);
			}
			catch (MLECallError __e)
			{
				__e.printStackTrace();
			}
			catch (__CancelNativeException__ ignored)
			{
			}
		
		switch (loadType)
		{
				// GIF? (GIF8)
			case NativeImageLoadType.LOAD_GIF:
				ExtendedDataInputStream in =
					new ExtendedDataInputStream(__is);
				in.setEndianess(DataEndianess.LITTLE);
				
				return new GIFReader(in, __factory).parse();
		
				// PNG?
			case NativeImageLoadType.LOAD_PNG:
				return new PNGReader(__is, __factory).parse();
		
				// JPEG?
			case NativeImageLoadType.LOAD_JPEG:
				return new JPEGReader(__is).parse();
		
				// SVG?
			case NativeImageLoadType.LOAD_SVG:
				throw Debugging.todo();
		
				// XPM?
			case NativeImageLoadType.LOAD_XPM:
				return new XPMReader(__is, __factory).parse();
			
				/* {@squirreljme.error EB0s Unsupported image format.} */
			default:
				throw new IOException("EB0s");
		}
	}
}

