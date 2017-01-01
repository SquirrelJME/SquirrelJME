// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirrelscavenger.game.block;

/**
 * This represents the type of block that something is.
 *
 * @since 2016/10/09
 */
public enum BlockType
{
	/** Air. */
	AIR,
	
	/** Dirt. */
	DIRT,
	
	/** End. */
	;
	
	/**
	 * Can the player spawn in this block?
	 *
	 * @return {@code true} if the player can spawn in this block.
	 * @since 2016/10/09
	 */
	public final boolean canPlayerSpawnInBlock()
	{
		return this == AIR;
	}
	
	/**
	 * Can the player spawn on top of this block?
	 *
	 * @return {@code true} if the player can spawn on top of this block.
	 * @since 2016/10/09
	 */
	public final boolean canPlayerSpawnOnTop()
	{
		return this == DIRT;
	}
}

