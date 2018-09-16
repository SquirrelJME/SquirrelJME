// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.springcoat.vm;

import cc.squirreljme.kernel.suiteinfo.EntryPoint;
import cc.squirreljme.kernel.suiteinfo.EntryPoints;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.multiphasicapps.classfile.ClassFile;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.InvalidClassFormatException;
import net.multiphasicapps.classfile.MethodNameAndType;
import net.multiphasicapps.tool.manifest.JavaManifest;

/**
 * This class contains the instance of the SpringCoat virtual machine and has
 * a classpath along with all the needed storage for variables and such.
 *
 * @since 2018/07/29
 */
public final class SpringMachine
	implements Runnable
{
	/** The class loader. */
	protected final SpringClassLoader classloader;
	
	/** The boot index. */
	protected final int bootdx;
	
	/** Threads which are available. */
	private final List<SpringThread> _threads =
		new ArrayList<>();
	
	/** Classes and their {@link Class} instances in the VM. */
	private final Map<SpringClass, SpringObject> _classobjects =
		new HashMap<>();
	
	/** Static fields which exist within the virtual machine. */
	private final Map<SpringField, SpringFieldStorage> _staticfields =
		new HashMap<>();
	
	/** The next thread ID to use. */
	private volatile int _nextthreadid;
	
	/**
	 * Initializes the virtual machine.
	 *
	 * @param __cl The class loader.
	 * @param __bootdx The entry point which should be booted when the VM
	 * runs.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/03
	 */
	public SpringMachine(SpringClassLoader __cl, int __bootdx)
		throws NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		this.classloader = __cl;
		this.bootdx = __bootdx;
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
			SpringThread rv = new SpringThread(++this._nextthreadid, __n);
			
			// Store thread
			threads.add(rv);
			return rv;
		}
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
			
			// {@squirreljme.error BK0g Could not locate the static field
			// storage?}
			if (rv == null)
				throw new SpringVirtualMachineException("BK0g");
			
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
	 * {@inheritDoc}
	 * @since 2018/09/13
	 */
	@Override
	public final void run()
	{
		// Obtain the boot library to read entry points from
		SpringClassLoader classloader = this.classloader;
		SpringClassLibrary bootbin = classloader.bootLibrary();
		
		// Need to load the manifest where the entry points will be
		EntryPoints entries;
		try (InputStream in = bootbin.resourceAsStream("META-INF/MANIFEST.MF"))
		{
			// {@squirreljme.error BK0v Entry point JAR has no manifest.}
			if (in == null)
				throw new SpringVirtualMachineException("BK0v");
			
			entries = new EntryPoints(new JavaManifest(in));
		}
		
		// {@squirreljme.error BK0u Failed to read the manifest.}
		catch (IOException e)
		{
			throw new SpringVirtualMachineException("BK0u", e);
		}
		
		// Print entry points out out for debug
		todo.DEBUG.note("Entry points:");
		int n = entries.size();
		for (int i = 0; i < n; i++)
			todo.DEBUG.note("    %d: %s", i, entries.get(i));
		
		// Use the first program if the ID is not valid
		int launchid = this.bootdx;
		if (launchid < 0 || launchid >= n)
			launchid = 0;
		
		// Thread that will be used as the main thread of execution, also used
		// to initialize classes when they are requested
		SpringThread mainthread = this.createThread("main");
		
		// We will be using the same logic in the thread worker if we need to
		// initialize any objects or arguments
		SpringThreadWorker worker = new SpringThreadWorker(this,
			mainthread);
		
		// Load the entry point class
		EntryPoint entry = entries.get(launchid);
		SpringClass entrycl = worker.loadClass(new ClassName(
			entry.entryPoint().replace('.', '/')));
		
		// Find the method to be entered in
		SpringMethod mainmethod;
		boolean ismidlet;
		if ((ismidlet = entry.isMidlet()))
			mainmethod = entrycl.lookupMethod(false,
				new MethodNameAndType("startApp", "()V"));
		else
			mainmethod = entrycl.lookupMethod(true,
				new MethodNameAndType("main", "(Ljava/lang/String;)V"));
		
		// If this is a midlet, we are going to need to initialize a new
		// instance of our MIDlet and then push that to the current frame's
		// stack then call the main method on it
		Object[] entryargs;
		if (ismidlet)
		{
			// Allocate an object for our instance
			SpringObject midinstance = worker.allocateObject(entrycl);
			
			// Initialize the object
			SpringMethod defcon = entrycl.lookupDefaultConstructor();
			mainthread.enterFrame(defcon, midinstance);
			
			// Since the constructor was entered, run all the code needed to
			// actually initialize it and such, this method will return once
			// there are no frames left
			worker.run();
			
			// The arguments to the method we are calling is just the instance
			// of the midlet we created and initialized
			entryargs = new Object[]{midinstance};
		}
		
		// Initialize program arguments from a bunch of string arguments that
		// way they are passed to the main entry point
		else
		{
			if (true)
				throw new todo.TODO();
		}
		
		// Enter the frame for that method using the arguments we passed (in
		// a static fashion)
		mainthread.enterFrame(mainmethod, entryargs);
		
		// The main although it executes in this context will always have the
		// same exact logic as other threads running apart from this main
		// thread, so no code is needed to be duplicated at all.
		worker.run();
		
		// Wait until all threads have terminated before actually leaving
		throw new todo.TODO();
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

