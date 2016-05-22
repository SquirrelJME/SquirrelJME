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

/**
 * This interface describes an image reader which is capable of reading RGB
 * image data.
 *
 * @since 2016/05/08
 */
public interface ImageReader
{
	/**
	 * Checks whether this image reader is capable of reading this image type.
	 *
	 * @param __m The mime type or extension of the file.
	 * @return {@code true} if this is capable of reading the given file type.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/10
	 */
	public boolean canRead(String __m)
		throws NullPointerException;
	
	/**
	 * Reads the given input stream and decodes an image from it.
	 *
	 * @param __is The input stream to read image data from.
	 * @return The data of the image.
	 * @throws IOException On read errors.
	 * @throws NullPointerException If no stream was specified.
	 * @since 2016/05/08
	 */
	public abstract ImageData readImage(InputStream __is)
		throws IOException, NullPointerException;
}

