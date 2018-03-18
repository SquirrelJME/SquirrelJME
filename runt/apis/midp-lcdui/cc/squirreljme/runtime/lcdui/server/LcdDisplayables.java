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
import java.util.Map;
import net.multiphasicapps.collections.SortedTreeMap;

/**
 * This represents and manages all of the displayables which are available to
 * the display server.
 *
 * @since 2018/03/18
 */
public abstract class LcdDisplayables
{
	/** Displayables which currently exist. */
	private final Map<Integer, LcdDisplayable> _displayables =
		new SortedTreeMap<>();
	
	/** The next handle. */
	private volatile int _nexthandle;
	
	/**
	 * Initializes the base displayable.
	 *
	 * @param __handle The handle for this displayable.
	 * @param __task The task owning this displayable.
	 * @param __type The type of displayable this is.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/17
	 */
	protected abstract LcdDisplayable internalCreateDisplayable(int __handle,
		SystemTask __task, DisplayableType __type)
		throws NullPointerException;
	
	/**
	 * Creates a new displayable.
	 *
	 * @param __task The owning task.
	 * @param __type The type of displayable to create.
	 * @return The newly created displayable.
	 * @since 2018/03/18
	 */
	public final LcdDisplayable createDisplayable(SystemTask __task,
		DisplayableType __type)
		throws NullPointerException
	{
		if (__task == null || __type == null)
			throw new NullPointerException("NARG");
		
		// Generate a new handle
		int handle = this._nexthandle++;
		
		// Internally create it
		LcdDisplayable rv = this.internalCreateDisplayable(handle, __task,
			__type);
		if (handle != rv.handle())
			throw new RuntimeException("OOPS");
		
		// Store active displayables
		this._displayables.put(handle, rv);
		
		// Use this
		return rv;
	}
	
	/**
	 * Returns the displayable by the given handle.
	 *
	 * @param __server The server requesting the handle.
	 * @param __handle The handle to obtain.
	 * @return The displayable.
	 * @throws IllegalArgumentException If the displayable does not exist or
	 * is of another task.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/18
	 */
	public final LcdDisplayable getDisplayable(LcdServer __server,
		int __handle)
		throws IllegalArgumentException, NullPointerException
	{
		if (__server == null)
			throw new NullPointerException("NARG");
		
		return this.getDisplayable(__server.task(), __handle);
	}
	
	/**
	 * Returns the displayable by the given handle.
	 *
	 * @param __task The task requesting the handle.
	 * @param __handle The handle to obtain.
	 * @return The displayable.
	 * @throws IllegalArgumentException If the displayable does not exist or
	 * is of another task.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/18
	 */
	public final LcdDisplayable getDisplayable(SystemTask __task, int __handle)
		throws IllegalArgumentException, NullPointerException
	{
		if (__task == null)
			throw new NullPointerException("NARG");
		
		Map<Integer, LcdDisplayable> displayables = this._displayables;
		
		// {@squirreljme.error EB1y The specified handle does not exist
		// for the given task. (The handle)}
		LcdDisplayable rv = displayables.get(__handle);
		if (rv == null || !__task.equals(rv.task()))
			throw new IllegalStateException(String.format("EB1y %d",
				__handle));
		
		return rv;
	}
}

