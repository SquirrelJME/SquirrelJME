// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirrelquarrel.player;

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
 * @since 2017/02/10
 */
public enum PlayerColor
{
	/** Red. */
	RED("Red", 0xDE4949),
	
	/** Yellow. */
	YELLOW("Yellow", 0xFFB937),
	
	/** Green. */
	GREEN("Green", 0x30B06E),
	
	/** Blue. */
	BLUE("Blue", 0x33521E1),
	
	/** Purple. */
	PURPLE("Purple", 0x533364),
	
	/** Gray. */
	GRAY("Gray", 0x5A7D8B),
	
	/** End. */
	;
	
	/** The maximum number of players that can play at once. */
	public static final int MAX_PLAYERS =
		4;
	
	/** Creeps that just annoy you. */
	public static final CREEPS =
		PlayerColor.PURPLE;
	
	/** The neutral player. */
	public static final NEUTRAL =
		PlayerColor.GRAY;
	
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
	private PlayerColor(String __name, int __rgb)
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
	
	/**
	 * The ARGB code for the player.
	 *
	 * @return The ARGB color.
	 * @since 2017/02/10
	 */
	public final int argb()
	{
		return this.argb;
	}
	
	/**
	 * The RGB code for the player.
	 *
	 * @return The RGB color.
	 * @since 2017/02/10
	 */
	public final int rgb()
	{
		return this.rgb;
	}
	
	/**
	 * Returns the mask that is used for the player.
	 *
	 * The mask is used for stuff such as alliances, vision, and detection.
	 *
	 * Players outside of the basic four players have no mask.
	 *
	 * @return The player mask.
	 * @since 2017/02/14
	 */
	public final int mask()
	{
		// Only use mask for players that are in the game
		int i = this.ordinal();
		if (i < PlayerColor.MAX_PLAYERS)
			return 1 << i;
		return 0;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	public String toString()
	{
		return this.name;
	}
	
	/**
	 * This converts the player ordinal back into the player color.
	 *
	 * @param __i The ordinal.
	 * @return The player color for that ordinal.
	 * @since 2017/02/14
	 */
	public static final PlayerColor of(int __i)
	{
		// Depends
		switch (__i)
		{
				// Normal colors;
			case 0: return RED;
			case 1: return YELLOW;
			case 2: return GREEN;
			case 3: return BLUE;
			case 4: return PURPLE;
			case 5: return GRAY;
			
				// {@squirreljme.error BE06 Player index out of range. (The
				// player index)}
			default:
				throw new IllegalArgumentException(String.format("BE06 %d",
					__i));
		}
	}
}

