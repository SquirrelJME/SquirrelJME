// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.lis.realm;

/**
 * Memory caching for realm related structures.
 *
 * @since 2025/12/21
 */
public final class MemoryCache
{
	/** {@link AreaTile} queue. */
	private final __Queue__<AreaTile> _areaTiles =
		new __Queue__<>();
	
	/**
	 * Returns a blank area tile.
	 *
	 * @return A blank area tile.
	 * @since 2025/12/21
	 */
	public AreaTile areaTile()
	{
		__Queue__<AreaTile> queue = this._areaTiles;
		
		// Pull from the queue
		AreaTile rv = queue.__next();
		if (rv != null)
			return rv;
		
		// Queue is blank, so make a new one
		return new AreaTile(queue._self);
	}
}
