// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.summercoat;

import java.util.HashMap;
import java.util.Map;

/**
 * This contains and records the state for multiple tasks which would be
 * running at once, it allows the status to be retrieved for each task
 * regardless of whether it is directly accessible or not.
 *
 * @since 2019/01/05
 */
public final class TaskStatuses
{
	/** Tasks which are currently available. */
	private final Map<Integer, TaskStatus> _statuses =
		new HashMap<>();
	
	/** The next task ID. */
	private volatile int _nextid;
	
	/**
	 * Waits for all tasks to be terminated (in the exited state).
	 *
	 * @param __ms The number of milliseconds to wait for a check, zero means
	 * to wait forever.
	 * @return {@code true} if all tasks were exited.
	 * @throws InterruptedException If the thread was interrupted while this
	 * was requested.
	 * @since 2019/01/05
	 */
	public final boolean awaitFinished(long __ms)
		throws InterruptedException
	{
		// Lock on self since we control the map
		Map<Integer, TaskStatus> statuses = this._statuses;
		synchronized (this)
		{
			// This loop is only here for the wait forever case because signal
			// might end otherwise.
			for (boolean initrun = true;; initrun = false)
			{
				// Find any task which is not in the exit state, because if
				// there is then 
				boolean notinexit = false;
				for (TaskStatus s : statuses.values())
					if (!s._state.isExited())
					{
						notinexit = true;
						break;
					}
				
				// If no task is running, we know we can quickly terminate
				// without waiting
				if (!notinexit)
					return true;
				
				// If we wanted to wait for a given duration and this is not
				// the initial run, then instead of waiting again just stop
				// saying that things are running
				if (!initrun && __ms != 0)
					return false;
				
				// Wait for signal on self, 0 is to wait forever
				// If InterruptedException is thrown then it will be sent to
				// the calling method accordingly
				this.wait(__ms);
			}
		}
	}
	
	/**
	 * Creates a status holder for a task giving it a unique ID.
	 *
	 * @return The newly created task with a newly allocated ID.
	 * @since 2019/01/05
	 */
	public final TaskStatus createNew()
	{
		// Lock on self
		synchronized (this)
		{
			// Setup new task with this ID
			int tid;
			TaskStatus rv = new TaskStatus((tid = this._nextid++));
			
			// Need to keep track of this task, so we know when it exits
			this._statuses.put(tid, rv);
			
			// And return the created task
			return rv;
		}
	}
}

