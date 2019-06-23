// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm;

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
	 * @return The loaded class information.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/06/23
	 */
	public final int loadClassInfo(String __cl)
		throws NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		// Debug
		todo.DEBUG.note("Loading class %s...", __cl);
		
		Assembly.breakpoint();
		throw new todo.TODO();
	}
}

