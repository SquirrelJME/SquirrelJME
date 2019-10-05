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
	/** Returns the address of the framebuffer. */
	public static final byte ADDRESS =
		1;
	
	/** Returns the width of the framebuffer. */
	public static final byte WIDTH =
		2;
	
	/** Returns the height of the framebuffer. */
	public static final byte HEIGHT =
		3;
	
	/** Returns the scanline length. */
	public static final byte SCANLEN =
		4;
	
	/** Flush the display because it has been drawn. */
	public static final byte FLUSH =
		5;
	
	/** Returns the pixel format of the screen. */
	public static final byte FORMAT =
		6;
	
	/** Returns the scanline length in bytes. */
	public static final byte SCANLEN_BYTES =
		7;
	
	/** Returns the number of bytes per pixel. */
	public static final byte BYTES_PER_PIXEL =
		8;
	
	/** Returns the number of available pixels. */
	public static final byte NUM_PIXELS =
		9;
	
	/** Bits per pixel. */
	public static final byte BITS_PER_PIXEL =
		10;
	
	/** Get backlight level. */
	public static final byte BACKLIGHT_LEVEL_GET =
		11;
	
	/** Set backlight level. */
	public static final byte BACKLIGHT_LEVEL_SET =
		12;
	
	/** Maximum backlight level. */
	public static final byte BACKLIGHT_LEVEL_MAX =
		13;
	
	/** The number of properties. */
	public static final byte NUM_PROPERTIES =
		14;
	
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
}

