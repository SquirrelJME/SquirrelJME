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
import cc.squirreljme.runtime.lcdui.BacklightControl;
import cc.squirreljme.runtime.lcdui.scritchui.HeadlessDisplayException;
import cc.squirreljme.runtime.midlet.ActiveMidlet;
import javax.microedition.lcdui.Display;
import javax.microedition.midlet.MIDlet;
import org.jetbrains.annotations.Range;

/**
 * Samsung vendor API for LCD backlight functions.
 *
 * @since 2026/04/07
 */
@Api
public final class LCDLight
{
	/**
	 * Returns whether the device is capable changing its LCD backlight state
	 * through J2ME calls.
	 *
	 * @return Whether LCD Backlight control is supported.
	 * @since 2026/04/07
	 */
	@Api
	public static boolean isSupported()
	{
		return BacklightControl.available();
	}

	/**
	 * Disables the device's LCD backlight.
	 *
	 * @throws IllegalStateException If this device does not support LCD
	 * backlight control.
	 * @return {@code true} if SMS is supported, else {@code false}.
	 * @since 2026/04/07
	 */
	@Api
	public static void off()
		throws IllegalStateException
	{
		LCDLight.on(0);
	}

	/**
	 * Enables the device's LCD backlight for {@code __duration} milliseconds.
	 *
	 * @param __duration The vibration duration in positive milliseconds.
	 * @throws IllegalStateException If this device does not support LCD
	 * backlight control.
	 * @throws IllegalArgumentException If {@code __duration} is not in a valid
	 * range.
	 * @since 2026/04/07
	 */
	@Api
	public static void on(
		@Range(from = 0, to = Integer.MAX_VALUE) int __duration)
		throws IllegalStateException
	{
		/* {@squirreljme.error SS0u LCD backlight control is not supported.} */
		if (!isSupported())
			throw new IllegalStateException("SS0u");

		/* {@squirreljme.error EB2z Cannot blink for a negative duration.} */
		if (__duration < 0)
			throw new IllegalArgumentException("EB2z");

		// Is a midlet being used?
		MIDlet midlet = ActiveMidlet.optional();
		if (midlet == null)
			return;

		// Obtain the display used by the current midlet.
		Display display;
		try
		{
			display = Display.getDisplay(midlet);
			if (display == null)
				return;
		}
		catch (IllegalStateException|HeadlessDisplayException ignored)
		{
			return;
		}

		// Forward to the new method to use.
		display.flashBacklight((int)Math.min(Integer.MAX_VALUE, __duration));
	}
}
