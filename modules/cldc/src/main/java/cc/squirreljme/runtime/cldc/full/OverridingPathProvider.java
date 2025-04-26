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
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Overriding path provider, to allow different paths to be used rather
 * than the system implied paths.
 *
 * @since 2024/03/15
 */
@SquirrelJMEVendorApi
public class OverridingPathProvider
	extends SystemPathProvider
{
	/** The provider to wrap. */
	@SquirrelJMEVendorApi
	protected final SystemPathProvider provider;
	
	/**
	 * Initializes the provider.
	 *
	 * @param __provider The provider to wrap around.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/03/15
	 */
	@SquirrelJMEVendorApi
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
		Path replace = OverridingPathProvider.__envOrProperty(
			"SQUIRRELJME_CACHE_HOME",
			"cc.squirreljme.cache.home");
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
		Path replace = OverridingPathProvider.__envOrProperty(
			"SQUIRRELJME_CONFIG_HOME",
			"cc.squirreljme.config.home");
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
		Path replace = OverridingPathProvider.__envOrProperty(
			"SQUIRRELJME_DATA_HOME",
			"cc.squirreljme.data.home");
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
		Path replace = OverridingPathProvider.__envOrProperty(
			"SQUIRRELJME_STATE_HOME",
			"cc.squirreljme.state.home");
		if (replace != null)
			return replace;
		
		return this.provider.state();
	}
	
	/**
	 * Obtains the path from the system environment.
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
		
		// Resolve and transmute path
		return OverridingPathProvider.__transmute(Paths.get(val));
	}
	
	/**
	 * Obtains a path from an environment variable, falling back to a system
	 * property if not found.
	 *
	 * @param __var The environment variable to get.
	 * @param __property The system property to get, if there was no variable.
	 * @return The resultant path.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/04/23
	 */
	private static Path __envOrProperty(String __var, String __property)
		throws NullPointerException
	{
		if (__var == null)
			throw new NullPointerException("NARG");
		
		// Is there something from the environment?
		Path result = OverridingPathProvider.__env(__var);
		if (result != null)
			return result;
		
		// Otherwise use system property
		return OverridingPathProvider.__property(__property);
	}
	
	/**
	 * Obtains an overridden variable set by a system property.
	 *
	 * @param __property The property to read from.
	 * @return The resultant path.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/04/23
	 */
	private static Path __property(String __property)
		throws NullPointerException
	{
		if (__property == null)
			throw new NullPointerException("NARG");
		
		// Get from the system properties, ignore if missing
		String val = System.getProperty(__property);
		if (val == null)
			return null;
		
		// Resolve and transmute path
		return OverridingPathProvider.__transmute(Paths.get(val));
	}
	
	/**
	 * Transmutes the path to handle home directories.
	 *
	 * @param __path The input path.
	 * @return The resultant path, the home directory is aliased accordingly.
	 * @throws NullPointerException On null arguments.
	 * @since 2025/04/23
	 */
	private static Path __transmute(Path __path)
		throws NullPointerException
	{
		if (__path == null)
			throw new NullPointerException("NARG");
		
		// Aliased home directory?
		if (!((__path.getRoot() == null || !__path.isAbsolute()) &&
			__path.getNameCount() >= 1 &&
			"~".equals(__path.getName(0).toString())))
			return __path;
		
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
			return __path;
		
		// Resolve the final path
		Path home = Paths.get(homeVal);
		return home.resolve(__path.subpath(1, __path.getNameCount()));
	}
}
