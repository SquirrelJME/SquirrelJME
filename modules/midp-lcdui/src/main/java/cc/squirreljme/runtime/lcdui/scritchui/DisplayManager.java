// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.lcdui.scritchui;

import cc.squirreljme.jvm.mle.scritchui.ScritchEnvironmentInterface;
import cc.squirreljme.jvm.mle.scritchui.ScritchInterface;
import cc.squirreljme.jvm.mle.scritchui.ScritchScreenInterface;
import cc.squirreljme.jvm.mle.scritchui.ScritchWindowInterface;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchScreenBracket;
import cc.squirreljme.jvm.mle.scritchui.brackets.ScritchWindowBracket;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.scritchui.fb.DefaultScritchInterface;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
	
	/** The ScritchUI interface used. */
	protected final ScritchInterface scritch;
	
	/** The mapping of displays. */
	protected final Map<Integer, DisplayState> _displays =
		new LinkedHashMap<>();
	
	/**
	 * Initializes the base tracker.
	 *
	 * @param __scritch The scritch interface to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/03/09
	 */
	private DisplayManager(ScritchInterface __scritch)
		throws NullPointerException
	{
		if (__scritch == null)
			throw new NullPointerException("NARG");
		
		this.scritch = __scritch;
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
		
		throw Debugging.todo();
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
		
		throw Debugging.todo();
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
		
		ScritchInterface scritchApi = this.scritch;
		ScritchEnvironmentInterface envApi = scritchApi.environment();
		ScritchWindowInterface winApi = scritchApi.window();
		ScritchScreenInterface screenApi = scritchApi.screen();
		
		// Are there any actual screens to map?
		ScritchScreenBracket[] screens = envApi.screens();
		if (screens == null || screens.length == 0)
			return new Display[0];
		
		// Resultant displays
		List<Display> result = new ArrayList<>();
		
		// Map any displays which are not yet mapped
		Map<Integer, DisplayState> displays = this._displays;
		for (ScritchScreenBracket screen : screens)
			synchronized (this)
			{
				// Each screen has a unique ID
				Integer id = screenApi.id(screen);
				
				// If the display already exists, use it
				DisplayState display = displays.get(id);
				if (display != null)
				{
					result.add(display.display());
					continue;
				}
				
				// Setup new window for this display
				ScritchWindowBracket window = winApi.newWindow();
				
				// Otherwise it needs to be created
				display = __factory.create(scritchApi, window, screen);
				displays.put(id, display);
				
				// Cache screen
				result.add(display.display());
			}
		
		// Return any attached screen
		return result.toArray(new Display[result.size()]);
	}
	
	/**
	 * Returns the ScritchUI interface in use.
	 *
	 * @return The ScritchUI interface.
	 * @since 2024/03/09
	 */
	public ScritchInterface scritch()
	{
		throw Debugging.todo();
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
		
		instance = new DisplayManager(DefaultScritchInterface.instance());
		DisplayManager._INSTANCE = instance;
		
		return instance;
	}
}
