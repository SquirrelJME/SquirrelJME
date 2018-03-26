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
import cc.squirreljme.runtime.lcdui.CollectableType;
import cc.squirreljme.runtime.lcdui.gfx.PixelFormat;

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
	/** The current widget being shown, owned by a task. */
	volatile LcdWidget _current;
	
	/**
	 * Initiazes the display.
	 *
	 * @param __handle The display handle.
	 * @since 2018/03/18
	 */
	public LcdDisplay(int __handle)
		throws NullPointerException
	{
		super(__handle, CollectableType.DISPLAY_HEAD);
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
	 * Returns the pixel format of this display.
	 *
	 * @return The display pixel format.
	 * @since 2018/03/24
	 */
	public abstract PixelFormat pixelFormat();
	
	/**
	 * Vibrates the display for the given duration.
	 *
	 * @param __ms The number of milliseconds to vibrate for, zero or negative
	 * values should stop vibrating.
	 * @since 2018/03/19
	 */
	public abstract void vibrate(int __ms);
	
	/**
	 * {@inheritDoc}
	 * @since 2018/03/24
	 */
	@Override
	public final void internalAdd(LcdWidget __w)
		throws NullPointerException
	{
		if (__w == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error EB2b Displays cannot have components added to
		// them.}
		throw new IllegalStateException("EB2b");
	}
	
	/**
	 * Sets the widget to be shown on this display.
	 *
	 * @param __w The widget to show on the display, if {@code null} then
	 * it is removed.
	 * @since 2018/03/23
	 */
	public final void setCurrent(LcdWidget __w)
		throws IllegalStateException
	{
		// If there is already a widget, clear it
		LcdWidget current = this._current;
		if (current != null)
		{
			this.internalSetCurrent(null);
			this._current = null;
		}
		
		// Set new widget
		if (__w != null)
		{
			this._current = __w;
			this.internalSetCurrent(__w);
		}
	}
}

