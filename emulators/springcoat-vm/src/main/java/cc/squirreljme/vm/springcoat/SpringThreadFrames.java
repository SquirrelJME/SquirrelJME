// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.springcoat;

import cc.squirreljme.vm.springcoat.exceptions.SpringVirtualMachineException;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages thread frames.
 *
 * @since 2024/01/26
 */
public final class SpringThreadFrames
{
	/** The thread frames. */
	private final List<SpringThreadFrame> _frames =
		new ArrayList<>();
	
	/**
	 * Returns all frames.
	 *
	 * @return All the frames.
	 * @since 2024/01/26
	 */
	public SpringThreadFrame[] all()
	{
		List<SpringThreadFrame> frames = this._frames;
		synchronized (this)
		{
			return frames.toArray(new SpringThreadFrame[frames.size()]);
		}
	}
	
	/**
	 * Returns the frame count.
	 *
	 * @return The number of frames that exist.
	 * @since 2024/01/26
	 */
	public int count()
	{
		List<SpringThreadFrame> frames = this._frames;
		synchronized (this)
		{
			return frames.size();
		}
	}
	
	/**
	 * Returns the current frame.
	 *
	 * @return The current frame or {@code null} if there are none.
	 * @since 2024/01/26
	 */
	public SpringThreadFrame current()
	{
		List<SpringThreadFrame> frames = this._frames;
		synchronized (this)
		{
			if (frames.isEmpty())
				return null;
			return frames.get(frames.size() - 1);
		}
	}
	
	/**
	 * Enters a new frame.
	 *
	 * @param __inClass The class the new frame is in.
	 * @param __inMethod The method the new frame is in.
	 * @param __vmArgs Arguments to be placed in local variables.
	 * @return The resultant frame.
	 * @since 2024/01/26
	 */
	public SpringThreadFrame enter(SpringClass __inClass,
		SpringMethod __inMethod, Object[] __vmArgs)
	{
		// Lock on frames as a new one is added
		List<SpringThreadFrame> frames = this._frames;
		SpringThreadFrame rv;
		synchronized (this)
		{
			rv = new SpringThreadFrame(frames.size(), __inClass,
				__inMethod, __vmArgs);
			
			frames.add(rv);
		}
		
		return rv;
	}
	
	/**
	 * Enters a blank frame.
	 *
	 * @return The resultant frame.
	 * @throws SpringVirtualMachineException If there are too many frames
	 * on the stack.
	 * @since 2024/01/26
	 */
	public SpringThreadFrame enterBlank()
		throws SpringVirtualMachineException
	{
		// Setup blank frame
		List<SpringThreadFrame> frames = this._frames;
		
		// Lock on frames as a new one is added
		SpringThreadFrame rv;
		synchronized (this)
		{
			/* {@squirreljme.error BK1j Stack overflow.} */
			if (frames.size() >= SpringThread.MAX_STACK_DEPTH)
				throw new SpringVirtualMachineException("BK1j");
			
			rv = new SpringThreadFrame(frames.size());
			
			frames.add(rv);
		}
		
		return rv;
	}
	
	/**
	 * Exits all frames.
	 *
	 * @since 2024/01/26
	 */
	public void exitAll()
	{
		// Lock on frames to remove them all
		List<SpringThreadFrame> frames = this._frames;
		synchronized (this)
		{
			frames.clear();
		}
	}
	
	/**
	 * Pops a single frame.
	 *
	 * @return The popped frame.
	 * @throws SpringVirtualMachineException If there are no frames to pop.
	 * @since 2024/01/26
	 */
	public SpringThreadFrame pop()
		throws SpringVirtualMachineException
	{
		List<SpringThreadFrame> frames = this._frames;
		synchronized (this)
		{
			/* {@squirreljme.error BK1o No frames to pop.} */
			int n;
			if ((n = frames.size()) <= 0)
				throw new SpringVirtualMachineException("BK1o");	
			
			return frames.remove(n - 1);
		}
	}
}
