// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.nttdocomo.util;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import java.lang.ref.Reference;
import java.util.TimerTask;

/**
 * Listener for timer tasks.
 *
 * @since 2024/12/05
 */
@SquirrelJMEVendorApi
final class __ExpireListener__
	extends TimerTask
{
	/** The expiration store to use. */
	private final __ExpireStore__ _expire;
	
	/**
	 * Initializes the expiration listener.
	 *
	 * @param __expire The expiration storage to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/12/05
	 */
	@SquirrelJMEVendorApi
	__ExpireListener__(__ExpireStore__ __expire)
		throws NullPointerException
	{
		if (__expire == null)
			throw new NullPointerException("NARG");
		
		this._expire = __expire;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/12/05
	 */
	@Override
	public void run()
	{
		this._expire.run();
	}
}
