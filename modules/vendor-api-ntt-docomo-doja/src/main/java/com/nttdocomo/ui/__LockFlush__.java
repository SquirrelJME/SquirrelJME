// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.nttdocomo.ui;

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
	 * Specifies that a double buffered draw operation has started. If
	 * double buffering is not supported, this does nothing.
	 *
	 * @since 2024/06/24
	 */
	void __lock()
	{
		// Count up
		synchronized (this)
		{
			this._count++;
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
		}
		
		// Flush graphics?
		if (count == 0)
		{
			// Ignore if GCed
			Canvas target = this._target.get();
			if (target == null)
				return;
			
			// Tell canvas to repaint itself
			target.__displayable().repaint();
		}
	}
}
