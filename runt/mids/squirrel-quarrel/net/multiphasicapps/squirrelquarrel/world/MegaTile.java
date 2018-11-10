// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirrelquarrel.world;

import java.io.DataInputStream;
import java.io.InputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * This represents a single mega tile which represents a small region in the
 * level.
 *
 * @since 2017/02/09
 */
public class MegaTile
{
	/** The number of frames which must pass before fog of war is cycled. */
	private static final int _FOG_OF_WAR_CYCLE_MASK =
		63;
	
	/** The number of tiles per mega tile. */
	public static final int TILES_PER_MEGA_TILE =
		8;
	
	/** The number of tiles in mega tiles. */
	public static final int TILES_IN_MEGA_TILE =
		64;
	
	/** The size of megatiles in pixels. */
	public static final int PIXEL_SIZE =
		256;
	
	/** Megatile X position. */
	protected final int megax;
	
	/** Megatile Y position. */
	protected final int megay;
	
	/** Terrain information. */
	private final byte[] _terrain =
		new byte[TILES_IN_MEGA_TILE];
	
	/**
	 * This contains the first cycle for the fog of war. The fog of war acts
	 * in two cycles, so that previously revealed areas go away and new ones
	 * are set. This means that the fog of war does not need to be cleared
	 * every frame by units, only set. The fog is in two cycles because on a
	 * new cycle this array is cleared, and it would be possible for a unit to
	 * be blind while its logic is handled. Also fog of war updates do not need
	 * to be that instant.
	 */
	private final byte[] _fogcyclea =
		new byte[TILES_IN_MEGA_TILE];
	
	/** Fog of war second cycle. */
	private final byte[] _fogcycleb =
		new byte[TILES_IN_MEGA_TILE];
	
	/**
	 * Initializes a basic megatile.
	 *
	 * @param __x Megatile X position.
	 * @param __y Megatile Y position.
	 * @since 2017/02/10
	 */
	public MegaTile(int __mx, int __my)
	{
		// Set
		this.megax = __mx;
		this.megay = __my;
		
		// Initialize it with some pattern
		byte[] terrain = this._terrain;
		for (int y = 0; y < TILES_PER_MEGA_TILE; y++)
			for (int x = 0; x < TILES_PER_MEGA_TILE; x++)
				terrain[(y * TILES_PER_MEGA_TILE) + x] =
					(byte)(((x + y) / 2) & 1);
	}
	
	/**
	 * Gets the terrain for the given sub-tile.
	 *
	 * @param __x The tile X position.
	 * @param __y The tile Y position.
	 * @return The terrain type for the given tile.
	 * @throws IndexOutOfBoundsException If the position is not in the megatile
	 * bounds.
	 * @since 2017/02/11
	 */
	public TerrainType subTileTerrain(int __x, int __y)
		throws IndexOutOfBoundsException
	{
		// {@squirreljme.error BE0i Cannot get terrain because the tile is
		// out of range.}
		if (__x < 0 || __y < 0 || __x >= TILES_PER_MEGA_TILE ||
			__y >= TILES_PER_MEGA_TILE)
			throw new IndexOutOfBoundsException("BE0i");
		
		// Depends
		return TerrainType.of(
			this._terrain[(__y * TILES_PER_MEGA_TILE) + __x]);
	}
	
	/**
	 * Return the megatile X position.
	 *
	 * @return The X position of the megatile.
	 * @since 2017/02/17
	 */
	public int x()
	{
		return this.megax;
	}
	
	/**
	 * Return the megatile Y position.
	 *
	 * @return The Y position of the megatile.
	 * @since 2017/02/17
	 */
	public int y()
	{
		return this.megay;
	}
	
	/**
	 * This cycles the fog of war.
	 *
	 * @since 2017/02/14
	 */
	void __cycleFog()
	{
		// Transfer all fog state from the first cycle to the second
		byte[] fogcyclea = this._fogcyclea;
		byte[] fogcycleb = this._fogcyclea;
		for (int i = 0, n = TILES_IN_MEGA_TILE; i < n; i++)
		{
			// Set the second cycle to the first
			fogcycleb[i] = fogcyclea[i];
			
			// Clear the first
			fogcyclea[i] = 0;
		}
	}
	
	/**
	 * Runs the megatile logic.
	 *
	 * @param __frame The current frame.
	 * @since 2017/02/14
	 */
	public void run(int __frame)
	{
		// Cycle the fog of war?
		if ((__frame & _FOG_OF_WAR_CYCLE_MASK) == 0)
			__cycleFog();
	}
}

