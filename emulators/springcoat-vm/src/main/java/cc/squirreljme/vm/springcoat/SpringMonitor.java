// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import cc.squirreljme.emulator.profiler.ProfiledFrame;
import cc.squirreljme.jvm.mle.constants.MonitorResultType;
import cc.squirreljme.jvm.mle.constants.ThreadStatusType;
import cc.squirreljme.vm.springcoat.exceptions.SpringIllegalMonitorStateException;

/**
 * This is a monitor which is associated with an object.
 *
 * @since 2018/09/15
 */
public final class SpringMonitor
{
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
		for (;;)
			synchronized (this)
			{
				// We take possession of this monitor
				SpringThread owner = this._owner;
				if (owner == null)
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
						SpringMonitor.__changeState(__t, true);
						
						this.wait();
					}
					catch (InterruptedException e)
					{
						// Ignore
					}
					finally
					{
						SpringMonitor.__changeState(__t, false);
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
		synchronized (this)
		{
			/* {@squirreljme.error BK1c This thread does not own the
			monitor.} */
			if (this._owner != __t)
				throw new SpringIllegalMonitorStateException("BK1c");	
			
			/* {@squirreljme.error BK1d No previous entry call was made.
			(The monitor entry count)} */
			int count = this._count;
			if (count <= 0)
				throw new SpringIllegalMonitorStateException(
					String.format("BK1d %d", count));
			
			// If the count reaches zero, no thread owns this now
			this._count = --count;
			if (count <= 0)
			{
				this._owner = null;
				
				// Wake up all threads so that they try and lock on the lock
				// so whoever gets that chance
				if (__notify)
					this.notifyAll();
			}
		}
	}
	
	/**
	 * Checks if this monitor is held by the given thread.
	 * 
	 * @param __vmThread The virtual machine thread.
	 * @return If this is held by the given thread.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/06/27
	 */
	public boolean isHeldBy(SpringThread __vmThread)
		throws NullPointerException
	{
		if (__vmThread == null)
			throw new NullPointerException("NARG");
		
		synchronized (this)
		{
			return this._owner == __vmThread;
		}
	}
	
	/**
	 * Notifies on this monitor and returns the status.
	 *
	 * @param __by The thread that is doing the notify.
	 * @param __all Notify all threads?
	 * @return The {@link MonitorResultType}.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/20
	 */
	public final int monitorNotify(SpringThread __by, boolean __all)
		throws NullPointerException
	{
		if (__by == null)
			throw new NullPointerException("NARG");
		
		// Lock on the monitor lock
		synchronized (this)
		{
			// Wrong thread?
			if (this._owner != __by)
				return MonitorResultType.NOT_OWNED;
			
			// Nothing is waiting, do not bother at all!
			int waitcount = this._waitcount;
			if (waitcount == 0)
				return 0;
			
			// Notify all threads or just one?
			// Never let the notify count exceed the wait count as well
			int notifycount = this._notifycount;
			this._notifycount = Math.min(waitcount,
				(__all ? waitcount : notifycount + 1));
			
			// Notify all threads that something happened with the lock
			this.notifyAll();
			
			return 0;
		}
	}
	
	/**
	 * Waits on the monitor.
	 *
	 * @param __by The thread doing the wait.
	 * @param __ms The milliseconds to wait.
	 * @param __ns The nanoseconds to wait.
	 * @return The {@link MonitorResultType}.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/21
	 */
	public final int monitorWait(SpringThread __by, long __ms, int __ns)
		throws NullPointerException
	{
		if (__by == null)
			throw new NullPointerException("NARG");
		
		// Lock on the monitor lock
		synchronized (this)
		{
			// Wrong thread?
			if (this._owner != __by)
				return MonitorResultType.NOT_OWNED;
			
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
				System.nanoTime() + (__ms * 1_000_000L) + __ns);
			for (;;)
			{
				// read our wait and notify counts to determine if we
				// should take control here
				int nownotifycount = this._notifycount,
					nowwaitcount = this._waitcount;
				
				// We were notified, interrupted, or expired, take it and leave
				if (interrupted || expired || nownotifycount > 0)
				{
					// Reduce the notify count, but not when interrupted or
					// expired
					if (!interrupted && !expired)
						this._notifycount--;
					
					// Reduce wait count
					int waitcount = this._waitcount;
					this._waitcount = --waitcount;
					
					// Never let the notification count exceed the wait count
					if (this._notifycount > waitcount)
						this._notifycount = waitcount;
					
					// Re-enter the monitor
					this.enter(__by);
					
					// Whatever state we ended up in
					if (interrupted)
						return MonitorResultType.INTERRUPTED;
					return MonitorResultType.NOT_INTERRUPTED;
				}
				
				// Otherwise wait for notification to happen
				else
				{
					// Could be interrupted
					try
					{
						// Indicate that we are waiting on a monitor
						SpringMonitor.__changeState(__by, true);
						
						// Check if time expired
						if (!waitforever)
						{
							long rem = end - System.nanoTime();
							if (rem <= 0)
							{
								expired = true;
								continue;
							}
							
							// Wait for this time
							this.wait(rem / 1_000_000L,
								(int)(rem % 1_000_000L));
						}
						
						// Wait forever
						else
						{
							this.wait();
						}
					}
					
					// Was interrupted
					catch (InterruptedException e)
					{
						interrupted = true;
					}
					
					// Go back to the running state
					finally
					{
						SpringMonitor.__changeState(__by, false);
					}
				}
			}
		}
	}
	
	/**
	 * Changes the state of the thread to indicate monitor status.
	 * 
	 * @param __thread The thread to change the state of.
	 * @param __wait Are we waiting?
	 * @since 2021/04/25
	 */
	private static void __changeState(SpringThread __thread, boolean __wait)
	{
		// Get the profiler information
		SpringThreadFrame currentFrame = __thread.currentFrame();
		ProfiledFrame profiler = (currentFrame == null ? null :
			currentFrame._profiler);
		
		// Is now waiting
		if (__wait)
		{
			// Set as waiting
			__thread.setStatus(ThreadStatusType.MONITOR_WAIT);
			
			// Do not count CPU time
			if (profiler != null)
				profiler.sleep(true, System.nanoTime());
		}
		
		// No longer waiting
		else
		{
			// Is now running
			__thread.setStatus(ThreadStatusType.RUNNING);
			
			// Continue counting CPU time
			if (profiler != null)
				profiler.sleep(false, System.nanoTime());
		}
	}
}

