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
import cc.squirreljme.jvm.ThreadStartIndex;
import cc.squirreljme.runtime.cldc.asm.TaskAccess;
import cc.squirreljme.vm.VMClassLibrary;
import cc.squirreljme.vm.springcoat.exceptions.SpringFatalException;
import cc.squirreljme.vm.springcoat.exceptions.SpringMachineExitException;
import cc.squirreljme.vm.springcoat.exceptions.SpringVirtualMachineException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.MethodNameAndType;
import net.multiphasicapps.classfile.PrimitiveType;

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
	
	/** The manager for suites. */
	protected final VMSuiteManager suites;
	
	/** Task manager. */
	protected final SpringTaskManager tasks;
	
	/** The profiling information. */
	protected final ProfilerSnapshot profiler;
	
	/** Pointer manager. */
	protected final SpringPointerManager pointers =
		new SpringPointerManager();
	
	/** Threads which are available. */
	private final List<SpringThread> _threads =
		new ArrayList<>();
	
	/** Static fields which exist within the virtual machine. */
	@Deprecated
	private final Map<SpringField, SpringFieldStorage> _staticfields =
		new HashMap<>();
	
	/** Class objects which represent a given class. */
	@Deprecated
	private final Map<ClassName, SpringObject> _classobjects =
		new HashMap<>();
	
	/** Class names by their objects. */
	@Deprecated
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
	 * @param __args Main entry point arguments.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/03
	 */
	public SpringMachine(VMSuiteManager __sm, SpringClassLoader __cl,
		SpringTaskManager __tm, String __bootcl, ProfilerSnapshot __profiler,
		Map<String, String> __sprops, String... __args)
		throws NullPointerException
	{
		if (__cl == null || __sm == null || __bootcl == null)
			throw new NullPointerException("NARG");
		
		this.suites = __sm;
		this.classloader = __cl;
		this.tasks = __tm;
		this.bootcl = __bootcl;
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
				this.profiler.measureThread(String.format("%s-vm%08x-%d-%s",
				this.classloader.bootLibrary().name(),
				System.identityHashCode(this), v, __n)));
			
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
			
			// {@squirreljme.error BK19 Could not locate the static field
			// storage?}
			if (rv == null)
				throw new SpringVirtualMachineException("BK19");
			
			return rv;
		}
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
		
		// Must be specified
		String entryClass = this.bootcl;
		
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
			entryClass.replace('.', '/')));
		
		// All code enters at a static main method, regardless of the
		// circumstances
		SpringMethod main = entrycl.lookupMethod(true,
			new MethodNameAndType("main", "([Ljava/lang/String;)V"));
		SpringPointer mainFp = this.tasks.memory.bindMethod(main);
		
		// Initialize main program arguments
		String[] inargs = this._args;
		int inlen = inargs.length;
		
		// Setup array
		SpringArrayObject outargs = worker.allocateArray(
			worker.resolveClass(new ClassName("java/lang/String")), inlen);
		
		// Initialize the argument array
		for (int i = 0; i < inlen; i++)
			outargs.set(i, worker.asVMObject(inargs[i]));
		
		// Initialize thread index values
		SpringArrayObjectLong tsiArgs =
			(SpringArrayObjectLong)worker.allocateArray(
			worker.resolveClass(PrimitiveType.LONG.toClassName()),
			ThreadStartIndex.NUM_INDEXES);
		tsiArgs.set(ThreadStartIndex.MAIN_CLASS_INFO,
			ObjectLoader.loadClassInfo(worker, entrycl).pointerArea().base());
		tsiArgs.set(ThreadStartIndex.MAIN_ARGUMENTS,
			outargs.pointerArea().base());
		
		// Enter the frame for that method using the arguments we passed (in
		// a static fashion)
		mainthread.enterFrame(worker.loadClass(
			new ClassName("java/lang/__ThreadStarter__")).lookupMethod(
			true, new MethodNameAndType("__start", "([J)V")),
			tsiArgs.pointerArea().base());
		
		// The main although it executes in this context will always have the
		// same exact logic as other threads running apart from this main
		// thread, so no code is needed to be duplicated at all.
		worker.run();
		
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
}

