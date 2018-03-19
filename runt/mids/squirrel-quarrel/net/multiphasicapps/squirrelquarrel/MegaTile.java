// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirrelquarrel;

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
@Deprecated
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
		TILES_PER_MEGA_TILE * TILES_PER_MEGA_TILE;
	
	/** The shift for pixels. */
	public static final int TILE_PIXEL_SHIFT =
		5;
	
	/** The size of tiles in pixels. */
	public static final int TILE_PIXEL_SIZE =
		(1 << TILE_PIXEL_SHIFT);
	
	/** The mas for pixels. */
	public static final int TILE_PIXEL_MASK =
		TILE_PIXEL_SIZE - 1;
	
	/** The size of megatiles in pixels. */
	public static final int MEGA_TILE_PIXEL_SIZE =
		TILE_PIXEL_SIZE * TILES_PER_MEGA_TILE;
	
	/** The owning level */
	protected final Level level;
	
	/** Megatile X position. */
	protected final int megax;
	
	/** Megatile Y position. */
	protected final int megay;
	
	/** The units which are linked into this megatile. */
	final Set<Unit> _units =
		new LinkedHashSet<>();
	
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
	 * @param __l The owning level.
	 * @param __x Megatile X position.
	 * @param __y Megatile Y position.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/10
	 */
	public MegaTile(Level __l, int __mx, int __my)
		throws NullPointerException
	{
		// Check
		if (__l == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.level = __l;
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
	 * Loads units to be drawn into the specified collection.
	 *
	 * @param __d The target collection.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/17
	 */
	public void loadLinkedUnits(Collection<Unit.Pointer> __d)
		throws NullPointerException
	{
		// Check
		if (__d == null)
			throw new NullPointerException("NARG");
		
		// Could fail
		try
		{
			for (Unit u : this._units)
				try
				{
					__d.add(u.pointer());
				}
				catch (UnitDeletedException e)
				{
					// Ignore
				}
		}
		
		// Links were modified, stop reading
		catch (ConcurrentModificationException e)
		{
		}
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
		// {@squirreljme.error BE04 Cannot get terrain because the tile is
		// out of range.}
		if (__x < 0 || __y < 0 || __x >= TILES_PER_MEGA_TILE ||
			__y >= TILES_PER_MEGA_TILE)
			throw new IndexOutOfBoundsException("BE04");
		
		// Depends
		return TerrainType.of(
			this._terrain[(__y * TILES_PER_MEGA_TILE) + __x]);
	}
	
	/**
	 * This checks whether the given sub-tile is revealed by the given player.
	 *
	 * @param __p The player to check if they can see the given tile.
	 * @param __x The tile X position.
	 * @param __y The tile Y position.
	 * @return {@code true} if it is revealed.
	 * @throws IndexOutOfBoundsException If the position is not in the megatile
	 * bounds.
	 * @throws NullPointerException On null arguments
	 * @since 2017/02/13
	 */
	public boolean subTileRevealed(Player __p, int __x, int __y)
		throws IndexOutOfBoundsException, NullPointerException
	{
		// Check
		if (__p == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error BE05 Cannot get revealed state because the tile
		// is out of range.}
		if (__x < 0 || __y < 0 || __x >= TILES_PER_MEGA_TILE ||
			__y >= TILES_PER_MEGA_TILE)
			throw new IndexOutOfBoundsException("BE05");
		
		// Fog of war revealing is done in two cycles
		int vm = __p.visionMask(), dx = (__y * TILES_PER_MEGA_TILE) + __x;
		return (this._fogcyclea[dx] & vm) != 0 ||
			(this._fogcycleb[dx] & vm) != 0;
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
	void __run(int __frame)
	{
		// Cycle the fog of war?
		if ((__frame & _FOG_OF_WAR_CYCLE_MASK) == 0)
			__cycleFog();
	}
}

