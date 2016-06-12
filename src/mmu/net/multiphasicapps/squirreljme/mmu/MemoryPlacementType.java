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
 * This is the type of placement that should be performed for a new allocation.
 *
 * @since 2016/06/12
 */
public enum MemoryPlacementType
{
	/** Allocate in the lowest possible memory address. */
	LOWEST,
	
	/** Allocate in the highest possible memory address. */
	HIGHEST,
	
	/** Unspecified memory placement, depends on the allocator. */
	UNSPECIFIED,
	
	/** End. */
	;
}

