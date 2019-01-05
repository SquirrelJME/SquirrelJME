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

import cc.squirreljme.vm.VMClassLibrary;
import cc.squirreljme.vm.VMException;
import cc.squirreljme.vm.VMSuiteManager;
import java.util.HashMap;
import java.util.Map;
import net.multiphasicapps.profiler.ProfilerSnapshot;

/**
 * This class contains the root machine which is used to contain and manage
 * all of the various virtual machines which are running in SummerCoat. This
 * is essentially a hub which owns all of the sub virtual machines, handling
 * creation of them and such.
 *
 * @since 2019/01/01
 */
public final class RootMachine
{
	/** The manager for suites. */
	protected final VMSuiteManager suites;
	
	/** The profiler information output. */
	protected final ProfilerSnapshot profiler;
	
	/** The base depth of this virtual machine. */
	protected final int baseguestdepth;
	
	/** Statuses for each task. */
	protected final TaskStatuses statuses =
		new TaskStatuses();
	
	/** Cache of classes. */
	protected final RuntimeClassCache runtimeclasscache =
		new RuntimeClassCache();
	
	/**
	 * Initializes the root machine.
	 *
	 * @param __s The suite manager.
	 * @param __p The profiler snapshot output, this is optional.
	 * @param __gd The guest depth of this VM.
	 * @throws NullPointerException On null arguments, except for {@code __p}.
	 * @since 2019/01/01
	 */
	public RootMachine(VMSuiteManager __s, ProfilerSnapshot __p, int __gd)
		throws NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		this.suites = __s;
		this.profiler = __p;
		this.baseguestdepth = __gd;
	}
	
	/**
	 * Creates a new task which runs within a new instance of the virtual
	 * machine which uses the specified parameters based on the root machine's
	 * suite manager and such.
	 *
	 * @param __cp The classpath used.
	 * @param __maincl The main entry class.
	 * @param __ismid Is this a MIDlet?
	 * @param __sprops System properties.
	 * @param __args Arguments to the task.
	 * @return A new running task which when ran will execute the VM code
	 * accordingly.
	 * @throws NullPointerException On null arguments.
	 * @throws VMException If the task could not be initialized.
	 * @since 2019/01/01
	 */
	public final RunningTask createTask(VMClassLibrary[] __cp, String __maincl,
		boolean __ismid, Map<String, String> __sprops, String[] __args)
		throws NullPointerException, VMException
	{
		if (__cp == null || __maincl == null)
			throw new NullPointerException("NARG");
		
		// Defensive copy so things are not changed
		__cp = __cp.clone();
		__sprops = (__sprops == null ? new HashMap<String, String>() :
			new HashMap<>(__sprops));
		__args = (__args == null ? new String[0] : __args.clone());
		
		// Check for nulls
		for (VMClassLibrary l : __cp)
			if (l == null)
				throw new NullPointerException("NARG");
		for (String s : __args)
			if (s == null)
				throw new NullPointerException("NARG");
		for (Map.Entry<String, String> e : __sprops.entrySet())
			if (e.getKey() == null || e.getValue() == null)
				throw new NullPointerException("NARG");
		
		// Setup class loader for this task
		ClassLoader cl = new ClassLoader(this.runtimeclasscache, suites, __cp);
		
		// Create a new status for this task which contains some global
		// information that is needed
		TaskStatus status = this.statuses.createNew();
		
		// Setup a new base running task, which has no threads yet until the
		// first is created
		RunningTask rv = new RunningTask(status, cl, __sprops, this.profiler);
		
		// Setup main thread
		if (true)
			throw new todo.TODO();
		
		// Return out created task
		return rv;
	}
}

