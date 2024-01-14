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
import cc.squirreljme.jvm.mle.callbacks.NativeImageLoadCallback;
import cc.squirreljme.jvm.mle.constants.NativeImageLoadType;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.io.MarkableInputStream;
import cc.squirreljme.runtime.cldc.util.StreamUtils;
import java.io.IOException;
import java.io.InputStream;
import net.multiphasicapps.io.DataEndianess;
import net.multiphasicapps.io.ExtendedDataInputStream;

/**
 * This is used to dispatch to the correct parser when loading images.
 *
 * @param <S> Still image type.
 * @since 2017/02/28
 */
public class ImageReaderDispatcher<S>
{
	/** Native image loader. */
	protected final NativeImageLoadCallback loader;
	
	/**
	 * Initializes the base dispatcher.
	 *
	 * @param __native The callback to use for native image load.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/28
	 */
	public ImageReaderDispatcher(NativeImageLoadCallback __native)
		throws NullPointerException
	{
		if (__native == null)
			throw new NullPointerException("NARG");
		
		this.loader = __native;
	}
	
	/**
	 * Parses the image stream.
	 *
	 * @param __is The stream to read from.
	 * @return The parsed image data.
	 * @throws IOException If it could not be parsed.
	 * @since 2021/12/04
	 */
	public S parse(InputStream __is)
		throws IOException, NullPointerException
	{
		// Check
		if (__is == null)
			throw new NullPointerException("NARG");
		
		if (__is.markSupported())
			return this.__parse(__is);
		return this.__parse(new MarkableInputStream(__is));
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
	 * @return The image that came from the native data.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/12/05
	 */
	private S __native(int __type, InputStream __in)
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
				rawData, 0, rawData.length, this.loader);
			
			// Cancelled?
			if (result == null)
				throw new __CancelNativeException__();
		}
		
		/* {@squirreljme.error EB3q Could not load the native image.} */
		catch (MLECallError __e)
		{
			throw new IOException("EB3q", __e);
		}
		
		// Use the resultant decoded image
		return (S)result;
	}
	
	/**
	 * Parses the image stream.
	 *
	 * @param __is The stream to read from.
	 * @return The parsed image data.
	 * @throws IOException If it could not be parsed.
	 */
	private S __parse(InputStream __is)
		throws IOException, NullPointerException
	{
		// Check
		if (__is == null)
			throw new NullPointerException("NARG");
		
		NativeImageLoadCallback loader = this.loader;
		
		// Determine the image type
		int loadType = this.__determineType(__is);
		
		/* {@squirreljme.error EB0k Unsupported image type.} */
		if (loadType == 0 || Integer.bitCount(loadType) != 1)
			throw new IOException("EB0k");
		
		// Native image load is supported for this image type?
		if ((PencilShelf.nativeImageLoadTypes() & loadType) != 0)
			try
			{
				return this.__native(loadType, __is);
			}
			catch (MLECallError __e)
			{
				__e.printStackTrace();
			}
			catch (__CancelNativeException__ ignored)
			{
			}
		
		// Otherwise load it in software
		ImageReader reader;
		switch (loadType)
		{
				// GIF? (GIF8)
			case NativeImageLoadType.LOAD_GIF:
				ExtendedDataInputStream in =
					new ExtendedDataInputStream(__is);
				in.setEndianess(DataEndianess.LITTLE);
				
				reader = new GIFReader(in, loader);
				break;
		
				// PNG?
			case NativeImageLoadType.LOAD_PNG:
				reader = new PNGReader(__is, loader);
				break;
		
				// JPEG?
			case NativeImageLoadType.LOAD_JPEG:
				reader = new JPEGReader(__is, loader);
				break;
		
				// SVG?
			case NativeImageLoadType.LOAD_SVG:
				throw Debugging.todo();
		
				// XPM?
			case NativeImageLoadType.LOAD_XPM:
				reader = new XPMReader(__is, loader);
				break;
			
				/* {@squirreljme.error EB0s Unsupported image format.} */
			default:
				throw new IOException("EB0s");
		}
		
		// Parse image data
		reader.parse();
		
		// Finish loading of the image data
		return (S)loader.finish();
	}
}

