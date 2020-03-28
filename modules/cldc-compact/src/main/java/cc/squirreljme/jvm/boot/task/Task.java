// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.boot.task;

import cc.squirreljme.jvm.Assembly;
import cc.squirreljme.jvm.boot.Globals;
import cc.squirreljme.jvm.boot.lib.ClassInfoUtility;
import cc.squirreljme.jvm.boot.lib.ClassNameUtils;
import cc.squirreljme.jvm.boot.lib.ClassPath;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.util.HashMap;

/**
 * This represents a single task which has information on what it is running
 * along with its ID and such.
 *
 * @since 2019/06/22
 */
public final class Task
{
	/** The physical task ID. */
	public final int pid;
	
	/** The logical task ID. */
	public final int lid;
	
	/** Allocator for this task. */
	public final TaskAllocator allocator;
	
	/** The classpath. */
	public final ClassPath classpath;
	
	/** Classes which have been read and initialized. */
	private final HashMap<String, TaskClass> _classes =
		new HashMap<>();
	
	/** Class info parser utility. */
	private ClassInfoUtility _ciutil;
	
	/**
	 * Initializes the client task.
	 *
	 * @param __pid The PID.
	 * @param __lid The LID.
	 * @param __cp The classpath used.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/06/22
	 */
	public Task(int __pid, int __lid, ClassPath __cp)
		throws NullPointerException
	{
		if (__cp == null)
			throw new NullPointerException("NARG");
		
		this.pid = __pid;
		this.lid = __lid;
		this.classpath = __cp;
		
		// Initialize allocator for memory grabbing
		this.allocator = new TaskAllocator(__pid);
	}
	
	/**
	 * Since {@link cc.squirreljme.jvm.ClassInfo} is an important and often
	 * used part of loading classes within tasks, this is used to quickly
	 * cache and obtain a class information utility without requiring a search
	 * be done for it every time.
	 *
	 * @return The utility for {@code ClassInfo}.
	 * @since 2019/11/17
	 */
	public final ClassInfoUtility classInfoUtility()
	{
		ClassInfoUtility rv = this._ciutil;
		if (rv != null)
			return rv;
		
		// Create utility
		ClassPath classpath = this.classpath;
		this._ciutil = (rv = ClassInfoUtility.of(classpath.classParser(
			classpath.resourceClassFind("cc/squirreljme/jvm/ClassInfo"))));
		
		return rv;
	}
	
	/**
	 * Returns the current context thread of execution.
	 *
	 * @return The current context thread.
	 * @since 2019/12/14
	 */
	public final TaskThread contextThread()
	{
		todo.DEBUG.note("TODO -- Implement better contextThread(), lid=%d.",
			this.lid);
		
		// Could not determine the context thread, use fallback by calling
		// any thread and hoping it works
		return Globals.getThreadManager().anyThreadOwnedByTask(this.lid);
	}
	
	/**
	 * Creates a new thread
	 *
	 * @return The resulting thread.
	 * @since 2019/10/13
	 */
	public final TaskThread createThread()
	{
		return Globals.getThreadManager().createThread(this.lid);
	}
	
	/**
	 * Loads the given class.
	 *
	 * @param __cl The class to load.
	 * @return The pointer to the class information.
	 * @throws NullPointerException On null arguments.
	 * @throws TaskNoSuchClassException If the task does not have the specified
	 * class.
	 * @throws TaskVirtualMachineError If there is something wrong with the
	 * task virtual machine.
	 * @since 2019/10/13
	 */
	public final TaskClass loadClass(String __cl)
		throws NullPointerException, TaskNoSuchClassException,
			TaskVirtualMachineError
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		// Needed to search for classes
		TaskClass rv;
		
		// Try to find already initialized class
		HashMap<String, TaskClass> classes = this._classes;
		synchronized (this)
		{
			// Already made?
			rv = classes.get(__cl);
			if (rv != null)
				return rv;
			
			// {@squirreljme.error SV0m The specified class does not exist
			// and it is not a special class. (The class which does not exist)}
			int cldx = this.classpath.resourceClassFind(__cl);
			if (cldx < 0 && !ClassNameUtils.isSpecial(__cl))
				throw new TaskNoSuchClassException("SV0m " + __cl);
			
			// Setup new one and store it
			classes.put(__cl, (rv = new TaskClass(cldx)));
			
			// Pre-initialize the class
			rv.initializeClassInfoSetup(this, __cl);
		}
		
		// Perform post initialization as needed
		return rv.initializeClassInfo(this, __cl);
	}
	
	/**
	 * Loads an object array with the given values and class type.
	 *
	 * @param __cl The class type.
	 * @param __vs The values to store.
	 * @return The resulting object array.
	 * @since 2019/10/13
	 */
	public final long loadObjectArray(TaskClass __cl, int... __vs)
	{
		return this.allocator.allocateArrayInt(__cl, __vs);
	}
	
	/**
	 * Loads a string in the target virtual machine instance.
	 *
	 * @param __s The string to load.
	 * @return The resulting string pointer.
	 * @since 2019/10/13
	 */
	public final int loadString(String __s)
	{
		if (__s == null)
			return 0;
		
		throw new todo.TODO();
	}
	
	/**
	 * Allocates and initializes a new instance of the given class target.
	 *
	 * @param __cl The class to allocate.
	 * @return The pointer to the instance of the given object.
	 * @throws NoSuchMethodTaskException If there is no default constructor.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/10/13
	 */
	public final long newInstance(TaskClass __cl)
		throws NoSuchMethodTaskException, NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		Assembly.breakpoint();
		throw Debugging.todo();
		/*
		// Need this to load from the class info
		ClassInfoUtility ciutil = this.classInfoUtility();
		
		// {@squirreljme.error SV13 Class has no default constructor.}
		int defnew = ciutil.defaultNew(__cl);
		if (defnew == 0)
			throw new NoSuchMethodTaskException("SV13");
		
		// Allocate memory here
		long rv = this.allocator.allocateObject(__cl,
			ciutil.classAllocationSize(__cl));
		
		// Invoke the default constructor
		this.contextThread().execute(defnew, ciutil.poolPointer(__cl), rv);
		
		// Return the allocated pointer
		return rv;
		
		 */
	}
	
	/**
	 * Returns the physical ID of the task.
	 *
	 * @return The task physical ID.
	 * @since 2019/12/14
	 */
	public final int physicalProcessId()
	{
		return this.pid;
	}
}

