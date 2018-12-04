// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.asm;

/**
 * This class provides access to the native display system that is used by the
 * LCDUI code to display widgets and such to the screen. Any application may
 * access the screen directly and must manage exclusivity by itself if such a
 * thing is applicable for a single shared screen resource.
 *
 * @since 2018/11/09
 */
public final class NativeDisplayAccess
{
	/** The maximum number of integers for event data. */
	public static final int EVENT_SIZE =
		5;
	
	/** The number of parameters available. */
	public static final int NUM_PARAMETERS =
		8;
	
	/** The pixel format. */
	public static final int PARAMETER_PIXELFORMAT =
		0;
	
	/** The buffer width. */
	public static final int PARAMETER_BUFFERWIDTH =
		1;
	
	/** The buffer height. */
	public static final int PARAMETER_BUFFERHEIGHT =
		2;
	
	/** Alpha channel is used? */
	public static final int PARAMETER_ALPHA =
		3;
	
	/** Buffer pitch. */
	public static final int PARAMETER_PITCH =
		4;
	
	/** Buffer offset. */
	public static final int PARAMETER_OFFSET =
		5;
	
	/** Virtual X offset. */
	public static final int PARAMETER_VIRTXOFF =
		6;
	
	/** Virtual Y offset. */
	public static final int PARAMETER_VIRTYOFF =
		7;
	
	/**
	 * Not used.
	 *
	 * @since 2018/11/09
	 */
	private NativeDisplayAccess()
	{
	}
	
	/**
	 * Initialize and/or reset accelerated graphics operations.
	 *
	 * @param __id The display to initialize for.
	 * @return {@code true} if acceleration is supported.
	 * @since 2018/11/19
	 */
	public static final native boolean accelGfx(int __id);
	
	/**
	 * Performs accelerated graphics operation.
	 *
	 * @param __id The display ID.
	 * @param __func The function to call.
	 * @param __args Arguments to the operation.
	 * @return The result of the operation.
	 * @since 2018/11/19
	 */
	public static final native Object accelGfxFunc(int __id, int __func,
		Object... __args);
	
	/**
	 * Returns the capabilities of the display.
	 *
	 * @param __id The display ID.
	 * @return The capabilities of the display.
	 * @since 2018/11/17
	 */
	public static final native int capabilities(int __id);
	
	/**
	 * Returns the object representing the framebuffer data.
	 *
	 * @param __id The display ID.
	 * @return The framebuffer array.
	 * @since 2018/11/18
	 */
	public static final native Object framebufferObject(int __id);
	
	/**
	 * Specifies that the framebuffer has been painted.
	 *
	 * @param __id The display ID.
	 * @since 2018/11/18
	 */
	public static final native void framebufferPainted(int __id);
	
	/**
	 * Returns the palette of the framebuffer.
	 *
	 * @param __id The display ID.
	 * @return The palette of the framebuffer.
	 * @since 2018/11/18
	 */
	public static final native int[] framebufferPalette(int __id);
	
	/**
	 * Returns the parameters of the framebuffer.
	 *
	 * @param __id The display ID.
	 * @return The framebuffer parameters.
	 * @since 2018/11/18
	 */
	public static final native int[] framebufferParameters(int __id);
	
	/**
	 * Returns the state count of this framebuffer which is used to detect
	 * when the parameters have changed, where they must all be recalculated
	 * (that is the framebuffer wrapper must be recreated).
	 *
	 * @param __id The display ID.
	 * @return The state count for the framebuffer.
	 * @since 2018/12/02
	 */
	public static final native int framebufferStateCount(int __id);
	
	/**
	 * Is the specified display upsidedown?
	 *
	 * @param __id The ID of the display.
	 * @return If the display is upsidedown.
	 * @since 2018/11/17
	 */
	public static final native boolean isUpsideDown(int __id);
	
	/**
	 * Returns the number of permanent displays which are currently attached to
	 * the system.
	 *
	 * @return The number of displays attached to the system.
	 * @since 2018/11/16
	 */
	public static final native int numDisplays();
	
	/**
	 * Registers the class to be called for when display events are to be
	 * called.
	 *
	 * @param __cb The callback.
	 * @since 2018/12/03
	 */
	public static final native void registerEventCallback(
		NativeDisplayEventCallback __cb);
	
	/**
	 * Sets the title of the display.
	 *
	 * @param __id The display ID.
	 * @param __t The title to use.
	 * @since 2018/11/18
	 */
	public static final native void setDisplayTitle(int __id, String __t);
}

