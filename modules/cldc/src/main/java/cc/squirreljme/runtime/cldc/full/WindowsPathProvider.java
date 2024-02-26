// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.full;

import cc.squirreljme.jvm.mle.RuntimeShelf;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Provides Windows paths.
 *
 * @since 2024/02/25
 */
public class WindowsPathProvider
	extends SystemPathProvider
{
	/**
	 * {@inheritDoc}
	 * @since 2024/02/25
	 */
	@Override
	public Path cache()
	{
		Path appData = WindowsPathProvider.__appData(false);
		if (appData != null)
			return appData.resolve("squirreljme")
				.resolve("cache");
		
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/02/25
	 */
	@Override
	public Path config()
	{
		Path appData = WindowsPathProvider.__appData(true);
		if (appData != null)
			return appData.resolve("squirreljme")
				.resolve("config");
		
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/02/25
	 */
	@Override
	public Path data()
	{
		Path appData = WindowsPathProvider.__appData(true);
		if (appData != null)
			return appData.resolve("squirreljme")
				.resolve("data");
		
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/02/25
	 */
	@Override
	public Path state()
	{
		Path appData = WindowsPathProvider.__appData(false);
		if (appData != null)
			return appData.resolve("squirreljme")
				.resolve("state");
		
		return null;
	}
	
	/**
	 * Returns the application data path.
	 *
	 * @param __roaming Get the roaming application data?
	 * @return The application data path.
	 * @since 2024/02/25
	 */
	private static Path __appData(boolean __roaming)
	{
		String osName = System.getProperty("os.name").toLowerCase();
		
		// Where do we shove our data?
		String appDataEnv;
		if (__roaming || osName.contains("95") ||
			osName.contains("98") || osName.contains("me"))
			appDataEnv = RuntimeShelf.systemEnv("APPDATA");
		else
			appDataEnv = RuntimeShelf.systemEnv("LOCALAPPDATA");
		
		// Resolve it
		if (appDataEnv != null)
			return Paths.get(appDataEnv);
		
		// Fallback here for Windows
		String programData = RuntimeShelf.systemEnv("PROGRAMDATA");
		if (programData != null)
			return Paths.get(programData);
		
		// Unknown
		return null;
	}
}
