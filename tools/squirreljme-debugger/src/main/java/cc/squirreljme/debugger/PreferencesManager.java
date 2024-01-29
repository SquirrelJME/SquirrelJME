// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.debugger;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;

/**
 * Manager for preferences.
 *
 * @since 2024/01/28
 */
public class PreferencesManager
{
	/** The path where the configuration is. */
	protected final Path path;
	
	/**
	 * Initializes the preference manager.
	 *
	 * @since 2024/01/28
	 */
	public PreferencesManager()
	{
		this.path = PreferencesManager.baseDirectory()
			.resolve("debugger.json");
	}
	
	/**
	 * Loads the given preferences.
	 *
	 * @param __prefs The preferences to load.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/28
	 */
	public void load(Preferences __prefs)
		throws NullPointerException
	{
		if (__prefs == null)
			throw new NullPointerException("NARG");
	}
	
	/**
	 * Stores the given preferences.
	 *
	 * @param __prefs The preferences to store.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/28
	 */
	public void store(Preferences __prefs)
		throws NullPointerException
	{
		if (__prefs == null)
			throw new NullPointerException("NARG");
	}
	
	/**
	 * The base program directory for preference storage.
	 *
	 * @return The base program directory for preference storage.
	 * @since 2024/01/28
	 */
	public static Path baseDirectory()
	{
		return PreferencesManager.baseSystemDirectory()
			.resolve("squirreljme");
	}
	
	/**
	 * The base system directory for preference storage.
	 *
	 * @return The base system directory for preference storage.
	 * @since 2024/01/28
	 */
	public static Path baseSystemDirectory()
	{
		// Need to know which OS we are on
		String osName = System.getProperty("os.name")
			.toLowerCase(Locale.ROOT);
		String osVersion = System.getProperty("os.version")
			.toLowerCase(Locale.ROOT);
		
		// Windows
		if (osName.contains("windows") || osName.contains("reactos"))
		{
			// Where do we shove our data?
			String appDataEnv;
			if (osName.contains("95") || osName.contains("98") ||
				osName.contains("me"))
				appDataEnv = System.getenv("APPDATA");
			else
				appDataEnv = System.getenv("LOCALAPPDATA");
			
			// Resolve it
			if (appDataEnv != null)
				return Paths.get(appDataEnv);
			
			// Fallback here for Windows
			String programData = System.getenv("PROGRAMDATA");
			if (programData != null)
				return Paths.get(programData);
		}
		
		// Everything else
		else
		{
			// XDG Configuration
			String xdg = System.getenv("XDG_CONFIG_HOME");
			if (xdg != null)
				return Paths.get(xdg);
			
			// Home directory
			String homeDir = System.getenv("HOME");
			if (homeDir != null)
				return Paths.get(homeDir);
		}
		
		// Unknown, assume home, then fallback to pwd otherwise
		String homeDir = System.getProperty("user.home");
		if (homeDir != null)
			return Paths.get(homeDir);
		return Paths.get("user.dir");
	}
}
