// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.java.ci;

import net.multiphasicapps.squirreljme.java.symbols.ClassNameSymbol;

/**
 * This refers to another class.
 *
 * @since 2016/04/24
 */
public final class CIClassReference
	implements CIPoolEntry
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
	public CIClassReference(ClassNameSymbol __cn)
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
	public CIPoolTag tag()
	{
		return CIPoolTag.CLASS;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/24
	 */
	@Override
	public String toString()
	{
		return classname.toString();
	}
}

