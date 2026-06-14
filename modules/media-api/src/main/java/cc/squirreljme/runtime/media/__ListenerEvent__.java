// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.media;

import cc.squirreljme.runtime.cldc.annotation.KeepWhenCompacting;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;

/**
 * Stores event information.
 *
 * @since 2025/06/03
 */
@KeepWhenCompacting
final class __ListenerEvent__
{
	/** The player this event came from. */
	@SquirrelJMEVendorApi
	final AbstractPlayer _player;
	
	/** The event type. */
	@SquirrelJMEVendorApi
	final String _eventType;
	
	/** The event value. */
	@SquirrelJMEVendorApi
	final Object _eventValue;
	
	/** The time this event occurred. */
	@SquirrelJMEVendorApi
	final long _nanoTime;
	
	/**
	 * Initializes the event storage.
	 *
	 * @param __player The player this came from.
	 * @param __eventType The event type.
	 * @param __eventValue The event value.
	 * @param __nanoTime The time the event was emitted.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/06/03
	 */
	__ListenerEvent__(AbstractPlayer __player, String __eventType,
		Object __eventValue, long __nanoTime)
		throws NullPointerException
	{
		if (__player == null || __eventType == null)
			throw new NullPointerException("NARG");
		
		this._player = __player;
		this._eventType = __eventType;
		this._eventValue = __eventValue;
		this._nanoTime = __nanoTime;
	}
}
