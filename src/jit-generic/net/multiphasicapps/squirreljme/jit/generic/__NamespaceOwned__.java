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
 * This is extended by anything which is owned by a namespace.
 *
 * @since 2016/08/12
 */
abstract class __NamespaceOwned__
{
	/** The owning namespace. */
	protected final GenericNamespaceWriter owner;
	
	/**
	 * Initializes the base class.
	 *
	 * @param __nsw The owning namespace.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/12
	 */
	__NamespaceOwned__(GenericNamespaceWriter __nsw)
		throws NullPointerException
	{
		// Check
		if (__nsw == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.owner = __nsw;
	}
}

