// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.task;

import cc.squirreljme.jvm.Allocator;
import cc.squirreljme.jvm.Assembly;
import cc.squirreljme.jvm.Constants;
import cc.squirreljme.jvm.io.BinaryBlob;
import cc.squirreljme.jvm.Globals;
import cc.squirreljme.jvm.lib.ClassFileParser;
import cc.squirreljme.jvm.lib.ClassLibrary;
import cc.squirreljme.jvm.lib.ClassPath;
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
	 * Creates a new thread
	 *
	 * @return The resulting thread.
	 * @since 2019/10/13
	 */
	public final TaskThread createThread()
	{
		return Globals.getThreadManager().createThread(this.pid);
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
		ClassPath classpath = this.classpath;
		
		// Try to find already initialized class
		HashMap<String, TaskClass> classes = this._classes;
		synchronized (this)
		{
			// Already made?
			rv = classes.get(__cl);
			if (rv != null)
				return rv;
			
			// {@squirreljme.error SV0m The specified class does not exist.
			// (The class which does not exist)}
			int cldx = classpath.resourceClassFind(__cl);
			if (cldx < 0)
				throw new TaskNoSuchClassException("SV0m " + __cl);
			
			// Setup new one and store it
			classes.put(__cl, (rv = new TaskClass(cldx)));
			
			// Pre-initialize the class
			rv.initializeClassInfoSetup(this);
		}
		
		// Perform post initialization as needed
		return rv.initializeClassInfo(this);
	}
	
	/**
	 * Loads an object array with the given values and class type.
	 *
	 * @param __cl The class type.
	 * @param __vs The values to store.
	 * @return The resulting object array.
	 * @since 2019/10/13
	 */
	public final int loadObjectArray(TaskClass __cl, int... __vs)
	{
		throw new todo.TODO();
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
	 * @since 2019/10/13
	 */
	public final int newInstance(TaskClass __cl)
	{
		throw new todo.TODO();
	}
}

