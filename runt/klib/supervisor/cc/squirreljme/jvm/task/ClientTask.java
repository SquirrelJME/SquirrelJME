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
import cc.squirreljme.jvm.lib.ClassLibrary;
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
	public final ClassLibrary[] classpath;
	
	/** Classes which have been loaded. */
	@Deprecated
	public final HashMap<String, ClientClassInfo> classinfos =
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
	public ClientTask(int __pid, int __lid, ClassLibrary[] __cp)
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
		int dx = this.resourceClassFind("cc/squirreljme/jvm/ClassInfo");
		if (dx < 0)
			return null;
		
		// Cache it
		this._classinfoaccessor = (rv = new MiniClassAccessor(
			this.resourceData(dx)));
		
		// Use it
		return rv;
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
		int dx = this.resourceClassFind(__cl);
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
		int cip = this.allocate(Allocator.CHUNK_BIT_IS_OBJECT,
			Constants.OBJECT_BASE_SIZE + ccia.baseInstanceSize());
		if (cip == 0)
			throw new ClientLinkageError("SV04");
		
		// Class initialization involves many recursive calls into the class
		// loading being called, so as such store the class info pointer and
		// such before any processing is done so it is done first
		classinfos.put(__cl,
			(rv = new ClientClassInfo(cip, this.resourceData(dx))));
		
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
	 * Searches for the given class name resource for the given class
	 *
	 * @param __name The name of the class.
	 * @return A negative value if not found, otherwise the class path index
	 * will be shifted up by {@link #_INDEX_SHIFT} and the resource index will
	 * be on the lower mask.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/07/11
	 */
	public final int resourceClassFind(String __name)
		throws NullPointerException
	{
		if (__name == null)
			throw new NullPointerException("NARG");
		
		return this.resourceFind(__name + ".class");
	}
	
	/**
	 * Returns the data pointer for the given resource.
	 *
	 * @param __dx The index to get the data pointer for.
	 * @return The data pointer of the resource.
	 * @throws IndexOutOfBoundsException If the index is not found in any
	 * library.
	 * @since 2019/07/11
	 */
	public final BinaryBlob resourceData(int __dx)
		throws IndexOutOfBoundsException
	{
		// {@squirreljme.error SV08 Out of range resource.}
		int cpdx = __dx >>> _INDEX_SHIFT;
		ClassLibrary[] classpath = this.classpath;
		if (cpdx < 0 || cpdx >= classpath.length)
			throw new IndexOutOfBoundsException("SV08");
		
		// Get resource pointer from this
		return classpath[cpdx].resourceData(__dx & _INDEX_MASK);
	}
	
	/**
	 * Searches for the given resource in this client task.
	 *
	 * @param __name The name of the resource.
	 * @return A negative value if not found, otherwise the class path index
	 * will be shifted up by {@link #_INDEX_SHIFT} and the resource index will
	 * be on the lower mask.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/07/11
	 */
	public final int resourceFind(String __name)
		throws NullPointerException
	{
		if (__name == null)
			throw new NullPointerException("NARG");
		
		// Scan the classpath
		ClassLibrary[] classpath = this.classpath;
		for (int i = 0, n = classpath.length; i < n; i++)
		{
			// Locate resource
			int rv = classpath[i].indexOf(__name);
			
			// Was found?
			if (rv >= 0)
				return (i << _INDEX_SHIFT) | rv;
		}
		
		// Not found
		return -1;
	}
	
	/**
	 * Searches for the given resource in this client task in the given
	 * specified classpath library.
	 *
	 * @param __name The name of the resource.
	 * @param __in The class path library to look in.
	 * @return A negative value if not found, otherwise the class path index
	 * will be shifted up by {@link #_INDEX_SHIFT} and the resource index will
	 * be on the lower mask.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/07/11
	 */
	public final int resourceFindIn(String __name, int __in)
		throws NullPointerException
	{
		if (__name == null)
			throw new NullPointerException("NARG");
		
		// Out of range values are always not found
		ClassLibrary[] classpath = this.classpath;
		if (__in < 0 || __in >= classpath.length)
			return -1;
		
		// Locate resource
		int rv = classpath[__in].indexOf(__name);
		
		// If it was found shift in
		if (rv >= 0)
			return (__in << _INDEX_SHIFT) | rv;
		
		// Otherwise does not exist
		return -1;
	}
	
	/**
	 * Searches for the given resource in this client task in the given
	 * specified classpath library, if it is not found in that library then
	 * all libraries on the classpath are searched.
	 *
	 * @param __name The name of the resource.
	 * @param __in The class path library to look in.
	 * @return A negative value if not found, otherwise the class path index
	 * will be shifted up by {@link #_INDEX_SHIFT} and the resource index will
	 * be on the lower mask.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/07/11
	 */
	public final int resourceFindInOtherwise(String __name, int __in)
		throws NullPointerException
	{
		if (__name == null)
			throw new NullPointerException("NARG");
		
		// Search in this specific library first
		int rv = this.resourceFindIn(__name, __in);
		if (rv >= 0)
			return rv;
		
		// Then locate it in any class library
		return this.resourceFind(__name);
	}
}

