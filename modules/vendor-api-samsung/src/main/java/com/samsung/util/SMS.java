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
import org.jetbrains.annotations.NotNull;
import javax.microedition.io.Connector;

/**
 * Samsung vendor API for sending SMS.
 *
 * @since 2026/04/07
 */
@Api
public class SMS
{

	/**
	 * Returns whether the device is capable of sending and receiving SMS.
	 *
	 * @return Whether SMS is supported.
	 * @since 2026/04/07
	 */
	@Api
	public static boolean isSupported()
	{
		return Connector.isProtocolSupported("sms", false);
	}

	/**
	 * Sends an SMS. This method neither waits nor expects any kind of response
	 * to the send event, even in cases of failure.
	 *
	 * @param __sm The SMS data to send.
	 * @throws IllegalStateException If this device does not support SMS.
	 * @since 2026/04/07
	 */
	@Api
	public static void send(@NotNull SM __sm)
	{
		/* {@squirreljme.error SS1u SMS is not supported.} */
		if (!isSupported())
			throw new IllegalStateException("SS1u");

		throw Debugging.todo("Samsung SMS Send");
	}
}
