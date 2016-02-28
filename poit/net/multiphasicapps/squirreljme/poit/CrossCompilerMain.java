// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.poit;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Objects;
import net.multiphasicapps.squirreljme.zips.StandardZIPFile;

/**
 * Main entry point for the cross compiler using a real host virtual machine.
 *
 * @since 2016/02/26
 */
public class CrossCompilerMain
{
	/**
	 * Main entry point.
	 *
	 * @param __args Program arguments.
	 * @since 2016/02/26
	 */
	public static void main(String... __args)
	{
		// Force arguments to exist
		if (__args == null)
			__args = new String[0];
		
		// Not enough arguments?
		if (__args.length <= 2)
			throw new IllegalArgumentException("Usage: (os) (arch) (JARs...)");
		
		// Get OS and the architecture
		String tos = Objects.requireNonNull(__args[0],
			"No operating system was specified.");
		String tarch = Objects.requireNonNull(__args[1],
			"No architecture was specified.");
		
		// Go through all input JAR files to recompile as a group
		for (int i = 2; i < __args.length; i++)
		{
			// Try opening it
			try (FileChannel fc = FileChannel.open(Paths.get(__args[i]),
				StandardOpenOption.READ))
			{
				// Open zip file
				StandardZIPFile zip = new StandardZIPFile(fc);
				
				if (true)
					throw new Error("TODO");
			}
			
			// Failed to read/write
			catch (IOException ioe)
			{
				throw new RuntimeException(ioe);
			}
			
			throw new Error("TODO");
		}
		
		throw new Error("TODO");
	}
}

