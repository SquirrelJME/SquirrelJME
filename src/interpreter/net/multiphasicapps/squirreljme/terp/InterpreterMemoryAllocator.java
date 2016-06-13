// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.terp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import net.multiphasicapps.squirreljme.mmu.MemoryAllocateException;
import net.multiphasicapps.squirreljme.mmu.MemoryAllocator;
import net.multiphasicapps.squirreljme.mmu.MemoryPlacementType;

/**
 * This is allocator which is associated with the interpreter and is used to
 * partition the memory spaces within it.
 *
 * @since 2016/06/13
 */
public class InterpreterMemoryAllocator
	implements MemoryAllocator
{
	/** Compares {@link Allocation} and/or {@link Number}. */
	public static final Comparator<Object> ADDRESS_COMPARATOR =
		new Comparator<Object>()
		{
			/**
			 * {@inheritDoc}
			 * @since 2016/06/13
			 */
			@Override
			public int compare(Object __a, Object __b)
			{
				// Get requested addresses
				long a = ((__a instanceof Number) ? ((Number)__a).longValue() :
					((Allocation)__a).address());
				long b = ((__b instanceof Number) ? ((Number)__b).longValue() :
					((Allocation)__b).address());
				
				// Compare
				if (a < b)
					return -1;
				else if (a > b)
					return 1;
				return 0;
			}
		};
	
	/** The accessor the interpreter uses for memory. */
	protected final InterpreterMemoryAccessor coremem;
	
	/** The number of bytes available to the interpreter. */
	protected final long memorysize;
	
	/** The available allocations (must always sorted). */
	private final List<Allocation> _allocations =
		new ArrayList<Allocation>();
	
	/**
	 * This initializes the memory accessor used in the interpreter.
	 *
	 * @param __ima The memory accessor used by the interpreter to partition
	 * memory with.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/13
	 */
	public InterpreterMemoryAllocator(InterpreterMemoryAccessor __ima)
		throws NullPointerException
	{
		// Check
		if (__ima == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.coremem = __ima;
		long ms;
		this.memorysize = (ms = __ima.interpreterMemorySize());
		
		// Setup initial allocation
		Allocation ia = new Allocation();
		ia._address = 0L;
		ia._length = ms;
		ia._free = true;
		this._allocations.add(ia);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/06/13
	 */
	@Override
	public final long allocate(MemoryPlacementType __mp, long __bytes)
		throws IllegalArgumentException, MemoryAllocateException
	{
		// {@squirreljme.error AN0g The number of bytes to allocate is zero
		// or negative. (The number of bytes to allocate)}
		if (__bytes <= 0)
			throw new MemoryAllocateException(String.format("AN0g %d",
				__bytes));
		
		// Lock
		List<Allocation> allocs = this._allocations;
		synchronized (allocs)
		{
			throw new Error("TODO");
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/06/13
	 */
	@Override
	public final void free(long __addr)
		throws MemoryAllocateException
	{
		// Lock
		List<Allocation> allocs = this._allocations;
		synchronized (allocs)
		{
			throw new Error("TODO");
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/06/13
	 */
	@Override
	public final void resize(long __addr, long __bytes)
		throws IllegalArgumentException, MemoryAllocateException
	{
		// {@squirreljme.error AN0f The number of bytes to resize to is zero
		// or negative. (The number of bytes to resize to)}
		if (__bytes <= 0)
			throw new MemoryAllocateException(String.format("AN0f %d",
				__bytes));
		
		// Lock
		List<Allocation> allocs = this._allocations;
		synchronized (allocs)
		{
			throw new Error("TODO");
		}
	}
	
	/**
	 * This represents a region of memory that has been allocated.
	 *
	 * Allocations are mutable and may change state.
	 *
	 * @since 2016/06/13
	 */
	public final class Allocation
		implements Comparable<Allocation>
	{
		/** The address this allocation is at. */
		private volatile long _address =
			Long.MIN_VALUE;
		
		/** The length of the allocation. */
		private volatile long _length =
			Long.MIN_VALUE;
		
		/** Is this a free region? */
		private volatile boolean _free;
		
		/**
		 * Initializes the base allocation.
		 *
		 * @since 2016/06/13
		 */
		private Allocation()
		{
		}
		
		/**
		 * Returns the starting address of this allocation.
		 *
		 * @return The allocation starting address.
		 * @since 2016/06/13
		 */
		public final long address()
		{
			// Lock
			synchronized (InterpreterMemoryAllocator.this._allocations)
			{
				return this._address;
			}
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/06/13
		 */
		@Override
		public final int compareTo(Allocation __b)
		{
			long a = this.address();
			long b = __b.address();
			
			// Compare
			if (a < b)
				return -1;
			else if (a > b)
				return 1;
			return 0;
		}
		
		/**
		 * Returns {@code true} if this is allocated.
		 *
		 * @return {@code true} if allocated.
		 * @since 2016/06/13
		 */
		public final boolean isAllocated()
		{
			// Lock
			synchronized (InterpreterMemoryAllocator.this._allocations)
			{
				return !this._free;
			}
		}
		
		/**
		 * Returns {@code true} if this is not allocated.
		 *
		 * @return {@code true} if not allocated.
		 * @since 2016/06/13
		 */
		public final boolean isFree()
		{
			// Lock
			synchronized (InterpreterMemoryAllocator.this._allocations)
			{
				return this._free;
			}
		}
		
		/**
		 * Returns the number of bytes this allocation uses.
		 *
		 * @return The number of bytes this allocation consumes.
		 * @since 2016/06/13
		 */
		public final long length()
		{
			// Lock
			synchronized (InterpreterMemoryAllocator.this._allocations)
			{
				return this._length;
			}
		}
	}
}

