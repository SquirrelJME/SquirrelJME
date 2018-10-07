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
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.multiphasicapps.classfile.ClassFile;
import net.multiphasicapps.classfile.ClassName;
import net.multiphasicapps.classfile.ConstantValueString;
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
	/** Lock. */
	public final Object strlock =
		new Object();
	
	/** The class loader. */
	protected final SpringClassLoader classloader;
	
	/** Resources accessor. */
	protected final SpringResourceAccess resourceaccessor;
	
	/** The boot index. */
	protected final int bootdx;
	
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
	
	/**
	 * Initializes the virtual machine.
	 *
	 * @param __cl The class loader.
	 * @param __bootdx The entry point which should be booted when the VM
	 * runs.
	 * @param __args Main entry point arguments.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/09/03
	 */
	public SpringMachine(SpringClassLoader __cl, int __bootdx,
		String... __args)
		throws NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		this.classloader = __cl;
		this.bootdx = __bootdx;
		this._args = (__args == null ? new String[0] : __args.clone());
		
		// Setup resource accessor
		this.resourceaccessor = new SpringResourceAccess(__cl);
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
	 * Returns the access for resources.
	 *
	 * @return The resource access.
	 * @since 2018/10/07
	 */
	public final SpringResourceAccess resourceAccess()
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
				new MethodNameAndType("main", "([Ljava/lang/String;)V"));
		
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
			String[] inargs = this._args;
			int inlen = inargs.length;
			
			// Setup array
			SpringArrayObject outargs = worker.allocateArray(
				worker.resolveClass(new ClassName("java/lang/String")), inlen);
			
			// Initialize the argument array
			for (int i = 0; i < inlen; i++)
				outargs.set(i, worker.asVMObject(inargs[i]));
			
			// Entry arguments are just this array
			entryargs = new Object[]{outargs};
		}
		
		// Enter the frame for that method using the arguments we passed (in
		// a static fashion)
		mainthread.enterFrame(mainmethod, entryargs);
		
		// The main although it executes in this context will always have the
		// same exact logic as other threads running apart from this main
		// thread, so no code is needed to be duplicated at all.
		try
		{
			worker.run();
		}
		
		// Ooopsie!
		catch (RuntimeException e)
		{
			PrintStream err = System.err;
			
			err.println("****************************");
			
			// Print the real stack trace
			err.println("*** EXTERNAL STACK TRACE ***");
			e.printStackTrace(err);
			err.println();
			
			// Print the VM seen stack trace
			err.println("*** INTERNAL STACK TRACE ***");
			mainthread.printStackTrace(err);
			err.println();
			
			err.println("****************************");
			
			// Retoss
			throw e;
		}
		
		// Wait until all threads have terminated before actually leaving
		throw new todo.TODO();
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

