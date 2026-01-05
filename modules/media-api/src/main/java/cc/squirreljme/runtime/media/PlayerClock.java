// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.media;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * This is a player clock which is capable of keeping track of time for a
 * player across multiple tracks. This should be used by
 * all {@link AbstractPlayer} instances for time tracking.
 *
 * @since 2026/01/03
 */
@SquirrelJMEVendorApi
public final class PlayerClock
{
	/** The number of tracks this keeps track of. */
	@SquirrelJMEVendorApi
	public final int numTracks;
	
	/** Nanosecond time when the next event occurs on a given track. */
	@SquirrelJMEVendorApi
	volatile long[] _trackNext;
	
	/** The current track duration. */
	@SquirrelJMEVendorApi
	volatile long _durationNano =
		AbstractPlayer.TIME_UNKNOWN;
	
	/** The current clock time. */
	@SquirrelJMEVendorApi
	volatile long _currentNano =
		AbstractPlayer.TIME_UNKNOWN;
	
	/** The time to seek/fast-forward to. */
	@SquirrelJMEVendorApi
	volatile long _seekNano =
		AbstractPlayer.TIME_UNKNOWN;
	
	/**
	 * Initializes the player clock.
	 *
	 * @param __numTracks The number of tracks to store event counters for.
	 * @throws IllegalArgumentException If the number of tracks is zero or
	 * negative.
	 * @since 2026/01/03
	 */
	@SquirrelJMEVendorApi
	public PlayerClock(int __numTracks)
		throws IllegalArgumentException
	{
		if (__numTracks <= 0)
			throw new IllegalArgumentException("NEGV");
		
		throw Debugging.todo();
	}
	
	/**
	 * Returns the current time in microseconds.
	 *
	 * @return The current time in microseconds.
	 * @since 2026/01/03
	 */
	@SquirrelJMEVendorApi
	public final long currentMicros()
	{
		throw Debugging.todo();
	}
	
	/**
	 * Returns the current time in nanoseconds.
	 *
	 * @return The current time in nanoseconds.
	 * @since 2026/01/03
	 */
	@SquirrelJMEVendorApi
	public final long currentNanos()
	{
		throw Debugging.todo();
	}
	
	/**
	 * Returns the current duration.
	 *
	 * @return The current duration.
	 * @since 2026/01/03
	 */
	@SquirrelJMEVendorApi
	public final long duration()
	{
		throw Debugging.todo();
	}
	
	/**
	 * Sets the duration then returns it.
	 *
	 * @param __nanos THe new duration to set.
	 * @return The newly set duration.
	 * @since 2026/01/03
	 */
	@SquirrelJMEVendorApi
	public final long duration(long __nanos)
	{
		throw Debugging.todo();
	}
	
	/**
	 * Progresses the current clock based on the specified nanosecond.
	 *
	 * @param __nano The nanosecond to progress by.
	 * @return Equivalent of {@link #currentNanos()}.
	 * @since 2026/01/03
	 */
	@SquirrelJMEVendorApi
	public final long progressByNano(long __nano)
	{
		throw Debugging.todo();
	}
	
	/**
	 * Resets all clocks to the initial state.
	 * 
	 * @since 2026/01/03
	 */
	@SquirrelJMEVendorApi
	public final void reset()
	{
		throw Debugging.todo();
	}
	
	/**
	 * Returns the next event timings for all tracks. 
	 *
	 * @return The next event timings for all tracks.
	 * @since 2026/01/03
	 */
	@SquirrelJMEVendorApi
	public final long[] trackNext()
	{
		return this._trackNext;
	}
}
