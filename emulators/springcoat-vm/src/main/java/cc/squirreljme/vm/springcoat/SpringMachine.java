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

import cc.squirreljme.emulator.profiler.ProfilerSnapshot;
import cc.squirreljme.emulator.vm.VMResourceAccess;
import cc.squirreljme.emulator.vm.VMSuiteManager;
import cc.squirreljme.emulator.vm.VirtualMachine;
import cc.squirreljme.runtime.cldc.asm.TaskAccess;
import cc.squirreljme.vm.springcoat.exceptions.SpringFatalException;
import cc.squirreljme.vm.springcoat.exceptions.SpringMachineExitException;
import cc.squirreljme.vm.springcoat.exceptions.SpringVirtualMachineException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.ConstantValueString;
import net.multiphasicapps.classfile.MethodDescriptor;
import net.multiphasicapps.classfile.MethodNameAndType;

/**
 * This class contains the instance of the SpringCoat virtual machine and has
 * a classpath along with all the needed storage for variables and such.
 *
 * @since 2018/07/29
 */
public final class SpringMachine
	implements Runnable, VirtualMachine
{
	/** The class which contains the thread starting point. */
	private static final ClassName _START_CLASS =
		new ClassName("java/lang/__Start__");
	
	/** The method to enter for main threads. */
	private static final MethodNameAndType _MAIN_THREAD_METHOD =
		new MethodNameAndType("__main", "()V");
	
	/** The class loader. */
	protected final SpringClassLoader classloader;
	
	/** Resources accessor. */
	protected final VMResourceAccess resourceaccessor;
	
	/** The boot class. */
	protected final String bootClass;
	
	/** The manager for suites. */
	protected final VMSuiteManager suites;
	
	/** Task manager. */
	protected final SpringTaskManager tasks;
	
	/** The global VM state. */
	protected final GlobalState globalState;
	
	/** The profiling information. */
	protected final ProfilerSnapshot profiler;
	
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
	
	/** System properties. */
	final Map<String, String> _sysproperties;
	
	/** The next thread ID to use. */
	private volatile int _nextthreadid;
	
	/** Is the VM exiting? */
	private volatile boolean _exiting;
	
	/** Exit code of the VM. */
	volatile int _exitcode;
	
	/**
	 * Initializes the virtual machine.
	 *
	 * @param __sm The manager for suites.
	 * @param __cl The class loader.
	 * @param __tm Task manager.
	 * @param __bootcl The boot class.
	 * @param __profiler The profiler to use.
	 * @param __sprops System properties.
	 * @param __gs Global system state.
	 * @param __args Main entry point arguments.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/03
	 */
	public SpringMachine(VMSuiteManager __sm, SpringClassLoader __cl,
		SpringTaskManager __tm, String __bootcl,
		ProfilerSnapshot __profiler, Map<String, String> __sprops,
		GlobalState __gs, String... __args)
		throws NullPointerException
	{
		if (__cl == null || __sm == null)
			throw new NullPointerException("NARG");
		
		this.suites = __sm;
		this.classloader = __cl;
		this.tasks = __tm;
		this.bootClass = __bootcl;
		this.globalState = __gs;
		this._args = (__args == null ? new String[0] : __args.clone());
		this.profiler = (__profiler != null ? __profiler :
			new ProfilerSnapshot());
		this._sysproperties = (__sprops == null ?
			new HashMap<String, String>() : new HashMap<>(__sprops));
		
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
	 * @param __n The name of the thread, may be {@code null} in which case
	 * the thread will just get an ID number.
	 * @param __main Is this a main thread?
	 * @return The newly created thread.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/01
	 */
	public final SpringThread createThread(String __n, boolean __main)
	{
		// Store thread
		List<SpringThread> threads = this._threads;
		synchronized (this)
		{
			// The name of the thread to use
			String usedName = (__n != null ? __n :
				"hwThread-" + this.numThreads());
			
			// Initialize new thread
			int v;
			SpringThread rv = new SpringThread(
				(v = ++this._nextthreadid), __main,
				usedName,
				this.profiler.measureThread(String.format("%s-vm%08x-%d-%s",
				this.classloader.bootLibrary().name(),
				System.identityHashCode(this), v, usedName)));
			
			// Signal that a major state has changed
			this.notifyAll();
			
			// Store thread
			threads.add(rv);
			return rv;
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
	 * Returns the main arguments.
	 *
	 * @return The main arguments.
	 * @since 2020/06/17
	 */
	public final String[] getMainArguments()
	{
		return this._args.clone();
	}
	
	/**
	 * Gets the thread by the given ID.
	 *
	 * @param __id The ID of the thread.
	 * @return The thread by this ID.
	 * @throws NoSuchElementException If there is no thread that exists by
	 * that ID.
	 * @since 2018/11/21
	 */
	public final SpringThread getThread(int __id)
		throws NoSuchElementException
	{
		List<SpringThread> threads = this._threads;
		synchronized (this)
		{
			for (SpringThread t : threads)
				if (t.id == __id)
					return t;
		}
		
		throw new NoSuchElementException("No such thread ID: " + __id);
	}
	
	/**
	 * Returns all of the process threads.
	 *
	 * @return All of the current process threads.
	 * @since 2020/06/17
	 */
	@SuppressWarnings("UnnecessaryLocalVariable")
	public final SpringThread[] getThreads()
	{
		List<SpringThread> rv = new ArrayList<>();
		
		// Go through threads but also cleanup any that have ended
		List<SpringThread> threads = this._threads;
		synchronized (this)
		{
			for (Iterator<SpringThread> it = threads.iterator(); it.hasNext();)
			{
				SpringThread thread = it.next();
				
				// If the thread is terminating, clean it up
				if (thread.isTerminated())
				{
					// Remove it
					it.remove();
					
					// Signal that a state of a thread has changed
					this.notifyAll();
				}
				
				// Otherwise add it
				else
					rv.add(thread);
			}
		}
		
		// Use whatever was found
		return rv.<SpringThread>toArray(new SpringThread[rv.size()]);
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
			
			// {@squirreljme.error BK19 Could not locate the static field
			// storage?}
			if (rv == null)
				throw new SpringVirtualMachineException("BK19");
			
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
		synchronized (this)
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
		// Thread that will be used as the main thread of execution, also used
		// to initialize classes when they are requested
		SpringThread mainThread = this.createThread("main", true);
		
		// We will be using the same logic in the thread worker if we need to
		// initialize any objects or arguments
		SpringThreadWorker worker = new SpringThreadWorker(this,
			mainThread, true);
		mainThread._worker = worker;
		
		// Enter the main entry point which handles the thread logic
		mainThread.enterFrame(worker.loadClass(SpringMachine._START_CLASS)
			.lookupMethod(true, SpringMachine._MAIN_THREAD_METHOD));
		
		// Initialize an instance of Thread for this thread, as this is
		// very important, the call to create VM threads will bind the instance
		// object and the vm thread together.
		worker.newInstance(
			new ClassName("java/lang/Thread"),
			new MethodDescriptor("(Ljava/lang/String;)V"),
			worker.asVMObject("main"));
		
		// The main although it executes in this context will always have the
		// same exact logic as other threads running apart from this main
		// thread, so no code is needed to be duplicated at all.
		try
		{
			worker.run();
		}
		
		// Either failed or threw exit exception
		catch (RuntimeException e)
		{
			throw e;
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
		
		// Any other exception is fatal and the task must be made to exit
		// with the error code otherwise the VM will stick trying to wait
		// to exit
		catch (RuntimeException|Error e)
		{
			PrintStream err = System.err;
			
			err.println("****************************");
			
			// Print the real stack trace
			err.println("*** EXTERNAL STACK TRACE ***");
			e.printStackTrace(err);
			err.println();
			
			err.println("****************************");
			
			return TaskAccess.EXIT_CODE_FATAL_EXCEPTION;
		}
	}
	
	/**
	 * Sets the current exit code.
	 * 
	 * @param __exitCode The exit code to set.
	 * @since 2020/06/27
	 */
	public final void setExitCode(int __exitCode)
	{
		this._exitcode = __exitCode;
	}
	
	/**
	 * Signals that the given thread terminated.
	 * 
	 * @param __thread The thread that was terminated.
	 * @since 2020/06/29
	 */
	public void signalThreadTerminate(SpringThread __thread)
	{
		// The act of getting all threads will clear out terminated threads
		this.getThreads();
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

