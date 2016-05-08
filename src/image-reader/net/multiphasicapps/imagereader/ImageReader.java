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
	 * Reads the given input stream and decodes an image from it.
	 *
	 * @param __is The input stream to read image data from.
	 * @param __dim The output dimensions of the image.
	 * @return The RGB data of the image.
	 * @throws IllegalArgumentException If the number of elements in the array
	 * is less than the dimensions of the image, extra values must be
	 * initialized to {@code -1}.
	 * @throws IOException On read errors.
	 * @throws NullPointerException If no stream was specified.
	 * @since 2016/05/08
	 */
	public abstract int[] readImage(InputStream __is, int[] __dim)
		throws IllegalArgumentException, IOException, NullPointerException;
}

