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
				try (FileChannel fc = FileChannel.open(p,
					StandardOpenOption.READ);
					Reader r = new InputStreamReader(
						Channels.newInputStream(fc)))
				{
					configs[i] = new SimulatorConfiguration(r);
				}
			}
			
			// Ignore
			catch (InvalidPathException|IOException e)
			{
			}
			
			throw new Error("TODO");
		}
		
		throw new Error("TODO");
	}
}

