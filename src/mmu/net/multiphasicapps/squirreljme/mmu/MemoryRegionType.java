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
 * Note that on most systems {@link #CODE} and {@link #DATA} will return the
 * same region, while on others they may be completely different. Code in the
 * kernel, garbage collector, and otherwise should not depend on the
 * assumption that the CPU is capable of executing code from the {@link #DATA}
 * region.
 *
 * @since 2016/06/09
 */
public enum MemoryRegionType
{
	/** Executable code. */
	CODE,
	
	/** Memory (for reading and writing). */
	DATA,
	
	/** End. */
	;
}

