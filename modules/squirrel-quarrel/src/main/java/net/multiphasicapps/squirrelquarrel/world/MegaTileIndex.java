// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirrelquarrel.world;

import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * This represents an index into a megatile.
 *
 * @since 2018/03/18
 */
public final class MegaTileIndex
	implements Comparable<MegaTileIndex>
{
	/** The x coordinate. */
	public final int x;
	
	/** The y coordinate. */
	public final int y;
	
	/** The ordinal index. */
	public final int ordinal;
	
	/**
	 * Initializes the mega tile index.
	 *
	 * @param __x The x coordinate.
	 * @param __y The y coordinate.
	 * @param __i The ordinal.
	 * @since 2018/03/18
	 */
	public MegaTileIndex(int __x, int __y, int __i)
	{
		this.x = __x;
		this.y = __y;
		this.ordinal = __i;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/18
	 */
	@Override
	public final int compareTo(MegaTileIndex __o)
	{
		return this.ordinal - __o.ordinal;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/18
	 */
	@Override
	public final boolean equals(Object __o)
	{
		if (this == __o)
			return true;
		
		if (!(__o instanceof MegaTileIndex))
			return false;
		
		MegaTileIndex o = (MegaTileIndex)__o;
		return this.ordinal == o.ordinal;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/18
	 */
	@Override
	public final int hashCode()
	{
		return this.ordinal;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/18
	 */
	@Override
	public final String toString()
	{
		throw Debugging.todo();
	}
	
	/**
	 * Translates a position to the position ordinal.
	 *
	 * @param __x The X coordinate.
	 * @param __y The Y coordinate.
	 * @return The ordinal in the tile map.
	 * @since 2018/03/19
	 */
	public static final int positionToOrdinal(int __x, int __y)
	{
		throw Debugging.todo();
	}
}

