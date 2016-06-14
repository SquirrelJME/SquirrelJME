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
import net.multiphasicapps.squirreljme.mmu.MemoryAccessor;
import net.multiphasicapps.squirreljme.mmu.MemoryAddressOperationException;
import net.multiphasicapps.squirreljme.mmu.MemoryAllocateException;
import net.multiphasicapps.squirreljme.mmu.MemoryReadException;
import net.multiphasicapps.squirreljme.mmu.MemoryRegionType;
import net.multiphasicapps.squirreljme.mmu.MemoryPlacementType;
import net.multiphasicapps.squirreljme.mmu.MemoryPointerComparison;
import net.multiphasicapps.squirreljme.mmu.MemoryPointerType;
import net.multiphasicapps.squirreljme.mmu.MemoryWriteException;

/**
 * This is the memory accessor used by the interpreter, it has a fixed set
 * of specifics and is backed by an int array internally (for speed purposes).
 *
 * The addressable region that is accessed by the internal array starts at
 * address zero and extends to higher addresses. This means that the
 * interpreter could have a theoretical maximum of 8GiB.
 *
 * Since the backing accessor uses {@code int}, access of these kind of values
 * will not have a reduction in speed.
 *
 * The address space is flat and linear.
 *
 * @since 2016/06/11
 */
public class InterpreterMemoryAccessor
	implements MemoryAccessor
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
	
	/** The backing int array. */
	protected final int[] memory;
	
	/** The size of allocated memory. */
	protected final long memorysize;
	
	/** The cache line size, generally not used except for alignment. */
	protected final int cls;
	
	/** The type of pointer used. */
	protected final MemoryPointerType pointertype;
	
	/** The available allocations (must always sorted). */
	private final List<Allocation> _allocations =
		new ArrayList<Allocation>();
	
	/**
	 * Initializes the interpreter memory accessor which is backed on an
	 * integer array.
	 *
	 * @param __bytes The number of bytes to use
	 * @param __cls The cache line size, this has a minimal cap of 1.
	 * @param __pt The type of pointer used.
	 * @throws IllegalArgumentException If the number of bytes is zero,
	 * negative, is not a multiple of four, or if divided by 4 exceeds the
	 * max size of an array.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/11
	 */
	public InterpreterMemoryAccessor(long __bytes, int __cls,
		MemoryPointerType __pt)
		throws IllegalArgumentException, NullPointerException
	{
		// Check
		if (__pt == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.pointertype = __pt;
		
		// {@squirreljme.error AN0b The number of requested bytes must be a
		// multiple of 4 and cannot be zero or negative. Also, the number of
		// bytes divided by 4 cannot exceed the max 32-bit integer value. (The
		// number of requested bytes)}
		long ints = __bytes >>> 2;
		long bytes = ints * 4L;
		if ((__bytes & 3L) != 0 || __bytes <= 0 || ints <= 0L ||
			ints > Integer.MAX_VALUE)
			throw new IllegalArgumentException(String.format("AN0b %d",
				__bytes));
		
		// Allocate memory
		this.memory = new int[(int)ints];
		this.memorysize = bytes;
		this.cls = Math.max(1, __cls);
		
		// Setup initial allocation
		Allocation ia = new Allocation();
		ia._address = 0L;
		ia._length = bytes;
		ia._free = true;
		this._allocations.add(ia);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/06/11
	 */
	@Override
	public final long addAddress(long __base, long __off)
		throws MemoryAddressOperationException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/06/11
	 */
	@Override
	public final boolean atomicCompareAndSetInt(long __a, int __exp,
		int __set)
		throws MemoryReadException, MemoryWriteException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/06/11
	 */
	@Override
	public final int atomicGetAndAddInt(long __a, int __val)
		throws MemoryReadException, MemoryWriteException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/06/11
	 */
	@Override
	public final int atomicGetAndSetInt(long __a, int __val)
		throws MemoryReadException, MemoryWriteException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/06/11
	 */
	@Override
	public final int cacheLineSize()
	{
		return this.cls;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/06/11
	 */
	@Override
	public final MemoryPointerComparison compareAddress(long __a, long __b)
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/06/11
	 */
	@Override
	public final long offsetFrom(long __base, long __other)
		throws MemoryAddressOperationException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/06/11
	 */
	@Override
	public final MemoryPointerType pointerType()
	{
		return this.pointertype;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/06/11
	 */
	@Override
	public final byte readByte(long __addr)
		throws MemoryReadException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/06/11
	 */
	@Override
	public final int readInt(long __addr)
		throws MemoryReadException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/06/11
	 */
	@Override
	public final long readLong(long __addr)
		throws MemoryReadException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/06/11
	 */
	@Override
	public final short readShort(long __addr)
		throws MemoryReadException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/06/13
	 */
	@Override
	public final long regionClaim(MemoryPlacementType __mp, long __bytes)
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
	public final void regionFree(long __addr)
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
	public final void regionResize(long __addr, long __bytes)
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
	 * {@inheritDoc}
	 * @since 2016/06/11
	 */
	@Override
	public final MemoryRegionType regionType()
	{
		// Java is a harvard architecture where the execution environment
		// does not generally have access to static code data. Sort of anyway
		return MemoryRegionType.DATA;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/06/11
	 */
	@Override
	public final void writeByte(long __addr, byte __v)
		throws MemoryWriteException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/06/11
	 */
	@Override
	public final void writeInt(long __addr, int __v)
		throws MemoryWriteException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/06/11
	 */
	@Override
	public final void writeLong(long __addr, long __v)
		throws MemoryWriteException
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/06/11
	 */
	@Override
	public final void writeShort(long __addr, short __v)
		throws MemoryWriteException
	{
		throw new Error("TODO");
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
			synchronized (InterpreterMemoryAccessor.this._allocations)
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
			synchronized (InterpreterMemoryAccessor.this._allocations)
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
			synchronized (InterpreterMemoryAccessor.this._allocations)
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
			synchronized (InterpreterMemoryAccessor.this._allocations)
			{
				return this._length;
			}
		}
	}
}

