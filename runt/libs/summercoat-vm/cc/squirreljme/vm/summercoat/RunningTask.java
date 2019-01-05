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

import cc.squirreljme.vm.VirtualMachine;
import cc.squirreljme.vm.VMClassLibrary;
import cc.squirreljme.vm.VMException;
import cc.squirreljme.vm.VMSuiteManager;
import java.util.HashMap;
import java.util.Map;
import net.multiphasicapps.profiler.ProfilerSnapshot;

/**
 * This represents a task which is running within the virtual machine.
 *
 * @since 2019/01/01
 */
public final class RunningTask
{
	/** The status of this task. */
	protected final TaskStatus status;
	
	/** The class loader. */
	protected final ClassLoader classloader;
	
	/** The profiler information output. */
	protected final ProfilerSnapshot profiler;
	
	/** System properties. */
	private final Map<String, String> _sysprops;
	
	/** Threads which are available for usage. */
	private final Map<Integer, RunningThread> _threads =
		new HashMap<>();
	
	/** The next thread ID. */
	private volatile int _nextthreadid;
	
	/**
	 * Initializes the running task.
	 *
	 * @param __st The task status.
	 * @param __cl The class loader.
	 * @param __sprops System properties.
	 * @param __p Profiler information.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/01/05
	 */
	public RunningTask(TaskStatus __st, ClassLoader __cl,
		Map<String, String> __sprops, ProfilerSnapshot __p)
		throws NullPointerException
	{
		if (__st == null || __cl == null)
			throw new NullPointerException("NARG");
		
		// Defensive copy and check for nulls
		__sprops = (__sprops == null ? new HashMap<String, String>() :
			new HashMap<>(__sprops));
		for (Map.Entry<String, String> e : __sprops.entrySet())
			if (e.getKey() == null || e.getValue() == null)
				throw new NullPointerException("NARG");
		
		// Set
		this.status = __st;
		this.classloader = __cl;
		this.profiler = __p;
		this._sysprops = __sprops;
	}
}

