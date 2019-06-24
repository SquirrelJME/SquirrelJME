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
	 * Loads the class information for this class.
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
		
		// The library it is in and the resource index
		BootLibrary inlib = null;
		int dx = -1;
		
		// Go through the class path and find this class
		BootLibrary[] classpath = this.classpath;
		{
			// Find this class
			String filename = __cl + ".class";
			
			// Scan through all the class path
			for (int i = 0, n = classpath.length; i < n; i++)
			{
				BootLibrary bl = classpath[i];
				
				// Search for the class fiole
				dx = bl.indexOf(filename);
				if (dx >= 0)
				{
					inlib = bl;
					break;
				}
			}
			
			// If it was not found, remember this and then stop
			if (dx < 0)
			{
				// Cache for later
				classinfos.put(__cl, null);
				
				// Not found
				return 0;
			}
		}
		
		Assembly.breakpoint();
		throw new todo.TODO();
	}
}

