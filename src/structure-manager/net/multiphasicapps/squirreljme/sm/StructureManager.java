// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.sm;

import net.multiphasicapps.squirreljme.mmu.MemoryAccessor;
import net.multiphasicapps.squirreljme.mmu.MemoryRegionType;

/**
 * This is the manager which manages all allocated structures.
 *
 * @since 2016/06/08
 */
public class StructureManager
{
	/** Accessors for each memory region type. */
	private final MemoryAccessor[] _accessors;
	
	/**
	 * Intializes the object manager.
	 *
	 * @param __ma The memory accessor which are used to access the system
	 * memory space.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/08
	 */
	public StructureManager(MemoryAccessor... __ma)
		throws NullPointerException
	{
		// Check
		if (__ma == null)
			throw new NullPointerException("NARG");
		
		// Map all input regions to accessors (for code/data)
		int nb = MemoryRegionType.usedBits();
		
		throw new Error("TODO");
	}
	
	/**
	 * Returns the memory accessor which is associated with the manager.
	 *
	 * @param __rt The type of region to find a match for. Only single ordinal
	 * values are permitted.
	 * @return The memory accessor this structure manager uses for the given
	 * region type, if there is none then {@code null} is returned.
	 * @throws IllegalArgumentException If the ordinal mask for the region
	 * type does not contain a single bit.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/09
	 */
	public final MemoryAccessor memoryAccessor(MemoryRegionType __rt)
		throws IllegalArgumentException, NullPointerException
	{
		// Check
		if (__rt == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error BY01 The ordinal for the requested region type
		// has zero or more than one bit. (The requested region type)}
		if (Integer.bitCount(__rt.ordinal()) != 1)
			throw new IllegalArgumentException(String.format("BY01 %s", __rt));
		
		// Go through all regions and return the one associated with the
		// given type.
		MemoryAccessor[] mas = this._accessors;
		int n = mas.length;
		for (int i = 0; i < n; i++)
			if (true)
				throw new Error("TODO");
		
		throw new Error("TODO");
	}
}

