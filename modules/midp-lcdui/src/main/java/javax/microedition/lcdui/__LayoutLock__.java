// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.lcdui;

import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * Lock for the layout.
 *
 * @since 2022/07/20
 */
final class __LayoutLock__
	implements AutoCloseable
{
	/** The thread that is locking. */
	private volatile Thread _thread;
	
	/** The number of usages. */
	private volatile int _useCount; 
	
	/**
	 * Locks the layout.
	 * 
	 * @since 2022/07/20
	 */
	public void lock()
	{
		Thread current = Thread.currentThread();
		synchronized (this)
		{
			for (;;)
			{
				Thread thread = this._thread;
				
				// Do nothing if we locked it
				if (thread == current)
					return;
				
				// Claim it if it is free
				else if (thread == null)
				{
					this._thread = current;
					return;
				}
				
				// Otherwise, wait until it gets free
				try
				{
					this.wait(1000);
				}
				catch (InterruptedException ignored)
				{
				}
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/07/20
	 */
	@Override
	public void close()
	{
		// Performs the opposite of utilize
		synchronized (this)
		{
			/* {@squirreljme.error EB35 Form layout update called outside
			permitted method.} */
			if (this._thread != Thread.currentThread())
				throw new IllegalStateException("EB35");
			
			// Utilize down
			this._useCount = Math.max(0, this._useCount - 1);
		}
	}
	
	/**
	 * Utilizes the lock.
	 * 
	 * @throws IllegalStateException If this was called outside of the
	 * permitted methods.
	 * @since 2022/07/20
	 */
	public __LayoutLock__ utilize()
		throws IllegalStateException
	{
		synchronized (this)
		{
			/* {@squirreljme.error EB34 Form layout update called outside
			permitted method.} */
			if (this._thread != Thread.currentThread())
				throw new IllegalStateException("EB34");
			
			// Utilize up
			this._useCount++;
		}
		
		// Self always
		return this;
	} 
	
	/**
	 * Unlocks the layout.
	 * 
	 * @since 2022/07/20
	 */
	public void unlock()
	{
		Thread current = Thread.currentThread();
		synchronized (this)
		{
			Thread thread = this._thread;
			
			// Ignore if not our own thread
			if (thread != current)
				return;
			
			// Otherwise, reset it
			this._thread = null;
			this._useCount = 0;
			
			// Notify anything waiting for an unlock that it actually
			// happened!
			this.notifyAll();
		}
	}
}
