// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.summercoat;

/**
 * This represents the starting region of memory.
 *
 * @since 2019/04/21
 */
public interface Memory
{
	/**
	 * The starting region of this memory.
	 *
	 * @return The starting region of this memory.
	 * @since 2019/04/21
	 */
	int memRegionOffset();
	
	/**
	 * The length of this memory region.
	 *
	 * @return The memory region length.
	 * @since 2019/04/21
	 */
	int memRegionSize();
}

