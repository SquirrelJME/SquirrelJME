// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.summercoat.ld.mem;

/**
 * This represents the starting region of memory.
 *
 * @since 2019/04/21
 */
public interface Memory
{
	/** Maximum 32-bit memory. */
	long MAX_32BIT =
		0xFFFF_FFFFL;
	
	/** Maximum 64-bit memory. */
	long MAX_64BIT =
		0xFFFF_FFFF__FFFF_FFFFL;
	
	/**
	 * The starting region of this memory.
	 *
	 * @return The starting region of this memory.
	 * @since 2019/04/21
	 */
	long memRegionOffset();
	
	/**
	 * The length of this memory region.
	 *
	 * @return The memory region length.
	 * @since 2019/04/21
	 */
	long memRegionSize();
}

