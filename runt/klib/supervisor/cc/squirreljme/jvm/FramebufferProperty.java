// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm;

/**
 * This is used to get/set the property of the framebuffer.
 *
 * @since 2019/06/20
 */
public interface FramebufferProperty
{
	/**
	 * Returns the address of the framebuffer.
	 *
	 * @return The framebuffer address.
	 */
	public static final byte ADDRESS =
		1;
	
	/**
	 * Returns the width of the framebuffer.
	 *
	 * @return The framebuffer width.
	 */
	public static final byte WIDTH =
		2;
	
	/**
	 * Returns the height of the framebuffer.
	 *
	 * @return The framebuffer height.
	 */
	public static final byte HEIGHT =
		3;
	
	/**
	 * Returns the scanline length.
	 *
	 * @return The framebuffer scanline length.
	 */
	public static final byte SCANLEN =
		4;
	
	/**
	 * Flush the display because it has been drawn.
	 */
	public static final byte FLUSH =
		5;
	
	/**
	 * Returns the pixel format of the screen.
	 *
	 * @return The pixel format of the screen.
	 */
	public static final byte FORMAT =
		6;
	
	/**
	 * Returns the scanline length in bytes.
	 *
	 * @return The scanline length in bytes.
	 */
	public static final byte SCANLEN_BYTES =
		7;
	
	/**
	 * Returns the number of bytes per pixel.
	 *
	 * @return The bytes per pixel.
	 */
	public static final byte BYTES_PER_PIXEL =
		8;
	
	/**
	 * Returns the number of available pixels.
	 *
	 * @return The number of pixels.
	 */
	public static final byte NUM_PIXELS =
		9;
	
	/**
	 * Bits per pixel.
	 *
	 * @return The bits per pixel.
	 */
	public static final byte BITS_PER_PIXEL =
		10;
	
	/**
	 * Get backlight level.
	 *
	 * @return The current backlight level.
	 */
	public static final byte BACKLIGHT_LEVEL_GET =
		11;
	
	/**
	 * Set backlight level.
	 *
	 * @param 1 The level to set.
	 */
	public static final byte BACKLIGHT_LEVEL_SET =
		12;
	
	/**
	 * Maximum backlight level.
	 *
	 * @return The maximum backlight level.
	 */
	public static final byte BACKLIGHT_LEVEL_MAX =
		13;
	
	/**
	 * Uploads an integer array of pixel data to the framebuffer.
	 *
	 * @param 1 The address of the array to upload.
	 * @since 2019/12/21
	 */
	public static final byte UPLOAD_ARRAY_INT =
		14;
	
	/**
	 * The array which backs the framebuffer, if there is one.
	 *
	 * @return The backing array object, if there is one.
	 * @since 2019/12/28
	 */
	public static final byte BACKING_ARRAY_OBJECT =
		15;
	
	/** The number of framebuffer properties. */
	public static final byte NUM_PROPERTIES =
		16;
	
	/** Screen is RGB 32-bit. */
	public static final byte FORMAT_INTEGER_RGB888 =
		0;
	
	/** Screen is 8-bit indexed. */
	public static final byte FORMAT_BYTE_INDEXED =
		1;
	
	/** Screen is 16-bit RGB565. */
	public static final byte FORMAT_SHORT_RGB565 =
		2;
	
	/** Screen is packed 1 bit values. */
	public static final byte FORMAT_PACKED_ONE =
		3;
	
	/** Screen is packed 2 bit values. */
	public static final byte FORMAT_PACKED_TWO =
		4;
	
	/** Screen is packed 4 bit values. */
	public static final byte FORMAT_PACKED_FOUR =
		5;
	
	/** The IPC ID for the graphics callbacks. */
	public static final int IPC_ID =
		0x47665821;
}

