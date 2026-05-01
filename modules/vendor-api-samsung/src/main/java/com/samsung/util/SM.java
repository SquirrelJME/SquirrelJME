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
import org.jetbrains.annotations.Nullable;

/**
 * Samsung vendor API for GSM Short Messages.
 *
 * @since 2026/04/07
 */
@Api
public class SM
{

	/**
	 * Constructs an empty Short Message.
	 *
	 * @since 2026/04/07
	 */
	@Api
	public SM()
	{
		throw Debugging.todo("Samsung create empty SM");
	}

	/**
	 * Constructs a Short Message containing the given destination and callback
	 * number addresses, and text message.
	 *
	 * @param __dest The destination number address.
	 * @param __callback The callback number address.
	 * @param __textMessage The actual message contents.
	 * @throws IllegalArgumentException If {@code __textMessage} has more than
	 * 80 characters, or {@code __dest} is not a valid number.
	 * @since 2026/04/07
	 */
	@Api
	public SM(@NotNull String __dest, @Nullable String __callback,
		@NotNull String __textMessage)
		throws IllegalArgumentException
	{
		throw Debugging.todo("Samsung create SM");
	}

	/**
	 * Retrieves the current callback address set onto this Short Message.
	 *
	 * @return The currently set callback address on this Short Message,
	 * @since 2026/04/07
	 */
	@Api
	public String getCallbackAddress()
	{
		throw Debugging.todo("Samsung SM getCallbackAddress");
	}

	/**
	 * Retrieves the current text data set onto this Short Message.
	 *
	 * @return The currently set text content on this Short Message,
	 * @since 2026/04/07
	 */
	@Api
	public String getData()
	{
		throw Debugging.todo("Samsung SM getData");
	}

	/**
	 * Retrieves the current destination address set onto this Short Message.
	 *
	 * @return The currently set destination address on this Short Message,
	 * @since 2026/04/07
	 */
	@Api
	public String getDestAddress()
	{
		throw Debugging.todo("Samsung SM getDestAddress");
	}

	/**
	 * Sets a new callback address onto this Short Message. Samsung docs state
	 * that this function call is ignored entirely.
	 *
	 * @param __address The callback address to set.
	 * @since 2026/04/07
	 */
	@Api
	public void setCallbackAddress(@Nullable String __address)
	{
		throw Debugging.todo("Samsung SM setCallbackAddress");
	}

	/**
	 * Sets a new text data onto this Short Message, which must contain at most
	 * 80 characters.
	 *
	 * @param __textMessage The text data to set.
	 * @throws IllegalArgumentException If the text data has more than 80
	 * characters.
	 * @since 2026/04/07
	 */
	@Api
	public void setData(@NotNull String __textMessage)
		throws IllegalArgumentException
	{
		/* {@squirreljme.error EB4l SMS is larger than the allowed size.} */
		if(__textMessage.length() > 80)
			throw new IllegalArgumentException("EB4l");

		throw Debugging.todo("Samsung SM setData");
	}

	/**
	 * Sets a new destination address onto this Short Message, which must be a
	 * valid phone number.
	 *
	 * @param __address The destination address to set.
	 * @throws IllegalArgumentException If the destination address is not a
	 * valid phone number.
	 * @since 2026/04/07
	 */
	@Api
	public void setDestAddress(@NotNull String __address)
		throws IllegalArgumentException
	{
		throw Debugging.todo("Samsung SM setDestAddress");
	}
}
