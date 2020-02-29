// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.task;

/**
 * This class manages threads within the system.
 *
 * @since 2019/10/13
 */
public final class ThreadManager
{
	/** Default number of threads. */
	private static final int _DEFAULT_THREAD_COUNT =
		16;
	
	/** The number of slots to grow by when out of threads. */
	private static final int _GROW_SIZE =
		8;
	
	/** This is the bootstrap main thread. */
	public final TaskThread BOOT_THREAD =
		new TaskThread(0, 0, 0);
	
	/** Threads that currently exist. */
	private TaskThread[] _threads =
		new TaskThread[ThreadManager._DEFAULT_THREAD_COUNT];
	
	/** The next logical thread id. */
	private int _nextlid;
	
	/**
	 * Always fills in the boot thread.
	 *
	 * @since 2019/10/19
	 */
	{
		this._threads[0] = this.BOOT_THREAD;
	}
	
	/**
	 * Returns any thread that is owned by the given task.
	 *
	 * @param __pid The process ID.
	 * @return The thread owned by the given task or {@code null}.
	 * @since 2019/12/14
	 */
	public final TaskThread anyThreadOwnedByTask(int __pid)
	{
		// Lock self to inspect threads
		TaskThread[] threads = this._threads;
		synchronized (this)
		{
			// The array may be dynamically resized
			int n = threads.length;
			
			// Search for a thread
			for (int i = 0; i < n; i++)
			{
				TaskThread thread = threads[i];
				
				if (thread == null || thread.processId() != __pid)
					continue;
				
				return thread;
			}
		}
		
		// Not found
		return null;
	}
	
	/**
	 * Creates the specified thread owned by the given task.
	 *
	 * @param __pid The owning PID.
	 * @return The thread which was created.
	 * @since 2019/10/13
	 */
	public final TaskThread createThread(int __pid)
	{
		TaskThread rv;
		
		// Need to lock on our own manager, since only a single thread an
		// request a new thread
		TaskThread[] threads = this._threads;
		synchronized (this)
		{
			// The array may be dynamically resized
			int n = threads.length;
			
			// Find free spot in physical thread list
			int freespot = -1;
			for (int i = 0; i < n; i++)
				if (threads[i] == null)
				{
					freespot = i;
					break;
				}
			
			// If we ran out of free spots then we need to grow the array to
			// fit more threads
			if (freespot < 0)
			{
				// Setup and copy source
				TaskThread[] newthreads = new TaskThread[n + ThreadManager._GROW_SIZE];
				for (int i = 0; i < n; i++)
					newthreads[i] = threads[i];
				
				// Set as new
				this._threads = (threads = newthreads);
				
				// Use the old size as the end point
				freespot = n;
			}
			
			// Setup thread object itself and store
			rv = new TaskThread(__pid, freespot, ++this._nextlid);
			threads[freespot] = rv;
		}
		
		return rv;
	}
}

