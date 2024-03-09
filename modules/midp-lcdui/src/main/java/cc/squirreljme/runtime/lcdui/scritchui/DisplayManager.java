// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.scritchui;

import cc.squirreljme.jvm.mle.scritchui.ScritchInterface;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import javax.microedition.lcdui.Display;
import javax.microedition.lcdui.DisplayListener;

/**
 * Tracker for displays.
 *
 * @since 2024/03/09
 */
@SquirrelJMEVendorApi
public final class DisplayManager
{
	/** The instance of the display tracker. */
	private static volatile DisplayManager _INSTANCE;
	
	/**
	 * Initializes the base tracker.
	 *
	 * @since 2024/03/09
	 */
	private DisplayManager()
	{
	}
	
	/**
	 * Adds a display listener.
	 *
	 * @param __dl The display listener to add.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/03/09
	 */
	@SquirrelJMEVendorApi
	public void displayListenerAdd(DisplayListener __dl)
		throws NullPointerException
	{
		if (__dl == null)
			throw new NullPointerException("NARG");
		
		throw cc.squirreljme.runtime.cldc.debug.Debugging.todo();
	}
	
	/**
	 * Removes the display listener.
	 *
	 * @param __dl The display listener to remove.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/03/09
	 */
	public void displayListenerRemove(DisplayListener __dl)
		throws NullPointerException
	{
		if (__dl == null)
			throw new NullPointerException("NARG");
		
		throw cc.squirreljme.runtime.cldc.debug.Debugging.todo();
	}
	
	/**
	 * Maps the given set of screens to displays.
	 *
	 * @param __factory The factory used for initializing new displays.
	 * @return The resultant displays.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/03/09
	 */
	@SquirrelJMEVendorApi
	public Display[] mapScreens(DisplayFactory __factory)
		throws NullPointerException
	{
		if (__factory == null)
			throw new NullPointerException("NARG");
		
		throw cc.squirreljme.runtime.cldc.debug.Debugging.todo();
	}
	
	/**
	 * Returns the ScritchUI interface in use.
	 *
	 * @return The ScritchUI interface.
	 * @since 2024/03/09
	 */
	public ScritchInterface scritch()
	{
		throw cc.squirreljme.runtime.cldc.debug.Debugging.todo();
	}
	
	/**
	 * Obtains the display tracker instance.
	 *
	 * @return The tracker for displays.
	 * @since 2024/03/09
	 */
	@SquirrelJMEVendorApi
	public static DisplayManager instance()
	{
		DisplayManager instance = DisplayManager._INSTANCE;
		if (instance != null)
			return instance;
		
		instance = new DisplayManager();
		DisplayManager._INSTANCE = instance;
		
		return instance;
	}
}
