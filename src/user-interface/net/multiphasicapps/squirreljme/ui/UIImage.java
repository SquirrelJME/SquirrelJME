// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.ui;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.ListIterator;
import java.util.LinkedList;
import net.multiphasicapps.imagereader.ImageData;
import net.multiphasicapps.imagereader.ImageType;

/**
 * This is an image which may be shown by the user interface. The image may
 * come in many formats and internally is usually backed by a buffer containing
 * the image data. Due to the wide range of systems supported by SquirrelJME
 * an input image may be associated with multiple actual images which represent
 * the size of the image along with specific color details. The internal
 * representation may choose an image of a specific type to be displayed. For
 * example on low resolution systems, a 16x16 image may be chosen while on
 * a system which a much higher pixel density might choose one that is of a
 * higher resolution.
 *
 * If an image is requested and it is not available then it may be created from
 * basic input images and such.
 *
 * @since 2016/05/22
 */
public class UIImage
	extends UIBase
{
	/** The available concrete images. */
	protected final List<ImageData> images =
		new LinkedList<>();
	
	/** Virtually created images (if creation was desired). */
	private final List<Reference<ImageData>> _virt =
		new LinkedList<>();
	
	/**
	 * Initializes the image.
	 *
	 * @param __dm The owning display manager.
	 * @since 2016/05/22
	 */
	UIImage(UIManager __dm)
	{
		super(__dm);
	}
	
	/**
	 * Adds the specified image to be referenced by this image.
	 *
	 * @param __id The image data to reference.
	 * @throws NullPointerException On null arguments.
	 * @throws UIException If the image could not be added.
	 * @since 2016/05/22
	 */
	public void addImageData(ImageData __id)
		throws NullPointerException, UIException
	{
		// Check
		if (__id == null)
			throw new NullPointerException("NARG");
		
		// Get image details
		int w = __id.width();
		int h = __id.height();
		ImageType t = __id.type();
		
		// Lock
		synchronized (this.lock)
		{
			// Make iterator
			List<ImageData> images = this.images;
			ListIterator<ImageData> it = images.listIterator();
			
			// Go through all images to locate the position
			while (it.hasNext())
			{
				// Get the next image
				ImageData id = it.next();
				
				// Determine if it is to be placed here
				int comp = __id.compareTo(id);
				if (comp <= 0)
				{
					// Place before the current marker
					if (comp < 0)
						it.previous();
					
					it.add(__id);
					return;
				}
			}
			
			// Add to the end otherwise
			it.add(__id);
		}
	}
	
	/**
	 * Obtains an image which matches the specified width, height, and type
	 * of color information. Optionally if one is not found, it may be created.
	 *
	 * @param __w The width of the image.
	 * @param __h The height of the image.
	 * @param __t The type of image to obtain.
	 * @param __cr If {@code true}, an image is created which matches the
	 * associated information.
	 * @return The associated image data or {@code null} if it was not found
	 * and {@code __cr} was {@code false}.
	 * @throws UIException If an image could not be obtained.
	 * @since 2016/05/23
	 */
	public ImageData getImage(int __w, int __h, ImageType __t, boolean __cr)
		throws UIException
	{
		// Negative or zero width/height will never be found
		if (__w <= 0 || __h <= 0)
			return null;
		
		// Lock
		synchronized (this.lock)
		{
			// Make iterator
			List<ImageData> images = this.images;
			ListIterator<ImageData> it = images.listIterator();
			
			// Find the matching image
			while (it.hasNext())
			{
				ImageData rv = it.next();
				
				// Exact match?
				if (__w == rv.width() && __h == rv.height() &&
					__t == rv.type())
					return rv;
			}
			
			// Not creating? stop
			if (!__cr)
				return null;
			
			throw new Error("TODO");
		}
	}
}

