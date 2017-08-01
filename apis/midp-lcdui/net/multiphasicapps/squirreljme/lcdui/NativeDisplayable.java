// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.lcdui;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.Displayable;

/**
 * This is the base class for all native displayables.
 *
 * @since 2017/05/24
 */
public abstract class NativeDisplayable
{
	/** Reference to the the LCDUI displayable. */
	protected final Reference<Displayable> displayable;
	
	/** The head this is attached to. */
	volatile NativeDisplay.Head _head;
	
	/**
	 * Initializes the base native displayable which has a back reference to
	 * the LCDUI displayable which created this native displayable.
	 *
	 * @param __ref The reference to the native displayable.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/05/24
	 */
	public NativeDisplayable(Reference<Displayable> __ref)
		throws NullPointerException
	{
		// Check
		if (__ref == null)
			throw new NullPointerException("NARG");
		
		//Set
		this.displayable = __ref;
	}
	
	/**
	 * Returns the head that this is attached to.
	 *
	 * @return The display head this is attached.
	 * @since 2017/06/24
	 */
	public final NativeDisplay.Head attachedHead()
	{
		return this._head;
	}
	
	/**
	 * Returns the height of the displayable.
	 *
	 * @return The displayable's height.
	 * @since 2017/05/24
	 */
	public final int getHeight()
	{
		NativeDisplay.Head head = attachedHead();
		if (head == null)
			return head.getContentHeight();
		return NativeDisplay.DISPLAY.defaultHead().getMaximumHeight();
	}
	
	/**
	 * Returns the width of the displayable.
	 *
	 * @return The displayable's width.
	 * @since 2017/07/24
	 */
	public final int getWidth()
	{
		NativeDisplay.Head head = attachedHead();
		if (head == null)
			return head.getContentWidth();
		return NativeDisplay.DISPLAY.defaultHead().getMaximumWidth();
	}
}

