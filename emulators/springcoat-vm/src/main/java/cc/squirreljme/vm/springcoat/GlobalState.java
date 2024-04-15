// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Contains virtual machine global state.
 *
 * @since 2020/05/30
 */
public final class GlobalState
{
	/** Ticker for atomic values. */
	public static final AtomicInteger TICKER =
		new AtomicInteger();
	
	/** The GC lock. */
	public static final AtomicInteger GC_LOCK =
		new AtomicInteger();
}
