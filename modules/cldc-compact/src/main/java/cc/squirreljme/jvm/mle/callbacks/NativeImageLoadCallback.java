// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.callbacks;

import cc.squirreljme.jvm.mle.PencilShelf;

/**
 * Callback to be used with
 * {@link PencilShelf#nativeImageLoadRGBA(int, byte[], int, int,
 * NativeImageLoadCallback)}, for the purpose of expanding native image loading
 * in the future for various types of images and otherwise.
 *
 * @since 2022/06/28
 */
public interface NativeImageLoadCallback
{
	/**
	 * Adds an image to the image, this may be called multiple times for
	 * animated images.
	 * 
	 * @param __buf The image data buffer.
	 * @param __off The offset into the buffer.
	 * @param __len The length of the buffer.
	 * @param __frameDelay The frame delay, if any.
	 * @param __hasAlpha Does this image have alpha?
	 * @since 2022/06/28
	 */
	void addImage(int[] __buf, int __off, int __len, int __frameDelay,
		boolean __hasAlpha);
	
	/**
	 * Finishes the image and creates the final image.
	 * 
	 * @return The final created image.
	 * @since 2022/06/28
	 */
	Object finish();
	
	/**
	 * Initializes the image.
	 * 
	 * @param __width The width of the image.
	 * @param __height The height of the image.
	 * @param __animated Is this image animated?
	 * @param __scalable Is this image scalable?
	 * @since 2022/06/28
	 */
	void initialize(int __width, int __height, boolean __animated,
		boolean __scalable);
	
	/**
	 * Sets the loop count of the image.
	 * 
	 * @param __loopCount The loop count.
	 * @since 2022/06/28
	 */
	void setLoopCount(int __loopCount);
}
