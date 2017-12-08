// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.runtime.cldc;

/**
 * This contains the integer based index of the available APIs.
 *
 * @since 2017/12/07
 */
public interface APIList
{
	/** The primary communication bridge. */
	public static final int COMM_BRIDGE =
		0;
	
	/** The current chore. */
	public static final int CURRENT_CHORE =
		1;
	
	/** Start of non-system use objects. */
	public static final int START_OF_NON_SYSTEM =
		3;
	
	/** Access to the clock. */
	public static final int CLOCK =
		START_OF_NON_SYSTEM + 1;
	
	/** Chores. */
	public static final int CHORES =
		START_OF_NON_SYSTEM + 2;
	
	/** The maximum number of APIs. */
	public static final int MAX_API =
		CHORES + 1;
}

