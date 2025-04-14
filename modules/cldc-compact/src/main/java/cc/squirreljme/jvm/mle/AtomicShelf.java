// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;

/**
 * This shelf provides helpers for atomic operations.
 *
 * @since 2020/05/30
 */
@SuppressWarnings("UnstableApiUsage")
@SquirrelJMEVendorApi
public final class AtomicShelf
{
	/**
	 * Not used.
	 *
	 * @since 2020/05/30
	 */
	private AtomicShelf()
	{
	}
	
	/**
	 * Lock for garbage collection operations.
	 *
	 * @return The locking key if locked, otherwise {@code 0} when busy.
	 * @since 2020/05/30
	 */
	@SquirrelJMEVendorApi
	public static native int gcLock();
	
	/**
	 * Unlocks the garbage collector provided that the previous locking key
	 * matches.
	 *
	 * @param __key The key that was previously used to lock the garbage
	 * collector.
	 * @since 2020/05/30
	 */
	@SquirrelJMEVendorApi
	public static native void gcUnlock(int __key);
	
	/**
	 * For constant lock spins, this provides a consistent means of counting
	 * and then potentially fire-spinning or resting depending on the
	 * situation. This may cause a thread to preempt.
	 *
	 * On cooperatively tasked systems, this may switch to another thread.
	 *
	 * @param __count The number of times the lock has spun.
	 * @since 2020/05/30
	 */
	@SquirrelJMEVendorApi
	public static native void spinLock(int __count);
	
	/**
	 * Returns a "unique" atomic tick value. The value returned should be
	 * unique enough but the actual value that is returned should not be used
	 * in any way for comparisons other than equality.
	 *
	 * @return The tick value, note that it should not be used for comparisons
	 * other than equality.
	 * @since 2020/05/03
	 */
	@SquirrelJMEVendorApi
	public static native int tick();
}
