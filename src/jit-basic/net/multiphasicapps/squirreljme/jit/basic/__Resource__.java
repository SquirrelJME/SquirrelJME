// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.basic;

/**
 * This represents a resource that was written.
 *
 * @since 2016/09/11
 */
@Deprecated
class __Resource__
	extends __Positioned__
{
	/** The name of the resource. */
	protected final BasicConstantEntry<String> name;
	
	/**
	 * Initializes the resource information holder.
	 *
	 * @param __name The name of the resource.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/09/11
	 */
	__Resource__(BasicConstantEntry<String> __name)
		throws NullPointerException
	{
		// Check
		if (__name == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.name = __name;
	}
}

