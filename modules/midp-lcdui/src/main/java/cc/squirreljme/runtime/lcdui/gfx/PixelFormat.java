// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.gfx;

import cc.squirreljme.jvm.Framebuffer;
import javax.microedition.lcdui.Graphics;

/**
 * This represents the pixel format that is used.
 *
 * @since 2018/03/24
 */
@Deprecated
public enum PixelFormat
{
	/** Byte Packed Indexed, 1-bit. */
	@Deprecated
	BYTE_INDEXED1,
	
	/** Byte Packed Indexed, 2-bit. */
	@Deprecated
	BYTE_INDEXED2,
	
	/** Byte Packed Indexed, 4-bit. */
	@Deprecated
	BYTE_INDEXED4,
	
	/** Byte Indexed. */
	@Deprecated
	BYTE_INDEXED8,
	
	/** Byte RGB332. */
	@Deprecated
	BYTE_RGB332,
	
	/** Short Indexed. */
	@Deprecated
	SHORT_INDEXED16,
	
	/** Short ARGB4444. */
	@Deprecated
	SHORT_ARGB4444,
	
	/** Short RGB565. */
	@Deprecated
	SHORT_RGB565,
	
	/** Integer ARGB8888. */
	@Deprecated
	INTEGER_ARGB8888,
	
	/** Integer RGB888. */
	@Deprecated
	INTEGER_RGB888,
	
	/** End. */
	;
	
	/**
	 * Creates an array buffer which is capable of containing an image of
	 * the specified size.
	 *
	 * @param __p The buffer pitch.
	 * @param __h The buffer height.
	 * @return The created buffer.
	 * @since 2018/03/28
	 */
	public final Object createBuffer(int __p, int __h)
	{
		// Depends on the format
		int dim = __p * __h;
		switch (this)
		{
			case BYTE_INDEXED1:
				return new byte[dim / 8];
					
			case BYTE_INDEXED2:
				return new byte[dim / 4];
					
			case BYTE_INDEXED4:
				return new byte[dim / 2];
					
			case BYTE_INDEXED8:
			case BYTE_RGB332:
				return new byte[dim];
					
			case SHORT_INDEXED16:
			case SHORT_ARGB4444:
			case SHORT_RGB565:
				return new short[dim];
			
			case INTEGER_ARGB8888:
			case INTEGER_RGB888:
				return new int[dim];
			
				// Unknown
			default:
				throw new todo.OOPS();
		}
	}
	
	/**
	 * Creates the graphics object for drawing graphics, no absolute
	 * translation is used for the graphics.
	 *
	 * @param __buf The buffer to send the result into after drawing.
	 * @param __pal The palette to use for the remote buffer.
	 * @param __bw The buffer width.
	 * @param __bh The buffer height.
	 * @param __alpha Is an alpha channel being used?
	 * @param __pitch The number of elements for the width in the buffer.
	 * @param __offset The offset into the buffer to the actual image data.
	 * @return The graphics object for drawing graphics.
	 * @throws ClassCastException If the class type of the input pixels is not
	 * an array of the expected type.
	 * @throws NullPointerException On null arguments except for {@code __pal}
	 * unless an indexed mode is used.
	 * @since 2018/03/28
	 */
	public final Graphics createGraphics(Object __buf,
		int[] __pal, int __bw, int __bh, boolean __alpha, int __pitch,
		int __offset)
		throws ClassCastException, NullPointerException
	{
		if (__buf == null)
			throw new NullPointerException("NARG");
		
		return this.createGraphics(__buf, __pal, __bw, __bh, __alpha,
			__pitch, __offset, 0, 0);
	}
	
	/**
	 * Creates the graphics object for drawing graphics.
	 *
	 * @param __buf The buffer to send the result into after drawing.
	 * @param __pal The palette to use for the remote buffer.
	 * @param __bw The buffer width.
	 * @param __bh The buffer height.
	 * @param __alpha Is an alpha channel being used?
	 * @param __pitch The number of elements for the width in the buffer.
	 * @param __offset The offset into the buffer to the actual image data.
	 * @param __atx Absolute X translation.
	 * @param __aty Absolute Y translation.
	 * @return The graphics object for drawing graphics.
	 * @throws ClassCastException If the class type of the input pixels is not
	 * an array of the expected type.
	 * @throws NullPointerException On null arguments except for {@code __pal}
	 * unless an indexed mode is used.
	 * @since 2018/03/28
	 */
	public final Graphics createGraphics(Object __buf,
		int[] __pal, int __bw, int __bh, boolean __alpha, int __pitch,
		int __offset, int __atx, int __aty)
		throws ClassCastException, NullPointerException
	{
		if (__buf == null)
			throw new NullPointerException("NARG");
		
		// Initialize graphics according to the pixel type
		switch (this)
		{
			case INTEGER_ARGB8888:
				return new AdvancedGraphics((int[])__buf, true, null,
					__bw, __bh, __pitch, __offset, __atx, __aty);
			
			case INTEGER_RGB888:
				return new AdvancedGraphics((int[])__buf, false, null,
					__bw, __bh, __pitch, __offset, __atx, __aty);
			
			default:
				throw new todo.OOPS(this.toString());
		}
	}
	
	/**
	 * Returns the number of alpha levels.
	 *
	 * @return The number of alpha levels.
	 * @since 2019/05/05
	 */
	public final int numAlphaLevels()
	{
		switch (this)
		{
			case BYTE_INDEXED1:
			case BYTE_INDEXED2:
			case BYTE_INDEXED4:
			case BYTE_INDEXED8:
			case SHORT_INDEXED16:
			case BYTE_RGB332:
			case SHORT_RGB565:
			case INTEGER_RGB888:
				return 0;
				
			case SHORT_ARGB4444:
				return 16;
				
			case INTEGER_ARGB8888:
				return 256;
			
				// Unknown
			default:
				throw new todo.OOPS(this.name());
		}
	}
	
	/**
	 * Returns the number of possible colors.
	 *
	 * @return The number of possible colors.
	 * @since 2019/05/05
	 */
	public final int numColors()
	{
		switch (this)
		{
			case BYTE_INDEXED1:
				return 2;
				
			case BYTE_INDEXED2:
				return 4;
				
			case BYTE_INDEXED4:
				return 16;
				
			case BYTE_INDEXED8:
			case BYTE_RGB332:
				return 256;
				
			case SHORT_ARGB4444:
				return 4096;
				
			case SHORT_INDEXED16:
			case SHORT_RGB565:
				return 65536;
				
			case INTEGER_ARGB8888:
			case INTEGER_RGB888:
				return 16777216;
			
				// Unknown
			default:
				throw new todo.OOPS(this.name());
		}
	}
	
	/**
	 * Maps the pixel format ID to the pixel format type.
	 *
	 * @param __id The input ID.
	 * @return The pixel format for the ID.
	 * @throws IllegalArgumentException If the ID is not valid.
	 * @since 2018/11/18
	 */
	public static final PixelFormat of(int __id)
		throws IllegalArgumentException
	{
		switch (__id)
		{
			case 0:		return PixelFormat.BYTE_INDEXED1;
			case 1:		return PixelFormat.BYTE_INDEXED2;
			case 2:		return PixelFormat.BYTE_INDEXED4;
			case 3:		return PixelFormat.BYTE_INDEXED8;
			case 4:		return PixelFormat.BYTE_RGB332;
			case 5:		return PixelFormat.SHORT_INDEXED16;
			case 6:		return PixelFormat.SHORT_ARGB4444;
			case 7:		return PixelFormat.SHORT_RGB565;
			case 8:		return PixelFormat.INTEGER_ARGB8888;
			case 9:		return PixelFormat.INTEGER_RGB888;
			
				// {@squirreljme.error EB0k Unknown pixel buffer format. (ID)}
			default:
				throw new IllegalArgumentException("EB0k " + __id);
		}
	}
	
	/**
	 * Returns the pixel format used for the native framebuffer.
	 *
	 * @param __id The format ID.
	 * @return The associated pixel format.
	 * @throws IllegalArgumentException If the ID is not valid.
	 * @since 2020/01/12
	 */
	public static final PixelFormat ofFramebuffer(int __id)
		throws IllegalArgumentException
	{
		switch (__id)
		{
			case Framebuffer.FORMAT_INTEGER_RGB888:
				return PixelFormat.INTEGER_RGB888;
			
			case Framebuffer.FORMAT_BYTE_INDEXED:
				return PixelFormat.BYTE_INDEXED8;
			
			case Framebuffer.FORMAT_SHORT_RGB565:
				return PixelFormat.SHORT_RGB565;
			
			case Framebuffer.FORMAT_PACKED_ONE:
				return PixelFormat.BYTE_INDEXED1;
				
			case Framebuffer.FORMAT_PACKED_TWO:
				return PixelFormat.BYTE_INDEXED2;
				
			case Framebuffer.FORMAT_PACKED_FOUR:
				return PixelFormat.BYTE_INDEXED4;
			
				// {@squirreljme.error EB3a Unknown frame buffer pixel buffer
				// format. (ID}}
			default:
				throw new IllegalArgumentException("EB3a " + __id);
		}
	}
}

