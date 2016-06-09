// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.mmu;

/**
 * This is an interface which is used to access memory.
 *
 * Addresses which exist in a system might not be comparable to each other (for
 * example if they belong to another bank or segment). However to the view of
 * SquirrelJME, all pointers are a maximum of 64-bits. Comparison of pointers
 * not using the pointer comparison method is undefined. Additionally, adding
 * or subtracting a number of bytes to the raw {@code long} value is
 * undefined, the add/subtract operations should be used instead.
 *
 * For example on the NES there is bank switching. A memory address that
 * exists within a bank cannot be compared to another pointer that is outside
 * of the bank or is in a separate bank.
 *
 * All addresses are unsigned.
 *
 * @since 2016/06/09
 */
public interface MemoryAccessor
{
	/**
	 * Adds the given offset number of bytes to the given base address and
	 * returns it. It is possible for the resulting address to not be valid, if
	 * this is the case then an exception is thrown.
	 *
	 * @param __base The base address to add the offset into, this is treated
	 * as an unsigned value.
	 * @param __off The offset to add to the base, this is treated as a
	 * signed value.
	 * @return The newly transformed address.
	 * @throws MemoryAddressOperationException If the resulting address has no
	 * representation, likely because it overflows a given bound.
	 * @since 2016/06/09
	 */
	public abstract long addAddress(long __base, long __off)
		throws MemoryAddressOperationException;
	
	/**
	 * This returns the size of the cache line which is used on the CPU.
	 *
	 * @return The cache line size used in the CPU, if the CPU does not have a
	 * cache or in situations this is not defined then this must return
	 * {@code 1}.
	 * @since 2016/06/09
	 */
	public abstract int cacheLineSize();
	
	/**
	 * Compares two addresses in an implementation defined manner.
	 *
	 * @param __a The first address, this is unsigned.
	 * @param __b The second address, this is unsigned.
	 * @return The comparison result of the two pointers.
	 * @since 2016/06/09
	 */
	public abstract PointerComparison compareAddress(long __a, long __b);
	
	/**
	 * Calculates the number of bytes that the other address is offset from
	 * the base address.
	 *
	 * @param __base The base address to get the offset to another address
	 * from, this value is unsigned.
	 * @param __other The address to calculate the offset of using the base
	 * address, this value is unsigned.
	 * @return The signed offset from the base address.
	 * @throws MemoryAddressOperationException If the offset cannot be
	 * calculated because it exceeds the maximum signed long value or the two
	 * addresses cannot compare to each other.
	 * @since 2016/06/09
	 */
	public abstract long offsetFrom(long __base, long __other)
		throws MemoryAddressOperationException;
	
	/**
	 * Reads a single byte value.
	 *
	 * @param __addr The address to read to.
	 * @return The read value.
	 * @throws MemoryReadException If the memory could not be read from.
	 * @since 2016/06/09
	 */
	public abstract byte readByte(long __addr)
		throws MemoryReadException;
	
	/**
	 * Reads a single int value.
	 *
	 * @param __addr The address to read to.
	 * @return The read value.
	 * @throws MemoryReadException If the memory could not be read from.
	 * @since 2016/06/09
	 */
	public abstract int readInt(long __addr)
		throws MemoryReadException;
	
	/**
	 * Reads a single long value.
	 *
	 * @param __addr The address to read to.
	 * @return The read value.
	 * @throws MemoryReadException If the memory could not be read from.
	 * @since 2016/06/09
	 */
	public abstract long readLong(long __addr)
		throws MemoryReadException;
	
	/**
	 * Reads a single short value.
	 *
	 * @param __addr The address to read to.
	 * @return The read value.
	 * @throws MemoryReadException If the memory could not be read from.
	 * @since 2016/06/09
	 */
	public abstract short readShort(long __addr)
		throws MemoryReadException;
	
	/**
	 * Writes a single byte value.
	 *
	 * @param __addr The address to write to.
	 * @param __v The value to write.
	 * @throws MemoryWriteException If the memory could not be written to.
	 * @since 2016/06/09
	 */
	public abstract void writeByte(long __addr, byte __v)
		throws MemoryWriteException;
	
	/**
	 * Writes a single int value.
	 *
	 * @param __addr The address to write to.
	 * @param __v The value to write.
	 * @throws MemoryWriteException If the memory could not be written to.
	 * @since 2016/06/09
	 */
	public abstract void writeInt(long __addr, int __v)
		throws MemoryWriteException;
	
	/**
	 * Writes a single long value.
	 *
	 * @param __addr The address to write to.
	 * @param __v The value to write.
	 * @throws MemoryWriteException If the memory could not be written to.
	 * @since 2016/06/09
	 */
	public abstract void writeLong(long __addr, long __v)
		throws MemoryWriteException;
	
	/**
	 * Writes a single short value.
	 *
	 * @param __addr The address to write to.
	 * @param __v The value to write.
	 * @throws MemoryWriteException If the memory could not be written to.
	 * @since 2016/06/09
	 */
	public abstract void writeShort(long __addr, short __v)
		throws MemoryWriteException;
}

