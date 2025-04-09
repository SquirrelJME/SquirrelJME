// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.nttdocomo.ui;

import cc.squirreljme.jvm.mle.scritchui.NativeScritchInterface;
import cc.squirreljme.runtime.lcdui.gfx.DoubleBuffer;
import java.lang.ref.WeakReference;

/**
 * Handler for any flush operation for drawing.
 *
 * @since 2024/06/24
 */
final class __LockFlush__
{
	/** The target to the flush. */
	private final WeakReference<Canvas> _target;
	
	/** The lock count. */
	private volatile int _count;
	
	/** Called out of thread? */
	private volatile boolean _outOfThread;
	
	/**
	 * Initializes the lock flush against the given canvas. 
	 *
	 * @param __canvas The canvas to target.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/06/24
	 */
	__LockFlush__(Canvas __canvas)
		throws NullPointerException
	{
		if (__canvas == null)
			throw new NullPointerException("NARG");
		
		this._target = new WeakReference<>(__canvas);
	}
	
	/**
	 * This is used to flag if another thread in DoJa is performing the
	 * drawing, rather than the main event loop which is the proper way of
	 * handling this.
	 *
	 * @return {@code this}.
	 * @since 2025/04/09
	 */
	__LockFlush__ __checkThread()
	{
		// Flag if we are not in the event thread
		if (!NativeScritchInterface.nativeInterface().eventLoop().inLoop())
			synchronized (this)
			{
				this._outOfThread = true;
			}
		
		return this;
	}
	
	/**
	 * Returns whether this is locked or not.
	 *
	 * @return Whether this is locked or not.
	 * @since 2025/04/09
	 */
	boolean __isLocked()
	{
		synchronized (this)
		{
			return this._count > 0;
		}
	}
	
	/**
	 * Specifies that a double buffered draw operation has started. If
	 * double buffering is not supported, this does nothing.
	 *
	 * @since 2024/06/24
	 */
	void __lock()
	{
		synchronized (this)
		{
			// Count up
			this._count++;
		}
		
		// Ignore if GCed
		Canvas target = this._target.get();
		if (target == null)
			return;
		
		// Clear the off-screen buffer before drawing
		target._midpCanvas._doubleBuffer.clear();
	}
	
	/**
	 * Returns whether this was claimed outside the ScritchUI thread.
	 *
	 * @return Whether this was claimed outside the ScritchUI thread.
	 * @since 2025/04/09
	 */
	boolean __outOfThread()
	{
		synchronized (this)
		{
			return this._outOfThread;
		}
	}
	
	/**
	 * Unlocks the double buffering operation.
	 *
	 * @param __forced If the operation is forced
	 * then the buffer is immediately drawn and the lock count is set to
	 * zero, otherwise this will only draw when the lock count is zero. 
	 * @since 2024/06/24
	 */
	void __unlock(boolean __forced)
	{
		// If we are in the event loop, do not lock
		if (NativeScritchInterface.nativeInterface().eventLoop().inLoop())
			return;
		
		// Count down
		int count;
		synchronized (this)
		{
			count = this._count;
			if (__forced)
				count = 0;
			else
				count = Math.max(0, count - 1);
			
			// Set new count
			this._count = count;
			
			// Notify that the lock state has changed
			this.notifyAll();
		}
		
		// Flush graphics?
		if (count == 0)
		{
			// Ignore if GCed
			Canvas target = this._target.get();
			if (target == null)
				return;
			
			// Tell canvas to repaint itself
			DoubleBuffer doubleBuffer = target._midpCanvas._doubleBuffer;
			doubleBuffer.flush();
			target.__displayable().repaint();
		}
	}
}
