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
 * This is an image which is backed by a {@code int[]}, this is generally the
 * fastest image type to use on most systems because modern hardware uses
 * 32-bit image data.
 *
 * @since 2016/05/23
 */
public class ImageDataInt
	extends ImageData
{
	/** The data buffer for the image. */
	protected final int[] buffer;
	
	/**
	 * Initializes the integer based image.
	 *
	 * @param __buf The buffer to use to store the image data, if {@code null}
	 * then a blank one is used. The input buffer is used directly and is not
	 * copied.
	 * @param __t The type of image to store.
	 * @param __w The image width.
	 * @param __h The image height.
	 * @throws IllegalArgumentException If the input buffer is not correctly
	 * sized for the image, or the type is not valid for integer array backed
	 * images.
	 * @since 2016/05/23
	 */
	public ImageDataInt(int[] __buf, ImageType __t, int __w, int __h)
		throws IllegalArgumentException
	{
		this(__buf, __t, __w, __h, 0, 0);
	}
	
	/**
	 * Initializes the integer based image.
	 *
	 * @param __buf The buffer to use to store the image data, if {@code null}
	 * then a blank one is used. The input buffer is used directly and is not
	 * copied.
	 * @param __t The type of image to store.
	 * @param __w The image width.
	 * @param __h The image height.
	 * @param __x The X hotspot.
	 * @param __y The Y hotspot.
	 * @throws IllegalArgumentException If the input buffer is not correctly
	 * sized for the image, or the type is not valid for integer array backed
	 * images.
	 * @since 2016/05/23
	 */
	public ImageDataInt(int[] __buf, ImageType __t, int __w, int __h, int __x,
		int __y)
		throws IllegalArgumentException
	{
		super(__t, __w, __h, __x, __y);
		
		// {@squirreljme.error BM03 The specified image type is not compatible
		// with integer based data. (The image type)}
		if (!this.type._prim.equals(int.class))
			throw new IllegalArgumentException(String.format("BM03 %s", __t));
		
		// Create blank image?
		int area = __w * __h;
		if (__buf == null)
			this.buffer = new int[area];
		
		// Use pre-existing
		else
		{
			// {@squirreljme.error BM02 The surface area of the input buffer
			// does not match the surface area of the requested image.
			// (The array length; The expected length)}
			if (__buf.length != area)
				throw new IllegalArgumentException(String.format("BM02 %d %d",
					__buf.length, area));
			
			// Set
			this.buffer = __buf;
		}
	}
	
	/**
	 * Returns the linear ARGB color at the given coordinate.
	 *
	 * @param __x The X coordinate.
	 * @param __y The Y coordinate.
	 * @return The linear ARGB value at this coordinate.
	 * @throws IndexOutOfBoundsException If the pixel is out of bounds.
	 * @since 2016/05/23
	 */
	public int atARGB(int __x, int __y)
		throws IndexOutOfBoundsException
	{
		// {@squirreljme.error BM04 Pixel out of image bounds.}
		int w = this.width;
		if (__x < 0 || __y < 0 || __x >= w || __y >= this.height)
			throw new IndexOutOfBoundsException("BM04");
		
		// Calculate the color here
		return this.type.__atARGB(this.buffer, (__y * w) + __x);
	}
}

