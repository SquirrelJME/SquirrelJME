// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jdwp.host;

import cc.squirreljme.jdwp.JDWPStepDepth;
import cc.squirreljme.jdwp.JDWPStepSize;
import cc.squirreljme.jdwp.host.views.JDWPViewFrame;
import cc.squirreljme.jdwp.host.views.JDWPViewThread;
import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * This is a tracker for stepping events.
 *
 * @since 2021/04/28
 */
public final class JDWPHostStepTracker
{
	/** The position this was at. */
	protected final Position was =
		new Position();
	
	/** The position this is at now. */
	protected final Position now =
		new Position();
	
	/** The current stepping mode. */
	volatile JDWPStepSize _stepSize;
	
	/** The stepping depth. */
	volatile JDWPStepDepth _stepDepth;
	
	/** Is this in stepping mode? */
	private volatile boolean _inStepping;
	
	/** The baseline depth. */
	private volatile int _baseDepth =
		-1;
	
	/**
	 * Clears the stepper.
	 * 
	 * @since 2021/04/28
	 */
	public void clear()
	{
		synchronized (this)
		{
			// Clear parameters
			this._inStepping = false;
			this._stepSize = null;
			this._stepDepth = null;
			
			// Clear positions
			this._baseDepth = -1;
			this.was.__set(-1, -1, -1);
			this.now.__set(-1, -1, -1);
		}
	}
	
	/**
	 * Returns the stepping depth.
	 * 
	 * @return The stepping depth.
	 * @since 2021/04/29
	 */
	public JDWPStepDepth depth()
	{
		synchronized (this)
		{
			return this._stepDepth;
		}
	}
	
	/**
	 * Checks if this is in stepping mode.
	 * 
	 * @return If this is in stepping mode.
	 * @since 2021/04/28
	 */
	public boolean inSteppingMode()
	{
		synchronized (this)
		{
			return this._inStepping;
		}
	}
	
	/**
	 * Sets the stepping mode.
	 * 
	 * @param __controller The controller used.
	 * @param __thread The current thread.
	 * @param __size The size of the step.
	 * @param __depth The stepping depth.
	 * @since 2021/04/28
	 */
	public void steppingSet(JDWPHostController __controller, Object __thread,
		JDWPStepSize __size, JDWPStepDepth __depth)
	{
		synchronized (this)
		{
			// Set
			this._inStepping = true;
			
			// Always choose the smallest of the stepping sizes
			JDWPStepSize oldSize = this._stepSize;
			this._stepSize = (__size == null || oldSize == JDWPStepSize.MIN ?
				JDWPStepSize.MIN : __size);
			
			// Depth can change regardless
			this._stepDepth = (__depth == null ? JDWPStepDepth.INTO : __depth);
			
			// Get the top-most frame
			Object[] frames = __controller.viewThread().frames(__thread);
			int numFrames = frames.length;
			Object frame = (numFrames == 0 ? null : frames[0]);
			
			// Set initial depth
			this._baseDepth = numFrames;
			
			// Clear and set initial parameters
			JDWPViewFrame viewFrame = __controller.viewFrame();
			Position was = this.was;
			was.__set(numFrames,
				(frame == null ? -1 : viewFrame.atCodeIndex(frame)),
				(frame == null ? -1 : viewFrame.atLineIndex(frame)));
			this.now.__copyFrom(was);
		}
	}
	
	/**
	 * Ticks this to check if it will trigger a step event.
	 * 
	 * @param __jdwp The controller.
	 * @param __thread The thread to tick.
	 * @return If this will trigger a step event.
	 * @since 2021/04/28
	 */
	public boolean tick(JDWPHostController __jdwp, Object __thread)
	{
		synchronized (this)
		{
			// These must be valid and set
			JDWPStepSize stepSize = this._stepSize;
			JDWPStepDepth stepDepth = this._stepDepth;
			if (stepSize == null || stepDepth == null)
				return false;
			
			// Get the viewers
			JDWPViewThread viewThread = __jdwp.viewThread();
			JDWPViewFrame viewFrame = __jdwp.viewFrame();
			
			// Get the top-most frame
			Object[] frames = viewThread.frames(__thread);
			int numFrames = frames.length;
			Object frame = (numFrames == 0 ? null : frames[0]);
			
			// Was this never set? Set it just in case
			int baseDepth = this._baseDepth;
			if (baseDepth < 0)
				baseDepth = numFrames;
			
			// Determine the current position
			long nowCode = (frame == null ? -1 :
				viewFrame.atCodeIndex(frame));
			long nowLine = (frame == null ? -1 :
				viewFrame.atLineIndex(frame));
			
			// Old and current positions
			Position was = this.was;
			Position now = this.now;
			
			// Copy old 
			was.__copyFrom(now);
			now.__set(numFrames, nowCode, nowLine);
			
			// Specific actions depend on the stepping depth action
			int nowDepth = now._depth;
			switch (stepDepth)
			{
					// Only count when on lower levels
				case OUT:
					if (nowDepth >= baseDepth)
						return false;
					break;
					
					// Only count when on the same level
				case OVER:
					if (nowDepth != baseDepth)
						return false;
					break;
			}
			
			// If the depth has changed, count it as a step since the user
			// will want to reorient themselves to the entry/exit.
			if (nowDepth != was._depth)
				return true;
			
			// First check for line number changes
			// This is only valid if all the lines are valid! Otherwise, fall
			// back to positional checks. But regardless, if the line number
			// changes then we take the step.
			long wasLine = was._line;
			if (stepSize == JDWPStepSize.LINE && wasLine >= 0 && nowLine >= 0)
				return wasLine != nowLine;
			
			// If the code index has changed, then we consider it a step
			return nowCode != was._code;
		}
	}
	
	/**
	 * A position for the stepping tracker.
	 * 
	 * @since 2021/04/28
	 */
	public static final class Position
	{
		/** The current frame depth. */
		volatile int _depth =
			-1;
		
		/** The code index. */
		volatile long _code =
			-1;
		
		/** The line index. */
		volatile long _line =
			-1;
		
		/**
		 * Copies from the given position.
		 * 
		 * @param __from The source position.
		 * @since 2021/04/28
		 */
		void __copyFrom(Position __from)
		{
			this._depth = __from._depth;
			this._code = __from._code;
			this._line = __from._line;
		}
		
		/**
		 * Sets the current position.
		 * 
		 * @param __numFrames The frame depth.
		 * @param __nowCode The current code position.
		 * @param __nowLine The current line position.
		 * @since 2021/04/28
		 */
		void __set(int __numFrames, long __nowCode, long __nowLine)
		{
			this._depth = __numFrames;
			this._code = __nowCode;
			this._line = __nowLine;
		}
	}
}
