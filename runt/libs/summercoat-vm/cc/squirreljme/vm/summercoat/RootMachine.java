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
	
	/** Cache of SCRF classes. */
	protected final RegisterClassCache scrfcache =
		new RegisterClassCache();
	
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
		
		// Normalize the main class
		__maincl = __maincl.replace('.', '/');
		
		// Create a new status for this task which contains some global
		// information that is needed, it needs our system properties and the
		// classpath since they both may be accessed
		ClassLoader cl;
		TaskStatus status = this.statuses.createNew(
			(cl = new ClassLoader(this.scrfcache, suites, __cp)),
			__sprops, this.profiler);
		
		// Setup a new base running task, which has no threads yet until the
		// first is created
		RunningTask rv = new RunningTask(status);
		
		// Create a new main thread which will be where our execution context
		// will be (since we need to initialize objects)
		RunningThread thr = rv.createThread();
		
		// Determine the entry method and the entry arguments to use
		Instance vmsm;
		Instance entryarg;
		if (__ismid)
		{
			vmsm = thr.vmStaticMethod(false,
				"javax/microedition/midlet/MIDlet",
				"startApp", "()V");
			entryarg = thr.vmTranslateString(__maincl);
		}
		else
		{
			vmsm = thr.vmStaticMethod(true, __maincl,
				"main", "([Ljava/lang/String;)V");
			
			// Setup array that is the same size as the input arguments
			int n = __args.length;
			ArrayInstance ai = thr.vmNewArray("[Ljava/lang/String;", n);
			
			// Translate string arguments
			for (int i = 0; i < n; i++)
				ai.set(i, thr.vmTranslateString(__args[i]));
			
			// We just pass this to the thread
			entryarg = ai;
		}
		
		// Create main thread instance which uses our given starting points
		// accordingly so it knows which method to invoke
		Instance threadobj = thr.vmNewInstance("java/lang/Thread",
			"(Ljava/lang/String;ILcc/squirreljme/runtime/cldc/asm/" +
			"StaticMethod;Ljava/lang/Object;)V", thr.vmTranslateString("Main"),
			IntegerValue.of((__ismid ? 3 : 4)), vmsm, entryarg);
		
		// Enter the __start() method for Thread
		thr.execEnterMethod(false, "java/lang/Thread", "__start", "()V");
		
		// Now that the thread has been initialized it must be started, it
		// will keep running executing the method it starts in until
		// termination occurs
		thr.start();
		
		// Return out created task
		return rv;
	}
}

