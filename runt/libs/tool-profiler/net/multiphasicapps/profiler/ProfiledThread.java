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

import java.util.ArrayDeque;
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
	private final Map<FrameLocation, ProfiledFrame> _frames =
		new LinkedHashMap<>();
	
	/** The stack of currently active frames. */
	private final Deque<ProfiledFrame> _stack =
		new LinkedList<>();
	
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
		
		// If the frame was not already recorded in the map then add a new one
		// but we always keep existing frames since their times add up and
		// such
		Map<FrameLocation, ProfiledFrame> frames = this._frames;
		ProfiledFrame rv = frames.get(loc);
		if (rv == null)
			frames.put(loc, (rv = new ProfiledFrame(loc)));
		
		// Tell the top-most frame that we are in an invoke, so this removes
		// self time accordingly
		Deque<ProfiledFrame> stack = this._stack;
		ProfiledFrame top = stack.peek();
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
		// {@squirreljme.error AH03 No frame is in the stack to exit in.}
		Deque<ProfiledFrame> stack = this._stack;
		ProfiledFrame rv = stack.pop();
		if (rv == null)
			throw new IllegalStateException("AH03");
		
		// Tell that popped frame we left
		long[] times = rv.exitedFrame(__ns);
		
		// If we had a frame underneath, say the invocation has ended
		ProfiledFrame newtop = stack.peek();
		if (newtop != null)
			newtop.invokeEnd(__ns);
		
		// Return the frame which was popped
		return rv;
	}
}

