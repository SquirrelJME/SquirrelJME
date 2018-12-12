// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.util;

/**
 * This is a thread which provides the basis for the timer.
 *
 * @since 2018/12/11
 */
final class __TimerThread__
	extends Thread
{
	/**
	 * Tasks which may be running, note that this is not a binary heap as
	 * Java SE says since there should not be thousands of running tasks.
	 * This is Java ME and timers are less likely to be heavy.
	 */
	final List<TimerTask> _tasks =
		new ArrayList<>();
	
	/** Cancel execution? */
	volatile boolean _cancel;
	
	/**
	 * Initializes the thread.
	 *
	 * @param __n The thread name.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/12/11
	 */
	__TimerThread__(String __n)
		throws NullPointerException
	{
		super(__n);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/11
	 */
	@Override
	public final void run()
	{
		List<TimerTask> tasks = this._tasks;
		
		// Constantly loop on our own look since we will mess with things
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
					
					// Otherwise see how long we need to wait
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
					throw new todo.TODO();
				}
			}
	}
	
	/**
	 * Schedules the task.
	 *
	 * @param __task The task to run.
	 * @param __first The time when the task should run.
	 * @param __rep Repeat the task?
	 * @param __fixed Fixed delays from execution?
	 * @param __period The period between each repetition.
	 * @throws IllegalArgumentException If the date is negative or the
	 * period is zero or negative.
	 * @throws IllegalStateException If a task was already scheduled, a task
	 * was cancelled, or this timer was cancelled.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/12/11
	 */
	void __schedule(TimerTask __task, Date __first,
		boolean __rep, boolean __fixed, long __period)
		throws IllegalArgumentException, IllegalStateException,
			NullPointerException
	{
		if (__task == null || __first == null)
			throw new NullPointerException("NARG");
		
		// Need to determine when 
		long datemilli = __first.getTime(),
			nowtime = System.currentTimeMillis(),
			diff = datemilli - nowtime;
		
		// {@squirreljme.error ZZ3n Cannot use a date which is far into the
		// past.}
		if (datemilli < 0)
			throw new IllegalArgumentException("ZZ3n");
		
		// Schedule immedietly?
		if (diff < 0)
			diff = 0;
		
		// Forward since we use fixed delay schedule
		this.__schedule(__task, diff, __rep, __fixed, __period);
	}
	
	/**
	 * Schedules the task.
	 *
	 * @param __task The task to run.
	 * @param __delay The delay before the first invocation.
	 * @param __rep Repeat the task?
	 * @param __fixed Fixed delays from execution?
	 * @param __period The period between each repetition.
	 * @throws IllegalArgumentException If the delay is negative or the
	 * period is zero or negative.
	 * @throws IllegalStateException If a task was already scheduled, a task
	 * was cancelled, or this timer was cancelled.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/12/11
	 */
	void __schedule(TimerTask __task, long __delay,
		boolean __rep, boolean __fixed, long __period)
		throws IllegalArgumentException, IllegalStateException,
			NullPointerException
	{
		if (__task == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error ZZ3o The delay cannot be negative.}
		if (__delay < 0)
			throw new IllegalArgumentException("ZZ3o");
		
		// {@squirreljme.error ZZ3p The period cannot be zero or negative.}
		if (__rep && __period <= 0)
			throw new IllegalArgumentException("ZZ3p");
		
		// When is the time to be scheduled?
		long now = System.currentTimeMillis(),
			sched = now + __delay;
		
		// Lock on self
		List<TimerTask> tasks = this._tasks;
		synchronized (this)
		{
			// {@squirreljme.error ZZ3q Cannot add a task to a timer which
			// was cancelled or a task which was cancelled.}
			if (this._cancel || __task._cancel)
				throw new IllegalStateException("ZZ3q");
			
			// Set task properties
			__task._schedtime = sched;
			__task._scheduled = true;
			__task._repeated = __rep;
			__task._fixed = __fixed;
			__task._period = __period;
			
			// Add task to the task list, but in task sorted order
			// It is always inserted into the correct location
			int dx = (tasks.isEmpty() ? 0 :
				Collections.<TimerTask>binarySearch(tasks,
				__task, new __TaskSchedComparator__()));
			if (dx < 0)
				dx = (-(dx) - 1);
			
			// Add task at the index we found it should be at
			tasks.add(dx, __task);
			
			// And notify that there is a new task in place
			this.notifyAll();
		}
	}
}

