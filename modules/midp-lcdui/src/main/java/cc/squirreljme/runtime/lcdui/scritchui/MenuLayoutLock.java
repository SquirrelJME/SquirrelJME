// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.scritchui;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * Manages the layout lock for command items and menus.
 *
 * @since 2024/07/18
 */
@SquirrelJMEVendorApi
public final class MenuLayoutLock
	implements AutoCloseable
{
	/**
	 * {@inheritDoc}
	 * @since 2024/07/18
	 */
	@Override
	@SquirrelJMEVendorApi
	public void close()
	{
		throw Debugging.todo();
	}
	
	/**
	 * Opens the lock for editing layout placement.
	 *
	 * @param __root If this is a root lock.
	 * @return Always {@code this}.
	 * @throws IllegalStateException If the lock could not obtained.
	 * @since 2024/07/18
	 */
	@SquirrelJMEVendorApi
	public MenuLayoutLock open(boolean __root)
		throws IllegalStateException
	{
		throw Debugging.todo();
	}
}
