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
import cc.squirreljme.jvm.mle.constants.VMDescriptionType;
import cc.squirreljme.jvm.mle.exceptions.MLECallError;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.intellij.lang.annotations.MagicConstant;

/**
 * Provider for system paths.
 *
 * @since 2024/02/25
 */
@SquirrelJMEVendorApi
public final class SystemPathProvider
{
	/** The single instance. */
	private static volatile SystemPathProvider _INSTANCE;
	
	/**
	 * Not used.
	 *
	 * @since 2026/01/16
	 */
	private SystemPathProvider()
	{
	}
	
	/**
	 * Returns the bucket data path.
	 *
	 * @return The bucket data path.
	 * @since 2026/01/16
	 */
	@SquirrelJMEVendorApi
	public Path bucketData()
	{
		return SystemPathProvider.__vmDesc(
			VMDescriptionType.DEFAULT_DIR_BUCKET_DATA);
	}
	
	/**
	 * Returns the cache path or {@code null} if there is none.
	 *
	 * @return The cache path.
	 * @since 2024/02/25
	 */
	@SquirrelJMEVendorApi
	public Path cache()
	{
		return SystemPathProvider.__vmDesc(
			VMDescriptionType.DEFAULT_DIR_CACHE);
	}
	
	/**
	 * Returns the config path or {@code null} if there is none.
	 *
	 * @return The config path.
	 * @since 2024/02/25
	 */
	@SquirrelJMEVendorApi
	public Path config()
	{
		return SystemPathProvider.__vmDesc(
			VMDescriptionType.DEFAULT_DIR_CONFIG);
	}
	
	/**
	 * Returns the data path or {@code null} if there is none.
	 *
	 * @return The cache path.
	 * @since 2024/02/25
	 */
	@SquirrelJMEVendorApi
	public Path data()
	{
		return SystemPathProvider.__vmDesc(
			VMDescriptionType.DEFAULT_DIR_DATA);
	}
	
	/**
	 * Returns the path where libraries exist.
	 *
	 * @return The library paths.
	 * @since 2026/01/16
	 */
	@SquirrelJMEVendorApi
	public Path libraries()
	{
		return SystemPathProvider.__vmDesc(
			VMDescriptionType.DEFAULT_DIR_LIBRARIES);
	}
	
	/**
	 * Returns the state path or {@code null} if there is none.
	 *
	 * @return The state path.
	 * @since 2024/02/25
	 */
	@SquirrelJMEVendorApi
	public Path state()
	{
		return SystemPathProvider.__vmDesc(
			VMDescriptionType.DEFAULT_DIR_STATE);
	}
	
	/**
	 * Returns the path of the given system path type.
	 *
	 * @param __path The path to get.
	 * @return The resultant path.
	 * @throws NullPointerException On null arguments.
	 * @deprecated Do not use, only used by the debugger which will be
	 * rewritten at some point.
	 * @since 2024/02/25
	 */
	@SquirrelJMEVendorApi
	@Deprecated
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
	 * @deprecated Do not use, only used by the debugger which will be
	 * rewritten at some point.
	 * @since 2024/02/25
	 */
	@SquirrelJMEVendorApi
	@Deprecated
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
	@SquirrelJMEVendorApi
	public static SystemPathProvider provider()
	{
		// Already exists?
		SystemPathProvider rv = SystemPathProvider._INSTANCE;
		if (rv != null)
			return rv;
		
		// Create one and cache it 
		rv = new SystemPathProvider();
		SystemPathProvider._INSTANCE = rv;
		return rv;
	}
	
	/**
	 * Returns the path for the given ID.
	 *
	 * @param __id The ID to get.
	 * @return The resultant path, or {@code null} if it does not exist or
	 * it is not valid.
	 * @since 2026/01/16
	 */
	private static Path __vmDesc(
		@MagicConstant(valuesFromClass = VMDescriptionType.class) int __id)
	{
		if (__id <= VMDescriptionType.DEFAULT_DIR_UNKNOWN ||
			__id >= VMDescriptionType.DEFAULT_DIR_NUM_TYPES)
			return null;
		
		// Ignore any failures
		try
		{
			// Get the native path through NanoCoat
			String desc = RuntimeShelf.vmDescription(__id);
			if (desc == null)
				return null;
			
			// Parse it
			return Paths.get(desc);
		}
		catch (InvalidPathException|MLECallError ignored)
		{
			return null;
		}
	}
}
