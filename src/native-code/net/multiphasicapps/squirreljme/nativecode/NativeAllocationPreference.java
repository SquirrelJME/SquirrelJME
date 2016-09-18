// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.nativecode;

/**
 * DESCRIBE THIS.
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

