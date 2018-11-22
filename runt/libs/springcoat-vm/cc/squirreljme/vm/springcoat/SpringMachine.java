// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import cc.squirreljme.runtime.cldc.asm.TaskAccess;
import cc.squirreljme.runtime.cldc.lang.GuestDepth;
import cc.squirreljme.runtime.swm.EntryPoint;
import cc.squirreljme.runtime.swm.EntryPoints;
import cc.squirreljme.vm.VirtualMachine;
import cc.squirreljme.vm.VMClassLibrary;
import cc.squirreljme.vm.VMNativeDisplayAccess;
import cc.squirreljme.vm.VMResourceAccess;
import cc.squirreljme.vm.VMSuiteManager;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.multiphasicapps.classfile.ClassFile;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.ConstantValueString;
import net.multiphasicapps.classfile.InvalidClassFormatException;
import net.multiphasicapps.classfile.MethodDescriptor;
import net.multiphasicapps.classfile.MethodNameAndType;
import net.multiphasicapps.profiler.ProfilerSnapshot;
import net.multiphasicapps.tool.manifest.JavaManifest;

/**
 * This class contains the instance of the SpringCoat virtual machine and has
 * a classpath along with all the needed storage for variables and such.
 *
 * @since 2018/07/29
 */
public final class SpringMachine
	implements Runnable, VirtualMachine
{
	/** Lock. */
	public final Object strlock =
		new Object();
	
	/** The class loader. */
	protected final SpringClassLoader classloader;
	
	/** Resources accessor. */
	protected final VMResourceAccess resourceaccessor;
	
	/** The boot class. */
	protected final String bootcl;
	
	/** Is the boot a midlet? */
	protected final boolean bootmid;
	
	/** The boot index. */
	protected final int bootdx;
	
	/** The manager for suites. */
	protected final VMSuiteManager suites;
	
	/** Task manager. */
	protected final SpringTaskManager tasks;
	
	/** The depth of this machine. */
	protected final int guestdepth;
	
	/** The profiling information. */
	protected final ProfilerSnapshot profiler;
	
	/** Access to the native display. */
	protected final VMNativeDisplayAccess nativedisplay;
	
	/** Threads which are available. */
	private final List<SpringThread> _threads =
		new ArrayList<>();
	
	/** Static fields which exist within the virtual machine. */
	private final Map<SpringField, SpringFieldStorage> _staticfields =
		new HashMap<>();
	
	/** Global strings representing singular constants. */
	private final Map<ConstantValueString, SpringObject> _strings =
		new HashMap<>();
	
	/** Class objects which represent a given class. */
	private final Map<ClassName, SpringObject> _classobjects =
		new HashMap<>();
	
	/** Class names by their objects. */
	private final Map<SpringObject, ClassName> _namesbyclass =
		new HashMap<>();
	
	/** Main entry point arguments. */
	private final String[] _args;
	
	/** Long to string map. */
	private final Map<Long, String> _strlongtostring =
		new HashMap<>();
	
	/** String to long map. */
	private final Map<String, Long> _strstringtolong =
		new HashMap<>();
	
	/** The next thread ID to use. */
	private volatile int _nextthreadid;
	
	/** The next long to choose. */
	private long _strnextlong;
	
	/** Is the VM exiting? */
	private volatile boolean _exiting;
	
	/** Exit code of the VM. */
	private volatile int _exitcode;
	
	/**
	 * Initializes the virtual machine.
	 *
	 * @param __sm The manager for suites.
	 * @param __cl The class loader.
	 * @param __tm Task manager.
	 * @param __bootcl The boot class.
	 * @param __bootmid The boot class a midlet.
	 * @param __bootdx The entry point which should be booted when the VM
	 * runs.
	 * @param __gd Guest depth.
	 * @param __profiler The profiler to use.
	 * @param __nda The native display provider.
	 * @param __args Main entry point arguments.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/03
	 */
	public SpringMachine(VMSuiteManager __sm, SpringClassLoader __cl,
		SpringTaskManager __tm, String __bootcl, boolean __bootmid,
		int __bootdx, int __gd, ProfilerSnapshot __profiler,
		VMNativeDisplayAccess __nda, String... __args)
		throws NullPointerException
	{
		if (__cl == null || __sm == null || __nda == null)
			throw new NullPointerException("NARG");
		
		this.suites = __sm;
		this.classloader = __cl;
		this.tasks = __tm;
		this.bootcl = __bootcl;
		this.bootmid = __bootmid;
		this.bootdx = __bootdx;
		this.guestdepth = __gd;
		this.nativedisplay = __nda;
		this._args = (__args == null ? new String[0] : __args.clone());
		this.profiler = (__profiler != null ? __profiler :
			new ProfilerSnapshot());
		
		// Setup resource accessor
		this.resourceaccessor = new VMResourceAccess(__sm);
	}
	
	/**
	 * Returns the class loader.
	 *
	 * @return The class loader.
	 * @since 2018/09/08
	 */
	public final SpringClassLoader classLoader()
	{
		return this.classloader;
	}
	
	/**
	 * Creates a new thread within the virtual machine.
	 *
	 * @param __n The name of the thread.
	 * @return The newly created thread.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/01
	 */
	public final SpringThread createThread(String __n)
		throws NullPointerException
	{
		if (__n == null)
			throw new NullPointerException("NARG");
		
		// Store thread
		List<SpringThread> threads = this._threads;
		synchronized (threads)
		{
			// Initialize new thread
			int v;
			SpringThread rv = new SpringThread((v = ++this._nextthreadid), __n,
				this.profiler.measureThread(String.format("vm%08x-%d-%s",
				System.identityHashCode(this), v, __n)));
			
			// Store thread
			threads.add(rv);
			return rv;
		}
	}
	
	/**
	 * Resolves the given string pointer.
	 *
	 * @param __p The pointer.
	 * @return The string at the given pointer or {@code null} if it has no
	 * resolution.
	 * @since 2018/09/29
	 */
	public final String debugResolveString(long __p)
	{
		if (__p == -1L)
			return null;
		
		synchronized (this.strlock)
		{
			return this._strlongtostring.get(__p);
		}
	}
	
	/**
	 * Unresolves the given string.
	 *
	 * @param __s The string to unresolve.
	 * @return The pointer to the string.
	 * @since 2018/09/29
	 */
	public final long debugUnresolveString(String __s)
	{
		if (__s == null)
			return -1L;
		
		synchronized (this.strlock)
		{
			Long rv = this._strstringtolong.get(__s);
			if (rv != null)
				return rv.longValue();
			
			Long next = Long.valueOf(++this._strnextlong);
			this._strstringtolong.put(__s, next);
			this._strlongtostring.put(next, __s);
			
			return next;
		}
	}
	
	/**
	 * Exits the virtual machine.
	 *
	 * @param __code The exit code.
	 * @throws SpringMachineExitException To signal virtual machine exit.
	 * @since 2018/10/13
	 */
	public final void exit(int __code)
		throws SpringMachineExitException
	{
		// Set as exiting
		this._exitcode = __code;
		this._exiting = true;
		
		// Now signal exit
		throw new SpringMachineExitException(__code);
	}
	
	/**
	 * Checks whether the virtual machine is exiting.
	 *
	 * @throws SpringMachineExitException If the VM is exiting.
	 * @since 2018/10/13
	 */
	public final void exitCheck()
		throws SpringMachineExitException
	{
		// Only if exiting
		if (this._exiting)
			throw new SpringMachineExitException(this._exitcode);
	}
	
	/**
	 * Exits the virtual machine without throwing an exception.
	 *
	 * @param __code The exit code.
	 * @since 2018/10/13
	 */
	public final void exitNoException(int __code)
		throws SpringMachineExitException
	{
		// Set as exiting
		this._exitcode = __code;
		this._exiting = true;
	}
	
	/**
	 * Gets the thread by the given ID.
	 *
	 * @param __id The ID of the thread.
	 * @return The thread by this ID or {@code null} if it was not found.
	 * @since 2018/11/21
	 */
	public final SpringThread getThread(int __id)
	{
		List<SpringThread> threads = this._threads;
		synchronized (threads)
		{
			for (SpringThread t : threads)
				if (t.id == __id)
					return t;
		}
		
		return null;
	}
	
	/**
	 * Returns the static field for the given field.
	 *
	 * @param __f The field to get the static field for.
	 * @return The static field.
	 * @throws NullPointerException On null arguments.
	 * @throws SpringVirtualMachineException If the field does not exist.
	 * @since 2018/09/09
	 */
	public final SpringFieldStorage lookupStaticField(SpringField __f)
		throws NullPointerException
	{
		if (__f == null)
			throw new NullPointerException("NARG");
		
		// Static fields may be added to when class loading is happening and
		// as such there must be a lock to be given safe access
		Map<SpringField, SpringFieldStorage> sfm = this._staticfields;
		synchronized (this.classloader.classLoadingLock())
		{
			SpringFieldStorage rv = sfm.get(__f);
			
			// {@squirreljme.error BK0u Could not locate the static field
			// storage?}
			if (rv == null)
				throw new SpringVirtualMachineException("BK0u");
			
			return rv;
		}
	}
	
	/**
	 * Returns the number of threads which are currently alive and active.
	 *
	 * @return The number of active and alive threads.
	 * @since 2018/09/03
	 */
	public final int numThreads()
	{
		// Store thread
		List<SpringThread> threads = this._threads;
		synchronized (threads)
		{
			return threads.size();
		}
	}
	
	/**
	 * Returns the access for resources.
	 *
	 * @return The resource access.
	 * @since 2018/10/07
	 */
	public final VMResourceAccess resourceAccess()
	{
		return this.resourceaccessor;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/09/13
	 */
	@Override
	public final void run()
	{
		// Obtain the boot library to read entry points from
		SpringClassLoader classloader = this.classloader;
		VMClassLibrary bootbin = classloader.bootLibrary();
		
		// May be specified or not
		String entryclass = this.bootcl;
		boolean ismidlet = this.bootmid;
		int launchid = this.bootdx;
		
		// Lookup the entry class via the manifest
		if (entryclass == null)
		{
			// Need to load the manifest where the entry points will be
			EntryPoints entries;
			try (InputStream in = bootbin.resourceAsStream(
				"META-INF/MANIFEST.MF"))
			{
				// {@squirreljme.error BK0v Entry point JAR has no manifest.
				// (The name of the boot binary)}
				if (in == null)
					throw new SpringVirtualMachineException("BK0v " +
						bootbin.name());
				
				entries = new EntryPoints(new JavaManifest(in));
			}
			
			// {@squirreljme.error BK0w Failed to read the manifest.}
			catch (IOException e)
			{
				throw new SpringVirtualMachineException("BK0w", e);
			}
			
			int n = entries.size();
			
			// Print entry points out out for debug, but only for the first
			// guest because this is annoying!
			if (GuestDepth.guestDepth() + 1 == this.guestdepth)
			{
				todo.DEBUG.note("Entry points:");
				for (int i = 0; i < n; i++)
					todo.DEBUG.note("    %d: %s", i, entries.get(i));
			}
			
			// Use the first program if the ID is not valid
			if (launchid < 0 || launchid >= n)
				launchid = 0;
			
			// Needed to enter the machine
			EntryPoint entry = entries.get(launchid);
			entryclass = entry.entryPoint().toString();
			ismidlet = entry.isMidlet();
		}
		
		// Thread that will be used as the main thread of execution, also used
		// to initialize classes when they are requested
		SpringThread mainthread = this.createThread("main");
		
		// We will be using the same logic in the thread worker if we need to
		// initialize any objects or arguments
		SpringThreadWorker worker = new SpringThreadWorker(this,
			mainthread, true);
		mainthread._worker = worker;
		
		// Load the entry point class
		SpringClass entrycl = worker.loadClass(new ClassName(
			entryclass.replace('.', '/')));
		
		// Find the method to be entered in
		SpringMethod mainmethod;
		if (ismidlet)
			mainmethod = entrycl.lookupMethod(false,
				new MethodNameAndType("startApp", "()V"));
		else
			mainmethod = entrycl.lookupMethod(true,
				new MethodNameAndType("main", "([Ljava/lang/String;)V"));
		
		// Setup object to initialize with for thread
		SpringVMStaticMethod vmsm = new SpringVMStaticMethod(mainmethod);
		
		// Determine the entry argument, midlets is just the class to run
		Object entryarg;
		if (ismidlet)
			entryarg = worker.asVMObject(entryclass.replace('.', '/'));
		else
		{
			String[] inargs = this._args;
			int inlen = inargs.length;
			
			// Setup array
			SpringArrayObject outargs = worker.allocateArray(
				worker.resolveClass(new ClassName("java/lang/String")), inlen);
			
			// Initialize the argument array
			for (int i = 0; i < inlen; i++)
				outargs.set(i, worker.asVMObject(inargs[i]));
			
			entryarg = outargs;
		}
		
		// Setup new thread object
		SpringObject threadobj = worker.newInstance(worker.loadClass(
			new ClassName("java/lang/Thread")), new MethodDescriptor(
			"(Ljava/lang/String;ILcc/squirreljme/runtime/cldc/asm/" +
			"StaticMethod;Ljava/lang/Object;)V"), worker.asVMObject("Main"),
			(ismidlet ? 3 : 4), vmsm, entryarg);
		
		// Enter the frame for that method using the arguments we passed (in
		// a static fashion)
		mainthread.enterFrame(worker.loadClass(
			new ClassName("java/lang/Thread")).lookupMethod(false,
			new MethodNameAndType("__start", "()V")), threadobj);
		
		// The main although it executes in this context will always have the
		// same exact logic as other threads running apart from this main
		// thread, so no code is needed to be duplicated at all.
		try
		{
			worker.run();
		}
		
		// Virtual machine exited, do not print fatal trace just exit here
		catch (SpringMachineExitException e)
		{
			throw e;
		}
		
		// Ooopsie!
		catch (RuntimeException e)
		{
			/*PrintStream err = System.err;
			
			err.println("****************************");
			
			// Print the real stack trace
			err.println("*** EXTERNAL STACK TRACE ***");
			e.printStackTrace(err);
			err.println();
			
			// Print the VM seen stack trace
			err.println("*** INTERNAL STACK TRACE ***");
			mainthread.printStackTrace(err);
			err.println();
			
			err.println("****************************");*/
			
			// Retoss
			throw e;
		}
		
		// Wait until all threads have terminated before actually leaving
		for (;;)
		{
			// Check if the VM is exiting, this would have happen if another
			// thread called exit
			// If we do not check, then the VM will never exit even after
			// another thread has exited
			this.exitCheck();
			
			// No more threads left?
			int okay = 0,
				notokay = 0;
			List<SpringThread> threads = this._threads;
			synchronized (threads)
			{
				for (SpringThread t : threads)
					if (t.isExitOkay())
						okay++;
					else
						notokay++;
			}
			
			// Okay to exit?
			if (notokay == 0)
				return;
			
			// Wait a short duration before checking again
			try
			{
				Thread.sleep(500);
			}
			catch (InterruptedException e)
			{
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/17
	 */
	@Override
	public final int runVm()
	{
		// Run until the VM terminates
		try
		{
			this.run();
			
			// Success, maybe
			return this._exitcode;
		}
		
		// Exit VM with given code
		catch (SpringMachineExitException e)
		{
			return e.code();
		}
		
		// Ignore these exceptions, just fatal exit
		catch (SpringFatalException e)
		{
			return TaskAccess.EXIT_CODE_FATAL_EXCEPTION;
		}
	}
	
	/**
	 * Returns the suite manager which is available.
	 *
	 * @return The suite manager that is available.
	 * @since 2018/10/26
	 */
	public final VMSuiteManager suiteManager()
	{
		return this.suites;
	}
	
	/**
	 * Returns the task manager which is used.
	 *
	 * @return The task manager.
	 * @since 2018/11/04
	 */
	public final SpringTaskManager taskManager()
	{
		return this.tasks;
	}
	
	/**
	 * Splits long to integers.
	 *
	 * @param __dx The index.
	 * @param __v The output integers.
	 * @param __l The input long.
	 * @since 2018/09/29
	 */
	public static final void longToInt(int __dx, int[] __v, long __l)
	{
		__v[__dx] = (int)(__l >>> 32);
		__v[__dx + 1] = (int)__l;
	}
	
	/**
	 * Returns the mapping of class names to {@link Class} instances.
	 *
	 * @return The mapping of class names to object instances.
	 * @since 2018/09/19
	 */
	final Map<ClassName, SpringObject> __classObjectMap()
	{
		return this._classobjects;
	}
	
	/**
	 * Returns the mapping for objects to class names.
	 *
	 * @return The mapping of class objects to names.
	 * @since 2018/09/29
	 */
	final Map<SpringObject, ClassName> __classObjectToNameMap()
	{
		return this._namesbyclass;
	}
	
	/**
	 * Returns the map of static fields.
	 *
	 * @return The static field map.
	 * @since 2018/09/08
	 */
	final Map<SpringField, SpringFieldStorage> __staticFieldMap()
	{
		return this._staticfields;
	}
	
	/**
	 * Returns the global string map.
	 *
	 * @return The global string map.
	 * @since 2018/09/16
	 */
	final Map<ConstantValueString, SpringObject> __stringMap()
	{
		return this._strings;
	}
}

