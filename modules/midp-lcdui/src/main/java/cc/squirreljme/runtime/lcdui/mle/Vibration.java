// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.mle;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * Support for vibration.
 *
 * @since 2022/02/14
 */
@SquirrelJMEVendorApi
public final class Vibration
{
	/**
	 * Not used.
	 * 
	 * @since 2022/02/14
	 */
	private Vibration()
	{
	}
	
	/**
	 * Attempts to vibrate the device for the given number of milliseconds.
	 *
	 * The values here only set the duration to vibrate for from the current
	 * point in time and will not increase the length of vibration.
	 *
	 * The return value will be {@code false} if the display is in the
	 * background, the device cannot vibrate, or the vibrator cannot be
	 * controlled.
	 *
	 * Note that excessive vibration may cause the battery life for a device to
	 * be lowered, thus it should be used sparingly.
	 *
	 * @param __d The number of milliseconds to vibrate for, if zero the
	 * vibrator will be switched off.
	 * @return {@code true} if the vibrator is controllable by this application
	 * and the display is active.
	 * @throws IllegalArgumentException If the duration is negative.
	 * @since 2022/02/14
	 */
	@SquirrelJMEVendorApi
	public static boolean vibrate(int __d)
		throws IllegalArgumentException
	{
		/* {@squirreljme.error EB1n Cannot vibrate for a negative duration.} */
		if (__d < 0)
			throw new IllegalArgumentException("EB1n");
		
		Debugging.todoNote("Vibration?");
		return false;
	}
}
