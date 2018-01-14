// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
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
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
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
		// Nothing can be done
		if (__args == null || __args.length <= 0)
			return;
		
		// Load arguments
		List<String> args = new ArrayList<>();
		for (String s : __args)
			args.add(s);
		
		// Reading by stream rather than by block?
		boolean dostream = "-c".equals(args.get(0));
		if (dostream)
			args.remove(0);
		
		// Nothing to be done?
		if (args.isEmpty())
			return;
		
		// Note the variation
		System.err.println((dostream ? "Dumping by stream." :
			"Dumping by block."));
		
		// Filter files?
		Set<String> filter = new HashSet<>();
		for (int i = 1, n = args.size(); i < n; i++)
			filter.add(args.get(i));
		
		// Open all files and dump entries
		String lastentry = null;
		Path inputzip = Paths.get(args.get(0));
		try (FileChannel fc = FileChannel.open(inputzip,
			StandardOpenOption.READ))
		{
			// As a stream
			if (dostream)
				try (ZipStreamReader zsr = new ZipStreamReader(
					Channels.newInputStream(fc)))
				{
					for (;;)
						try (ZipStreamEntry e = zsr.nextEntry())
						{
							// No more entries
							if (e == null)
								break;
							
							__dump(filter, (lastentry = e.name()), e);
						}
				}
			
			// As a block
			else
				try (ZipBlockReader zsr = new ZipBlockReader(
					new FileChannelBlockAccessor(fc)))
				{
					// Go through all entries
					for (ZipBlockEntry entry : zsr)
						try (InputStream in = entry.open())
						{
							__dump(filter, (lastentry = entry.name()), in);
						}
				}
		}
	
		// {@squirreljme.error AX04 Failed to properly read the specified
		// file. (The file name; The last entry read)}
		catch (IOException e)
		{
			throw new RuntimeException(String.format("AX04 %s %s", inputzip,
				lastentry), e);
		}
	}
	
	/**
	 * Dumps the given stream.
	 *
	 * @param __f The filter used.
	 * @param __n The name of the file.
	 * @param __s The stream to dump.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/08/22
	 */
	private static void __dump(Set<String> __f, String __n, InputStream __s)
		throws IOException, NullPointerException
	{
		// Check
		if (__f == null || __n == null || __s == null)
			throw new NullPointerException("NARG");
		
		// Filtered?
		if (!__f.isEmpty() && !__f.contains(__n))
			return;
		System.err.printf("> Dumping `%s`...%n", __n);
		
		// Dump bytes
		try (OutputStream os = new HexDumpOutputStream(System.out))
		{
			for (byte[] buf = new byte[512];;)
			{
				int rc = __s.read(buf);
	
				// EOF?
				if (rc < 0)
					break;
		
				// Dump
				os.write(buf, 0, rc);
			}
		}
	}
}

