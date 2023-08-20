// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package java.nio.file;

import cc.squirreljme.jvm.DriverFactory;
import cc.squirreljme.jvm.DriverLoader;
import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import cc.squirreljme.runtime.cldc.full.NullFileSystem;
import java.util.NoSuchElementException;

/**
 * This contains a static method which is used to obtain the default filesystem
 * which is used by SquirrelJME's native filesystem interface.
 *
 * @since 2017/06/12
 */
@Api
public final class FileSystems
{
	/** Instance of the file system driver. */
	private static volatile FileSystem _INSTANCE;
	
	/**
	 * Not used.
	 *
	 * @since 2017/06/12
	 */
	private FileSystems()
	{
	}
	
	/**
	 * This returns the default filesystem which is used by the system to
	 * access the filesystem.
	 *
	 * @return The system's default filesystem.
	 * @since 2017/06/12
	 */
	@Api
	public static FileSystem getDefault()
	{
		// Already loaded?
		FileSystem instance = FileSystems._INSTANCE;
		if (instance != null)
			return instance;
		
		// Load in the driver
		try
		{
			instance = DriverLoader.loadBest(FileSystem.class);
		}
		catch (NoSuchElementException ignored)
		{
		}
		
		// If no driver was found, setup blank no-op filesystem
		if (instance == null)
			instance = NullFileSystem.INSTANCE;
		
		// Cache for later and use it now
		FileSystems._INSTANCE = instance;
		return instance;
	}
}

