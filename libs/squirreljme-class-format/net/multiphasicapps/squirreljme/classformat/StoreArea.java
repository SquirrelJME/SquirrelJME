// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.classformat;

/**
 * This is the area in which variables are stored.
 *
 * @since 2017/03/31
 */
public enum StoreArea
{
	/** Local variables. */
	LOCAL,
	
	/** Stack veriables. */
	STACK,
	
	/** Work variables which are destroyed at the end of each instruction. */
	WORK,
	
	/** End. */
	;
}

