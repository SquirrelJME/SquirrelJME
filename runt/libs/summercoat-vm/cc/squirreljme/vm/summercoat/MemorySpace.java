// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.summercoat;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * This contains the memory space that is used for a task, it contains the
 * instance space and the static field space.
 *
 * @since 2019/04/17
 */
public final class MemorySpace
{
	/** The default size of memory. */
	public static final int DEFAULT_MEMORY_SIZE =
		16777216;
	
	/** The total amount of memory available. */
	public final int total;
	
	/** The memory bytes. */
	public final byte[] memory;
	
	/** Partitions within memory. */
	private final LinkedList<Partition> _parts =
		new LinkedList<>();
	
	/** Method handles currently registered. */
	private Map<MethodHandle, Integer> _handles =
		new HashMap<>();
	
	/**
	 * Intializes the memory space with default settings.
	 *
	 * @since 2019/04/17
	 */
	public MemorySpace()
	{
		this(DEFAULT_MEMORY_SIZE);
	}
	
	/**
	 * Initializes the memory space with the given settings.
	 *
	 * @param __msz The amount of memory to allocate for the VM.
	 * @since 2019/04/17
	 */
	public MemorySpace(int __msz)
	{
		// Determine total to use
		this.total = (__msz <= 0 ? DEFAULT_MEMORY_SIZE : __msz);
		
		// Allocate memory chunk
		this.memory = new byte[__msz];
		
		// Initial memory partition is all of the free space
		this._parts.add(new Partition(0, __msz));
	}
	
	/**
	 * Allocates the specified amount of memory.
	 * 
	 * @param __sz The number of bytes to allocate.
	 * @return The address of the allocation.
	 * @throws IllegalArgumentException If the size is zero or negative.
	 * @since 2019/04/18
	 */
	public final int allocate(int __sz)
		throws IllegalArgumentException
	{
		// {@squirreljme.error AE0k Cannot allocate a zero or negative
		// amount of memory.}
		if (__sz <= 0)
			throw new IllegalArgumentException("AE0k");
		
		// Lock on self since only a single thread should be allocating at
		// a time
		synchronized (this)
		{
			throw new todo.TODO();
		}
	}
	
	/**
	 * Registers the method handle.
	 *
	 * @param __mh The handle to register.
	 * @return The index of the handle.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/19
	 */
	public final int registerHandle(MethodHandle __mh)
		throws NullPointerException
	{
		if (__mh == null)
			throw new NullPointerException("NARG");
		
		Map<MethodHandle, Integer> handles = this._handles;
		synchronized (handles)
		{
			// Use pre-existing index
			Integer rv = handles.get(__mh);
			if (rv != null)
				return rv;
			
			// Store
			int dx = handles.size();
			handles.put(__mh, dx);
			return dx;
		}
	}
	
	/**
	 * This represents a single partition within memory, generally
	 * representing free space.
	 *
	 * @since 2019/04/18
	 */
	public static final class Partition
	{
		/** Start, inclusive. */
		public final int start;
		
		/** End, exclusive. */
		public final int end;
		
		/** The partition size. */
		public final int size;
		
		/**
		 * Initializes the parition.
		 *
		 * @param __s The start.
		 * @param __e The end.
		 * @throws IllegalArgumentException If the end is at the start or
		 * is before it.
		 * @since 2019/04/18
		 */
		public Partition(int __s, int __e)
			throws IllegalArgumentException
		{
			// {@squirreljme.error AE0j End of partition cannot be at or
			// before the start of a partition.}
			if (__e <= __s)
				throw new IllegalArgumentException("AE0j");
			
			this.start = __s;
			this.end = __e;
			this.size = __e - __s;
		}
	}
}

