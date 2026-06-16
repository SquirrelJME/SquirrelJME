// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.samsung.util;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import org.jetbrains.annotations.Range;

/**
 * Samsung vendor API for vibration functions.
 *
 * @since 2026/04/07
 */
@Api
public final class Vibration
{

	/**
	 * Returns whether the device is capable of generating vibrations.
	 *
	 * @return Whether vibration is supported.
	 * @since 2026/04/07
	 */
	@Api
	public static boolean isSupported()
	{
		return cc.squirreljme.runtime.lcdui.mle.Vibration.available();
	}

	/**
	 * Starts the device's vibrator for {@code __duration} milliseconds, and
	 * {@code __strength} power.
	 *
	 * @param __duration The vibration duration in positive milliseconds.
	 * @param __strength The vibration strength, from 1 to 5.
	 * @throws IllegalStateException If this device does not support vibration.
	 * @throws IllegalArgumentException If either {@code __duration} or
	 * {@code __strength} are in an invalid range.
	 * @since 2026/04/07
	 */
	@Api
	public static void start(
		@Range(from = 0, to = Integer.MAX_VALUE) int __duration,
		@Range(from = 1, to = 5) int __strength)
		throws IllegalStateException, IllegalArgumentException
	{
		/* {@squirreljme.error SS2u Vibration is not supported.} */
		if (!isSupported())
			throw new IllegalStateException("SS2u");

		/* {@squirreljme.error EB33 Cannot vibrate for a negative duration.} */
		if (__duration < 0)
			throw new IllegalArgumentException("EB33");

		/* {@squirreljme.error EB34 Frequency out of range. (The frequency)} */
		if (__strength < 1 || __strength > 5)
			throw new IllegalArgumentException("EB34 " + __strength);

		// We cannot adjust vibration strength yet.
		Debugging.todoNote("Samsung Vibration Strength?");

		// Perform the vibration
		cc.squirreljme.runtime.lcdui.mle.Vibration.vibrate(
			(int)Math.min(Integer.MAX_VALUE, __duration), __strength * 20);
	}

	/**
	 * Stops vibrating entirely.
	 *
	 * @throws IllegalStateException If this device does not support vibration.
	 * @since 2026/04/07
	 */
	@Api
	public static void stop()
		throws IllegalStateException
	{
		/* {@squirreljme.error SS2u Vibration is not supported.} */
		if (!isSupported())
			throw new IllegalStateException("SS2u");

		// Stop any ongoing vibration
		cc.squirreljme.runtime.lcdui.mle.Vibration.stopVibrate();
	}
}
