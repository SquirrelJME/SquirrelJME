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

import net.multiphasicapps.squirreljme.mmu.MemoryAccessor;
import net.multiphasicapps.squirreljme.mmu.MemoryAddressOperationException;
import net.multiphasicapps.squirreljme.mmu.MemoryReadException;
import net.multiphasicapps.squirreljme.mmu.MemoryRegionType;
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
 * @since 2016/06/11
 */
public class InterpreterMemoryAccessor
	implements MemoryAccessor
{
	/** The backing int array. */
	protected final int[] memory;
	
	/** The cache line size, generally not used except for alignment. */
	protected final int cls;
	
	/** The type of pointer used. */
	protected final MemoryPointerType pointertype;
	
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
		if ((__bytes & 3L) != 0 || __bytes <= 0 || ints <= 0L ||
			ints > Integer.MAX_VALUE)
			throw new IllegalArgumentException(String.format("AN0b %d",
				__bytes));
		
		// Allocate memory
		this.memory = new int[(int)ints];
		this.cls = Math.max(1, __cls);
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
}

