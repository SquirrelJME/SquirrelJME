// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import cc.squirreljme.runtime.cldc.asm.ObjectAccess;
import java.util.LinkedHashSet;
import java.util.Set;

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
	volatile SpringThread _owner;
	
	/** Number of threads which are waiting on this monitor. */
	volatile int _waitcount;
	
	/** The number of notifications happening. */
	volatile int _notifycount;
	
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
	 * @param __notify Should threads be notified that an unlock happened?
	 * @throws NullPointerException On null arguments.
	 * @throws SpringIllegalMonitorStateException If the monitor is not owned
	 * by this thread.
	 * @since 2018/09/15
	 */
	public final void exit(SpringThread __t, boolean __notify)
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
				if (__notify)
					lock.notifyAll();
			}
		}
	}
	
	/**
	 * Notifies on this monitor and returns the status.
	 *
	 * @param __by The thread that is doing the notify.
	 * @param __all Notify all threads?
	 * @return The notification status.
	 * @throws NullPointerException
	 * @since 2018/11/20
	 */
	public final int monitorNotify(SpringThread __by, boolean __all)
		throws NullPointerException
	{
		if (__by == null)
			throw new NullPointerException("NARG");
		
		// Lock on the monitor lock
		Object lock = this.lock;
		synchronized (lock)
		{
			// Wrong thread?
			if (this._owner != __by)
				return ObjectAccess.MONITOR_NOT_OWNED;
			
			// Nothing is waiting, do not bother at all!
			int waitcount = this._waitcount;
			if (waitcount == 0)
				return 0;
			
			// Notify all threads or just one?
			this._notifycount = (__all ? waitcount : 1);
			
			// Notify all threads that something happened with the lock
			lock.notifyAll();
			
			return 0;
		}
	}
	
	/**
	 * Waits on the monitor.
	 *
	 * @param __by The thread doing the wait.
	 * @param __ms The milliseconds to wait.
	 * @param __ns The nanoseconds to wait.
	 * @return The wait result.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/21
	 */
	public final int monitorWait(SpringThread __by, long __ms, int __ns)
		throws NullPointerException
	{
		if (__by == null)
			throw new NullPointerException("NARG");
		
		// Lock on the monitor lock
		Object lock = this.lock;
		synchronized (lock)
		{
			// Wrong thread?
			if (this._owner != __by)
				return ObjectAccess.MONITOR_NOT_OWNED;
			
			// Increase our wait count
			this._waitcount++;
			
			// Relinquish control of this monitor, so that way when we actually
			// internally do the wait we check notify counts and such.
			this.exit(__by, true);
			
			// Do looped wait, but it may be timed
			boolean waitforever = (__ms == 0 && __ns == 0),
				interrupted = false,
				expired = false;
			long end = (waitforever ? Long.MAX_VALUE :
				System.nanoTime() + (__ms * 1000000L) + __ns);
			for (;;)
			{
				// read our wait and notify counts to determine if we
				// should take control here
				int nownotifycount = this._notifycount,
					nowwaitcount = this._waitcount;
				
				// We were notified, interrupted, or expired, take it and leave
				if (interrupted || expired || nownotifycount > 0)
				{
					// Reduce the notify count, but not when interrupted
					if (!interrupted)
						this._notifycount--;
					
					// Reduce wait count
					this._waitcount--;
					
					// Re-enter the monitor
					this.enter(__by);
					
					// Whatever state we ended up in
					if (interrupted)
						return ObjectAccess.MONITOR_INTERRUPTED;
					return ObjectAccess.MONITOR_NOT_INTERRUPTED;
				}
				
				// Otherwise wait for notification to happen
				else
				{
					// Could be interrupted
					try
					{
						// Check if time expired
						if (!waitforever)
						{
							long rem = end - System.nanoTime();
							if (rem < 0)
								continue;
							
							// Wait for this time
							lock.wait(rem / 1_000_000L,
								(int)(rem % 1_000_000L));
						}
						
						// Wait forever
						else
						{
							lock.wait();
						}
					}
					
					// Was interrupted
					catch (InterruptedException e)
					{
						interrupted = true;
					}
				}
			}
		}
	}
	
	/**
	 * Waiting information on this monitor.
	 *
	 * @since 2018/11/20
	 */
	private static final class __Waiting__
	{
	}
}

