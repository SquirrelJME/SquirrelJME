// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.lcdui.image.ImageFactory;

/**
 * Factory for the creation of resultant images.
 *
 * @since 2022/02/10
 */
final class __ImageFactory__
	implements ImageFactory<AnimatedImage, Image>
{
	/**
	 * {@inheritDoc}
	 * @since 2022/02/10
	 */
	@Override
	public AnimatedImage animatedImage(Image[] __images, int[] __frameTime,
		int __loopCount)
		throws IllegalArgumentException, NullPointerException
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/02/10
	 */
	@Override
	public Image stillImage(int[] __b, int __o, int __l, boolean __mut,
		boolean __alpha, int __w, int __h)
		throws IllegalArgumentException, IndexOutOfBoundsException,
			NullPointerException
	{
		return new Image(__b, __o, __l, __w, __h, __mut, __alpha);
	}
}
