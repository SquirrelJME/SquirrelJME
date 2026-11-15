// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.util;

import cc.squirreljme.jvm.mle.ThreadShelf;
import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * Manages internal periodic timers.
 * 
 * In SquirrelJME to better manage multiple timers concurrently, especially
 * in cooperatively threaded systems, rather than having them be individual
 * threads all times are managed by this class. This also is for systems
 * that only support a limited number of active threads.
 * 
 * Compared to the older 2018 code, this does technically allow for
 * any {@link TimerTask} instance to be run under multiple {@link Timer}s. 
 *
 * @since 2026/10/10
 */
final class __PeriodicTimers__
	implements Runnable
{
	/** The actual thread which owns the timers. */
	static volatile Thread _THREAD; 
	
	/** The periodic timer singleton instance. */
	static volatile __PeriodicTimers__ _INSTANCE;
	
	/** Timers which have been registered. */
	private final List<__PeriodicTimer__> _timers =
		new ArrayList<>(16);
	 
	/**
	 * {@inheritDoc}
	 * @since 2026/10/10
	 */
	@Override
	public void run()
	{
		List<__PeriodicTimer__> timers = this._timers;
		synchronized (this)
		{
			for (;;)
			{
				// What is the current time?
				long nowTime = System.nanoTime();
				long sleepOffset = 30_000L;
				
				// Round-robin through timers to find one to execute
				__PeriodicTimer__ soonest = null;
				for (int i = 0, n = timers.size(); i < n; i++)
				{
					// Ignore any blank slots
					__PeriodicTimer__ check = timers.get(i);
					if (check == null)
						continue;
					
					// Is this timer even eligible to run?
					if (!check.__eligible(nowTime))
					{
						// Use the shorter sleep offset, if applicable
						sleepOffset = Math.min(sleepOffset,
							check.__sleepOffset(nowTime));
						continue;
					}
					
					// No previous timer was set, or this is sooner?
					// Use this one if so
					if (soonest == null || check.__isBefore(soonest))
						soonest = check;
				}
				
				// Debugging
				if (Debugging.verbose())
					Debugging.debugNote("Timer cycle: %d %08x",
						nowTime, System.identityHashCode(soonest));
				
				// No timer to run, sleep for a while
				if (soonest == null)
					try
					{
						// Debugging
						if (Debugging.verbose())
							Debugging.debugNote("Timer sleep: %d ms",
								sleepOffset);
						
						// Wait for a signal
						if (sleepOffset > 1)
							this.wait(sleepOffset, 0);
						
						// Always re-run the loop
						continue;
					}
					catch (InterruptedException ignored)
					{
						// Always re-run the loop
						continue;
					}
				
				// Catch all exceptions the task could possibly throw as we
				// do not want this loop to be broken at all
				try
				{
					// Debugging
					if (Debugging.verbose())
						Debugging.debugNote("Exec timer: %s %08x",
							soonest._task.getClass().getName(),
							System.identityHashCode(soonest));
					
					// Run the task
					soonest._task.run();
				}
				catch (Throwable __t)
				{
					// Debug print
					__t.printStackTrace();
					
					// Always cancel on failure
					this.__cancel(soonest._task);
					
					// Skip repeat logic
					continue;
				}
				
				// Determine the next fire time, or if the task is to be
				// cancelled
				if (soonest.__charge(nowTime) == Long.MAX_VALUE)
					this.__cancel(soonest._task);
			}
		}
	}
	
	/**
	 * Cancels all tasks owned by the given timer.
	 *
	 * @param __timer The timer to cancel tasks for.
	 * @throws NullPointerException On null arguments.
	 * @since 2026/10/10
	 */
	void __cancel(Timer __timer)
		throws NullPointerException
	{
		if (__timer == null)
			throw new NullPointerException("NARG");
		
		List<__PeriodicTimer__> timers = this._timers;
		synchronized (this)
		{
			// Look through all timers
			for (Iterator<__PeriodicTimer__> it = timers.iterator();
				it.hasNext();)
			{
				// If the timer is in the list, then cancel it
				__PeriodicTimer__ timer = it.next();
				if (timer._timer == __timer)
					this.__cancel(it, timer);
			}
		}
	}
	
	/**
	 * Cancels the specific task.
	 *
	 * @param __task The task to cancel.
	 * @throws NullPointerException On null arguments.
	 * @since 2026/10/10
	 */
	boolean __cancel(TimerTask __task)
		throws NullPointerException
	{
		if (__task == null)
			throw new NullPointerException("NARG");
		
		List<__PeriodicTimer__> timers = this._timers;
		synchronized (this)
		{
			// Look through all timers
			for (Iterator<__PeriodicTimer__> it = timers.iterator();
				it.hasNext();)
			{
				// If the timer is in the list, then it is scheduled
				__PeriodicTimer__ timer = it.next();
				if (timer._task == __task)
					if (this.__cancel(it, timer))
					{
						// Notify self
						this.notifyAll();
						
						// Interrupt the thread
						Thread thread = __PeriodicTimers__._THREAD;
						thread.interrupt();
						
						// Was cancelled
						return true;
					}
			}
		}
		
		// Otherwise, nothing was cancelled
		return false;
	}
	
	/**
	 * Performs the actual cancel of the given periodic timer.
	 *
	 * @param __it The iterator to remove from.
	 * @param __timer The periodic timer to remove.
	 * @return If it was actually cancelled.
	 * @throws NullPointerException On null arguments.
	 * @since 2026/10/10
	 */
	private boolean __cancel(Iterator<__PeriodicTimer__> __it,
		__PeriodicTimer__ __timer)
		throws NullPointerException
	{
		if (__it == null || __timer == null)
			throw new NullPointerException("NARG");
		
		synchronized (this)
		{
			// Always remove it
			__it.remove();
			
			// Then cancel it if it already has not been
			return __timer.__cancel();
		}
	}
	
	/**
	 * Is the given task scheduled?
	 *
	 * @param __task The task to check.
	 * @return If this task is scheduled.
	 * @throws NullPointerException On null arguments.
	 * @since 2026/10/10
	 */
	boolean __isScheduled(TimerTask __task)
		throws NullPointerException
	{
		if (__task == null)
			throw new NullPointerException("NARG");
		
		List<__PeriodicTimer__> timers = this._timers;
		synchronized (this)
		{
			// Look through all timers
			for (Iterator<__PeriodicTimer__> it = timers.iterator();
				it.hasNext();)
			{
				// If the timer is in the list, then it is scheduled
				__PeriodicTimer__ timer = it.next();
				if (timer._task == __task)
					return true;
			}
		}
		
		// Otherwise, it is not
		return false;
	}
	
	/**
	 * Purges all tasks owned by the specified timer.
	 *
	 * @param __timer The timer to purge.
	 * @throws NullPointerException On null arguments.
	 * @since 2026/10/10
	 */
	void __purge(Timer __timer)
		throws NullPointerException
	{
		if (__timer == null)
			throw new NullPointerException("NARG");
		
		List<__PeriodicTimer__> timers = this._timers;
		synchronized (this)
		{
			// If the timer is in the list, then it is scheduled
			for (Iterator<__PeriodicTimer__> it = timers.iterator();
				it.hasNext();)
			{
				// Belongs to a different timer?
				__PeriodicTimer__ timer = it.next();
				if (timer._timer != __timer)
					continue;
				
				// Just remove it from the list here, this will eventually
				// and at some point GC it
				it.remove();
			}
		}
	}
	
	/**
	 * Schedules the given periodic timer.
	 *
	 * @param __timer The timer to schedule.
	 * @throws IllegalStateException If this task is already scheduled.
	 * @throws NullPointerException On null arguments.
	 * @since 2026/10/10
	 */
	void __schedule(__PeriodicTimer__ __timer)
		throws IllegalStateException, NullPointerException
	{
		if (__timer == null)
			throw new NullPointerException("NARG");
		
		synchronized (this)
		{
			// Already scheduled?
			if (this.__isScheduled(__timer._task))
				throw new IllegalStateException("EXST");
			
			// It just gets added to the end of the timer list
			this._timers.add(__timer);
			
			// Notify self
			this.notifyAll();
			
			// Interrupt the thread
			Thread thread = __PeriodicTimers__._THREAD;
			thread.interrupt();
		}
	}
	
	/**
	 * Calculates the scheduled execution time for the given task.
	 *
	 * If this task has not been scheduled, this value is undefined.
	 *
	 * @param __timerTask The task to check.
	 * @return The scheduled execution time.
	 * @throws NullPointerException On null arguments.
	 * @since 2026/10/10
	 */
	long __scheduledExecutionTime(TimerTask __timerTask)
		throws NullPointerException
	{
		if (__timerTask == null)
			throw new NullPointerException("NARG");
		
		List<__PeriodicTimer__> timers = this._timers;
		synchronized (this)
		{
			// If not scheduled, return some invalid time far into the
			// future
			if (!this.__isScheduled(__timerTask))
				return Long.MAX_VALUE;
			
			// Look through all timers
			for (Iterator<__PeriodicTimer__> it = timers.iterator();
				it.hasNext();)
			{
				// Skip timers that are not this
				__PeriodicTimer__ timer = it.next();
				if (timer._task != __timerTask)
					continue;
				
				// Return the next scheduled execution time
				return timer.__scheduledExecutionTime();
			}
		}
		
		// Not found in timers, never scheduled so return a time far in
		// the future
		return Long.MAX_VALUE;
	}
	
	/**
	 * Returns the periodic timer instance.
	 *
	 * @return The periodic timer instance.
	 * @since 2026/10/10
	 */
	static __PeriodicTimers__ __instance()
	{
		synchronized (__PeriodicTimers__.class)
		{
			// Already exists?
			__PeriodicTimers__ result = __PeriodicTimers__._INSTANCE;
			if (result != null)
				return result;
			
			// Setup instance
			result = new __PeriodicTimers__();
			__PeriodicTimers__._INSTANCE = result;
			
			// Start new thread to run timers
			// This is always a daemon thread so it does not keep the VM alive
			Thread thread = new Thread(result, "PeriodicTimers");
			__PeriodicTimers__._THREAD = thread;
			ThreadShelf.javaThreadSetDaemon(thread);
			thread.start();
			
			// Return the new instance
			return result;
		}
	}
}
