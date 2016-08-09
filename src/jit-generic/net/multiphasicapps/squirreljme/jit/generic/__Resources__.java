// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.generic;

/**
 * This represents multiple resources in a namespace.
 *
 * @since 2016/08/09
 */
class __Resources__
{
	/**
	 * Creates a new resource.
	 *
	 * @param __n The name of the resource.
	 * @return The newly created resource.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/09
	 */
	__Resource__ __newResource(String __n)
		throws NullPointerException
	{
		// Check
		if (__n == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
}

