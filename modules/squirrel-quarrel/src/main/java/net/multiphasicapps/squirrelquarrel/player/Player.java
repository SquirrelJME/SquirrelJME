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
 * This represents a game player, where actions may be performed and such.
 *
 * @since 2017/02/13
 */
public final class Player
{
	/** The color of this player. */
	protected final PlayerColor color;
	
	/** The team of this player. */
	protected final PlayerColor team;
	
	/** Is this player playing? */
	protected final boolean playing;
	
	/** The vision mask for this player. */
	private volatile int _visionmask;
	
	/**
	 * Initializes the game player.
	 *
	 * @param __c The player color.
	 * @param __team The team this player is on.
	 * @param __playing Is this player playing?
	 * @throws NullPointerException On null arguments.
	 * @since 2017/02/14
	 */
	public Player(PlayerColor __c, PlayerColor __team, boolean __playing)
		throws NullPointerException
	{
		// Check
		if (__c == null || __team == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.color = __c;
		this.team = __team;
		this.playing = __playing;
		
		// The player always has vision to self
		this._visionmask = __c.mask();
	}
	
	/**
	 * Returns the player color.
	 *
	 * @return The player color.
	 * @since 2017/02/14
	 */
	public final PlayerColor color()
	{
		return this.color;
	}
	
	/**
	 * Is this player playing?
	 *
	 * @return If this player is playing.
	 * @since 2018/03/19
	 */
	public final boolean isPlaying()
	{
		return this.playing;
	}
	
	/**
	 * Returns the team that this player is on.
	 *
	 * @return The team the player is on.
	 * @since 2018/03/19
	 */
	public final PlayerColor team()
	{
		return this.team;
	}
	
	/**
	 * This returns the mask that is used to check the vision bits.
	 *
	 * @return The bits used for vision.
	 * @since 2017/02/13
	 */
	public final int visionMask()
	{
		return this._visionmask;
	}
	
	/**
	 * Runs the player logic.
	 *
	 * @param __frame The current frame.
	 * @since 2017/02/14
	 */
	public void run(int __frame)
	{
	}
}

