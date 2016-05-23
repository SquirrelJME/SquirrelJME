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
import net.multiphasicapps.imagereader.ImageData;
import net.multiphasicapps.imagereader.ImageType;

/**
 * This is an internal representation of an image.
 *
 * @since 2016/05/22
 */
public abstract class InternalImage
	extends InternalElement<UIImage>
{
	/**
	 * Initializes the internal image.
	 *
	 * @param __r The reference to the external image.
	 * @since 2016/05/22
	 */
	public InternalImage(Reference<UIImage> __r)
	{
		super(__r);
	}
	
	/**
	 * Creates an internal representation of the image data which either
	 * copies or directly uses the specified {@link ImageData}.
	 *
	 * @param __id The external image data to map.
	 * @return The internal representation of the image data.
	 * @throws UIException If the image could not be mapped.
	 * @since 2016/05/23
	 */
	protected abstract Object internalCreateMapping(ImageData __id)
		throws UIException;
	
	/**
	 * Obtains an internal representation of a {@link ImageData} which is in
	 * the native form required by the display.
	 *
	 * @param <Q> The type of internal image to create.
	 * @param __cl The class of the type.
	 * @param __w The width of the image.
	 * @param __h The height of the image.
	 * @param __t The image type.
	 * @param __cr If {@code true} and the image is missing, it is created from
	 * other images.
	 * @return The closest matching image or {@code null} if it was not
	 * found.
	 * @throws UIException If the image data could not be obtained.
	 * @throws UIGarbageCollectedException If the external image was garbage
	 * collected.
	 * @since 2016/05/23
	 */
	public final <Q> Q internalMapImage(Class<Q> __cl, int __w, int __h,
		ImageType __t, boolean __cr)
		throws UIException, UIGarbageCollectedException
	{
		throw new Error("TODO");
	}
	
	/**
	 * Obtains the image from the external {@link UIImage}.
	 *
	 * @param __w The width of the image.
	 * @param __h The height of the image.
	 * @param __t The image type.
	 * @param __cr If {@code true} and the image is missing, it is created from
	 * other images.
	 * @return The closest matching image or {@code null} if it was not
	 * found.
	 * @throws UIException If the image data could not be obtained.
	 * @throws UIGarbageCollectedException If the external image was garbage
	 * collected.
	 * @since 2016/05/23
	 */
	public final ImageData internalGetImage(int __w, int __h, ImageType __t,
		boolean __cr)
		throws UIException, UIGarbageCollectedException
	{
		throw new Error("TODO");
	}
}

