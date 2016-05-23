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
 * This is the base class for all image containers.
 *
 * All images use linear RGB, not sRGB.
 *
 * The buffers passed to the image can be obtained and potentially modified.
 *
 * Images are compared to by type, width, and height.
 *
 * @since 2016/05/22
 */
public abstract class ImageData
	implements Comparable<ImageData>
{
	/** The type of image stored here. */
	protected final ImageType type;
	
	/** The image width. */
	protected final int width;
	
	/** The image height. */
	protected final int height;
	
	/** X hotspot position. */
	protected final int hotx;
	
	/** Y hotspot position. */
	protected final int hoty;
	
	/**
	 * Initializes the base image with the given width and height.
	 *
	 * @param __t The type of data stored in the image.
	 * @param __w The image width.
	 * @param __h The image height.
	 * @throws NullPointerException On null arguments.
	 * @throws IllegalArgumentException If the width and or height or zero
	 * or negative.
	 * @since 2016/05/22
	 */
	public ImageData(ImageType __t, int __w, int __h)
		throws IllegalArgumentException, NullPointerException
	{
		this(__t, __w, __h, 0, 0);
	}
	
	/**
	 * Initializes the base image with the given width and height.
	 *
	 * @param __t The type of data stored in the image.
	 * @param __w The image width.
	 * @param __h The image height.
	 * @param __x Hot X position (for cursors).
	 * @param __y Hot Y position (for cursors).
	 * @throws IllegalArgumentException If the width and or height or zero
	 * or negative.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/22
	 */
	public ImageData(ImageType __t, int __w, int __h, int __x, int __y)
		throws IllegalArgumentException, NullPointerException
	{
		// Check
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error BM01 The  (The image width; The image height)}
		if (__w <= 0 || __h <= 0)
			throw new IllegalArgumentException(String.format("BM01 %d %d",
				__w, __h));
		
		// Set
		this.type = __t;
		this.width = __w;
		this.height = __h;
		this.hotx = __x;
		this.hoty = __y;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/23
	 */
	@Override
	public final int compareTo(ImageData __o)
		throws NullPointerException
	{
		// Check
		if (__o == null)
			throw new NullPointerException("NARG");
		
		// Compare the area of the image, so larger images appear first
		int aw = this.width, bw = __o.width;
		int a = aw * this.height,
			b = bw * __o.height;
		if (a < b)
			return -1;
		else if (a > b)
			return 1;
		
		// If the area is the same, smaller widths are first
		if (aw < bw)
			return -1;
		else if (aw > bw)
			return 1;
		
		// Compare the type
		return this.type.compareTo(__o.type);
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
	 * Returns the X hotspot of the image.
	 *
	 * @return The x hotspot.
	 * @since 2016/05/22
	 */
	public final int hotSpotX()
	{
		return this.hotx;
	}
	
	/**
	 * Returns the Y hotspot of the image.
	 *
	 * @return The y hotspot.
	 * @since 2016/05/22
	 */
	public final int hotSpotY()
	{
		return this.hoty;
	}
	
	/**
	 * Returns the type of data the image uses internally.
	 *
	 * @return The image data type.
	 * @since 2016/05/22
	 */
	public ImageType type()
	{
		return this.type;
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

