// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.nttdocomo.util;

import cc.squirreljme.runtime.cldc.annotation.KeepWhenCompacting;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import java.lang.ref.Reference;

/**
 * Stores the expiration listener and counter.
 *
 * @since 2024/12/05
 */
@KeepWhenCompacting
final class __ExpireStore__
	implements Runnable
{
	/** The timer that owns this. */
	private final Reference<Timer> _owner;
	
	/** The current timer listener. */
	@SquirrelJMEVendorApi
	volatile TimerListener _listener;
	
	/**
	 * Initializes the expiration store.
	 *
	 * @param __owner The owning timer.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/12/05
	 */
	@KeepWhenCompacting
	__ExpireStore__(Reference<Timer> __owner)
		throws NullPointerException
	{
		if (__owner == null)
			throw new NullPointerException("NARG");
		
		this._owner = __owner;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/12/05
	 */
	@Override
	public void run()
	{
		// Get listener to be invoked
		TimerListener listener;
		synchronized (this)
		{
			listener = this._listener;
		}
		
		// Was there an actual listener to call?
		if (listener != null)
			listener.timerExpired(this._owner.get());
	}
	
	/**
	 * Sets the listener to use when the timer expires.
	 *
	 * @param __listener The lister to call on expiration.
	 * @since 2024/12/05
	 */
	@KeepWhenCompacting
	void __set(TimerListener __listener)
	{
		synchronized (this)
		{
			this._listener = __listener;
		}
	}
}
