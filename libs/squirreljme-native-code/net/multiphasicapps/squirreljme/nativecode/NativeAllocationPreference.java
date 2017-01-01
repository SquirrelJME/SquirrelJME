// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.nativecode;

/**
 * This selects which registers are used to be selected for allocation.
 *
 * @since 2016/09/18
 */
public enum NativeAllocationPreference
{
	/** Use any allocation scheme. */
	ANY,
	
	/** Use only saved registers. */
	ONLY_SAVED,
	
	/** Use only temporary registers. */
	ONLY_TEMPORARY,
	
	/** End. */
	;
}

