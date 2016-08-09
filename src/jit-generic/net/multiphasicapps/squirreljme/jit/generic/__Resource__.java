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
 * This represents a single resource in the namespace.
 *
 * @since 2016/08/09
 */
class __Resource__
{
	/** The name of the resource. */
	protected final String name;
	
	/**
	 * Initializes the resource.
	 *
	 * @param __n The name of the resource.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/09
	 */
	__Resource__(String __n)
		throws NullPointerException
	{
		// Check
		if (__n == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.name = __n;
	}
	
	/**
	 * Returns the name of this resource.
	 *
	 * @return The resource name.
	 * @since 2016/08/09
	 */
	String __name()
	{
		return this.name;
	}
}

