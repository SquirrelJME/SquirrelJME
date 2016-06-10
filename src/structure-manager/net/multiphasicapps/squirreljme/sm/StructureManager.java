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
	/**
	 * Intializes the object manager.
	 *
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/08
	 */
	public StructureManager()
		throws NullPointerException
	{
		throw new Error("TODO");
	}
	
	/**
	 * Returns the memory accessor which is associated with the manager.
	 *
	 * @param __rt The type of region to find a match for.
	 * @return The memory accessor this structure manager uses for the given
	 * region type.
	 * @since 2016/06/09
	 */
	public final MemoryAccessor memoryAccessor(MemoryRegionType __rt)
	{
		throw new Error("TODO");
	}
}

