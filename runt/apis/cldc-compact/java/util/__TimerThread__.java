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
}

