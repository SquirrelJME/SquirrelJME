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
	/** The linker for units. */
	protected final UnitLinker linker;
	
	/** The current unit type. */
	private volatile UnitType _type;
	
	/** Was this unit deleted? */
	private volatile boolean _deleted;
	
	/** Hitpoints of the current unit. */
	private volatile int _hp;
	
	/** Unit shields. */
	private volatile int _shields;
	
	/** Is this unit linked into the megatile map? */
	private volatile boolean _islinked;
	
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
	 * @param __ln The linker for units.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/19
	 */
	public Unit(int __id, UnitLinker __ln)
		throws NullPointerException
	{
		if (__ln == null)
			throw new NullPointerException("NARG");
		
		this.id = __id;
		this.linker = __ln;
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
		
		// Do not think for units which are not linked, but do not delete them
		if (!this._islinked)
			return false;
		
		// Not deleted
		return false;
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
	
	
	
	
	/** The megatiles that this unit is in. */
	final List<MegaTile> _linked =
		new ArrayList<>();
	
	/** The unit information. */
	private volatile UnitInfo _info;
	
	/** A pointer to this unit. */
	private volatile Reference<Unit.Pointer> _pointer;
	
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
		
		// {@squirreljme.error BE08 Cannot morph a linked unit.}
		if (this._islinked)
			throw new IllegalStateException("BE08");
		
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
	 * Links or unlinks the unit into the mega tile unit list.
	 *
	 * @param __link If {@code true} then the unit becomes linked, otherwise
	 * it is unlinked.
	 * @since 2017/02/16
	 */
	void __link(boolean __link)
	{
		// Do nothing if the link state is the same
		if (this._islinked == __link)
			return;
		
		// Linking in?
		List<MegaTile> linked = this._linked;
		if (__link)
		{
			// Determine start and ending megatiles where this unit is located
			int sx = this._x1 / MegaTile.MEGA_TILE_PIXEL_SIZE,
				sy = this._y1 / MegaTile.MEGA_TILE_PIXEL_SIZE,
				ex = this._x2 / MegaTile.MEGA_TILE_PIXEL_SIZE,
				ey = this._y2 / MegaTile.MEGA_TILE_PIXEL_SIZE;
			
			// Get map size
			Level level = this.game.level();
			int mtw = level.megaTileWidth(),
				mth = level.megaTileHeight();
			
			// Force bounds within the map
			if (sx < 0)
				sx = 0;
			if (sy < 0)
				sy = 0;
			if (ex >= mtw)
				ex = mtw - 1;
			if (ey >= mth)
				ey = mth - 1;
			
			// Add this unit to the megatile chain
			for (; sy <= ey; sy++)
				for (int x = sx; x <= ex; x++)
				{
					MegaTile mt = level.megaTile(x, sy);
					
					// Link both sides
					linked.add(mt);
					mt._units.add(this);
				}
		}
		
		// Linking out
		else
		{
			// Remove this unit from the tile links
			for (int i = 0, n = linked.size(); i < n; i++)
				linked.get(i)._units.remove(this);
			
			// Remove all links
			linked.clear();
		}
		
		// Set new link state
		this._islinked = __link;
	}
	
	/**
	 * Moves the unit to the specified coordinates.
	 *
	 * @param __x The target X coordinate.
	 * @param __y The target Y coordinate.
	 * @throws IllegalStateException If the unit is linked.
	 * @since 2017/02/17
	 */
	void __move(int __x, int __y)
		throws IllegalStateException
	{
		// {@squirreljme.error BE0a Cannot move a linked unit.}
		if (this._islinked)
			throw new IllegalStateException("BE0a");
		
		// {@squirreljme.error BE0b Cannot move a unit of an unknown type.}
		UnitInfo info = this._info;
		if (info == null)
			throw new IllegalStateException("BE0b");
			
		// Center unit size
		Dimension d = info.pixeldimension;
		int mw = d.width / 2,
			mh = d.height / 2;
		
		// Set coordinates
		this._cx = __x;
		this._cy = __y;
		this._x1 = __x - mw;
		this._y1 = __y - mh;
		this._x2 = __x + mw;
		this._y2 = __y + mh;
	}
}

