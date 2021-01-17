// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.summercoat;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * This represents a single memory handle.
 *
 * @since 2020/11/28
 */
public final class MemHandle
{
	/** The handle count. */
	private final AtomicInteger _count =
		new AtomicInteger();
	
	/**
	 * Changes the count on the memory handle.
	 * 
	 * @param __up Count this handle up?
	 * @return The new count.
	 * @since 2020/11/28
	 */
	public final int count(boolean __up)
	{
		if (__up)
			return this._count.incrementAndGet();
		return this._count.decrementAndGet();
	}
	
	/**
	 * Sets the explicit memory handle count.
	 * 
	 * @param __count The count to set to.
	 * @since 2021/01/17
	 */
	public final void setCount(int __count)
	{
		this._count.set(__count);
	}
}
