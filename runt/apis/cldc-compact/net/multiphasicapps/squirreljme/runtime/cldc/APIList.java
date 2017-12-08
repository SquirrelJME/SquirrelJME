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
	/** Access to the clock. */
	public static final int CLOCK =
		1;
	
	/** Chores. */
	public static final int CHORES =
		2;
	
	/** The maximum number of APIs. */
	public static final int MAX_API =
		CHORES + 1;
	
	/** The start of the user-space APIs. */
	public static final int USERSPACE_START =
		MAX_API;
	
	/** The number of userspace APIs. */
	public static final int USERSPACE_COUNT =
		MAX_API - USERSPACE_START;
}

