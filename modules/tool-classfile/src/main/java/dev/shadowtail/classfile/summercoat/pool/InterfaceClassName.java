// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.classfile.summercoat.pool;

import net.multiphasicapps.classfile.ClassName;

/**
 * This represents a reference to an interface class.
 *
 * @since 2020/11/24
 */
public final class InterfaceClassName
{
	/** The name of the interface to reference. */
	public final ClassName name;
	
	/**
	 * Initializes the interface class name.
	 * 
	 * @param __name The name of the interface.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/11/24
	 */
	public InterfaceClassName(ClassName __name)
		throws NullPointerException
	{
		if (__name == null)
			throw new NullPointerException("NARG");
		
		this.name = __name;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/11/24
	 */
	@Override
	public boolean equals(Object __o)
	{
		if (this == __o)
			return true;
		
		if (!(__o instanceof InterfaceClassName))
			return false;
		
		return this.name.equals(((InterfaceClassName)__o).name);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/11/24
	 */
	@Override
	public int hashCode()
	{
		return this.name.hashCode();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/11/24
	 */
	@Override
	public String toString()
	{
		return "Interface " + this.name;
	}
}
