// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.media;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * General Audio System checks and functions.
 *
 * @since 2026/04/30
 */
@SquirrelJMEVendorApi
public final class AudioSystem
{
	/**
	 * Unused.
	 * 
	 * @since 2026/04/30
	 */
	private AudioSystem()
	{
	}

	/**
	 * Returns whether audio playback is supported by the device.
	 * 
	 * @return Whether audio playback is supported.
	 * @since 2026/04/30
	 */
	@SquirrelJMEVendorApi
	public static boolean available()
	{
		Debugging.todoNote("AudioAvailability");
		
		// Hard to imagine a platform that can run SquirrelJME at this stage
		// that does not support audio, return true until proper checks are in
		// place.
		return true;
	}
}