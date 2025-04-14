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
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Provides Unix Paths.
 *
 * @since 2024/02/25
 */
public class UnixPathProvider
	extends SystemPathProvider
{
	/**
	 * {@inheritDoc}
	 * @since 2024/02/25
	 */
	@Override
	public Path cache()
	{
		Path path = UnixPathProvider.__env("XDG_CACHE_HOME",
			".cache");
		if (path != null)
			return path.resolve("squirreljme");
		
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/02/25
	 */
	@Override
	public Path config()
	{
		Path path = UnixPathProvider.__env("XDG_CONFIG_HOME",
			".config");
		if (path != null)
			return path.resolve("squirreljme");
		
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/02/25
	 */
	@Override
	public Path data()
	{
		Path path = UnixPathProvider.__env("XDG_DATA_HOME",
			".local/share");
		if (path != null)
			return path.resolve("squirreljme");
		
		return null;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/02/25
	 */
	@Override
	public Path state()
	{
		Path path = UnixPathProvider.__env("XDG_STATE_HOME",
			".local/state");
		if (path != null)
			return path.resolve("squirreljme");
		
		return null;
	}
	
	/**
	 * Returns a value from the environment variable or the default.
	 *
	 * @param __var The variable to check.
	 * @param __fromHome The path from the home directory.
	 * @return The resultant path or {@code null} if there is none.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/02/25
	 */
	private static Path __env(String __var, String __fromHome)
		throws NullPointerException
	{
		if (__var == null || __fromHome == null)
			throw new NullPointerException("NARG");
		
		// From variable
		String xdg = RuntimeShelf.systemEnv(__var);
		if (xdg != null)
			return Paths.get(xdg);
		
		// Home directory
		String homeDir = RuntimeShelf.systemEnv("HOME");
		if (homeDir != null)
			return Paths.get(homeDir).resolve(__fromHome);
		
		return null;
	}
}
