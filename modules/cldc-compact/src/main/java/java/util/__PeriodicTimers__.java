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
		new LinkedList<>();
	 
	/**
	 * {@inheritDoc}
	 * @since 2026/10/10
	 */
	@Override
	public void run()
	{
		throw Debugging.todo();
		
		/*List<TimerTask> tasks = this._tasks;
		
		// Constantly loop on our own lock since we will mess with things
		for (;;)
			synchronized (this)
			{
				if (this._cancel)
				{
					// Set all tasks to cancel
					for (TimerTask t : tasks)
						t._cancel = true;
					
					// Clear all the tasks, because we no longer need them
					tasks.clear();
					
					// And just stop executing
					return;
				}
				
				// Task to run
				TimerTask execute = null;
				
				// Need to determine how long to wait to run a task for
				try
				{
					// If there are no tasks to run, then we wait forever
					if (tasks.isEmpty())
						this.wait();
					
					// Otherwise, see how long we need to wait
					else
					{
						// Need to determine if we are running this task
						// or just waiting
						TimerTask next = tasks.get(0);
						long now = System.currentTimeMillis(),
							sched = next._schedtime;
						
						// We cancelled the task, so remove and do not bother
						// at all
						if (next._cancel)
						{
							tasks.remove(0);
							continue;
						}
						
						// We can execute it!
						else if (sched <= now)
						{
							execute = next;
							tasks.remove(0);
						}
						
						// Wait around for it to happen, but another event
						// could come before this!
						else
							this.wait(sched - now);
					}
				}
				
				// If interrupted, try another run of the loop
				catch (InterruptedException e)
				{
					continue;
				}
				
				// Execute if things are to be done
				if (execute != null)
				{
					// We need to set the last one because fixed scheduling
					// will set a new schedule time while delayed will wait
					// on that
					long schedtime = execute._schedtime;
					execute._lastrun = schedtime;
					
					// Fixed scheduling has it where the next event gets the
					// period added to the scheduling time. This way if the
					// task runs too slowly it gets built up.
					boolean repeated = execute._repeated,
						fixed = execute._fixed;
					long period = execute._period;
					if (repeated && fixed)
						schedtime += period;
					
					// Execute the task
					execute._inrun = true;
					try
					{
						execute.run();
					}
					catch (Throwable t)
					{
						// Ignore
						t.printStackTrace();
					}
					execute._inrun = false;
					
					// Repeat as long as the task is not cancelled
					if (repeated && !execute._cancel)
					{
						// If not fixed use delay from the end of this
						// execution
						if (!fixed)
							schedtime = System.currentTimeMillis() + period;
						
						// Schedule for re-execution
						execute._schedtime = schedtime;
						this.__addTask(execute);
					}
				}
			}
			
		 */
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
					return this.__cancel(it, timer);
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
	 * @return If it was actually cancelled and removed from the iterator.
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
			throw Debugging.todo();
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
			
			throw Debugging.todo();
		}
		
		/*if (__task == null || __first == null)
			throw new NullPointerException("NARG");
		
		// Need to determine when 
		long datemilli = __first.getTime(),
			nowtime = System.currentTimeMillis(),
			diff = datemilli - nowtime;
		
		/* {@squirreljme.error ZZ3m Cannot use a date which is far into the
		past.} * /
		if (datemilli < 0)
			throw new IllegalArgumentException("ZZ3m");
		
		// Schedule immedietly?
		if (diff < 0)
			diff = 0;
		
		// Forward since we use fixed delay schedule
		this.__schedule(__task, diff, __rep, __fixed, __period);
		
		if (__task == null)
			throw new NullPointerException("NARG");
		
		/* {@squirreljme.error ZZ3n The delay cannot be negative.} * /
		if (__delay < 0)
			throw new IllegalArgumentException("ZZ3n");
		
		/* {@squirreljme.error ZZ3o The period cannot be zero or negative.} * /
		if (__rep && __period <= 0)
			throw new IllegalArgumentException("ZZ3o");
		
		// When is the time to be scheduled?
		long now = System.currentTimeMillis(),
			sched = now + __delay;
		
		// Lock on self
		List<TimerTask> tasks = this._tasks;
		synchronized (this)
		{
			/* {@squirreljme.error ZZ3p Cannot add a task to a timer which
			was cancelled or a task which was cancelled.} * /
			if (this._cancel || __task._cancel)
				throw new IllegalStateException("ZZ3p");
			
			// Set task properties
			__task._schedtime = sched;
			__task._scheduled = true;
			__task._repeated = __rep;
			__task._fixed = __fixed;
			__task._period = __period;
			
			// Add the task
			this.__addTask(__task);
			
			// And notify that there is a new task in place
			this.notifyAll();
		}
		
		 */
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
		
		throw Debugging.todo();
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
