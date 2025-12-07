// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
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
	GameSpeed(int __msbf)
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
			case SLOWEST:	return GameSpeed.SLOWER;
			case SLOWER:	return GameSpeed.SLOW;
			case SLOW:		return GameSpeed.NORMAL;
			case NORMAL:	return GameSpeed.FAST;
			case FAST:		return GameSpeed.FASTER;
			case FASTER:	return GameSpeed.FASTEST;
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
		return (double)this.msbetweenframes / GameSpeed._NORMAL_SPEED;
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
			case SLOWER:	return GameSpeed.SLOWEST;
			case SLOW:		return GameSpeed.SLOWER;
			case NORMAL:	return GameSpeed.SLOW;
			case FAST:		return GameSpeed.NORMAL;
			case FASTER:	return GameSpeed.FAST;
			case FASTEST:	return GameSpeed.FASTER;
			default:
				return this;
		}
	}
}

