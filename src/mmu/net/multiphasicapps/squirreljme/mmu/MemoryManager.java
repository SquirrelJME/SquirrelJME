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
 * This class is given an allocator and accessor and allows for singular
 * management in code and data regions of memory.
 *
 * This allows Harvard architectures to be given the ability to use just in
 * time and/or ahead of time compilation. The code and data sections may have
 * different alignment properties.
 *
 * @since 2016/06/13
 */
public class MemoryManager
{
	/**
	 * This initializes the memory manager which is used to bridge an accessor
	 * and all allocator for a given memory accessor.
	 *
	 * Note that if the allocator allocator access memory other than the
	 * accessor then it is likely that incorrect results will occur.
	 *
	 * @param __ac The accessor for the region of memory.
	 * @param __al The allocator which is capable of allocating new regions
	 * within the specified region.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/06/13
	 */
	public MemoryManager()
		throws NullPointerException
	{
		throw new Error("TODO");
	}
}

