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
public final class UIImage
	extends UIElement
{
	/** The available concrete images. */
	protected final List<ImageData> images =
		new LinkedList<>();
	
	/**
	 * Initializes the image.
	 *
	 * @param __dm The owning display manager.
	 * @since 2016/05/22
	 */
	UIImage(UIDisplayManager __dm)
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
}

