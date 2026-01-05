// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.nokia.mid.ui;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.annotation.ApiDefinedDeprecated;
import cc.squirreljme.runtime.lcdui.BacklightControl;
import cc.squirreljme.runtime.lcdui.mle.Vibration;
import cc.squirreljme.runtime.lcdui.scritchui.HeadlessDisplayException;
import cc.squirreljme.runtime.midlet.ActiveMidlet;
import javax.microedition.lcdui.Display;
import javax.microedition.midlet.MIDlet;

/**
 * This is used to utilize special hardware that exists on the device for
 * user feedback.
 *
 * @since 2019/10/05
 */
@Api
public class DeviceControl
{
	/**
	 * Flashes the LED on the device. Deprecated since Nokia UI API 1.1 in
	 * favor of {@link Display#flashBacklight(int)}
	 *
	 * @param __ms The number of milliseconds to flash for.
	 * @throws IllegalArgumentException If the duration is negative.
	 * @since 2019/10/05
	 */
	@Api
	@ApiDefinedDeprecated
	public static void flashLights(long __ms)
		throws IllegalArgumentException
	{
		/* {@squirreljme.error EB2z Cannot blink for a negative duration.} */
		if (__ms < 0)
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
		display.flashBacklight((int)Math.min(Integer.MAX_VALUE, __ms));
	}
	
	/**
	 * Sets the level of the backlight.
	 *
	 * @param __num The light number, this is always zero for the backlight.
	 * @param __lvl The level to set within the range of {@code [0, 100]}
	 * @throws IllegalArgumentException If the light number is not zero or
	 * the level is out of range.
	 * @since 2019/10/05
	 */
	@Api
	public static void setLights(int __num, int __lvl)
		throws IllegalArgumentException
	{
		/* {@squirreljme.error EB31 Only light number zero is supported.
		(The light number)} */
		if (__num != 0)
			throw new IllegalArgumentException("EB31 " + __num);
		
		/* {@squirreljme.error EB32 Light level out of range. (The level)} */
		if (__lvl < 0 || __lvl > 100)
			throw new IllegalArgumentException("EB32 " + __lvl);
		
		// Set the new level.
		BacklightControl.setLevel(__lvl);
	}
	
	/**
	 * Starts vibrating at the given frequency for the given duration. Has
	 * been deprecated since Nokia UI API 1.1 in favor of
	 * {@link Display#vibrate(int)}
	 *
	 * @param __freq The frequency of the vibration, must be in the range of
	 * {@code [0, 100]}.
	 * @param __ms The length to vibrate for in milliseconds.
	 * @throws IllegalArgumentException If the duration is negative or the
	 * frequency is out of range.
	 * @since 2019/10/05
	 */
	@Api
	@ApiDefinedDeprecated
	public static void startVibra(int __freq, long __ms)
		throws IllegalArgumentException
	{
		/* {@squirreljme.error EB33 Cannot vibrate for a negative duration.} */
		if (__ms < 0)
			throw new IllegalArgumentException("EB33");
		
		/* {@squirreljme.error EB34 Frequency out of range. (The frequency)} */
		if (__freq < 0 || __freq > 100)
			throw new IllegalArgumentException("EB34 " + __freq);
		
		// Perform the vibration
		Vibration.vibrate((int)Math.min(Integer.MAX_VALUE, __ms));
	}
	
	/**
	 * Stops any vibration that is happening. Has been deprecated since
	 * Nokia UI API 1.1 in favor of {@link Display#vibrate(int)}
	 *
	 * @since 2019/10/05
	 */
	@Api
	@ApiDefinedDeprecated
	public static void stopVibra()
	{
		Vibration.vibrate(0);
	}
}

