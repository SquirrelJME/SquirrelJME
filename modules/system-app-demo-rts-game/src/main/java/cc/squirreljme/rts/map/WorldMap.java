// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.rts.map;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;

/**
 * This contains an entire world map of chunks and any units within.
 *
 * @since 2026/06/10
 */
public class WorldMap
	implements Runnable
{
	/** Chunks within the map. */
	protected final Chunks chunks;
	
	/**
	 * Initializes the world map.
	 *
	 * @param __chunks The chunk data which makes up the map.
	 * @throws NullPointerException On null arguments.
	 * @since 2026/06/11
	 */
	public WorldMap(Chunks __chunks)
		throws NullPointerException
	{
		if (__chunks == null)
			throw new NullPointerException("NARG");
		
		// Setup self reference for binding the final map
		Reference<WorldMap> ref = new WeakReference<>(this);
		
		// Bind everything now!
		this.chunks = __chunks.bind(ref);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2026/06/11
	 */
	@Override
	public void run()
	{
	}
}
