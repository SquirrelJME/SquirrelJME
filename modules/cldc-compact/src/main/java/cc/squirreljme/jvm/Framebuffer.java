// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
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
	byte CONTROL_ADDRESS =
		1;
	
	/**
	 * Returns the width of the framebuffer.
	 *
	 * @squirreljme.syscallreturn The framebuffer width.
	 */
	byte CONTROL_WIDTH =
		2;
	
	/**
	 * Returns the height of the framebuffer.
	 *
	 * @squirreljme.syscallreturn The framebuffer height.
	 */
	byte CONTROL_HEIGHT =
		3;
	
	/**
	 * Returns the scanline length.
	 *
	 * @squirreljme.syscallreturn The framebuffer scanline length.
	 */
	byte CONTROL_SCANLEN =
		4;
	
	/**
	 * Flush the display because it has been drawn.
	 */
	byte CONTROL_FLUSH =
		5;
	
	/**
	 * Returns the pixel format of the screen.
	 *
	 * @squirreljme.syscallreturn The pixel format of the screen.
	 */
	byte CONTROL_FORMAT =
		6;
	
	/**
	 * Returns the scanline length in bytes.
	 *
	 * @squirreljme.syscallreturn The scanline length in bytes.
	 */
	byte CONTROL_SCANLEN_BYTES =
		7;
	
	/**
	 * Returns the number of bytes per pixel.
	 *
	 * @squirreljme.syscallreturn The bytes per pixel.
	 */
	byte CONTROL_BYTES_PER_PIXEL =
		8;
	
	/**
	 * Returns the number of available pixels.
	 *
	 * @squirreljme.syscallreturn The number of pixels.
	 */
	byte CONTROL_NUM_PIXELS =
		9;
	
	/**
	 * Bits per pixel.
	 *
	 * @squirreljme.syscallreturn The bits per pixel.
	 */
	byte CONTROL_BITS_PER_PIXEL =
		10;
	
	/**
	 * Get backlight level.
	 *
	 * @squirreljme.syscallreturn The current backlight level.
	 */
	byte CONTROL_BACKLIGHT_LEVEL_GET =
		11;
	
	/**
	 * Set backlight level.
	 *
	 * @squirreljme.syscallparam 1 The level to set.
	 */
	byte CONTROL_BACKLIGHT_LEVEL_SET =
		12;
	
	/**
	 * Maximum backlight level.
	 *
	 * @squirreljme.syscallreturn The maximum backlight level.
	 */
	byte CONTROL_BACKLIGHT_LEVEL_MAX =
		13;
	
	/**
	 * Uploads an integer array of pixel data to the framebuffer.
	 *
	 * @squirreljme.syscallparam 1 The address of the array to upload.
	 * @since 2019/12/21
	 */
	byte CONTROL_UPLOAD_ARRAY_INT =
		14;
	
	/**
	 * The array which backs the framebuffer, if there is one.
	 *
	 * @squirreljme.syscallreturn The backing array object, if there is one.
	 * @since 2019/12/28
	 */
	byte CONTROL_BACKING_ARRAY_OBJECT =
		15;
		
	/**
	 * Returns the capabilities of the display.
	 *
	 * @squirreljme.syscallreturn The display capabilities.
	 * @since 2020/01/10
	 */
	byte CONTROL_GET_CAPABILITIES =
		16;
	
	/**
	 * Query acceleration function.
	 *
	 * @squirreljme.syscallparam 1 The graphics function.
	 * @squirreljme.syscallreturn A non-zero value if this is supported.
	 * @since 2020/01/10
	 */
	byte CONTROL_ACCEL_FUNC_QUERY =
		17;
	
	/**
	 * Perform acceleration function.
	 *
	 * @squirreljme.syscallparam 1 The graphics function.
	 * @squirreljme.syscallparam ... Parameters to the function.
	 * @squirreljme.syscallreturn A value that is according to the invoked
	 * function, if it is supported or possible.
	 * @since 2020/01/10
	 */
	byte CONTROL_ACCEL_FUNC_INVOKE =
		18;
	
	/**
	 * Requests that the framebuffer be repainted.
	 *
	 * @squirreljme.syscallparam 1 The X coordinate.
	 * @squirreljme.syscallparam 2 The Y coordinate.
	 * @squirreljme.syscallparam 3 The width.
	 * @squirreljme.syscallparam 4 The height.
	 * @squirreljme.syscallreturn Returns {@code 0} if the repaint was not
	 * queued and it must be handled by the code running the application,
	 * @since 2020/01/15
	 */
	byte CONTROL_REPAINT_REQUEST =
		19;
	
	/**
	 * Sets the title of the framebuffer if applicable.
	 *
	 * @squirreljme.syscallparam 1 Char buffer pointer (high).
	 * @squirreljme.syscallparam 2 Char buffer pointer (low).
	 * @since 2020/01/15
	 */
	byte CONTROL_SET_TITLE =
		20;
	
	/** The number of framebuffer controls. */
	byte NUM_CONTROLS =
		21;
	
	/** Screen is RGB 32-bit. */
	byte FORMAT_INTEGER_RGB888 =
		0;
	
	/** Screen is 8-bit indexed. */
	byte FORMAT_BYTE_INDEXED =
		1;
	
	/** Screen is 16-bit RGB565. */
	byte FORMAT_SHORT_RGB565 =
		2;
	
	/** Screen is packed 1 bit values. */
	byte FORMAT_PACKED_ONE =
		3;
	
	/** Screen is packed 2 bit values. */
	byte FORMAT_PACKED_TWO =
		4;
	
	/** Screen is packed 4 bit values. */
	byte FORMAT_PACKED_FOUR =
		5;
	
	/** Has touch-screen. */
	byte CAPABILITY_TOUCH =
		0x01;
	
	/** Has keyboard. */
	byte CAPABILITY_KEYBOARD =
		0x02;
	
	/** The JVM pushes to the IPC handler when events happen. */
	byte CAPABILITY_IPC_EVENTS =
		0x04;
	
	/** Has screen flipping? */
	byte CAPABILITY_SCREEN_FLIP =
		0x08;
	
	/** Screen has color that is not just a single shade. */
	byte CAPABILITY_COLOR =
		0x10;
	
	/** Set color. */
	byte ACCEL_FUNC_SET_COLOR =
		0;
	
	/** Draw line. */
	byte ACCEL_FUNC_DRAW_LINE =
		1;
	
	/** Get the X clip. */
	byte ACCEL_FUNC_GET_CLIP_X =
		2;
	
	/** Get the Y clip. */
	byte ACCEL_FUNC_GET_CLIP_Y =
		3;
	
	/** Get the width clip. */
	byte ACCEL_FUNC_GET_CLIP_WIDTH =
		4;
	
	/** Get the height clip. */
	byte ACCEL_FUNC_GET_CLIP_HEIGHT =
		5;
	
	/** Set the clip. */
	byte ACCEL_FUNC_SET_CLIP =
		6;
	
	/** Draw rectangle. */
	byte ACCEL_FUNC_DRAW_RECT =
		7;
	
	/** Get the alpha color. */
	byte ACCEL_FUNC_GET_ALPHA_COLOR =
		8;
	
	/** Set the alpha color. */
	byte ACCEL_FUNC_SET_ALPHA_COLOR =
		9;
	
	/** Fill rectangle. */
	byte ACCEL_FUNC_FILL_RECT =
		10;
	
	/** Sets the fonts for the graphics. */
	byte ACCEL_FUNC_SET_FONT =
		11;
	
	/** Gets the font to use for drawing. */
	byte ACCEL_FUNC_GET_FONT =
		12;
	
	/** Draw sub-characters. */
	byte ACCEL_FUNC_DRAW_SUB_CHARS =
		13;
	
	/** Draw text. */
	byte ACCEL_FUNC_DRAW_TEXT =
		14;
	
	/** Get stroke style. */
	byte ACCEL_FUNC_GET_STROKE_STYLE =
		15;
	
	/** Set stroke style. */
	byte ACCEL_FUNC_SET_STROKE_STYLE =
		16;
	
	/** Copy area. */
	byte ACCEL_FUNC_COPY_AREA =
		17;
	
	/** Draw arc. */
	byte ACCEL_FUNC_DRAW_ARC =
		18;
	
	/** Draw ARGB16. */
	byte ACCEL_FUNC_DRAW_ARGB16 =
		19;
	
	/** Draw character. */
	byte ACCEL_FUNC_DRAW_CHAR =
		20;
	
	/** Draw characters. */
	byte ACCEL_FUNC_DRAW_CHARS =
		21;
	
	/** Draw RGB. */
	byte ACCEL_FUNC_DRAW_RGB =
		22;
	
	/** Draw RGB16. */
	byte ACCEL_FUNC_DRAW_RGB16 =
		23;
	
	/** Draw round rectangle. */
	byte ACCEL_FUNC_DRAW_ROUND_RECT =
		24;
	
	/** Fill arc. */
	byte ACCEL_FUNC_FILL_ARC =
		25;
	
	/** Fill round rectangle. */
	byte ACCEL_FUNC_FILL_ROUND_RECT =
		26;
	
	/** Fill triangle. */
	byte ACCEL_FUNC_FILL_TRIANGLE =
		27;
	
	/** Get blending mode. */
	byte ACCEL_FUNC_GET_BLENDING_MODE =
		28;
	
	/** Get display color. */
	byte ACCEL_FUNC_GET_DISPLAY_COLOR =
		29;
	
	/** Set blending mode. */
	byte ACCEL_FUNC_SET_BLENDING_MODE =
		30;
	
	/** Draw region. */
	byte ACCEL_FUNC_DRAW_REGION =
		31;
	
	/** Number of acceleration functions. */
	byte NUM_ACCEL_FUNC =
		32;
	
	/** The IPC ID for the graphics callbacks. */
	int IPC_ID =
		0x47665821;
}

