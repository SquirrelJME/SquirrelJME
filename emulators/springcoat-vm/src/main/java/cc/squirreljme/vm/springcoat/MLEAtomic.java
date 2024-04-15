// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import cc.squirreljme.jvm.mle.AtomicShelf;
import cc.squirreljme.vm.springcoat.exceptions.SpringMLECallError;

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
			synchronized (GlobalState.class)
			{
				// Generate a key which will be returned via the lock
				int key;
				do
				{
					key = (int)MLEAtomic.TICK.handle(__thread);
				} while (key == 0);
				
				// When unlocked the lock will have zero, so we can set it to
				// the key we generated... otherwise we fail here
				if (GlobalState.GC_LOCK.compareAndSet(0, key))
					return key;
				return 0;
			}
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
			synchronized (GlobalState.class)
			{
				// Unlocking is simple and only works if we have the key used
				// to lock the garbage collector
				if (!GlobalState.GC_LOCK
					.compareAndSet((int)__args[0], 0))
					throw new SpringMLECallError("Wrong lock code.");
				
				return null;
			}
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
			int count = (int)__args[0];
			
			if (count < 0)
				throw new SpringMLECallError("Negative spin count.");
			
			// If we spin for too long, instead give up our cycles
			if (count > MLEAtomic._SPIN_LIMIT)
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
			synchronized (GlobalState.class)
			{
				return GlobalState.TICKER.decrementAndGet();
			}
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
