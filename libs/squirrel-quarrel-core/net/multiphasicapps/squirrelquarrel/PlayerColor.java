// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirrelquarrel;

/**
 * The represents the color of a player.
 *
 * @since 2017/02/10
 */
public enum PlayerColor
{
	/** Red. */
	RED("Red", 0xFF0202),
	
	/** Blue. */
	BLUE("Blue", 0x0041FF),
	
	/** Blue. */
	TEAL("Teal", 0x18E6B9),
	
	/** Blue. */
	PURPLE("Purple", 0x530081),
	
	/** Blue. */
	YELLOW("Yellow", 0xFFFB01),
	
	/** Blue. */
	ORANGE("Orange", 0xFEB90D),
	
	/** Blue. */
	GREEN("Green", 0x20C000),
	
	/** Blue. */
	PINK("Pink", 0xE45BB0),
	
	/** Blue. */
	NEUTRAL("Neutral", 0x7DBEF1),
	
	/** End. */
	;
	
	/** The number of players available. */
	public static final int NUM_PLAYERS =
		PlayerColor.values().length;
	
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
	 * @return The player mask.
	 * @since 2017/02/14
	 */
	public final int mask()
	{
		// Neutral has no mask
		if (this == NEUTRAL)
			return 0;
		
		// Otherwise it is the bit shift
		return 1 << ordinal();
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
			case 1: return BLUE;
			case 2: return TEAL;
			case 3: return PURPLE;
			case 4: return YELLOW;
			case 5: return ORANGE;
			case 6: return GREEN;
			case 7: return PINK;
			case 8: return NEUTRAL;
			
				// {@squirreljme.error BE06 Player index out of range. (The
				// player index)}
			default:
				throw new IllegalArgumentException(String.format("BE06 %d",
					__i));
		}
	}
}

