// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirrelsim;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.file.InvalidPathException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;

/**
 * This is the main entry point for the SquirrelJME simulator.
 *
 * @since 2016/06/14
 */
public class Main
{
	/**
	 * Main entry point for the simulator.
	 *
	 * @param __args Program arguments.
	 * @since 2016/06/14
	 */
	public static void main(String... __args)
	{
		// Must exist
		if (__args == null)
			__args = new String[0];
		
		// {@squirreljme.error BV01 Expected configuration files to be used
		// for the simulators.}
		int n;
		if ((n = __args.length) <= 0)
			throw new IllegalArgumentException("BV01");
		
		// Setup simulator configurations for all systems
		SimulatorConfiguration[] configs = new SimulatorConfiguration[n];
		for (int i = 0; i < n; i++)
		{
			// Get configuration name
			String conf = __args[i];
			
			// See if it exists in the filesystem
			try
			{
				// Get path
				Path p = Paths.get(conf);
				
				// Try to use it
				if (Files.exists(p))
					try (FileChannel fc = FileChannel.open(p,
						StandardOpenOption.READ);
						InputStream is = Channels.newInputStream(fc);
						Reader r = new InputStreamReader(is))
					{
						configs[i] = new SimulatorConfiguration(r);
					
						// Do not try a resource next
						continue;
					}
			}
			
			// Treat as if it did not exist
			catch (InvalidPathException e)
			{
			}
			
			// {@squirreljme.error BV03 Could not read the configuration file
			// at the specified file system path. (The configuration path)}
			catch (IOException e)
			{
				throw new IllegalArgumentException(String.format("BV03 %s",
					conf), e);
			}
			
			// Try from a JAR resource
			try (InputStream is = SimulationProvider.getResourceAsStream(conf))
			{
				// {@squirreljme.error BV04 No file or resource exists with
				// the given name.}
				if (is == null)
					throw new IOException("BV04");
				
				// Treat as UTF-8
				try (Reader r = new InputStreamReader(is, "utf-8"))
				{
					configs[i] = new SimulatorConfiguration(r);
				}
			}
			
			// {@squirreljme.error BV02 Could not read configuration from the
			// JAR resource. (The resource name)}
			catch (IOException e)
			{
				throw new IllegalArgumentException(String.format("BV02 %s",
					conf), e);
			}
		}
		
		// Setup simulation group which uses the specified configurations to
		// initialize simulations with.
		SimulationGroup sg = new SimulationGroup(configs);
		
		// Run cycles until complete
		while (sg.runCycle())
			;
	}
}

