// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.debugger;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * Stores information on the current thread and frame.
 *
 * @since 2024/01/25
 */
public final class ContextThreadFrame
{
	/** The listeners used. */
	private final List<Reference<ContextThreadFrameListener>> _listeners =
		new ArrayList<>();
	
	/** The current thread. */
	private volatile InfoThread _thread;
	
	/** The current frame. */
	private volatile InfoFrame _frame;
	
	/**
	 * Adds a listener to the context update.
	 *
	 * @param __listener The listener used.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/25
	 */
	public void addListener(ContextThreadFrameListener __listener)
		throws NullPointerException
	{
		if (__listener == null)
			throw new NullPointerException("NARG");
		
		// Add to the list
		List<Reference<ContextThreadFrameListener>> listeners =
			this._listeners;
		synchronized (this)
		{
			listeners.add(new WeakReference<>(__listener));
		}
	}
	
	/**
	 * Returns the current thread.
	 *
	 * @return The current thread.
	 * @since 2024/01/25
	 */
	public InfoThread getThread()
	{
		synchronized (this)
		{
			return this._thread;
		}
	}
	
	/**
	 * Sets the context thread.
	 *
	 * @param __thread The thread context.
	 * @since 2024/01/25
	 */
	public void set(InfoThread __thread)
	{
		InfoThread oldThread;
		InfoFrame oldFrame;
		InfoThread newThread;
		InfoFrame newFrame;
		
		// Change thread
		synchronized (this)
		{
			// Get old values
			oldThread = this._thread;
			oldFrame = this._frame;
			
			// Setting the new thread is simple
			newThread = __thread;
			
			// Try to set a new frame to look at?
			if (newThread != null &&
				(oldFrame == null ||
					!Objects.equals(oldFrame.inThread, newThread)))
			{
				// Try to get a frame to look at
				InfoFrame[] possible = newThread.frames.get();
				if (possible != null && possible.length > 0)
					newFrame = possible[possible.length - 1];
				
				// No frames at all
				else
					newFrame = null;
			}
			
			// Keep same frame?
			else
				newFrame = oldFrame;
			
			// Set
			this._thread = newThread;
			this._frame = newFrame;
		}
		
		// Update listeners
		this.__contextChanged(oldThread, oldFrame, newThread, newFrame);
	}
	
	/**
	 * Sets the context frame.
	 *
	 * @param __frame The frame context.
	 * @since 2024/01/25
	 */
	public void set(InfoFrame __frame)
	{
		InfoThread oldThread;
		InfoFrame oldFrame;
		InfoThread newThread;
		InfoFrame newFrame;
		
		// Change thread and frame
		synchronized (this)
		{
			// Get old values
			oldThread = this._thread;
			oldFrame = this._frame;
			
			// Can easily set the new thread and frames
			if (__frame != null)
			{
				newThread = __frame.inThread;
				newFrame = __frame;
			}
			
			// Clear them
			else
			{
				newThread = null;
				newFrame = null;
			}
			
			// Set
			this._thread = newThread;
			this._frame = newFrame;
		}
		
		// Update listeners
		this.__contextChanged(oldThread, oldFrame, newThread, newFrame);
	}
	
	/**
	 * Calls all the updaters with the changed information.
	 *
	 * @param __oldThread The old thread.
	 * @param __oldFrame The old frame.
	 * @param __newThread The new thread.
	 * @param __newFrame The new frame.
	 * @since 2024/01/25
	 */
	private void __contextChanged(InfoThread __oldThread, InfoFrame __oldFrame,
		InfoThread __newThread, InfoFrame __newFrame)
	{
		// If there is no actual update, then just ignore
		if (Objects.equals(__oldThread, __newThread) &&
			Objects.equals(__oldFrame, __newFrame))
			return;
		
		// Debug
		Debugging.debugNote("Context: (%s, %s) -> (%s, %s)",
			__oldThread, __oldFrame, __newThread, __newFrame);
		
		// Need to get all the listeners first
		List<ContextThreadFrameListener> listeners;
		List<Reference<ContextThreadFrameListener>> list = this._listeners;
		synchronized (this)
		{
			// Setup base array
			listeners = new ArrayList<>(list.size());
			
			// Fill it in and cleanup anything that is gone forever
			for (Iterator<Reference<ContextThreadFrameListener>> it =
				 list.iterator(); it.hasNext(); )
			{
				Reference<ContextThreadFrameListener> ref = it.next();
				ContextThreadFrameListener item = ref.get();
				
				// Clear item if it got GCed
				if (item == null)
					it.remove();
				
				// Otherwise add
				else
					listeners.add(item);
			}
		}
		
		// Call all listeners to update
		for (ContextThreadFrameListener listener : listeners)
			listener.contextChanged(__oldThread, __oldFrame,
				__newThread, __newFrame);
	}
}
