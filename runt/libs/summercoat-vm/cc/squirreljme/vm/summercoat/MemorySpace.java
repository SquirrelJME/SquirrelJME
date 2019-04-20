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
import java.util.ListIterator;
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
		8388608;
	
	/** Static field area size. */
	public static final int STATIC_FIELD_SIZE =
		65536;
	
	/** The total amount of memory available. */
	public final int total;
	
	/** Pointer to the static field storage. */
	public final int staticfptr;
	
	/** The memory bytes. */
	protected final byte[] memory;
	
	/** Partitions within memory. */
	private final LinkedList<Partition> _parts =
		new LinkedList<>();
	
	/** Method handles currently registered. */
	private final Map<MethodHandle, Integer> _handles =
		new HashMap<>();
	
	/** Classes currently registered, mapped to ID. */
	private final List<LoadedClass> _classids =
		new LinkedList<>();
	
	/** Used space. */
	private volatile int _used;
	
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
		
		// Reserve an area for static fields to be stored in
		int staticfptr = ((__msz - STATIC_FIELD_SIZE) + 3) & (~3);
		this.staticfptr = staticfptr;
		
		// Initial memory partition is all of the free space minus the first
		// portion of memory which is used for null pointers
		this._parts.add(new Partition(16, staticfptr));
		
		// The first of these are considered as nothing
		this._handles.put(null, 0);
		this._classids.add(null);
	}
	
	/**
	 * Allocates the specified amount of memory.
	 *
	 * @param __isobj Is this an allocation for an object? This is so that
	 * finding objects is easier for emergency garbage collection.
	 * @param __sz The number of bytes to allocate.
	 * @return The address of the allocation.
	 * @throws IllegalArgumentException If the size is zero or negative.
	 * @since 2019/04/18
	 */
	public final int allocate(boolean __isobj, int __sz)
		throws IllegalArgumentException
	{
		// {@squirreljme.error AE0k Cannot allocate a zero or negative
		// amount of memory.}
		if (__sz <= 0)
			throw new IllegalArgumentException("AE0k");
		
		// Make sure all allocations are rounded so that they occur at
		// even boundaries
		if ((__sz & 3) != 0)
			__sz = (__sz + 3) & (~3);
		
		// Lock on self since only a single thread should be allocating at
		// a time
		LinkedList<Partition> parts = this._parts;
		synchronized (this)
		{
			// Retry loop
			for (boolean second = false;; second = true)
			{
				// Scan through and find a partition to allocate in
				ListIterator<Partition> li = parts.listIterator();
				while (li.hasNext())
				{
					Partition part = li.next();
					
					// Allocate in this part?
					if (__sz <= part.size)
						return this.__allocate(li, part, true, __sz);
				}
				
				// {@squirreljme.error AE0l Could not find a sufficient free
				// space span for allocation. (The amount of space requested
				// for allocation)}
				if (second)
					throw new VMOutOfMemoryException("AE0l " + __sz);
				
				// Perform some emergency garbage collection
				throw new todo.TODO();
			}
		}
	}
	
	/**
	 * Returns the class for the given index.
	 *
	 * @param __cli The class index.
	 * @return The class used or {@code null} if no class is registered with
	 * the given index.
	 * @since 2019/04/19
	 */
	public final LoadedClass classByIndex(int __cli)
	{
		if (__cli <= 0)
			return null;
		
		List<LoadedClass> classids = this._classids;
		synchronized (classids)
		{
			// Out of range
			if (__cli >= classids.size())
				return null;
			
			// Return class for this
			return classids.get(__cli);
		}
	}
	
	/**
	 * Reads from the given address.
	 *
	 * @param __atmc Is this an atomic operation?
	 * @param __addr The address to read from.
	 * @return The read value.
	 * @throws VMVirtualMachineException If the address is not aligned or is
	 * out of bounds of memory.
	 * @since 2019/04/19
	 */
	public final int memReadInt(boolean __atmc, int __addr)
		throws VMVirtualMachineException
	{
		// {@squirreljme.error AE0n Illegal memory access.
		// (The address)}
		if (__addr < 0 || __addr >= this.total - 3 || (__addr & 3) != 0)
			throw new VMVirtualMachineException(
				String.format("AE0n %08x", __addr));
		
		// Read from memory
		int rv;
		byte[] memory = this.memory;
		if (__atmc)
			synchronized (memory)
			{
				rv = ((memory[__addr++] & 0xFF) << 24) |
					((memory[__addr++] & 0xFF) << 16) |
					((memory[__addr++] & 0xFF) << 8) |
					(memory[__addr++] & 0xFF);
			}
		else
		{
			rv = ((memory[__addr++] & 0xFF) << 24) |
				((memory[__addr++] & 0xFF) << 16) |
				((memory[__addr++] & 0xFF) << 8) |
				(memory[__addr++] & 0xFF);
		}
		
		// Debug
		todo.DEBUG.note("*%08x -> %d", __addr, rv);
		
		return rv;
	}
	
	/**
	 * Writes memory to the given address.
	 *
	 * @param __atmc Is this an atomic operation?
	 * @param __addr The address to write to.
	 * @param __v The value to write.
	 * @throws VMVirtualMachineException If the address is not aligned or is
	 * out of bounds of memory.
	 * @since 2019/04/19
	 */
	public final void memWriteInt(boolean __atmc, int __addr, int __v)
		throws VMVirtualMachineException
	{
		// {@squirreljme.error AE0m Illegal memory access.
		// (The address; The value)}
		if (__addr < 0 || __addr >= this.total - 3 || (__addr & 3) != 0)
			throw new VMVirtualMachineException(
				String.format("AE0m %08x %d", __addr, __v));
		
		// Debug
		todo.DEBUG.note("*%08x <- %d", __addr, __v);
		
		// Write into memory
		byte[] memory = this.memory;
		if (__atmc)
			synchronized (memory)
			{
				memory[__addr++] = (byte)(__v >>> 24);
				memory[__addr++] = (byte)(__v >>> 16);
				memory[__addr++] = (byte)(__v >>> 8);
				memory[__addr++] = (byte)__v;
			}
		else
		{
			memory[__addr++] = (byte)(__v >>> 24);
			memory[__addr++] = (byte)(__v >>> 16);
			memory[__addr++] = (byte)(__v >>> 8);
			memory[__addr++] = (byte)__v;
		}
	}
	
	/**
	 * Attempts to read the class for the given pointer.
	 *
	 * @param __p The pointer to read.
	 * @return The class for the pointer or {@code null} if it is unknown or
	 * the pointer is not valid.
	 * @since 2019/04/19
	 */
	public final LoadedClass readClassOptional(int __p)
	{
		// If this breaks memory restrictions then do not do any reads
		if (__p < 0 || __p >= this.total - 3 || (__p & 3) != 0)
			return null;
		
		// Locate class from read memory
		return this.classByIndex(this.memReadInt(false, __p));
	}
	
	/**
	 * Registers the given class to a unique index.
	 *
	 * @param __cl The class to register.
	 * @return The class index.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/19
	 */
	public final int registerClass(LoadedClass __cl)
		throws NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		List<LoadedClass> classids = this._classids;
		synchronized (classids)
		{
			// Use pre-existing index?
			int rv = classids.indexOf(__cl);
			if (rv >= 0)
				return rv;
			
			// Register the class
			rv = classids.size();
			classids.add(__cl);
			return rv;
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
	 * Performs the actual allocation work.
	 *
	 * @param __li The list iterator for partition splitting.
	 * @param __p The current partition.
	 * @param __isobj Is an object being allocated?
	 * @param __sz The number of bytes to allocate.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/04/19
	 */
	private final int __allocate(ListIterator<Partition> __li, Partition __p,
		boolean __isobj, int __sz)
		throws NullPointerException
	{
		if (__li == null || __p == null)
			throw new NullPointerException("NARG");
		
		// The address to use is the starting point
		int rv = __p.start;
		
		// Clear the memory so that anything that any created objects are
		// auto zero-initialized
		byte[] memory = this.memory;
		for (int i = rv, n = rv + __sz; i < n; i++)
			memory[i] = 0;
		
		// Claim partition space, either replace the last index with the
		// new partition or drop it
		Partition np = __p.claimStart(__sz);
		if (np == null)
			__li.remove();
		else
			__li.set(np);
		
		// Claim used space
		this._used += __sz;
		
		// Debug
		todo.DEBUG.note("Alloc %d @ %08x", __sz, rv);
		
		// Use this pointer
		return rv;
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
		
		/**
		 * Claim the start of the partition for the given bytes and return
		 * a new partition to replace the current one with.
		 *
		 * @param __sz The number of bytes to claim.
		 * @return The new partition, or {@code null} if there is no space
		 * remaining.
		 * @since 2019/04/19
		 */
		public final Partition claimStart(int __sz)
		{
			// Claimed all the space? Will be removed
			if (__sz >= this.size)
				return null;
			
			// New partition to be used instead
			return new Partition(this.start + __sz, this.end);
		}
	}
}

