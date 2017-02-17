// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirrelquarrel;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * This represents and holds information for a single unit in the game.
 *
 * @since 2017/02/14
 */
public class Unit
{
	/** The game which owns this unit. */
	protected final Game game;
	
	/** The megatiles that this unit is in. */
	final List<MegaTile> _linked =
		new ArrayList<>();
	
	/** A pointer to this unit. */
	private volatile Reference<Unit.Pointer> _pointer;
	
	/** Was this unit deleted? */
	private volatile boolean _deleted;
	
	/**
	 * Initializes the unit.
	 *
	 * @param __g The owning game.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/14
	 */
	Unit(Game __g)
		throws NullPointerException
	{
		// Check
		if (__g == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.game = __g;
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
		
		throw new Error("TODO");
	}
	
	/**
	 * This returns a pointer to this unit which is used to refer to other
	 * units.
	 *
	 * @return A pointer to this unit.
	 * @throws UnitDeletedException If this unit has been deleted.
	 * @since 2017/02/14
	 */
	public Unit.Pointer pointer()
		throws UnitDeletedException
	{
		// {@squirreljme.error BE09 Cannot get the pointer of a deleted unit.}
		if (this._deleted)
			throw new UnitDeletedException("BE09");
		
		Reference<Unit.Pointer> ref = this._pointer;
		Unit.Pointer rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
			this._pointer = new WeakReference<>(rv = new Unit.Pointer(this));
		
		return rv;
	}
	
	/**
	 * Runs the unit logic.
	 *
	 * @param __framenum The frame number.
	 * @return {@code true} if the unit was deleted.
	 * @since 2017/02/14
	 */
	boolean __run(int __framenum)
	{
		// If the unit was deleted, do nothing
		if (this._deleted)
			return true;
		
		// Not deleted
		return false;
	}
	
	/**
	 * This is a pointer which points to a unit, it is used to allow units to
	 * refer to other units and having it where they can be referenced without
	 * needing to handle unit removal themselves. Since units would be garbage
	 * collected, it would be unspecified using references when units actually
	 * go away.
	 *
	 * @since 2017/02/14
	 */
	public static class Pointer
	{
		/** The unit hash code, needed because pointers can be hashmaps. */
		protected final int hash;
		
		/** The unit this points to. */
		private volatile Unit _unit;
		
		/**
		 * Initializes the unit pointer.
		 *
		 * @param __u The unit to point to.
		 * @throws NullPointerException On null arguments.
		 * @since 2017/02/14
		 */
		private Pointer(Unit __u)
			throws NullPointerException
		{
			// Check
			if (__u == null)
				throw new NullPointerException("NARG");
			
			// The unit it points to
			this._unit = __u;
			hash = __u.hashCode();
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2017/02/16
		 */
		@Override
		public boolean equals(Object __o)
		{
			if (!(__o instanceof Pointer))
				return false;
			
			Pointer o = (Pointer)__o;
			Unit a = this._unit, b = o._unit;
			
			// Null pointers only compare to self
			if (a == null || b == null)
				return (this == __o);
			
			// Otherwise must be the same unit
			return a == b;
		}
		
		/**
		 * Returns the unit that this pointer points to.
		 *
		 * @return The unit this points to.
		 * @throws UnitDeletedException If the unit was deleted.
		 * @since 2017/02/14
		 */
		public Unit get()
			throws UnitDeletedException
		{
			// {@squirreljme.error BE08 The unit has been deleted.}
			Unit rv = this._unit;
			if (rv == null)
				throw new UnitDeletedException("BE08");
			
			return rv;
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2017/02/16
		 */
		@Override
		public int hashCode()
		{
			return this.hash;
		}
	}
}

