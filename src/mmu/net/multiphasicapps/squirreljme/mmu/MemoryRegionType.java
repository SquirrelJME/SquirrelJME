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
 * This represents the type of memory that a region may be under.
 *
 * Note that on most systems {@link #CODE} and {@link #DATA} will be the
 * same region, while on others they may be completely different. Code in the
 * kernel, garbage collector, and otherwise should not depend on the
 * assumption that the CPU is capable of executing code from the {@link #DATA}
 * region.
 *
 * @since 2016/06/09
 */
public enum MemoryRegionType
{
	/** Not defined. */
	UNDEFINED,
	
	/** Executable code (01). */
	CODE,
	
	/** Memory (for reading and writing) (10). */
	DATA,
	
	/** Executable and data storage. (11). */
	BOTH,
	
	/** End. */
	;
	
	/** Used for ordinal to region. */
	private static final MemoryRegionType[] _VALUES =
		values();
	
	/** The number of bits which have a representation. */
	private static final int _MAX_BITS =
		Integer.bitCount(Integer.highestOneBit(this._VALUES) - 1);
	
	/** Bits to region mappings. */
	private static final MemoryRegionType[] _BITS;
	
	/**
	 * Determines which bits map to which regions based on the ordinal value.
	 *
	 * @since 2016/06/11
	 */
	static
	{
		// Load
		MemoryRegionType[] vals = _VALUES;
		int n = vals.length;
		int nb = MAX_BITS;
		
		// Set singular bit locations
		MemoryRegionType[] bb = new MemoryRegionType[nb];
		for (int i = 0, j = 0; i < n; i++)
		{
			MemoryRegionType v = vals[i];
			if (Integer.bitCount(v.ordinal()) == 1)
				bb[j++] = v;
		}
		_BITS = bb;
	}
	
	/**
	 * Returns the mapping of individual bits as shift locations to the
	 * memory region that is specified.
	 *
	 * @return The mapping for bit shifts to regions.
	 * @since 2016/06/11
	 */
	public static MemoryRegionType[] bitMap()
	{
		return _BITS.clone();
	}
	
	/**
	 * Returns the memory region type that is associated with the given bit.
	 *
	 * @param __i The shift of the bit value.
	 * @return The memory region that is associated with the given type.
	 * @since 2016/06/11
	 */
	public static MemoryRegionType ofBit(int __i)
	{
		// Will never match
		if (__i < 0)
			return null;
		
		// Oversized
		int n = _MAX_BITS:
		if (__i >= n)
			return null;
		
		// Return the region for the given shift
		return _BITS[__i];
	}
	
	/**
	 * Returns the number of bits that are used
	 *
	 * @return The number of region types.
	 * @since 2016/06/11
	 */
	public static int usedBits()
	{
		return _MAX_BITS;
	}
}

