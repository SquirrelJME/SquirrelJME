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

import cc.squirreljme.runtime.cldc.asm.NativeDisplayEventCallback;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;
import net.multiphasicapps.profiler.ProfilerSnapshot;

/**
 * This contains the status for a single task and is used to detect when a
 * task is running and if it has ever exited.
 *
 * @since 2019/01/05
 */
public class TaskStatus
{
	/** The monitor or the task statuses, used to signal state changes. */
	protected final Object monitor;
	
	/** The ID of this task. */
	protected final int id;
	
	/** The class loader. */
	protected final ClassLoader classloader;
	
	/** The profiler information output. */
	protected final ProfilerSnapshot profiler;
	
	/** System properties. */
	private final Map<String, String> _sysprops;
	
	/** Threads which are running. */
	private final Map<Integer, Reference<RunningThread>> _threads =
		new HashMap<>();
	
	/** The exit code for this virtual machine. */
	volatile int _exitcode =
		Integer.MIN_VALUE;
	
	/** The state of the task, is initially starting. */
	volatile TaskState _state =
		TaskState.STARTING;
	
	/** Requests that the task be stopped/exited. */
	volatile boolean _requestexit;
	
	/**
	 * The event callback for this task for LCDUI. This is used here because
	 * a task could be placed in the foreground (switched to) in which case
	 * it needs to have UI events sent to it.
	 */
	volatile NativeDisplayEventCallback _eventcallback;
	
	/**
	 * Initializes the task.
	 *
	 * @param __m The monitor to notify when the task changes state.
	 * @param __id The ID of the task.
	 * @param __cl The class loader used.
	 * @param __sp System properties for this task.
	 * @param __ps Snapshot for the profiler, is optional.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/01/05
	 */
	public TaskStatus(Object __m, int __id, ClassLoader __cl,
		Map<String, String> __sp, ProfilerSnapshot __ps)
		throws NullPointerException
	{
		if (__m == null || __cl == null)
			throw new NullPointerException("NARG");
		
		// Defensive copy and check for nulls
		__sp = (__sp == null ? new HashMap<String, String>() :
			new HashMap<>(__sp));
		for (Map.Entry<String, String> e : __sp.entrySet())
			if (e.getKey() == null || e.getValue() == null)
				throw new NullPointerException("NARG");
		
		this.monitor = __m;
		this.id = __id;
		this.classloader = __cl;
		this.profiler = __ps;
		this._sysprops = __sp;
	}
	
	/**
	 * Registers this thread with this task status, it will have a weak
	 * reference to it and will exist provided it is still running.
	 *
	 * @param __t The thread to register.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/01/10
	 */
	final void __registerThread(RunningThread __t)
		throws NullPointerException
	{
		if (__t == null)
			throw new NullPointerException("NARG");
		
		// Lock on self to add the thread
		synchronized (this)
		{
			this._threads.put(__t.id, new WeakReference<>(__t));
		}
	}
}

