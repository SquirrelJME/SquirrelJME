// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.system.api;

import cc.squirreljme.runtime.cldc.system.SystemFunction;
import cc.squirreljme.runtime.cldc.system.type.CharacterArray;

/**
 * Interface for {@link SystemFunction#MEMORY_MAP_CHARACTER_ARRAY}.
 *
 * @since 2018/03/14
 */
public interface MemoryMapCharacterArrayCall
	extends Call
{
	/**
	 * Memory maps the given memory address.
	 *
	 * @param __a The address to start mapping from.
	 * @param __l The number of elements in the array.
	 * @return The memory mapped array.
	 * @since 2018/03/14
	 */
	public abstract CharacterArray memoryMapCharacterArray(long __a, int __l);
}

