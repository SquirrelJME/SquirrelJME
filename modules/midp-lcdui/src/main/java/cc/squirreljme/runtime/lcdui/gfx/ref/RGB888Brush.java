// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.gfx.ref;

/**
 * This is a brush for RGB888 graphics.
 *
 * @since 2022/02/08
 */
public class RGB888Brush
	extends ReferenceBrush
{
	/** The pixels used. */
	protected final int[] pixels;
	
	/**
	 * Initializes the brush.
	 *
	 * @param __pixels The pixel data.
	 * @param __offset The offset into the buffer.
	 * @param __length The buffer length.
	 * @param __width The image width.
	 * @param __height The image height.
	 * @param __scanLen The scanline length.
	 * @param __sx Window X start.
	 * @param __sy Window Y start.
	 * @param __sw Window width.
	 * @param __sh Window height.
	 * @throws IllegalArgumentException If the image parameters are not
	 * correct.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/02/08
	 */
	public RGB888Brush(int[] __pixels, int __offset, int __length, int __width,
		int __height, int __scanLen,
		int __sx, int __sy, int __sw, int __sh)
		throws IllegalArgumentException, NullPointerException
	{
		super(__offset, __length, __width,
			__height, __scanLen,
			__sx, __sy, __sw,__sh);
		
		if (__pixels == null)
			throw new NullPointerException("NARG");
		
		this.pixels = __pixels;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/02/08
	 */
	@Override
	public int getPixel(int __i)
	{
		return this.pixels[__i] | 0xFF_000000;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/02/08
	 */
	@Override
	public void setPixel(int __i, int __color)
	{
		this.pixels[__i] = __color | 0xFF_000000;
	}
}
