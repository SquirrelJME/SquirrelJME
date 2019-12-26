// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.squirrelquarrel;

/**
 * The represents the color of a player.
 *
 * The colors here are from Electronic Art's Accessibility account which
 * specify the set of colors which are best used for all kinds of color
 * blind users to they can differentiate between all the colors.
 *
 * https://twitter.com/ea_accessible/status/968592657848193024
 * https://twitter.com/ea_accessible/status/968595073184092160
 *
 * @since 2019/12/25
 */
public enum PlayerIndex
{
	/** Red. */
	RED("Red", 0xDE4949),
	
	/** Purple. */
	PURPLE("Purple", 0x533364),
	
	/** Green. */
	GREEN("Green", 0x30B06E),
	
	/** Blue. */
	BLUE("Blue", 0x33521E1),
	
	/** Yellow. */
	YELLOW("Yellow", 0xFFB937),
	
	/** Gray. */
	GRAY("Gray", 0x5A7D8B),
	
	/** End. */
	;
	
	/** The maximum number of players that can play at once. */
	public static final int MAX_PLAYERS =
		4;
	
	/** The number of player colors. */
	public static final int NUM_COLORS =
		6;
	
	/** Creeps that just annoy you. */
	public static final PlayerIndex CREEPS =
		PlayerIndex.YELLOW;
	
	/** The neutral player. */
	public static final PlayerIndex NEUTRAL =
		PlayerIndex.GRAY;
	
	/** The ARGB color code. */
	protected final int argb;
	
	/** The RGB color code. */
	protected final int rgb;
	
	/** The color name. */
	protected final String name;
	
	/**
	 * Initializes the color code.
	 *
	 * @param __name The name of the color.
	 * @param __rgb The RGB used for the player color.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/10
	 */
	private PlayerIndex(String __name, int __rgb)
		throws NullPointerException
	{
		// Check
		if (__name == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.name = __name;
		this.argb = __rgb | 0xFF000000;
		this.rgb = __rgb & 0xFFFFFF;
	}
}

