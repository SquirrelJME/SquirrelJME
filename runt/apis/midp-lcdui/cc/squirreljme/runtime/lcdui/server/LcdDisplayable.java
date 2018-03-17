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
 * This represents a displayable which may be shown on a screen, it has a
 * handle and a specific type of displayable to show.
 *
 * @since 2018/03/17
 */
public abstract class LcdDisplayable
{
	/** The locking object. */
	protected final Object lock;
	
	/** The displayable handle. */
	protected final int handle;
	
	/** The owning task. */
	protected final SystemTask task;
	
	/** The type of displayable this is. */
	protected final DisplayableType type;
	
	/** The title of the displayable. */
	private volatile String _title;
	
	/**
	 * Initializes the base displayable.
	 *
	 * @param __lock The locking object used.
	 * @param __handle The handle for this displayable.
	 * @param __task The task owning this displayable.
	 * @param __type The type of displayable this is.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/17
	 */
	public LcdDisplayable(Object __lock, int __handle, SystemTask __task,
		DisplayableType __type)
		throws NullPointerException
	{
		if (__lock == null || __task == null || __type == null)
			throw new NullPointerException("NARG");
		
		this.lock = __lock;
		this.handle = __handle;
		this.task = __task;
		this.type = __type;
	}
	
	/**
	 * Internally sets the displayed title.
	 *
	 * @param __t The title to set.
	 * @since 2018/03/17
	 */
	protected abstract void internalSetTitle(String __t);
	
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
	 * Returns the title of this displayable.
	 *
	 * @return The displayable's title.
	 * @since 2018/03/17
	 */
	public final String getTitle()
	{
		synchronized (this.lock)
		{
			return this._title;
		}
	}
	
	/**
	 * Sets the title of the displayable.
	 *
	 * @param __s The title to set.
	 * @sicne 2018/03/17
	 */
	public final void setTitle(String __s)
	{
		synchronized (this.lock)
		{
			this._title = __s;
			
			// Tell the displayable of the new title
			this.internalSetTitle(__s);
		}
	}
	
	/**
	 * Returns the task which owns this displayble.
	 *
	 * @return The task which owns the displayable.
	 * @since 2018/03/17
	 */
	public final SystemTask task()
	{
		return this.task;
	}
}

