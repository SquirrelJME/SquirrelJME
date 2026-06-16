// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.nttdocomo.ui;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;

/**
 * Tracks modification counts.
 *
 * @since 2026/04/10
 */
@SquirrelJMEVendorApi
public final class ModificationCounter
{
	/** The current modification count. */
	private volatile int _modCount =
		1;
	
	/**
	 * Returns the current modification count.
	 *
	 * @return The current modification count.
	 * @since 2026/04/10
	 */
	@SquirrelJMEVendorApi
	public int current()
	{
		synchronized (this)
		{
			return this._modCount;
		}
	}
	
	/**
	 * Increments the modification count.
	 *
	 * @since 2026/04/10
	 */
	@SquirrelJMEVendorApi
	public void increment()
	{
		synchronized (this)
		{
			this._modCount++;
		}
	}
}
