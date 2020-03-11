// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Represents an atomically ticked value.
 *
 * @since 2020/03/10
 */
public final class AtomicTicker
{
	/** The ticked atomic value. */
	private static final AtomicInteger _TICKER =
		new AtomicInteger(-1);
	
	/**
	 * Not used.
	 *
	 * @since 2020/03/10
	 */
	private AtomicTicker()
	{
	}
	
	/**
	 * Returns the next atomically ticked value.
	 *
	 * @return The next atomically ticked value.
	 * @since 2020/03/10
	 */
	public static int next()
	{
		return AtomicTicker._TICKER.decrementAndGet();
	}
}
