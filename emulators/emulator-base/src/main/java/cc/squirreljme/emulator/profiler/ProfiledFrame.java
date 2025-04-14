// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator.profiler;

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
	/** Maximum stack depth. */
	public static final int MAX_STACK_DEPTH =
		64;
	
	/** The location of this frame. */
	protected final FrameLocation location;
	
	/** The depth of this frame. */
	final int _depth;
	
	/** The sub-frames of this frame. */
	final Map<FrameLocation, ProfiledFrame> _frames =
		new LinkedHashMap<>();
	
	/** The number of calls made into the frame. */
	int _numCalls;
	
	/** Cumulative time spent in this frame and child frames. */
	long _totalTime;
	
	/** Cumulative time spent in this frame and child frames with sleep. */
	long _totalCpuTime;
	
	/** Time only spent in this frame. */
	long _selfTime;
	
	/** Time only spent in this frame with sleep. */
	long _selfCpuTime;
	
	/** Time spent calling other methods. */
	private long _invokingTime;
	
	/** The time spent sleeping. */
	long _sleepTime;
	
	/** Current frame start time. */
	private long _currentstart =
		Long.MIN_VALUE;
	
	/** Current time sub-frame started execution, to remove self time. */
	private long _currentsubstart =
		Long.MIN_VALUE;
	
	/** Starting sleep time. */
	private long _sleepStart =
		Long.MIN_VALUE;
	
	/** The current in-call count for this frame. */
	private int _inCallCount;
	
	/**
	 * Initializes this frame.
	 *
	 * @param __l The frame location.
	 * @param __d The depth of this frame.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/11
	 */
	public ProfiledFrame(FrameLocation __l, int __d)
		throws NullPointerException
	{
		if (__l == null)
			throw new NullPointerException("NARG");
		
		this.location = __l;
		this._depth = __d;
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
		/* {@squirreljme.error AH01 Cannot enter frame which is in the
		entered state.} */
		long cs = this._currentstart;
		if (cs != Long.MIN_VALUE)
			throw new IllegalStateException("AH01");
			
		/* {@squirreljme.error AH02 Cannot enter frame that is in an invoke.} */
		if (this._currentsubstart != Long.MIN_VALUE)
			throw new IllegalStateException("AH02");
		
		// Mark time
		this._currentstart = __ns;
		
		// Increase call count
		this._inCallCount++;
		
		// Increase call count
		this._numCalls++;
	}
	
	/**
	 * Indicates that the frame has been exited.
	 *
	 * @param __ns The time of the exit.
	 * @return Total time with invocations, self time, self CPU time, and
	 * sleep time.
	 * @throws IllegalStateException If the frame has not been entered.
	 * @since 2018/11/11
	 */
	public final long[] exitedFrame(long __ns)
		throws IllegalStateException
	{
		/* {@squirreljme.error AH03 Cannot exit frame which is in the
		exited state.} */
		long cs = this._currentstart;
		if (cs == Long.MIN_VALUE)
			throw new IllegalStateException("AH03");
		
		/* {@squirreljme.error AH04 Cannot exit frame that is in an invoke.} */
		if (this._currentsubstart != Long.MIN_VALUE)
			throw new IllegalStateException("AH04");
		
		// Determine the cumulative and self time spent (without sub-invokes)
		long total = __ns - cs;
		long self = total - this._invokingTime;
		
		// Along with the CPU time
		long sleepTime = this._sleepTime;
		long selfCPU = self - sleepTime;
		
		// Before we leave, add our self time to be tracked
		this._totalTime += self;
		this._totalCpuTime += selfCPU;
		
		// And only this frame time
		this._selfTime += self;
		this._selfCpuTime += selfCPU;
		
		// Clear these for next time
		this._currentstart = Long.MIN_VALUE;
		this._invokingTime = 0;
		this._sleepTime = 0;
		
		// Lower the in-call count, make sure it never goes below zero
		if ((--this._inCallCount) < 0)
			this._inCallCount = 0;
		
		// Return both times since they may be useful
		return new long[]{total, self, selfCPU, sleepTime};
	}
	
	/**
	 * Returns the number of times this frame is currently in a call.
	 *
	 * @return The number of times this frame is considered in a call.
	 * @since 2020/06/17
	 */
	public final int inCallCount()
	{
		return this._inCallCount;
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
		/* {@squirreljme.error AH05 Frame is not in an invoke.} */
		long css = this._currentsubstart;
		if (css == Long.MIN_VALUE)
			throw new IllegalStateException("AH05");
		
		// Reset
		this._currentsubstart = Long.MIN_VALUE;
		
		// Return the amount of time that was spent in this invocation
		long rv = Math.max(0, __ns - css);
		this._invokingTime += rv;
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
		/* {@squirreljme.error AH06 Frame is already invoking another frame.} */
		long css = this._currentsubstart;
		if (css != Long.MIN_VALUE)
			throw new IllegalStateException("AH06");
		
		// Mark it
		this._currentsubstart = __ns;
	}
	
	/**
	 * Enters sleep mode for the current frame.
	 * 
	 * @param __enter Are we entering sleep?
	 * @param __ns The current time.
	 * @since 2021/04/25
	 */
	public void sleep(boolean __enter, long __ns)
	{
		long sleepStart = this._sleepStart;
		
		// Entering sleep?
		if (__enter)
		{
			// Frame already sleeping
			if (sleepStart != Long.MIN_VALUE)
				throw new IllegalStateException(
					"Frame is already sleeping.");
			
			// Mark it
			this._sleepStart = __ns;
		}
		
		// Ending sleep?
		else
		{
			// Frame is not asleep
			if (sleepStart == Long.MIN_VALUE)
				throw new IllegalStateException("Frame is not asleep.");
			
			// Reset
			this._sleepStart = Long.MIN_VALUE;
			
			// Reduce the amount of spent time on this
			this._sleepTime += Math.max(0, __ns - sleepStart);
		}
	}
}

