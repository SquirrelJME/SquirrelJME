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
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import net.multiphasicapps.io.hexdumpstream.HexDumpOutputStream;
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
		if (__args == null || __args.length <= 0)
			return;
		
		// Open all files and dump entries
		byte[] buf = new byte[512];
		for (String arg : __args)
			try (ZipStreamReader zsr = new ZipStreamReader(
				Channels.newInputStream(FileChannel.open(Paths.get(arg),
				StandardOpenOption.READ))))
			{
				// Dump all entries
				for (;;)
					try (ZipStreamEntry entry = zsr.nextEntry())
					{
						// No more?
						if (entry == null)
							break;
						
						// Note it
						System.out.printf("> Dumping `%s`...%n", entry.name());
						
						// Dump bytes
						try (OutputStream os = new HexDumpOutputStream(
							System.out))
						{
							for (;;)
							{
								int rc = os.read(buf);
							
								// EOF?
								if (rc < 0)
									break;
								
								// Dump
								os.write(buf, 0, rc);
							}
						}
					}
			}
		
			// {@squirreljme.error AX01 Failed to properly read the specified
			// file. (The file name)}
			catch (IOException e)
			{
				throw new RuntimeException(String.format("AX01 %s", arg), e);
			}
	}
}

