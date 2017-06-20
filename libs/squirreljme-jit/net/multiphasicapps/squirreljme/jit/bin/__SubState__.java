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
 * This hold the back reference to the {@link LinkerState} class. This allows
 * the linker state to be garbage collected because SquirrelJME uses a reference
 * counting garbage collector.
 *
 * @since 2017/06/18
 */
abstract class __SubState__
{
	/** The reference to the owning linker state. */
	private final Reference<LinkerState> _linkerstate;
	
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
		
		this._linkerstate = __r;
	}
	
	/**
	 * Returns the linker state and checks if it has been garbage collected, if
	 * it has then an exception is thrown which makes using objects of this
	 * class illegal.
	 *
	 * @return The linker state.
	 * @throws IllegalStateException If the linker state has been garbage
	 * collected.
	 * @since 2017/06/17
	 */
	final LinkerState __linkerState()
		throws IllegalStateException
	{
		// {@squirreljme.error JI0u The linker state has been garbage collected
		// and as such using this object is no longer valid.}
		LinkerState rv = this._linkerstate.get();
		if (rv == null)
			throw new IllegalStateException("JI0u");
		
		return rv;
	}
}

