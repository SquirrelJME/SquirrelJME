// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.gfx;

import javax.microedition.lcdui.Graphics;

/**
 * This represents the pixel format that is used.
 *
 * @since 2018/03/24
 */
public enum PixelFormat
{
	/** Byte Packed Indexed, 1-bit. */
	BYTE_INDEXED1,
	
	/** Byte Packed Indexed, 2-bit. */
	BYTE_INDEXED2,
	
	/** Byte Packed Indexed, 4-bit. */
	BYTE_INDEXED4,
	
	/** Byte Indexed. */
	BYTE_INDEXED8,
	
	/** Byte RGB332. */
	BYTE_RGB332,
	
	/** Short Indexed. */
	SHORT_INDEXED16,
	
	/** Short ARGB4444. */
	SHORT_ARGB4444,
	
	/** Short RGB565. */
	SHORT_RGB565,
	
	/** Integer ARGB8888. */
	INTEGER_ARGB8888,
	
	/** Integer RGB888. */
	INTEGER_RGB888,
	
	/** End. */
	;
	
	/**
	 * Creates an array buffer which is capable of containing an image of
	 * the specified size.
	 *
	 * @param __p The buffer pitch.
	 * @param __h The buffer height.
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
			case 0:		return BYTE_INDEXED1;
			case 1:		return BYTE_INDEXED2;
			case 2:		return BYTE_INDEXED4;
			case 3:		return BYTE_INDEXED8;
			case 4:		return BYTE_RGB332;
			case 5:		return SHORT_INDEXED16;
			case 6:		return SHORT_ARGB4444;
			case 7:		return SHORT_RGB565;
			case 8:		return INTEGER_ARGB8888;
			case 9:		return INTEGER_RGB888;
			
				// {@squirreljme.error EB2b Unknown pixel buffer format.}
			default:
				throw new IllegalArgumentException("EB2b " + __id);
		}
	}
}

