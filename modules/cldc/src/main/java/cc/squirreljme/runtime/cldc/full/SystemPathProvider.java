// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.full;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Provider for system paths.
 *
 * @since 2024/02/25
 */
public abstract class SystemPathProvider
{
	/**
	 * Returns the cache path or {@code null} if there is none.
	 *
	 * @return The cache path.
	 * @since 2024/02/25
	 */
	public abstract Path cache();
	
	/**
	 * Returns the config path or {@code null} if there is none.
	 *
	 * @return The config path.
	 * @since 2024/02/25
	 */
	public abstract Path config();
	
	/**
	 * Returns the data path or {@code null} if there is none.
	 *
	 * @return The cache path.
	 * @since 2024/02/25
	 */
	public abstract Path data();
	
	/**
	 * Returns the state path or {@code null} if there is none.
	 *
	 * @return The state path.
	 * @since 2024/02/25
	 */
	public abstract Path state();
	
	/**
	 * Returns the path of the given system path type.
	 *
	 * @param __path The path to get.
	 * @return The resultant path.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/02/25
	 */
	public final Path of(SystemPath __path)
		throws NullPointerException
	{
		if (__path == null)
			throw new NullPointerException("NARG");
		
		switch (__path)
		{
			case CACHE:
				return this.cache();
			
			case CONFIG:
				return this.config();
			
			case DATA:
				return this.data();
				
			case STATE:
				return this.state();
		}
		
		return null;
	}
	
	/**
	 * Returns the path of the given system path type or a fallback if it
	 * could not be found.
	 *
	 * @param __path The path to get.
	 * @return The resultant path.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/02/25
	 */
	public final Path ofFallback(SystemPath __path)
		throws NullPointerException
	{
		if (__path == null)
			throw new NullPointerException("NARG");
		
		// If a path is known use it
		Path path = this.of(__path);
		if (path != null)
			return path;
		
		// Use current directory instead
		Path userDir = Paths.get(System.getProperty("user.dir"));
		return userDir.resolve("squirreljme")
			.resolve(__path.name().toLowerCase());
	}
	
	/**
	 * Returns the path provider for the system.
	 *
	 * @return The system path provider.
	 * @since 2024/02/25
	 */
	public static SystemPathProvider provider()
	{
		String osName = System.getProperty("os.name")
			.toLowerCase();
		String osVersion = System.getProperty("os.version")
			.toLowerCase();
		
		// Windows
		if (osName.contains("windows") || osName.contains("reactos"))
			return new OverridingPathProvider(new WindowsPathProvider());
		
		// Fallback to Unix
		return new OverridingPathProvider(new UnixPathProvider());
	}
}
