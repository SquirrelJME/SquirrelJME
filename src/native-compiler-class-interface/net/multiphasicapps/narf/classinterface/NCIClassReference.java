// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.narf.classinterface;

import net.multiphasicapps.descriptors.ClassNameSymbol;

/**
 * This refers to another class.
 *
 * @since 2016/04/24
 */
public final class NCIClassReference
	implements NCIPoolEntry
{
	/** The class this refers to. */
	protected final ClassNameSymbol classname;
	
	/**
	 * Initializes the class reference.
	 *
	 * @param __cn The class to reference.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/04/24
	 */
	public NCIClassReference(ClassNameSymbol __cn)
		throws NullPointerException
	{
		// Check
		if (__cn == null)
			throw new NullPointerException("NARG");
		
		// Set
		classname = __cn;
	}
	
	/**
	 * Returns the class name that this references.
	 *
	 * @return The class to reference.
	 * @since 2016/04/24
	 */
	public ClassNameSymbol get()
	{
		return classname;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/24
	 */
	@Override
	public NCIPoolTag tag()
	{
		return NCIPoolTag.CLASS;
	}
}

