// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.bin;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * This hold the back reference to the {@link LinkerState} class.
 *
 * @since 2017/06/18
 */
abstract class __SubState__
{
	/** The reference to the owning linker state. */
	protected final Reference<LinkerState> linkerstate;
	
	/**
	 * Initializes the base sub-state with the back reference.
	 *
	 * @param __r The reference to the linker state.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/06/18
	 */
	__SubState__(Reference<LinkerState> __r)
		throws NullPointerException
	{
		// Check
		if (__r == null)
			throw new NullPointerException("NARG");
		
		this.linkerstate = __r;
	}
}

