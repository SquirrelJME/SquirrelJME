// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator.profiler;

import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * This stores information along with the thread information along with the
 * call stack.
 *
 * This class is not thread safe and it is assumed to be called from the same
 * thread each time.
 *
 * @since 2018/11/10
 */
public final class ProfiledThread
{
	/** The name of this thread. */
	protected final String name;
	
	/** The root frames for this thread. */
	final Map<FrameLocation, ProfiledFrame> _frames =
		new LinkedHashMap<>();
	
	/** The stack of currently active frames. */
	private final Deque<ProfiledFrame> _stack =
		new LinkedList<>();
	
	/** A reference to the current thread for time tracking. */
	
	/** Grand invocation total. */
	long _invtotal;
	
	/** Total time. */
	long _totalTime;
	
	/** CPU time. */
	long _cpuTime;
	
	/**
	 * Initializes the thread information.
	 *
	 * @param __n The name of the thread.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/10
	 */
	public ProfiledThread(String __n)
		throws NullPointerException
	{
		if (__n == null)
			throw new NullPointerException("NARG");
		
		this.name = __n;
	}
	
	/**
	 * Enters the given frame, the enter time is the system time.
	 *
	 * @param __cl The name of the class.
	 * @param __mn The name of the method.
	 * @param __md The type of the method.
	 * @throws NullPointerException On null arguments.
	 * @since 2019/06/30
	 */
	public ProfiledFrame enterFrame(String __cl, String __mn, String __md)
		throws NullPointerException
	{
		return this.enterFrame(__cl, __mn, __md, System.nanoTime());
	}
	
	/**
	 * Enters the given frame.
	 *
	 * @param __cl The name of the class.
	 * @param __mn The name of the method.
	 * @param __md The type of the method.
	 * @param __ns The The starting time in nanoseconds.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/10
	 */
	public ProfiledFrame enterFrame(String __cl, String __mn, String __md,
		long __ns)
		throws NullPointerException
	{
		if (__cl == null || __mn == null || __md == null)
			throw new NullPointerException("NARG");
		
		// Used as map key
		FrameLocation loc = new FrameLocation(__cl, __mn, __md);
		
		// We need to know the top-most frame because
		Deque<ProfiledFrame> stack = this._stack;
		ProfiledFrame top = stack.peek();
		
		// If the frame was not already recorded in the map then add a new one
		// but we always keep existing frames since their times add up and
		// such. Also remember that there is depth to this, like we cannot
		// just keep adding to the root and such!
		Map<FrameLocation, ProfiledFrame> frames = (top == null ?
			this._frames : top._frames);
		ProfiledFrame rv = frames.get(loc);
		if (rv == null)
			frames.put(loc,
				(rv = new ProfiledFrame(loc, stack.size() + 1)));
		
		// Tell the top-most frame that we are in an invoke, so this removes
		// self time accordingly
		if (top != null)
			top.invokeStart(__ns);
		
		// Push the frame to the stack since it is active
		this._stack.push(rv);
		
		// Indicate that this frame has been entered
		rv.enteredFrame(__ns);
		
		// Using this frame
		return rv;
	}
	
	/**
	 * Exits all frames as needed, the current time is used.
	 *
	 * @since 2019/06/30
	 */
	public void exitAll()
	{
		this.exitAll(System.nanoTime());
	}
	
	/**
	 * Exits all frames as needed.
	 *
	 * @param __ns The nanoseconds when exit all has happened.
	 * @since 2018/11/11
	 */
	public void exitAll(long __ns)
	{
		// Empty the frame stack
		Deque<ProfiledFrame> stack = this._stack;
		while (!stack.isEmpty())
			this.exitFrame(__ns);
	}
	
	/**
	 * Exits the frame which at the top of the stack, the current time is
	 * used.
	 *
	 * @return The exited frame.
	 * @throws IllegalStateException If there is no frame to exit.
	 * @since 2018/11/10
	 */
	public ProfiledFrame exitFrame()
		throws IllegalStateException
	{
		return this.exitFrame(System.nanoTime());
	}
	
	/**
	 * Exits the frame which at the top of the stack.
	 *
	 * @param __ns The nanoseconds when the frame exited.
	 * @return The exited frame.
	 * @throws IllegalStateException If there is no frame to exit.
	 * @since 2018/11/10
	 */
	public ProfiledFrame exitFrame(long __ns)
		throws IllegalStateException
	{
		/* {@squirreljme.error AH07 No frame is in the stack to exit in.} */
		Deque<ProfiledFrame> stack = this._stack;
		ProfiledFrame rv = stack.pop();
		if (rv == null)
			throw new IllegalStateException("AH07");
		
		// Tell that popped frame we left
		// returns: [0:total, 1:self, 2:self CPU, 3:sleepTime]
		long[] times = rv.exitedFrame(__ns);
		
		// The total thread time is the cumulative of the self times
		this._totalTime += Math.max(0, times[1]);
		this._cpuTime += Math.max(0, times[2]);
		
		// Every frame that is set gets the cumulative time of the self time
		// of the frame that just exited, so this way all parent frames will
		// have times greater than or equal to the sub-frames
		for (ProfiledFrame frame : stack)
		{
			frame._totalTime += Math.max(0, times[1]);
			frame._totalCpuTime += Math.max(0, times[2]);
		}
		
		// If we had a frame underneath, say the invocation has ended
		ProfiledFrame newTop = stack.peek();
		if (newTop != null)
		{
			// End the invocation
			newTop.invokeEnd(__ns);
		}
		
		// If all threads are out, count the times
		else
		{
			// Invocation total goes up after each method ends
			this._invtotal++;
		}
		
		// Return the frame which was popped
		return rv;
	}
}

