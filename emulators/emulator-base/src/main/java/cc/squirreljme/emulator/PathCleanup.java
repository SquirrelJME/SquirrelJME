// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Cleanup for paths.
 *
 * @since 2020/06/22
 */
public final class PathCleanup
	extends Thread
{
	/** The paths to clear. */
	private final Path[] _paths;
	
	/**
	 * Cleans up the paths.
	 * 
	 * @param __paths The paths to clear.
	 * @since 2020/06/22
	 */
	public PathCleanup(Path... __paths)
	{
		this._paths = (__paths == null ? new Path[0] : __paths.clone());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/06/22
	 */
	@Override
	public final void run()
	{
		// Notice
		System.err.println("Cleaning up temporary libraries...");
		
		// Cleanup
		for (Path p : this._paths)
			try
			{
				Files.delete(p);
			}
			catch (IOException ignored)
			{
			}
	}
}
