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
	 * Returns the duration of a single frame in nanoseconds.
	 *
	 * @return The frame duration in nanoseconds.
	 * @since 2017/02/10
	 */
	public int nanoFrameTime()
	{
		return this.nsbetweenframes;
	}
}

