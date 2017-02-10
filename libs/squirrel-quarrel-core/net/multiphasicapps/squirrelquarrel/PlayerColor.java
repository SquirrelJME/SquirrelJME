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
	 * {@inheritDoc}
	 * @since 2017/02/10
	 */
	@Override
	public String toString()
	{
		return this.name;
	}
}

