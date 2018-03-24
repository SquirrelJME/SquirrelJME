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

/**
 * This class represents a single display that is available, displayables may
 * be attached to it accordingly.
 *
 * @since 2018/03/18
 */
public abstract class LcdDisplay
{
	/** The index of this display. */
	protected final int index;
	
	/** The current widget being shown, owned by a task. */
	volatile LcdWidget _current;
	
	/**
	 * Initiazes the display.
	 *
	 * @param __dx The index of this display.
	 * @param __cb The callback manager.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/18
	 */
	public LcdDisplay(int __dx)
		throws NullPointerException
	{
		this.index = __dx;
	}
	
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
	 * @since 2018/03/18
	 */
	@Override
	public final int hashCode()
	{
		return this.index;
	}
	
	/**
	 * Returns the display index.
	 *
	 * @return The display index.
	 * @since 2018/03/18
	 */
	public final int index()
	{
		return this.index;
	}
	
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

