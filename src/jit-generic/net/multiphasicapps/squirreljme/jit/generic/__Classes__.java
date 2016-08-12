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

import java.util.HashMap;
import java.util.Map;
import net.multiphasicapps.squirreljme.java.symbols.ClassNameSymbol;

/**
 * This represents multiple classes within an output namespace.
 *
 * @since 2016/08/09
 */
class __Classes__
	extends __NamespaceOwned__
{
	/** The mapping of classes. */
	protected final Map<ClassNameSymbol, __Class__> classes =
		new HashMap<>();
	
	/**
	 * Initializes the class table.
	 *
	 * @param __nsw The owning namespace.
	 * @since 2016/07/12
	 */
	__Classes__(GenericNamespaceWriter __nsw)
	{
		super(__nsw);
	}
	
	/**
	 * Creates a new class in the output namespace.
	 *
	 * @param __cn The name of the class.
	 * @return The newly created class.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/08/09
	 */
	__Class__ __newClass(ClassNameSymbol __cn)
		throws NullPointerException
	{
		// Check
		if (__cn == null)
			throw new NullPointerException("NARG");
		
		// Create class
		__Class__ rv = new __Class__(__cn);
		
		// Add class, replace existing
		this.classes.put(__cn, rv);
		
		// Return it
		return rv;
	}
}

