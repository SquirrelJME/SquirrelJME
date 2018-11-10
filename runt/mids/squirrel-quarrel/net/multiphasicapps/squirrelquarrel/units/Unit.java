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

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * This represents and holds information for a single unit in the game.
 *
 * @since 2017/02/14
 */
public final class Unit
{
	/** The ID of this unit. */
	protected final int id;
	
	/** The current unit type. */
	private volatile UnitType _type;
	
	/** Was this unit deleted? */
	private volatile boolean _deleted;
	
	/** Hitpoints of the current unit. */
	private volatile int _hp;
	
	/** Unit shields. */
	private volatile int _shields;
	
	/** Reference to this unit via pointer. */
	private volatile Reference<UnitReference> _ref;
	
	/** Actual center position. */
	volatile int _cx, _cy;
	
	/** Position of the unit in the map. */
	volatile int _x1, _y1, _x2, _y2;
	
	/**
	 * Initializes the unit.
	 *
	 * @param __id The ID of the unit.
	 * @since 2018/03/19
	 */
	public Unit(int __id)
	{
		this.id = __id;
	}
	
	/**
	 * Returns the center X position.
	 *
	 * @return The center X position.
	 * @since 2017/02/17
	 */
	public int centerX()
	{
		return this._cx;
	}
	
	/**
	 * Returns the center Y position.
	 *
	 * @return The center Y position.
	 * @since 2017/02/17
	 */
	public int centerY()
	{
		return this._cy;
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
		
		if (!(__o instanceof Unit))
			return false;
		
		return this.id == ((Unit)__o).id;
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
	 * Returns the ID of this unit.
	 *
	 * @return The unit ID.
	 * @since 2018/03/19
	 */
	public final int id()
	{
		return this.id;
	}
	
	/**
	 * This morphs the current unit so that it is of the specified unit type.
	 * Certain aspects are transferred and translation whiles others may be
	 * reset.
	 *
	 * @param __t The unit type to morph to.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/15
	 */
	public void morph(UnitType __t)
		throws NullPointerException
	{
		// Check
		if (__t == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
		/*
		// {@squirreljme.error BE0a Cannot morph a linked unit.}
		if (this._islinked)
			throw new IllegalStateException("BE0a");
		
		// If the type remains the same, do nothing
		UnitType oldtype = this._type;
		if (oldtype == __t)
			return;
		
		// Get info, needed for some details
		UnitInfo oldinfo = this._info,
			newinfo = __t.info();
		
		// Set new type
		this._type = __t;
		this._info = newinfo;
		
		// No previous type, set details
		if (oldtype == null)
		{
			this._hp = newinfo.hp;
			this._shields = newinfo.shields;
		}
		
		// Average the stats so that the new health is a precentage of the
		// old health
		else
		{
			if (true)
				throw new todo.TODO();
			
			// Recenter
			__move(this._cx, this._cy);
		}
		*/
	}
	
	/**
	 * Returns a reference to this unit.
	 *
	 * @return The reference to this unit.
	 * @since 2018/03/19
	 */
	public final UnitReference reference()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Runs the unit logic.
	 *
	 * @param __framenum The frame number.
	 * @return {@code true} if the unit was deleted.
	 * @since 2017/02/14
	 */
	boolean run(int __framenum)
	{
		// If the unit was deleted, do nothing
		if (this._deleted)
			return true;
		
		throw new todo.TODO();
		/*
		// Do not think for units which are not linked, but do not delete them
		if (!this._islinked)
			return false;
		
		// Not deleted
		return false;*/
	}
	
	/**
	 * Returns the unit type.
	 *
	 * @return The unit type.
	 * @since 2017/02/17
	 */
	public UnitType type()
	{
		return this._type;
	}
}

