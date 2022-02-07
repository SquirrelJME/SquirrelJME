// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.gfx.ref;

/**
 * This is the base class for any reference brushes which are used to
 * read and write individual pixels.
 *
 * @since 2022/02/06
 */
public abstract class ReferenceBrush
{
	/** The buffer offset. */
	protected final int offset;
	
	/** The buffer length. */
	protected final int length;
	
	/** The image width. */
	protected final int width;
	
	/** The image height. */
	protected final int height;
	
	/** The scanline length. */
	protected final int scanLen;
	
	/**
	 * Initializes the base brush.
	 * 
	 * @param __offset The offset into the buffer.
	 * @param __length The buffer length.
	 * @param __width The image width.
	 * @param __height The image height.
	 * @param __scanLen The scanline length.
	 * @throws IllegalArgumentException If the image parameters are not
	 * correct.
	 * @since 2022/02/06
	 */
	protected ReferenceBrush(int __offset, int __length, int __width,
		int __height, int __scanLen)
		throws IllegalArgumentException
	{
		// {@squirreljme.error EB2z }
		if (__offset < 0 || __length < 0 || __width <= 1 || __height <= 1 ||
			__scanLen < __width)
			throw new IllegalArgumentException("EB2z");
		
		// {@squirreljme.error EB2y Buffer cannot fit the image.}
		if (__offset + (__height * __scanLen) > __length)
			throw new IllegalArgumentException("EB2y");
		
		this.offset = __offset;
		this.length = __length;
		this.width = __width;
		this.height = __height;
		this.scanLen = __scanLen;
	}
	
	/**
	 * Returns the color at the given index.
	 * 
	 * @param __i The index.
	 * @return The color at the given index.
	 * @since 2022/02/06
	 */
	public abstract int getPixel(int __i);
	
	/**
	 * Sets the color at the given index.
	 * 
	 * @param __i The index.
	 * @param __color The color to set.
	 * @since 2022/02/06
	 */
	public abstract void setPixel(int __i, int __color);
	
	/**
	 * Returns the color at the given pixel.
	 * 
	 * @param __x The X coordinate.
	 * @param __y The Y coordinate.
	 * @return The color at the given pixel.
	 * @since 2022/02/06
	 */
	public final int getPixel(int __x, int __y)
	{
		return this.getPixel(this.projectPixel(__x, __y));
	}
	
	/**
	 * Projects the pixel onto the linear buffer.
	 * 
	 * @param __x The X coordinate.
	 * @param __y The Y coordinate.
	 * @return The index of the pixel.
	 * @throws IndexOutOfBoundsException If the pixel is out of bounds.
	 * @since 2022/02/06
	 */
	public final int projectPixel(int __x, int __y)
		throws IndexOutOfBoundsException
	{
		// {@squirreljme.error EB2x Pixel is outside image bounds.}
		if (__x < 0 || __y < 0 || __x >= this.width || __y >= this.height)
			throw new IndexOutOfBoundsException("EB2x");
		
		return this.offset + (__y * this.scanLen) + __x;
	}
	
	/**
	 * Sets the color at the given pixel.
	 * 
	 * @param __x The X coordinate.
	 * @param __y The Y coordinate.
	 * @param __color The color to set.
	 * @since 2022/02/06
	 */
	public final void setPixel(int __x, int __y, int __color)
	{
		this.setPixel(this.projectPixel(__x, __y), __color);
	}
}
