// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.jit.bin;

import java.lang.ref.Reference;
import net.multiphasicapps.squirreljme.jit.java.ClassName;

/**
 * This represents a single class unit.
 *
 * @since 2017/06/17
 */
public final class Unit
	extends __SubState__
{
	/** The name of this class. */
	protected final ClassName classname;
	
	/** The super class of this class. */
	private volatile ClassName _superclass;
	
	/** The interfaces which this unit implements. */
	private volatile ClassName[] _interfaceclasses;
	
	/**
	 * Initializes the individual class unit.
	 *
	 * @param __ls The owning linker state.
	 * @param __n The name of the class.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/06/18
	 */
	Unit(Reference<LinkerState> __ls, ClassName __n)
		throws NullPointerException
	{
		super(__ls);
		
		// Check
		if (__n == null)
			throw new NullPointerException("NARG");
		
		this.classname = __n;
	}
	
	/**
	 * Returns the name of the class this unit is for.
	 *
	 * @return The name of this unit.
	 * @since 2017/07/08
	 */
	public final ClassName className()
	{
		return this.classname;
	}
	
	/**
	 * Sets the interfaces that this unit implements.
	 *
	 * @param __i The interfaces to implement.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/07/08
	 */
	public final void setInterfaceClasses(ClassName... __i)
		throws NullPointerException
	{
		// Check
		if (__i == null)
			throw new NullPointerException("NARG");
		
		// Never allow nulls
		__i = __i.clone();
		for (ClassName n : __i)
			if (n == null)
				throw new NullPointerException("NARG");
		
		this._interfaceclasses = __i;
	}
	
	/**
	 * Sets the name of the super class of this unit.
	 *
	 * @param __n The name of the super class.
	 * @since 2017/07/08
	 */
	public final void setSuperClass(ClassName __n)
	{
		this._superclass = __n;
	}
}

