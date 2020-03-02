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

/**
 * This represents a single tile in the world.
 *
 * @since 2018/03/18
 */
public final class Tile
{
	/** The shift for pixels. */
	public static final int PIXEL_SHIFT =
		5;
	
	/** The size of tiles in pixels. */
	public static final int PIXEL_SIZE =
		32;
	
	/** The mas for pixels. */
	public static final int PIXEL_MASK =
		31;
}

