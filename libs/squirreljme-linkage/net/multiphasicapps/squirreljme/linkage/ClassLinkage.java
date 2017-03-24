// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.linkage;

import net.multiphasicapps.squirreljme.java.symbols.ClassNameSymbol;

/**
 * This represents a link to a class.
 *
 * @since 2017/03/24
 */
public final class ClassLinkage
	implements Linkage
{
	/** The name of the class to link to. */
	protected final ClassNameSymbol name;
	
	/**
	 * Initializes the class linkage.
	 *
	 * @param __n The name of the class to link to.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/24
	 */
	public ClassLinkage(ClassNameSymbol __n)
		throws NullPointerException
	{
		// Check
		if (__n == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.name = __n;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/03/24
	 */
	@Override
	public boolean equals(Object __o)
	{
		// Check
		if (!(__o instanceof ClassLinkage))
			return false;
		
		return this.name.equals(((ClassLinkage)__o).name);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/03/24
	 */
	@Override
	public int hashCode()
	{
		return this.name.hashCode();
	}
	
	/**
	 * Returns the name of the linked class.
	 *
	 * @return The linked class name.
	 * @since 2017/03/24
	 */
	public ClassNameSymbol name()
	{
		return this.name;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/03/24
	 */
	@Override
	public String toString()
	{
		return this.name.toString();
	}
}

