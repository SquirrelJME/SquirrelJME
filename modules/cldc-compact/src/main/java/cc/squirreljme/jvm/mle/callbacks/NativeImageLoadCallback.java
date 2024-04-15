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
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;

/**
 * Callback to be used with
 * {@link PencilShelf#nativeImageLoadRGBA(int, byte[], int, int,
 * NativeImageLoadCallback)}, for the purpose of expanding native image loading
 * in the future for various types of images and otherwise.
 *
 * @since 2022/06/28
 */
@SquirrelJMEVendorApi
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
	@SquirrelJMEVendorApi
	void addImage(int[] __buf, int __off, int __len, int __frameDelay,
		boolean __hasAlpha);
	
	/**
	 * Cancel native image loading, this is used if a specific kind of image
	 * of a given type that is supported is not supported to be read in
	 * natively.
	 *
	 * @since 2024/01/14
	 */
	@SquirrelJMEVendorApi
	void cancel();
	
	/**
	 * Finishes the image and creates the final image.
	 * 
	 * @return The final created image, if cancelled this should return
	 * {@code null}.
	 * @since 2022/06/28
	 */
	@SquirrelJMEVendorApi
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
	@SquirrelJMEVendorApi
	void initialize(int __width, int __height, boolean __animated,
		boolean __scalable);
	
	/**
	 * Sets the loop count of the image.
	 * 
	 * @param __loopCount The loop count.
	 * @since 2022/06/28
	 */
	@SquirrelJMEVendorApi
	void setLoopCount(int __loopCount);
	
	/**
	 * Sets the palette of the next resultant image, if it is needed.
	 *
	 * @param __colors The colors for the palette.
	 * @param __off The offset into the buffer.
	 * @param __len The length of the buffer.
	 * @param __hasAlpha Is there an alpha channel?
	 * @param __transDx The transparent color index.
	 * @return If any subsequent calls should use the indexed variant of
	 * adding an image.
	 * @since 2024/01/14
	 */
	@SquirrelJMEVendorApi
	boolean setPalette(int[] __colors, int __off, int __len,
		boolean __hasAlpha, int __transDx);
}
