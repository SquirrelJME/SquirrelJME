// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.standalone.hosted;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Performs cleanup of the JARs on the filesystem so they are not polluted.
 *
 * @since 2023/12/03
 */
public class HostedCleanup
	implements Runnable
{
	/** The path to clean up. */
	protected final Path tempJars;
	
	/**
	 * Initializes the cleanup handler.
	 *
	 * @param __tempJars The path to clean.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/12/03
	 */
	public HostedCleanup(Path __tempJars)
		throws NullPointerException
	{
		if (__tempJars == null)
			throw new NullPointerException("NARG");
		
		this.tempJars = __tempJars;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2023/12/03
	 */
	@Override
	public void run()
	{
		// Note
		Debugging.debugNote("Cleaning up...");
		
		try
		{
			// Where is cleanup happening?
			Path tempJars = this.tempJars;
			
			// List directory contents
			for (Path jar : Files.list(tempJars).toArray(Path[]::new))
				try
				{
					Files.delete(jar);
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			
			// Delete final directory
			Files.delete(tempJars);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
