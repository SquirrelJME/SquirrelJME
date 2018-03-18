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

import cc.squirreljme.runtime.cldc.task.SystemTask;
import cc.squirreljme.runtime.lcdui.DisplayableType;
import cc.squirreljme.runtime.lcdui.LcdDisplayableTakenException;
import java.util.Objects;

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
	
	/** The current displayable being shown. */
	volatile LcdDisplayable _current;
	
	/** The displayable to be shown on exit. */
	private volatile LcdDisplayable _exit;
	
	/**
	 * Initiazes the display.
	 *
	 * @param __dx The index of this display.
	 * @since 2018/03/18
	 */
	public LcdDisplay(int __dx)
	{
		this.index = __dx;
	}
	
	/**
	 * This is called when the current display has been changed.
	 *
	 * @param __c The new current displayable to use, will be {@code null} when
	 * it has been cleared.
	 * @since 2018/03/18
	 */
	protected abstract void internalSetCurrent(LcdDisplayable __d);
	
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
	 * Sets the current displayable to be shown.
	 *
	 * @param __show The displayable to be shown.
	 * @param __exit The displayable to show on exit.
	 * @throws LcdDisplayableTakenException If the displayables are already
	 * being displayed somewhere.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/18
	 */
	public final void setCurrent(LcdDisplayable __show, LcdDisplayable __exit)
		throws LcdDisplayableTakenException, NullPointerException
	{
		if (__show == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error EB1c The displayable to display is currently
		// bound to another display.}
		if (__show._current != null)
			throw new LcdDisplayableTakenException("EB1c");
		
		// {@squirreljme.error EB1d The alert to be displayed is currently
		// bound to another display.}
		if (__exit != null && __exit._current != null)
			throw new LcdDisplayableTakenException("EB1d");
		
		// The old displayable needs to be cleaned up
		LcdDisplayable wascurrent = this._current;
		if (wascurrent != null)
		{
			// If any alert is currently being displayed then it will be
			// removed along with its timer (if any)
			if (wascurrent.type() == DisplayableType.ALERT)
			{
				throw new todo.TODO();
			}
			
			// The current display is being cleared
			this.internalSetCurrent(null);
			this._current = null;
			
			// The old current is not bound to a display anymore
			wascurrent._current = null;
		}
		
		// Set new display
		this._current = __show;
		__show._current = this;
		
		// Set the exit display too
		this._exit = __exit;
		if (__exit != null)
			__exit._current = this;
		
		// Tell the widget system that this has changed
		this.internalSetCurrent(__show);
	}
}

