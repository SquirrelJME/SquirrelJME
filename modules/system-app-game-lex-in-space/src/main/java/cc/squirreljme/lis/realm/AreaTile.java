// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.lis.realm;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.lang.ref.Reference;
import java.util.Arrays;

/**
 * Represents a fixed region within a realm, this contains data and state
 * for multiple tiles.
 *
 * @since 2025/12/21
 */
public final class AreaTile
	implements MemoryObject<AreaTile>
{
	/** Tile data. */
	final int[] _data;
	
	/** The memory caching queue which owns this. */
	private final Reference<__Queue__<AreaTile>> _owner;
	
	/**
	 * Area tile constructor.
	 *
	 * @since 2025/12/21
	 */
	AreaTile(Reference<__Queue__<AreaTile>> __mc)
		throws NullPointerException
	{
		if (__mc == null || __mc.get() == null)
			throw new NullPointerException("NARG");
		
		this._owner = __mc;
		this._data = new int[Dim.AREA_BLOCK_SPAN];
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/21
	 */
	@Override
	public void close()
	{
		throw Debugging.todo();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2025/12/21
	 */
	@Override
	public void reset()
	{
		// Everything is just wiped to zero here
		Arrays.fill(this._data, 0);
	}
}
