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
 * This is the result of a comparison between two pointer values.
 *
 * @since 2016/06/09
 */
public enum PointerComparison
{
	/** The two pointers cannot be compared. */
	NOT_COMPARABLE,
	
	/** The pointer pertains to the same address. */
	EQUAL,
	
	/** The first pointer is an address lower in memory. */
	LESS_THAN,
	
	/** The first pointer is an address higher in memory. */
	GREATER_THAN,
	
	/** End. */
	;
}

