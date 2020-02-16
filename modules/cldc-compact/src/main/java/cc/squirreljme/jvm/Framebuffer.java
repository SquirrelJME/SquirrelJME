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
public interface Framebuffer
{
	/**
	 * Returns the address of the framebuffer.
	 *
	 * @squirreljme.syscallreturn The framebuffer address.
	 */
	public static final byte CONTROL_ADDRESS =
		1;
	
	/**
	 * Returns the width of the framebuffer.
	 *
	 * @squirreljme.syscallreturn The framebuffer width.
	 */
	public static final byte CONTROL_WIDTH =
		2;
	
	/**
	 * Returns the height of the framebuffer.
	 *
	 * @squirreljme.syscallreturn The framebuffer height.
	 */
	public static final byte CONTROL_HEIGHT =
		3;
	
	/**
	 * Returns the scanline length.
	 *
	 * @squirreljme.syscallreturn The framebuffer scanline length.
	 */
	public static final byte CONTROL_SCANLEN =
		4;
	
	/**
	 * Flush the display because it has been drawn.
	 */
	public static final byte CONTROL_FLUSH =
		5;
	
	/**
	 * Returns the pixel format of the screen.
	 *
	 * @squirreljme.syscallreturn The pixel format of the screen.
	 */
	public static final byte CONTROL_FORMAT =
		6;
	
	/**
	 * Returns the scanline length in bytes.
	 *
	 * @squirreljme.syscallreturn The scanline length in bytes.
	 */
	public static final byte CONTROL_SCANLEN_BYTES =
		7;
	
	/**
	 * Returns the number of bytes per pixel.
	 *
	 * @squirreljme.syscallreturn The bytes per pixel.
	 */
	public static final byte CONTROL_BYTES_PER_PIXEL =
		8;
	
	/**
	 * Returns the number of available pixels.
	 *
	 * @squirreljme.syscallreturn The number of pixels.
	 */
	public static final byte CONTROL_NUM_PIXELS =
		9;
	
	/**
	 * Bits per pixel.
	 *
	 * @squirreljme.syscallreturn The bits per pixel.
	 */
	public static final byte CONTROL_BITS_PER_PIXEL =
		10;
	
	/**
	 * Get backlight level.
	 *
	 * @squirreljme.syscallreturn The current backlight level.
	 */
	public static final byte CONTROL_BACKLIGHT_LEVEL_GET =
		11;
	
	/**
	 * Set backlight level.
	 *
	 * @squirreljme.syscallparam 1 The level to set.
	 */
	public static final byte CONTROL_BACKLIGHT_LEVEL_SET =
		12;
	
	/**
	 * Maximum backlight level.
	 *
	 * @squirreljme.syscallreturn The maximum backlight level.
	 */
	public static final byte CONTROL_BACKLIGHT_LEVEL_MAX =
		13;
	
	/**
	 * Uploads an integer array of pixel data to the framebuffer.
	 *
	 * @squirreljme.syscallparam 1 The address of the array to upload.
	 * @since 2019/12/21
	 */
	public static final byte CONTROL_UPLOAD_ARRAY_INT =
		14;
	
	/**
	 * The array which backs the framebuffer, if there is one.
	 *
	 * @squirreljme.syscallreturn The backing array object, if there is one.
	 * @since 2019/12/28
	 */
	public static final byte CONTROL_BACKING_ARRAY_OBJECT =
		15;
		
	/**
	 * Returns the capabilities of the display.
	 *
	 * @squirreljme.syscallreturn The display capabilities.
	 * @since 2020/01/10
	 */
	public static final byte CONTROL_GET_CAPABILITIES =
		16;
	
	/**
	 * Query acceleration function.
	 *
	 * @squirreljme.syscallparam 1 The graphics function.
	 * @squirreljme.syscallreturn A non-zero value if this is supported.
	 * @since 2020/01/10
	 */
	public static final byte CONTROL_ACCEL_FUNC_QUERY =
		17;
	
	/**
	 * Perform acceleration function.
	 *
	 * @squirreljme.syscallparam 1 The graphics function.
	 * @squirreljme.syscallparam ... Parameters to the function.
	 * @squirreljme.syscallreturn A value that is according to the invoked function, if it is
	 * supported or possible.
	 * @since 2020/01/10
	 */
	public static final byte CONTROL_ACCEL_FUNC_INVOKE =
		18;
	
	/**
	 * Requests that the framebuffer be repainted.
	 *
	 * @squirreljme.syscallparam 1 The X coordinate.
	 * @squirreljme.syscallparam 2 The Y coordinate.
	 * @squirreljme.syscallparam 3 The width.
	 * @squirreljme.syscallparam 4 The height.
	 * @squirreljme.syscallreturn Returns {@code 0} if the repaint was not queued and it must be
	 * handled by the code running the application, 
	 * @since 2020/01/15
	 */
	public static final byte CONTROL_REPAINT_REQUEST =
		19;
	
	/**
	 * Sets the title of the framebuffer if applicable.
	 *
	 * @squirreljme.syscallparam 1 Character array buffer pointer.
	 * @since 2020/01/15
	 */
	public static final byte CONTROL_SET_TITLE =
		20;
	
	/** The number of framebuffer controls. */
	public static final byte NUM_CONTROLS =
		21;
	
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
	
	/** Has touch-screen. */
	public static final byte CAPABILITY_TOUCH =
		0x01;
	
	/** Has keyboard. */
	public static final byte CAPABILITY_KEYBOARD =
		0x02;
	
	/** The JVM pushes to the IPC handler when events happen. */
	public static final byte CAPABILITY_IPC_EVENTS =
		0x04;
	
	/** Has screen flipping? */
	public static final byte CAPABILITY_SCREEN_FLIP =
		0x08;
	
	/** Screen has color that is not just a single shade. */
	public static final byte CAPABILITY_COLOR =
		0x10;
	
	/** Set color. */
	public static final byte ACCEL_FUNC_SET_COLOR =
		0;
	
	/** Draw line. */
	public static final byte ACCEL_FUNC_DRAW_LINE =
		1;
	
	/** Get the X clip. */
	public static final byte ACCEL_FUNC_GET_CLIP_X =
		2;
	
	/** Get the Y clip. */
	public static final byte ACCEL_FUNC_GET_CLIP_Y =
		3;
	
	/** Get the width clip. */
	public static final byte ACCEL_FUNC_GET_CLIP_WIDTH =
		4;
	
	/** Get the height clip. */
	public static final byte ACCEL_FUNC_GET_CLIP_HEIGHT =
		5;
	
	/** Set the clip. */
	public static final byte ACCEL_FUNC_SET_CLIP =
		6;
	
	/** Draw rectangle. */
	public static final byte ACCEL_FUNC_DRAW_RECT =
		7;
	
	/** Get the alpha color. */
	public static final byte ACCEL_FUNC_GET_ALPHA_COLOR =
		8;
	
	/** Set the alpha color. */
	public static final byte ACCEL_FUNC_SET_ALPHA_COLOR =
		9;
	
	/** Fill rectangle. */
	public static final byte ACCEL_FUNC_FILL_RECT =
		10;
	
	/** Sets the fonts for the graphics. */
	public static final byte ACCEL_FUNC_SET_FONT =
		11;
	
	/** Gets the font to use for drawing. */
	public static final byte ACCEL_FUNC_GET_FONT =
		12;
	
	/** Draw sub-characters. */
	public static final byte ACCEL_FUNC_DRAW_SUB_CHARS =
		13;
	
	/** Draw text. */
	public static final byte ACCEL_FUNC_DRAW_TEXT =
		14;
	
	/** Get stroke style. */
	public static final byte ACCEL_FUNC_GET_STROKE_STYLE =
		15;
	
	/** Set stroke style. */
	public static final byte ACCEL_FUNC_SET_STROKE_STYLE =
		16;
	
	/** Copy area. */
	public static final byte ACCEL_FUNC_COPY_AREA =
		17;
	
	/** Draw arc. */
	public static final byte ACCEL_FUNC_DRAW_ARC =
		18;
	
	/** Draw ARGB16. */
	public static final byte ACCEL_FUNC_DRAW_ARGB16 =
		19;
	
	/** Draw character. */
	public static final byte ACCEL_FUNC_DRAW_CHAR =
		20;
	
	/** Draw characters. */
	public static final byte ACCEL_FUNC_DRAW_CHARS =
		21;
	
	/** Draw RGB. */
	public static final byte ACCEL_FUNC_DRAW_RGB =
		22;
	
	/** Draw RGB16. */
	public static final byte ACCEL_FUNC_DRAW_RGB16 =
		23;
	
	/** Draw round rectangle. */
	public static final byte ACCEL_FUNC_DRAW_ROUND_RECT =
		24;
	
	/** Fill arc. */
	public static final byte ACCEL_FUNC_FILL_ARC =
		25;
	
	/** Fill round rectangle. */
	public static final byte ACCEL_FUNC_FILL_ROUND_RECT =
		26;
	
	/** Fill triangle. */
	public static final byte ACCEL_FUNC_FILL_TRIANGLE =
		27;
	
	/** Get blending mode. */
	public static final byte ACCEL_FUNC_GET_BLENDING_MODE =
		28;
	
	/** Get display color. */
	public static final byte ACCEL_FUNC_GET_DISPLAY_COLOR =
		29;
	
	/** Set blending mode. */
	public static final byte ACCEL_FUNC_SET_BLENDING_MODE =
		30;
	
	/** Draw region. */
	public static final byte ACCEL_FUNC_DRAW_REGION =
		31;
	
	/** Number of acceleration functions. */
	public static final byte NUM_ACCEL_FUNC =
		32;
	
	/** The IPC ID for the graphics callbacks. */
	public static final int IPC_ID =
		0x47665821;
}

