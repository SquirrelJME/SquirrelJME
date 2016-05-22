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

/**
 * This class is used to store the actual data of the image.
 *
 * @since 2016/05/22
 */
public abstract class ImageData
{
	/** The image width. */
	protected final int width;
	
	/** The image height. */
	protected final int height;
	
	/**
	 * Initializes the base image with the given width and height.
	 *
	 * @param __w The image width.
	 * @param __h The image height.
	 * @throws IllegalArgumentException If the width and or height or zero
	 * or negative.
	 * @since 2016/05/22
	 */
	public ImageData(int __w, int __h)
		throws IllegalArgumentException
	{
		// {@squirreljme.error BM01 The  (The image width; The image height)}
		if (__w <= 0 || __h <= 0)
			throw new IllegalArgumentException(String.format("BM01 %d %d",
				__w, __h));
		
		// Set
		this.width = __w;
		this.height = __h;
	}
	
	/**
	 * Returns the height of the image.
	 *
	 * @return The image height.
	 * @since 2016/05/22
	 */
	public final int height()
	{
		return this.height;
	}
	
	/**
	 * Returns the type of data the image uses internally.
	 *
	 * @return The image data type.
	 * @since 2016/05/22
	 */
	public ImageType type()
	{
		throw new Error("TODO");
	}
	
	/**
	 * Returns the width of the image.
	 *
	 * @return The image width.
	 * @since 2016/05/22
	 */
	public final int width()
	{
		return this.width;
	}
}

