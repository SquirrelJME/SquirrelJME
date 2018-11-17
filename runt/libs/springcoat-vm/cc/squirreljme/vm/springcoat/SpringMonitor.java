// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.springcoat.vm;

/**
 * This is a monitor which is associated with an object.
 *
 * @since 2018/09/15
 */
public final class SpringMonitor
{
	/** The monitor lock. */
	protected final Object lock =
		new Object();
	
	/** The thread which owns this monitor. */
	private volatile SpringThread _owner;
	
	/** The entry count on the monitor. */
	private int _count;
	
	/**
	 * Enters the monitor.
	 *
	 * @param __t The thread trying to lock the monitor.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/15
	 */
	public final void enter(SpringThread __t)
		throws NullPointerException
	{
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// Lock on the monitor lock
		Object lock = this.lock;
		for (;;)
			synchronized (lock)
			{
				// We take possession of this monitor
				SpringThread owner = this._owner;
				if (this._owner == null)
				{
					this._owner = __t;
					this._count = 1;
					return;
				}
				
				// We own the monitor, so increase the count
				else if (owner == __t)
				{
					this._count++;
					return;
				}
				
				// Need to wait for it to be cleared
				else
				{
					// Wait for lock to be freed
					try
					{
						lock.wait();
					}
					catch (InterruptedException e)
					{
						// Ignore
					}
				}
			}
	}
	
	/**
	 * Exits the monitor.
	 *
	 * @param __t The thread exiting the monitor.
	 * @throws NullPointerException On null arguments.
	 * @throws SpringIllegalMonitorStateException If the monitor is not owned
	 * by this thread.
	 * @since 2018/09/15
	 */
	public final void exit(SpringThread __t)
		throws NullPointerException, SpringIllegalMonitorStateException
	{
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// Lock on the monitor lock
		Object lock = this.lock;
		synchronized (lock)
		{
			// {@squirreljme.error BK0x This thread does not own the
			// monitor.}
			if (this._owner != __t)
				throw new SpringIllegalMonitorStateException("BK0x");	
			
			// {@squirreljme.error BK0y No previous entry call was made.
			// (The monitor entry count)}
			int count = this._count;
			if (count <= 0)
				throw new SpringIllegalMonitorStateException(
					String.format("BK0y %d", count));
			
			// If the count reaches zero, no thread owns this now
			this._count = --count;
			if (count <= 0)
			{
				this._owner = null;
				
				// Wake up all threads so that they try and lock on the lock
				// so whoever gets that chance
				lock.notifyAll();
			}
		}
	}
}

