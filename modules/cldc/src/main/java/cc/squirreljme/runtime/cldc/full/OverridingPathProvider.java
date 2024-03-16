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
 * Overriding path provider, to allow different paths to be used rather
 * than the system implied paths.
 *
 * @since 2024/03/15
 */
public class OverridingPathProvider
	extends SystemPathProvider
{
	/** The provider to wrap. */
	protected final SystemPathProvider provider;
	
	/**
	 * Initializes the provider.
	 *
	 * @param __provider The provider to wrap around.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/03/15
	 */
	public OverridingPathProvider(SystemPathProvider __provider)
		throws NullPointerException
	{
		if (__provider == null)
			throw new NullPointerException("NARG");
		
		this.provider = __provider;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/15
	 */
	@Override
	public Path cache()
	{
		Path replace = OverridingPathProvider.__env(
			"SQUIRRELJME_CACHE_HOME");
		if (replace != null)
			return replace;
		
		return this.provider.cache();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/15
	 */
	@Override
	public Path config()
	{
		Path replace = OverridingPathProvider.__env(
			"SQUIRRELJME_CONFIG_HOME");
		if (replace != null)
			return replace;
		
		return this.provider.config();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/15
	 */
	@Override
	public Path data()
	{
		Path replace = OverridingPathProvider.__env(
			"SQUIRRELJME_DATA_HOME");
		if (replace != null)
			return replace;
		
		return this.provider.data();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/03/15
	 */
	@Override
	public Path state()
	{
		throw Debugging.todo();
	}
	
	/**
	 * Obtains the variable with some transmutation as needed.
	 *
	 * @param __var The variable to get.
	 * @return The resultant variable.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/03/15
	 */
	private static Path __env(String __var)
		throws NullPointerException
	{
		if (__var == null)
			throw new NullPointerException("NARG");
		
		// Get from the system environment, ignore if missing
		String val = RuntimeShelf.systemEnv(__var);
		if (val == null)
			return null;
		
		// Resolve path
		Path result = Paths.get(val);
		
		// Aliased home directory?
		if ((result.getRoot() == null || !result.isAbsolute()) &&
			result.getNameCount() >= 1 &&
			"~".equals(result.getName(0).toString()))
		{
			// Which OS are we on?
			String osName = System.getProperty("os.name").toLowerCase();
			
			// Need to find the home directory
			String homeVal;
			if (osName.contains("windows") || osName.contains("reactos"))
				homeVal = RuntimeShelf.systemEnv("USERPROFILE");
			else
				homeVal = RuntimeShelf.systemEnv("HOME");
			
			// Not valid? Just assume the given path is valid
			if (homeVal == null)
				return result;
			
			// Resolve the final path
			Path home = Paths.get(homeVal);
			return home.resolve(result.subpath(1, result.getNameCount()));
		}
		
		// Use the given path
		return result;
	}
}
