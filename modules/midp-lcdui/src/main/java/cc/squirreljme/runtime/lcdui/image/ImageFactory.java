// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.image;

import javax.microedition.lcdui.AnimatedImage;
import javax.microedition.lcdui.Image;

/**
 * This factory is used for creating images.
 *
 * @param <A> The animated image type.
 * @param <S> The still image type.
 * @since 2022/02/10
 */
public interface ImageFactory<A extends S, S>
{
	/**
	 * Initializes a new animated image.
	 * 
	 * @param __images The images to put into the frame.
	 * @param __frameTime The frame time of the image.
	 * @param __loopCount The number of times to loop.
	 * @return The resultant animated image.
	 * @throws IllegalArgumentException if the images set and frame time set
	 * are not the same length.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/02/10
	 */
	A animatedImage(S[] __images,
		int[] __frameTime, int __loopCount)
		throws IllegalArgumentException, NullPointerException;
	
	/**
	 * Initializes a basic still image.
	 * 
	 * @param __b The buffer data, this should be used directly regardless if
	 * the image is mutable or not so that there is no copy of a buffer on
	 * initialization.
	 * @param __o The offset into the buffer.
	 * @param __l The length of the data to use.
	 * @param __mut Is this mutable?
	 * @param __alpha Is alpha used?
	 * @param __w The image width.
	 * @param __h The image height.
	 * @return The resultant still image.
	 * @throws IllegalArgumentException If an image dimension is zero or
	 * negative.
	 * @throws IndexOutOfBoundsException if the offset and/or length exceed
	 * the buffer bounds.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/02/10
	 */
	S stillImage(int[] __b, int __o, int __l,
		boolean __mut, boolean __alpha, int __w, int __h)
		throws IllegalArgumentException, IndexOutOfBoundsException,
			NullPointerException;
}
