// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import cc.squirreljme.jvm.mle.AtomicShelf;

/**
 * Functions for {@link AtomicShelf}.
 *
 * @since 2020/06/18
 */
public enum MLEAtomic
	implements MLEFunction
{
	/** {@link AtomicShelf#gcLock()}. */
	GC_LOCK("gcLock:()I")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/06/18
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			// Generate a key which will be returned in the lock
			int key = (int)MLEAtomic.TICK.handle(__thread);
			
			// When unlocked the lock will have zero, so we can set it to the
			// key we generated... otherwise we fail here
			if (GlobalState.GC_LOCK.compareAndSet(0, key))
				return key;
			return 0;
		}
	},
	
	/** {@link AtomicShelf#gcUnlock(int)}. */
	GC_UNLOCK("gcUnlock:(I)V")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/06/18
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			// Unlocking is simple and only works if we have the key used to
			// lock the garbage collector
			GlobalState.GC_LOCK.compareAndSet((int)__args[0], 0);
			
			return null;
		}
	},
	
	/** {@link AtomicShelf#spinLock(int)}. */
	SPIN_LOCK("spinLock:(I)V")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/06/18
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			// If we spin for too long, instead give up our cycles
			if ((int)__args[0] > MLEAtomic._SPIN_LIMIT)
				Thread.yield();
			
			return null;
		}
	},
	
	/** {@link AtomicShelf#tick()}. */
	TICK("tick:()I")
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/06/18
		 */
		@Override
		public Object handle(SpringThreadWorker __thread, Object... __args)
		{
			return GlobalState.TICKER.decrementAndGet();
		}
	},
	
	/* End. */
	;
	
	/** How many times to spin before yielding. */
	private static final int _SPIN_LIMIT =
		8;
	
	/** The dispatch key. */
	protected final String key;
	
	/**
	 * Initializes the dispatcher info.
	 *
	 * @param __key The key.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/06/18
	 */
	MLEAtomic(String __key)
		throws NullPointerException
	{
		if (__key == null)
			throw new NullPointerException("NARG");
		
		this.key = __key;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/06/18
	 */
	@Override
	public String key()
	{
		return this.key;
	}
}
