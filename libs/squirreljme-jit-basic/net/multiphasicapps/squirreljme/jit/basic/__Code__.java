// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.basic;

/**
 * This represents a single set of byte code for a given method and such.
 *
 * @since 2016/09/14
 */
@Deprecated
class __Code__
	extends __Positioned__
{
	/** The method containing this code. */
	final __Method__ _method;
	
	/**
	 * Initializes the code information.
	 *
	 * @param __m The owning method.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/15
	 */
	__Code__(__Method__ __m)
		throws NullPointerException
	{
		// Check
		if (__m == null)
			throw new NullPointerException("NARG");
		
		// Set
		this._method = __m;
	}
}

