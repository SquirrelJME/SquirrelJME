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
	
	/**
	 * Not used.
	 *
	 * @since 2018/11/09
	 */
	private NativeDisplayAccess()
	{
	}
	
	/**
	 * Returns the capabilities of the display.
	 *
	 * @param __id The display ID.
	 * @return The capabilities of the display.
	 * @since 2018/11/17
	 */
	public static final native int capabilities(int __id);
	
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
	 * Polls the next event, blocking until the next one occurs.
	 *
	 * @param __ed Event data.
	 * @return The next event, this will be the even type.
	 * @since 2018/11/17
	 */
	public static final native int pollEvent(short[] __ed);
	
	/**
	 * Posts the specified event to the end of the event queue.
	 *
	 * @param __type The event type to push.
	 * @param __d0 Datapoint 1.
	 * @param __d1 Datapoint 2.
	 * @param __d2 Datapoint 3.
	 * @param __d3 Datapoint 4.
	 * @param __d4 Datapoint 5.
	 * @since 2018/11/18
	 */
	public static final native void postEvent(int __type,
		short __d0, short __d1, short __d2, short __d3, short __d4);
}

