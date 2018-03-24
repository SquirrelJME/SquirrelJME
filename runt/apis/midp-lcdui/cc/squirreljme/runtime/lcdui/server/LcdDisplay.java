// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.server;

import cc.squirreljme.runtime.cldc.system.type.RemoteMethod;
import cc.squirreljme.runtime.lcdui.WidgetType;

/**
 * This class represents a single display that is available, displayables may
 * be attached to it accordingly.
 *
 * A display is a widget and as such it must have a unique handle in order for
 * it to be found accordingly.
 *
 * @since 2018/03/18
 */
public abstract class LcdDisplay
	extends LcdWidget
{
	/** The callback method for events. */
	protected final RemoteMethod callback;
	
	/** The current widget being shown, owned by a task. */
	volatile LcdWidget _current;
	
	/**
	 * Initiazes the display.
	 *
	 * @param __handle The display handle.
	 * @param __cb The callback method for events.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/18
	 */
	public LcdDisplay(int __handle, RemoteMethod __cb)
		throws NullPointerException
	{
		super(__handle, WidgetType.DISPLAY);
		
		if (__cb == null)
			throw new NullPointerException("NARG");
		
		this.callback = __cb;
	}
	
	/**
	 * This is called after a widget is made current on a display.
	 *
	 * @param __w The widget which is now shown, if this is {@code null} then
	 * the old one should be cleared.
	 * @since 2018/03/23
	 */
	protected abstract void internalSetCurrent(LcdWidget __w);
	
	/**
	 * Vibrates the display for the given duration.
	 *
	 * @param __ms The number of milliseconds to vibrate for, zero or negative
	 * values should stop vibrating.
	 * @since 2018/03/19
	 */
	public abstract void vibrate(int __ms);
	
	/**
	 * Sets the widget to be shown on this display.
	 *
	 * @param __w The widget to show on the display.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/23
	 */
	public final void setCurrent(LcdWidget __w)
		throws IllegalStateException, NullPointerException
	{
		if (__w == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

