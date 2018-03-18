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
}

