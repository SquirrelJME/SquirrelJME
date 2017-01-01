// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirrelscavenger.game.chunk;

/**
 * This is used to generate information about the details of the biome to
 * generate for.
 *
 * @since 2016/10/09
 */
public class BiomeGenerator
{
	/** The seed used for the generator. */
	protected final long seed;
	
	/**
	 * Initializes the biome generator.
	 *
	 * @param __seed The seed to use.
	 * @since 2016/10/09
	 */
	public BiomeGenerator(long __seed)
	{
		// Set
		this.seed = __seed;
	}
}

