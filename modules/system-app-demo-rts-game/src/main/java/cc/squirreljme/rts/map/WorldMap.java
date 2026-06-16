// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.rts.map;

import cc.squirreljme.rts.player.Executor;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Arrays;

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
	
	/** Executors within the map, controlling each player. */
	private final Executor[] _executors;
	
	/**
	 * Initializes the world map.
	 *
	 * @param __chunks The chunk data which makes up the map.
	 * @param __executors Executors within the game.
	 * @throws IllegalArgumentException If an executor has been duplicated.
	 * @throws NullPointerException On null arguments.
	 * @since 2026/06/11
	 */
	public WorldMap(Chunks __chunks, Executor... __executors)
		throws IllegalArgumentException, NullPointerException
	{
		if (__chunks == null)
			throw new NullPointerException("NARG");
		
		// Setup self reference for binding the final map
		Reference<WorldMap> ref = new WeakReference<>(this);
		
		// Set up executors, which control each player initially
		// Note that anything after the max is just ignored
		Executor[] executors = new Executor[Executor.MAX_EXECUTORS];
		this._executors = executors;
		if (__executors != null)
			for (int n = Math.min(Executor.MAX_EXECUTORS,
				__executors.length), i = 0; i < n; i++)
			{
				// Ignore blank slots
				Executor executor = __executors[i];
				if (executor == null)
					continue;
				
				// There cannot be a duplicate executor
				if (Arrays.binarySearch(executors, executor) >= 0)
					throw new IllegalArgumentException("DUPE");
				
				// Set!
				executors[i] = executor;
			}
		
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
