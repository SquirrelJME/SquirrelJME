// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.dumpzip;

import java.io.InputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashSet;
import java.util.Set;
import net.multiphasicapps.io.hexdumpstream.HexDumpOutputStream;
import net.multiphasicapps.zip.blockreader.ZipBlockEntry;
import net.multiphasicapps.zip.blockreader.ZipBlockReader;
import net.multiphasicapps.zip.streamreader.ZipStreamEntry;
import net.multiphasicapps.zip.streamreader.ZipStreamReader;

/**
 * This dumps ZIP files to standard output.
 *
 * @since 2017/03/01
 */
public class Main
{
	/**
	 * Main entry point.
	 *
	 * @param __args Program arguments.
	 * @since 2017/03/01
	 */
	public static void main(String... __args)
	{
		// Do nothing
		if (__args == null)
			return;
		
		// Filter files?
		Set<String> filter = new HashSet<>();
		for (int i = 1, n = __args.length; i < n; i++)
			filter.add(__args[i]);
		
		// Open all files and dump entries
		byte[] buf = new byte[512];
		String lastentry = null;
		try (ZipBlockReader zsr = new ZipBlockReader(
			new FileChannelBlockAccessor(FileChannel.open(Paths.get(__args[0]),
			StandardOpenOption.READ))))
		{
			// Go through all entries
			for (ZipBlockEntry entry : zsr)
			{
				// Filtered?
				String name = entry.name();
				if (!filter.isEmpty() && !filter.contains(name))
					continue;
				System.out.printf("> Dumping `%s`...%n", name);
				lastentry = name;
				
				// Read it
				try (InputStream in = entry.open())
				{
					// Dump bytes
					try (OutputStream os = new HexDumpOutputStream(
						System.out))
					{
						for (;;)
						{
							int rc = in.read(buf);
						
							// EOF?
							if (rc < 0)
								break;
							
							// Dump
							os.write(buf, 0, rc);
						}
					}
				}
			}
		}
	
		// {@squirreljme.error AX01 Failed to properly read the specified
		// file. (The file name; The last entry read)}
		catch (IOException e)
		{
			throw new RuntimeException(String.format("AX01 %s %s", __args[0],
				lastentry), e);
		}
	}
}

