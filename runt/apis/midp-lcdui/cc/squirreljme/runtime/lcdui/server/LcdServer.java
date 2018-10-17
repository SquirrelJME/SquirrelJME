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

import cc.squirreljme.runtime.lcdui.CollectableType;
import cc.squirreljme.runtime.lcdui.LcdFunction;
import cc.squirreljme.runtime.lcdui.LcdFunctionInterrupted;
import cc.squirreljme.runtime.lcdui.ui.UiCollectable;
import cc.squirreljme.runtime.lcdui.ui.UiCommand;
import cc.squirreljme.runtime.lcdui.ui.UiDisplay;
import cc.squirreljme.runtime.lcdui.ui.UiDisplayHead;
import cc.squirreljme.runtime.lcdui.ui.UiInterface;
import cc.squirreljme.runtime.lcdui.ui.UiMenu;
import cc.squirreljme.runtime.lcdui.ui.UiTicker;
import java.util.HashMap;
import java.util.Map;

/**
 * This class implements the base for the LCDUI interface used for the
 * server end which resides in the kernel.
 *
 * @since 2018/03/16
 */
public final class LcdServer
{
	/** The displays which are available. */
	protected final LcdDisplays displays;
	
	/** Widgets which are currently available to this server. */
	private final Map<Integer, UiCollectable> _collects =
		new HashMap<>();
	
	/** Local widgets which wrap displays. */
	private final Map<UiDisplayHead, UiDisplay> _localdisplays =
		new HashMap<>();
	
	/** The next handle to use. */
	private int _nexthandle;
	
	/**
	 * Initializes the LCDUI server.
	 *
	 * @param __ld The displays which are available for usage.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/17
	 */
	public LcdServer(LcdDisplays __ld)
		throws NullPointerException
	{
		throw new todo.TODO();
		/*
		if (__task == null || __ld == null)
			throw new NullPointerException("NARG");
		
		this.task = __task;
		this.displays = __ld;
		*/
	}
	
	/**
	 * Creates a new collectable of the specified type.
	 *
	 * @param __type The type of collectable to create.
	 * @return The newly created collectable.
	 * @throws IllegalArgumentException If the given collectable does not
	 * permitted to be created.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/04/05
	 */
	public final UiCollectable createCollectable(CollectableType __type)
		throws IllegalArgumentException, NullPointerException
	{
		if (__type == null)
			throw new NullPointerException("NARG");
		
		// {@squirreljme.error EB29 Cannot create display heads.}
		if (__type == CollectableType.DISPLAY_HEAD)
			throw new IllegalArgumentException("EB29");
		
		// Determine the handle to use
		int handle = ++this._nexthandle;
		
		// Some collectables are pre-handled
		UiCollectable rv;
		if (__type == CollectableType.COMMAND)
			rv = new UiCommand(handle);
		else if (__type == CollectableType.MENU)
			rv = new UiCommand(handle);
		else if (__type == CollectableType.TICKER)
			rv = new UiTicker(handle);
		
		// Implementation specific
		else
			rv = this.displays.__internalCreateCollectable(handle, __type);
		
		// Store it so it may be referenced
		this._collects.put(rv.handle(), rv);
		return rv;
	}
	
	/**
	 * Returns the displays that are available.
	 *
	 * @return The displays that are available.
	 * @since 2018/03/23
	 */
	public final LcdDisplays displays()
	{
		return this.displays;
	}
	
	/**
	 * Obtains the collectable by the given type and handle.
	 *
	 * @param <U> The class type to get.
	 * @param __cl The class type tp get.
	 * @param __handle The handle of the collectable to get.
	 * @return The collectable for the given handle or {@code null} if it does
	 * not exist or is of the wrong class.
	 * @since 2018/04/05
	 */
	public final <U extends UiInterface> U get(Class<U> __cl, int __handle)
		throws NullPointerException
	{
		if (__cl == null)
			throw new NullPointerException("NARG");
		
		UiInterface rv = this._collects.get(__handle);
		if (rv == null || !__cl.isInstance(rv))
			return null;
		return __cl.cast(rv);
	}
}

