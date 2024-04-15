// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package dev.shadowtail.squirrelquarrel;

import java.util.NoSuchElementException;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Image;

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
	
	/** Player 1. */
	public static final PlayerIndex P1 =
		PlayerIndex.RED;
	
	/** Player 2. */
	public static final PlayerIndex P2 =
		PlayerIndex.PURPLE;
	
	/** Player 3. */
	public static final PlayerIndex P3 =
		PlayerIndex.GREEN;
	
	/** Player 4. */
	public static final PlayerIndex P4 =
		PlayerIndex.BLUE;
	
	/** Creeps that just annoy you. */
	public static final PlayerIndex CREEPS =
		PlayerIndex.YELLOW;
	
	/** The neutral player. */
	public static final PlayerIndex NEUTRAL =
		PlayerIndex.GRAY;
	
	/** The ARGB color code. */
	public final int argb;
	
	/** The RGB color code. */
	public final int rgb;
	
	/** The color name. */
	public final String name;
	
	/** The colorbox. */
	private Image _colorbox;
	
	/**
	 * Initializes the color code.
	 *
	 * @param __name The name of the color.
	 * @param __rgb The RGB used for the player color.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/10
	 */
	PlayerIndex(String __name, int __rgb)
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
	 * Returns the color box for this player.
	 *
	 * @return The color box.
	 * @since 2019/12/26
	 */
	public final Image colorBox()
	{
		// Pre-made already?
		Image rv = this._colorbox;
		if (rv != null)
			return rv;
		
		Display d = Display.getDisplay(MainMidlet.INSTANCE);
		this._colorbox = (rv = Image.createImage(
			d.getBestImageWidth(Display.LIST_ELEMENT),
			d.getBestImageHeight(Display.LIST_ELEMENT), false, this.argb));
		return rv;
	}
	
	/**
	 * Returns the player index of the given index.
	 *
	 * @param __i The index.
	 * @return The player index.
	 * @throws NoSuchElementException If it was not found.
	 * @since 2019/12/26
	 */
	public static final PlayerIndex of(int __i)
		throws NoSuchElementException
	{
		switch (__i)
		{
			case 0:	return PlayerIndex.RED;
			case 1:	return PlayerIndex.PURPLE;
			case 2:	return PlayerIndex.GREEN;
			case 3:	return PlayerIndex.BLUE;
			case 4:	return PlayerIndex.YELLOW;
			case 5:	return PlayerIndex.GRAY;
			
				/* {@squirreljme.error BE0u Invalid player index.} */
			default:
				throw new NoSuchElementException("BE0u");
		}
	}
}

