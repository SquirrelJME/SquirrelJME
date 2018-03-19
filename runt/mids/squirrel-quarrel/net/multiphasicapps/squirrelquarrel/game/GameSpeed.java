// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirrelquarrel.game;

/**
 * This represents the game speed which determines the amount of time between
 * frames.
 *
 * @since 2017/02/10
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
	
	/** The speed of normal. */
	private static final int _NORMAL_SPEED =
		67;
	
	/** Milliseconds between frames. */
	protected final int msbetweenframes;
	
	/** Nanoseconds between frames. */
	protected final int nsbetweenframes;
	
	/**
	 * Initializes the game speed.
	 *
	 * @param __msbf Milliseconds between frames.
	 * @since 2017/02/10
	 */
	private GameSpeed(int __msbf)
	{
		this.msbetweenframes = __msbf;
		this.nsbetweenframes = __msbf * 1_000_000;
	}
	
	/**
	 * Returns the game speed which is faster than this one.
	 *
	 * @return The faster game speed.
	 * @since 2017/02/12
	 */
	public GameSpeed faster()
	{
		// Depends
		switch (this)
		{
			case SLOWEST:	return SLOWER;
			case SLOWER:	return SLOW;
			case SLOW:		return NORMAL;
			case NORMAL:	return FAST;
			case FAST:		return FASTER;
			case FASTER:	return FASTEST;
			default:
				return this;
		}
	}
	
	/**
	 * Returns the duration of a single frame in nanoseconds.
	 *
	 * @return The frame duration in nanoseconds.
	 * @since 2017/02/10
	 */
	public int nanoFrameTime()
	{
		return this.nsbetweenframes;
	}
	
	/**
	 * Returns the ratio this game speed and the normal game speed.
	 *
	 * @return The ratio between speeds.
	 * @since 2017/02/12
	 */
	public double ratio()
	{
		return (double)this.msbetweenframes / _NORMAL_SPEED;
	}
	
	/**
	 * Returns the game speed which is slower than this one.
	 *
	 * @return The slower game speed.
	 * @since 2017/02/12
	 */
	public GameSpeed slower()
	{
		// Depends
		switch (this)
		{
			case SLOWER:	return SLOWEST;
			case SLOW:		return SLOWER;
			case NORMAL:	return SLOW;
			case FAST:		return NORMAL;
			case FASTER:	return FAST;
			case FASTEST:	return FASTER;
			default:
				return this;
		}
	}
}

