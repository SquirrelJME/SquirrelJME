// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.sprintpcs.util;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.midlet.CleanupHandler;

/**
 * Spring System Utilities.
 *
 * @since 2022/08/28
 */
@Api
public class System
{
	/**
	 * Adds a listener for system related events.
	 * 
	 * @param __listener The listener used.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/08/28
	 */
	@Api
	public static void addSystemListener(SystemEventListener __listener)
		throws NullPointerException
	{
		if (__listener == null)
			throw new NullPointerException("NARG");
		
		// TODO: Currently there are no known events where this would make
		// TODO: sense, add when they are known...
		Debugging.todoNote("Add listeners...");
	}
	
	@Api
	public static String[] getPropertiesList()
	{
		throw Debugging.todo();
	}
	
	@Api
	public static String getProtectedProperty(String __key)
		throws SecurityException
	{
		throw Debugging.todo();
	}
	
	/**
	 * Returns the state of the given option.
	 * 
	 * @param __option The option to check.
	 * @return The state of the given option.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/08/28
	 */
	@Api
	public static String getSystemState(String __option)
		throws NullPointerException
	{
		if (__option == null)
			throw new NullPointerException("NARG");
		
		switch (__option)
		{
				// The form factor of the device, i.e. is the display flipped
				// open ("OPEN") or is it closed ("CLOSED")?
				// SquirrelJME currently only ever has the display opened...
			case "sprint.device.formfactor":
				return "OPEN";
			
			default:
				throw Debugging.todo(__option);
		}
	}
	
	@Api
	public static void promptMasterVolume()
	{
		throw Debugging.todo();
	}
	
	/**
	 * When the system exits, the specified URL will be activated in the
	 * browser.
	 *
	 * @param __uri The URL to execute on exit.
	 * @since 2022/08/28
	 */
	@Api
	public static void setExitURI(String __uri)
	{
		if (__uri != null)
			CleanupHandler.add(new __ExitUri__(__uri));
	}
	
	@Api
	public static void setSystemSetting(String __key, String __value)
		throws SecurityException, IllegalArgumentException, NullPointerException
	{
		throw Debugging.todo();
	}
}
