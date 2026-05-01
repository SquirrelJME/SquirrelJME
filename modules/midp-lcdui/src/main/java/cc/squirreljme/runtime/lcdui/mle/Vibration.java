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
import org.jetbrains.annotations.Range;

/**
 * Support for vibration.
 *
 * @since 2022/02/14
 */
@SquirrelJMEVendorApi
public final class Vibration
{

	/* The highest vibration strength allowed. */
	@SquirrelJMEVendorApi
	public static final int MAX_STR = 100;

	/* The lowest vibration strength allowed, which is just no vibration. */
	@SquirrelJMEVendorApi
	public static final int MIN_STR = 0;

	/**
	 * Not used.
	 * 
	 * @since 2022/02/14
	 */
	private Vibration()
	{
	}

	/**
	 * Returns whether vibration is supported by the device.
	 * 
	 * @return {@code true} If vibration is supported, {@code false}
	 * otherwise.
	 * @since 2026/04/19
	 */
	@SquirrelJMEVendorApi
	public static boolean available()
	{
		throw Debugging.todo("VibrationAvailability");
	}
	
	/**
	 * Attempts to vibrate the device for the given number of milliseconds, at
	 * the maximum vibration strength allowed by such device.
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
	public static boolean vibrate(
		@Range(from = 0, to = Integer.MAX_VALUE) int __d)
		throws IllegalArgumentException
	{
		return Vibration.vibrate(__d, 100);
	}

	/**
	 * Attempts to vibrate the device for the given number of milliseconds at
	 * the specified vibration strength.
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
	 * @param __s The vibration's strength, goes from 0 to 100 in intensity.
	 * @return {@code true} if the vibrator is controllable by this application
	 * and the display is active.
	 * @throws IllegalArgumentException If the duration is negative or strength
	 * is less than 0 or more than 100.
	 * @since 2026/04/30
	 */
	@SquirrelJMEVendorApi
	public static boolean vibrate(
		@Range(from = 0, to = Integer.MAX_VALUE) int __d,
		@Range(from = Vibration.MIN_STR, to = Vibration.MAX_STR) int __s)
		throws IllegalArgumentException
	{
		/* {@squirreljme.error EB1n Cannot vibrate for a negative duration.} */
		if (__d < 0 || __s < 0 || __s > 100)
			throw new IllegalArgumentException("EB1n");
		
		Debugging.todoNote("Vibration?");
		return false;
	}

	/**
	 * Attempts to stop the device from vibrating.
	 *
	 * Note that if the device is not vibrating when this is called, or the
	 * vibrator cannot be controlled at that point in time, this method will
	 * return {@code false}.
	 *
	 * @return {@code true} if the vibrator is controllable by this application,
	 * the display is active and the vibration was stopped.
	 * @since 2026/04/30
	 */
	@SquirrelJMEVendorApi
	public static boolean stopVibrate()
	{
		Debugging.todoNote("stopVibrate?");
		return false;
	}
}
