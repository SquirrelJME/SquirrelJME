// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirrelquarrel.units;

/**
 * This is a reference which points to a unit, it is used to allow units to
 * refer to other units and having it where they can be referenced without
 * needing to handle unit removal themselves. Since units would be garbage
 * collected, it would be unspecified using references when units actually
 * go away.
 *
 * @since 2018/03/19
 */
public final class UnitReference
{
	/** The unit ID. */
	protected final int id;
	
	/** The referring unit. */
	private volatile Unit _unit;
	
	/**
	 * Initializes the reference to the given unit.
	 *
	 * @param __u The unit to link to.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/19
	 */
	public UnitReference(Unit __u)
		throws NullPointerException
	{
		if (__u == null)
			throw new NullPointerException("NARG");
		
		this.id = __u.id();
		this._unit = __u;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/19
	 */
	@Override
	public final boolean equals(Object __o)
	{
		if (this == __o)
			return true;
		
		if (!(__o instanceof UnitReference))
			return false;
		
		return this.id == ((UnitReference)__o).id;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/19
	 */
	@Override
	public final int hashCode()
	{
		return this.id;
	}
	
	/**
	 * Returns the unit this is a reference to or {@code null} if it has been
	 * deleted/killed.
	 *
	 * @return The unit this points to or {@code null} if it has been
	 * deleted/killed.
	 * @since 2018/03/19
	 */
	public final Unit get()
	{
		throw new todo.TODO();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/19
	 */
	@Override
	public final String toString()
	{
		throw new todo.TODO();
	}
}

