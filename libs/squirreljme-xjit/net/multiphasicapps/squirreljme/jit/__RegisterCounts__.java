// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit;

/**
 * This keeps track of how many times a register has been requested for
 * allocation. This is used as a heuristic to choose the least active
 * registers when choosing registers for allocation.
 *
 * @since 2017/04/16
 */
class __RegisterCounts__
{
	/** The code handler for the method. */
	final __Code__ _code;
	
	/**
	 * Initializes the register counter.
	 *
	 * @param __c The owning code parser.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/04/16
	 */
	__RegisterCounts__(__Code__ __c)
		throws NullPointerException
	{
		// Check
		if (__c == null)
			throw new NullPointerException("NARG");
		
		// Set
		this._code = __c;
	}
}

