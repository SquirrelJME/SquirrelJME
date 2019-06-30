// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm;

import java.util.HashMap;

/**
 * This represents a single task which has information on what it is running
 * along with its ID and such.
 *
 * @since 2019/06/22
 */
public final class ClientTask
{
	/** The shift for index access. */
	private static final int _INDEX_SHIFT =
		24;
	
	/** The mask for the index. */
	private static final int _INDEX_MASK =
		0x00FFFFFF;
	
	/** The physical task ID. */
	public final int pid;
	
	/** The logical task ID. */
	public final int lid;
	
	/** Allocation tag bit. */
	public final int tagbits;
	
	/** The classpath. */
	public final BootLibrary[] classpath;
	
	/** Classes which have been loaded. */
	public final HashMap<String, ClientClassInfo> classinfos =
		new HashMap<>();
	
	/** The layout for client class information. */
	private volatile ClientClassInfoLayout _classinfolayout;
	
	/**
	 * Initializes the client task.
	 *
	 * @param __pid The PID.
	 * @param __lid The LID.
	 * @param __cp The classpath used.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/06/22
	 */
	public ClientTask(int __pid, int __lid, BootLibrary[] __cp)
		throws NullPointerException
	{
		if (__cp == null)
			throw new NullPointerException("NARG");
		
		this.pid = __pid;
		this.lid = __lid;
		this.classpath = __cp;
		
		// The tag bits are just the PID shifted up a bit
		this.tagbits = __pid << 4;
	}
	
	/**
	 * Allocates memory for this task context.
	 *
	 * @param __tag The tag used.
	 * @param __sz The number of bytes to allocate.
	 * @return The allocated bytes.
	 * @since 2019/06/23
	 */
	public final int allocate(int __tag, int __sz)
	{
		// Just perform the allocation with our PID as part of the tag and
		// whatever was passed, masked correctly
		return Allocator.allocate(
			this.tagbits | (__tag & Allocator.CHUNK_BITS_VALUE_MASK), __sz);
	}
	
	/**
	 * Returns the layout which describes how the class info structure is
	 * laid out in memory.
	 *
	 * @return The class info layout.
	 * @since 2019/06/29
	 */
	public final ClientClassInfoLayout classInfoLayout()
	{
		// Has already been read?
		ClientClassInfoLayout rv = this._classinfolayout;
		if (rv != null)
			return rv;
		
		// Debug
		todo.DEBUG.note("Determining ClassInfo layout...");
		
		// Find index for object and 
		BootLibrary[] classpath = this.classpath;
		int jodx = ClientTask.__findClassIndex(classpath, "java/lang/Object"),
			cidx = ClientTask.__findClassIndex(classpath,
				"cc/squirreljme/jvm/ClassInfo");
		
		// This is not good
		if (jodx < 0 || cidx < 0)
			return null;
		
		Assembly.breakpoint();
		throw new todo.TODO();
	}
	
	/**
	 * Loads the in-memory class information for the given class.
	 *
	 * @param __cl The class to load.
	 * @return The loaded class information or {@code 0} if it is not found.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/06/23
	 */
	public final int loadClassInfo(String __cl)
		throws NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		// See if it was loaded already
		HashMap<String, ClientClassInfo> classinfos = this.classinfos;
		ClientClassInfo rv = classinfos.get(__cl);
		if (classinfos.containsKey(__cl))
			return (rv != null ? rv.classinfopointer : 0);
		
		// Debug
		todo.DEBUG.note("Finding class %s...", __cl);
		
		// Attempt to locate the class
		BootLibrary[] classpath = this.classpath;
		int dx = ClientTask.__findClassIndex(classpath, __cl);
		
		// If it was not found, remember this and then stop
		if (dx < 0)
		{
			// Cache for later
			classinfos.put(__cl, null);
			
			// Not found
			return 0;
		}
		
		// Get the layout for the class information (where fields go)
		ClientClassInfoLayout ccil = this.classInfoLayout();
		
		// Debug
		todo.DEBUG.note("Initializing class info %s...", __cl);
		
		Assembly.breakpoint();
		throw new todo.TODO();
	}
	
	/**
	 * Locates the given class within the classpath.
	 *
	 * @param __cp The classpath.
	 * @param __cl The class to locate.
	 * @return A negative value if not found, otherwise the classpath index
	 * shifted up by {@link #_INDEX_SHIFT} and then the resource index.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/06/29
	 */
	public static final int __findClassIndex(BootLibrary[] __cp, String __cl)
		throws NullPointerException
	{
		if (__cp == null || __cl == null)
			throw new NullPointerException("NARG");
		
		// The resource the class will be in
		String filename = __cl + ".class";
		
		// Debug
		todo.DEBUG.note("Scanning for %s...", filename);
		
		// Scan the classpath
		for (int i = 0, n = __cp.length; i < n; i++)
		{
			// Locate resource
			int rv = __cp[i].indexOf(filename);
			
			// Was found?
			if (rv >= 0)
				return (i << _INDEX_SHIFT) | rv;
		}
		
		// Not found
		return -1;
	}
}

