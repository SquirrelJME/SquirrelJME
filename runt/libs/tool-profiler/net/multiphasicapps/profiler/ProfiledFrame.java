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

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * This contains information and statistics for a single frame within the
 * thread stack.
 *
 * @since 2018/11/10
 */
public final class ProfiledFrame
{
	/** The location of this frame. */
	protected final FrameLocation location;
	
	/** The sub-frames of this frame. */
	final Map<FrameLocation, ProfiledFrame> _frames =
		new LinkedHashMap<>();
	
	/** The number of calls made into the frame. */
	int _numcalls;
	
	/** Cumulative time spent in this frame and child frames. */
	long _traceruntime;
	
	/** Cumulative time spent in this frame and child frames without sleep. */
	long _tracecputime;
	
	/** Time only spent in this frame. */
	long _frameruntime;
	
	/** Time only spent in this frame without sleep. */
	long _framecputime;
	
	/** Time to subtract from the measured self times. */
	private long _subtractself;
	
	/** Current frame start time. */
	private long _currentstart =
		Long.MIN_VALUE;
	
	/** Current time sub-frame started execution, to remove self time. */
	private long _currentsubstart =
		Long.MIN_VALUE;
	
	/**
	 * Initializes this frame.
	 *
	 * @param __l The frame location.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/11
	 */
	public ProfiledFrame(FrameLocation __l)
		throws NullPointerException
	{
		if (__l == null)
			throw new NullPointerException("NARG");
		
		this.location = __l;
	}
	
	/**
	 * Indicates that this frame has been entered.
	 *
	 * @param __ns The starting nanoseconds.
	 * @throws IllegalStateException If the frame is already entered.
	 * @since 2018/11/11
	 */
	public final void enteredFrame(long __ns)
		throws IllegalStateException
	{
		// {@squirreljme.error AH04 Cannot enter frame which is in the
		// entered state.}
		long cs = this._currentstart;
		if (cs != Long.MIN_VALUE)
			throw new IllegalStateException("AH04");
			
		// {@squirreljme.error AH07 Cannot enter frame that is in an invoke.}
		if (this._currentsubstart != Long.MIN_VALUE)
			throw new IllegalStateException("AH07");
		
		// Mark time
		this._currentstart = __ns;
	}
	
	/**
	 * Indicates that the frame has been exited.
	 *
	 * @param __ns The time of the exit.
	 * @return The time spent in this frame, in total and self time.
	 * @throws IllegalStateException If the frame has not been entered.
	 * @since 2018/11/11
	 */
	public final long[] exitedFrame(long __ns)
		throws IllegalStateException
	{
		// {@squirreljme.error AH05 Cannot exit frame which is in the
		// exited state.}
		long cs = this._currentstart;
		if (cs == Long.MIN_VALUE)
			throw new IllegalStateException("AH05");
		
		// {@squirreljme.error AH06 Cannot exit frame that is in an invoke.}
		if (this._currentsubstart != Long.MIN_VALUE)
			throw new IllegalStateException("AH06");
		
		// Determine the cumulative and self time spent
		long total = __ns - cs,
			self = total - this._subtractself;
		
		// Increase call count
		this._numcalls++;
		
		// All sub-frames times
		this._traceruntime += total;
		this._tracecputime += total;
		
		// And only this frame time
		this._frameruntime += self;
		this._framecputime += self;
		
		// Clear these for next time
		this._currentstart = Long.MIN_VALUE;
		this._subtractself = 0;
		
		// Return both times since they may be useful
		return new long[]{total, self};
	}
	
	/**
	 * Indicates that this frame is no longer invoking some other method.
	 *
	 * @param __ns The ending nanoseconds.
	 * @return The time which has passed.
	 * @throws IllegalStateException If the frame is not in an invoke.
	 * @since 2018/11/11
	 */
	public final long invokeEnd(long __ns)
		throws IllegalStateException
	{
		// {@squirreljme.error AH02 Frame is not in an invoke.}
		long css = this._currentsubstart;
		if (css == Long.MIN_VALUE)
			throw new IllegalStateException("AH02");
		
		// Reset
		this._currentsubstart = Long.MIN_VALUE;
		
		// Return the amount of time that was spend in this invocation
		long rv = __ns - css;
		this._subtractself += rv;
		return rv;
	}
	
	/**
	 * Indicates that this frame is invoking some other method.
	 *
	 * @param __ns The starting nanoseconds.
	 * @throws IllegalStateException If the frame is already in an invoke.
	 * @since 2018/11/11
	 */
	public final void invokeStart(long __ns)
		throws IllegalStateException
	{
		// {@squirreljme.error AH01 Frame is already invoking another frame.}
		long css = this._currentsubstart;
		if (css != Long.MIN_VALUE)
			throw new IllegalStateException("AH01");
		
		// Mark it
		this._currentsubstart = __ns;
	}
}

