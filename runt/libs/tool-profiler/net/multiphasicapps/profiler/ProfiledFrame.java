// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.profiler;

/**
 * This contains information and statistics for a single frame within the
 * thread stack.
 *
 * @since 2018/11/10
 */
public final class ProfiledFrame
{
	/** The number of calls made into the frame. */
	private int _numcalls;
	
	/** Cumulative time spent in this frame and child frames. */
	private long _traceselftime;
	
	/** Cumulative time spent in this frame and child frames without sleep. */
	private long _tracecputime;
	
	/** Time only spent in this frame. */
	private long _frameselftime;
	
	/** Time only spent in this frame without sleep. */
	private long _framecputime;
}

