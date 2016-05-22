// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.imagereader;

import java.io.InputStream;
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ServiceLoader;

/**
 * This is a factory which is used to read images of any given format.
 *
 * @since 2016/05/22
 */
public class ImageReaderFactory
{
	/** The single factory instance. */
	private static volatile Reference<ImageReaderFactory> _INSTANCE;
	
	/** The service loader for image readers. */
	protected final ServiceLoader<ImageReader> readers =
		ServiceLoader.<ImageReader>load(ImageReader.class);
	
	/**
	 * Initializes an instance of the image reader factory.
	 *
	 * @since 2016/05/22
	 */
	public ImageReaderFactory()
	{
	}
	
	/**
	 * Goes through all available image services and finds one which an handle
	 * the given image type.
	 *
	 * @param __t The type of image to read.
	 * @param __is The input stream which contains the image data.
	 * @return The read image or {@code null} if no reader was found.
	 * @throws IOException If the image could not be read.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/22
	 */
	public ImageData readImage(String __t, InputStream __is)
		throws IOException, NullPointerException
	{
		// Check
		if (__t == null || __is == null)
			throw new NullPointerException("NARG");
		
		// Go through all readers
		ServiceLoader<ImageReader> readers = this.readers;
		synchronized (readers)
		{
			for (ImageReader ir : readers)
				if (ir.canRead(__t))
					return ir.readImage(__is);
		}
		
		// No image loaded
		return null;
	}
	
	/**
	 * Returns the instance of the factory.
	 *
	 * @return The instance of a pre-initialized factory.
	 * @since 2016/05/22
	 */
	public static ImageReaderFactory instance()
	{
		Reference<ImageReaderFactory> ref = _INSTANCE;
		ImageReaderFactory rv;
		
		// Create?
		if (ref == null || null == (rv = ref.get()))
			_INSTANCE = new WeakReference<>((rv = new ImageReaderFactory()));
		
		// Return it
		return rv;
	}
}

