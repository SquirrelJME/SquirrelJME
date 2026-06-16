// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.rts.rate;

/**
 * Standard frame rate speeds.
 *
 * @since 2026/06/10
 */
public enum RateSpeed
{
	/** Slowest game speed, ~6 FPS. */
	SLOWEST(167),
	
	/** Slower game speed, ~9 FPS. */
	SLOWER(111),
	
	/** Slow game speed, ~12 FPS. */
	SLOW(83),
	
	/** Normal game speed, ~15 FPS. */
	NORMAL(67),
	
	/** Fast game speed, ~18 FPS. */
	FAST(56),
	
	/** Faster game speed, ~21 FPS. */
	FASTER(48),
	
	/** Fastest game speed, ~24 FPS. */
	FASTEST(42),
	
	/* End. */
	;
	
	/** The milliseconds per tic. */
	public final int msPerTic;
	
	/** Nanoseconds per tic. */
	public final long nanosPerTic;
	
	/**
	 * Initializes the rate.
	 *
	 * @param __msPerTic The milliseconds per tic.
	 * @since 2026/06/10
	 */
	RateSpeed(int __msPerTic)
	{
		this.msPerTic = __msPerTic;
		this.nanosPerTic = __msPerTic * 1_000_000L;
	}
}
