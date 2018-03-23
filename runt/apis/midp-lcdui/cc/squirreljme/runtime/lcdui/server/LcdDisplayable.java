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

/**
 * This represents a single displayable which may be shown on a Display as
 * required.
 *
 * @since 2018/03/18
 */
public abstract class LcdDisplayable
{
	/** The displayable handle. */
	protected final int handle;
	
	/** The task this owns this displayable. */
	protected final SystemTask task;
	
	/** The type of displayable this is. */
	protected final DisplayableType type;
	
	/** The callback for this displayable. */
	protected final LcdCallbackManager callbacks;
	
	/** The display this is attached to. */
	volatile LcdDisplay _current;
	
	/**
	 * Initializes the base displayable.
	 *
	 * @param __handle The handle for this displayable.
	 * @param __task The task owning this displayable.
	 * @param __type The type of displayable this is.
	 * @param __cb The callback manager.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/17
	 */
	public LcdDisplayable(int __handle, SystemTask __task,
		DisplayableType __type, LcdCallbackManager __cb)
		throws NullPointerException
	{
		if (__task == null || __type == null || __cb == null)
			throw new NullPointerException("NARG");
		
		this.handle = __handle;
		this.task = __task;
		this.type = __type;
		this.callbacks = __cb;
	}
	
	/**
	 * Returns the height of the displayable.
	 *
	 * @return The displayable height.
	 * @since 2018/03/19
	 */
	protected abstract int internalGetHeight();
	
	/**
	 * Returns the width of the displayable.
	 *
	 * @return The displayable width.
	 * @since 2018/03/19
	 */
	protected abstract int internalGetWidth();
	
	/**
	 * Returns the title of the displayable.
	 *
	 * @return The displayable title.
	 * @since 2018/03/18
	 */
	public abstract String getTitle();
	
	/**
	 * Specifies that the given region should be repainted.
	 *
	 * @param __x The X coordinate.
	 * @param __y The Y coordinate.
	 * @param __w The width.
	 * @param __h The height.
	 * @since 2018/03/18
	 */
	public abstract void repaint(int __x, int __y, int __w, int __h);
	
	/**
	 * Sets the title of the displayable.
	 *
	 * @param __t The displayable title.
	 * @since 2018/03/18
	 */
	public abstract void setTitle(String __t);
	
	/**
	 * This is called when the displayable is garbage collected.
	 *
	 * @since 2018/03/23
	 */
	public final void cleanup()
	{
		throw new todo.TODO();
	}
	
	/**
	 * Returns the display this is currently attached to.
	 *
	 * @return The currently attached display.
	 * @since 2018/03/18
	 */
	public final LcdDisplay getCurrent()
	{
		return this._current;
	}
	
	/**
	 * Returns the height of the displayable.
	 *
	 * @return The displayable height or a negative value if the displayable
	 * is not active.
	 * @since 2018/03/19
	 */
	public final int getHeight()
	{
		if (this._current == null)
			return Integer.MIN_VALUE;
		return this.internalGetHeight();
	}
	
	/**
	 * Returns the width of the displayable.
	 *
	 * @return The displayable width or a negative value if the displayable
	 * is not active.
	 * @since 2018/03/19
	 */
	public final int getWidth()
	{
		if (this._current == null)
			return Integer.MIN_VALUE;
		return this.internalGetWidth();
	}
	
	/**
	 * Returns the handle of the displayable.
	 *
	 * @return The handle used.
	 * @since 2018/03/17
	 */
	public final int handle()
	{
		return this.handle;
	}
	
	/**
	 * Returns the type of displayable this is.
	 *
	 * @return The displayable type.
	 * @since 2018/03/18
	 */
	public final DisplayableType type()
	{
		return this.type;
	}
	
	/**
	 * Returns the task which owns this displayable.
	 *
	 * @return The owning task.
	 * @since 2018/03/18
	 */
	public final SystemTask task()
	{
		return this.task;
	}
}

