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
	
	/** Classes which have been loaded. */
	@Deprecated
	public final HashMap<String, ClientClassInfo> classinfos =
		new HashMap<>();
	
	/** Classes which have been read and initialized. */
	private final HashMap<String, TaskClass> _classes =
		new HashMap<>();
	
	/** The accessor for client class information. */
	private volatile MiniClassAccessor _classinfoaccessor;
	
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
	 * Returns the mini-class accessor for the {@link ClassInfo} class.
	 *
	 * @return The ClassInfo mini-class accessor.
	 * @since 2019/07/11
	 */
	@Deprecated
	public final MiniClassAccessor classInfoAccessor()
	{
		// If it has already been used, only get it once!
		MiniClassAccessor rv = this._classinfoaccessor;
		if (rv != null)
			return rv;
		
		// Debug
		todo.DEBUG.note("Searching for ClassInfo...");
		
		// Locate class resource
		int dx = this.classpath.resourceClassFind(
			"cc/squirreljme/jvm/ClassInfo");
		if (dx < 0)
			return null;
		
		// Cache it
		this._classinfoaccessor = (rv = new MiniClassAccessor(
			this.classpath.resourceData(dx)));
		
		// Use it
		return rv;
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
	 * @throws TaskVirtualMachineError If there is something wrong with the
	 * task virtual machine.
	 * @since 2019/10/13
	 */
	public final TaskClass loadClass(String __cl)
		throws NullPointerException, TaskVirtualMachineError
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		TaskClass rv;
		
		// Try to find already initialized class
		HashMap<String, TaskClass> classes = this._classes;
		synchronized (this)
		{
			// Already made?
			rv = classes.get(__cl);
			if (rv != null)
				return rv;
			
			// Otherwise store and set it
			classes.put(__cl, (rv = new TaskClass()));
		}
		
		// Needed to search for classes
		ClassPath classpath = this.classpath;
		
		// {@squirreljme.error SV0l Task does has ClassInfo in its
		// class path.}
		int cidx = classpath.resourceClassFind("cc/squirreljme/jvm/ClassInfo");
		if (cidx < 0)
			throw new TaskVirtualMachineError("SV0l");
		
		// Get parser for this class, because we need its info
		ClassFileParser ciparser = new ClassFileParser(
			classpath.resourceData(cidx));
		
		// Need to allocate class data
		TaskAllocator allocator = this.allocator;
		
		// Allocate the space needed to store the class information
		int infopointer = allocator.allocateObject(Constants.OBJECT_BASE_SIZE +
			ciparser.fieldSize(false));
		rv._infopointer = infopointer;
		
		throw new todo.TODO();
	}
	
	/**
	 * Loads the in-memory class information for the given class.
	 *
	 * @param __cl The class to load.
	 * @return The loaded class information or {@code null} if it is not found.
	 * @throws ClientLinkageError If the client class could not be initialized.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/06/23
	 */
	@Deprecated
	public final ClientClassInfo loadClassInfo(String __cl)
		throws ClientLinkageError, NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		// See if it was loaded already
		HashMap<String, ClientClassInfo> classinfos = this.classinfos;
		ClientClassInfo rv = classinfos.get(__cl);
		if (classinfos.containsKey(__cl))
			return (rv != null ? rv : null);
		
		// Debug
		todo.DEBUG.note("Finding class %s...", __cl);
		
		// Attempt to locate the class, if not found remember this and stop
		int dx = this.classpath.resourceClassFind(__cl);
		if (dx < 0)
		{
			classinfos.put(__cl, null);
			return null;
		}
		
		// Debug
		todo.DEBUG.note("Initializing class info %s...", __cl);
		
		// Get the layout for the class information (where fields go)
		MiniClassAccessor ccia = this.classInfoAccessor();
		
		// {@squirreljme.error SV04 Could not allocate class information.}
		int cip = this.allocator.allocate(Allocator.CHUNK_BIT_IS_OBJECT,
			Constants.OBJECT_BASE_SIZE + ccia.baseInstanceSize());
		if (cip == 0)
			throw new ClientLinkageError("SV04");
		
		// Class initialization involves many recursive calls into the class
		// loading being called, so as such store the class info pointer and
		// such before any processing is done so it is done first
		classinfos.put(__cl,
			(rv = new ClientClassInfo(cip, this.classpath.resourceData(dx))));
		
		// Write the class type for the class information
		Assembly.memWriteInt(cip, Constants.OBJECT_CLASS_OFFSET,
			this.loadClassInfo("cc/squirreljme/jvm/ClassInfo").
			classinfopointer);
		
		// "Open" the class so that its accessor can be used
		try (ClientClassInfo xp = rv.open())
		{
			// Load the mini-class accessor
			MiniClassAccessor clma = xp.accessor();
			
			Assembly.breakpoint();
			throw new todo.TODO();
		}
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

