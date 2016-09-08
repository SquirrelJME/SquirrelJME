// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirrelquarrel.game;

/**
 * This represents the current game speed that is being used.
 *
 * @since 2016/09/07
 */
public enum GameSpeed
{
	/** Slowest. */
	SLOWEST(167),
	
	/** Slower. */
	SLOWER(111),
	
	/** Slow. */
	SLOW(83),
	
	/** Normal. */
	NORMAL(67),
	
	/** Fast. */
	FAST(56),
	
	/** Faster. */
	FASTER(48),
	
	/** Fastest. */
	FASTEST(42),
	
	/** End. */
	;
	
	/** Milliseconds per frame. */
	protected final int msperframe;
	
	/** Nanoseconds per frame. */
	protected final int nsperframe;
	
	/**
	 * Initializes the game speed setting.
	 *
	 * @param __ms The number of milliseconds to wait between frames.
	 * @throws IllegalArgumentException If the delay is zero or negative.
	 * @since 2016/09/07
	 */
	private GameSpeed(int __ms)
		throws IllegalArgumentException
	{
		// {@squirreljme.error BY01 The milliseconds per frame is zero or
		// negative.}
		if (__ms <= 0)
			throw new IllegalArgumentException("BY01");
		
		// Set
		this.msperframe = __ms;
		this.nsperframe = __ms * 1_000_000;
	}
	
	/**
	 * Returns the number of milliseconds that each frame lasts for.
	 *
	 * @return The milliseconds per frame.
	 * @since 2016/09/07
	 */
	public int millisPerFrame()
	{
		return this.msperframe;
	}
	
	/**
	 * Returns the number of nanoseconds that each frame lasts for.
	 *
	 * @return The nanoseconds per frame.
	 * @since 2016/09/07
	 */
	public int nanosPerFrame()
	{
		return this.nsperframe;
	}
}

