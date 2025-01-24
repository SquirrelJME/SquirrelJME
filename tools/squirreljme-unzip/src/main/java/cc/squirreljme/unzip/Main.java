// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.unzip;

import cc.squirreljme.runtime.cldc.io.HexDumpOutputStream;
import cc.squirreljme.runtime.cldc.util.StreamUtils;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import net.multiphasicapps.zip.streamreader.ZipStreamEntry;
import net.multiphasicapps.zip.streamreader.ZipStreamReader;

/**
 * Unzip program.
 *
 * @since 2023/12/30
 */
public class Main
{
	/**
	 * Describe this. 
	 *
	 * @param __args Program arguments.
	 * @throws IOException On read/write errors.
	 * @since 2023/12/30
	 */
	public static void main(String... __args)
		throws IOException
	{
		Path inZip;
		Path outPath;
		
		// Banner
		System.err.println("SquirrelJME UnZip");
		System.err.println("    Copyright (C) Stephanie Gawroriski " +
			"<xer@multiphasicapps.net>");
		System.err.println("------------------------------------------------" +
			"---------------------------");
		System.err.println("SquirrelJME is under the Mozilla Public License " +
			"Version 2.0.");
		System.err.println("See license.mkd for licensing and copyright " +
			"information.");
		System.err.println("------------------------------------------------" +
			"---------------------------");
		
		// Optional input and output
		if (__args.length == 2)
		{
			inZip = Paths.get(__args[0]);
			
			if (__args[1].equals("-"))
				outPath = null;
			else
				outPath = Paths.get(__args[1]);
		}
		else if (__args.length == 1)
		{
			inZip = null;
			
			if (__args[0].equals("-"))
				outPath = null;
			else
				outPath = Paths.get(__args[0]);
		}
		else
		{
			System.err.println("Usage: [source.zip] (-|outDir)");
			System.err.println("\toutDir of - will hexdump.");
			System.err.println("\tIf source.zip not specified will read " +
				"from stdin.");
			
			throw new IllegalArgumentException("Incorrect arguments.");
		}
		
		// Read input ZIP
		byte[] buf = new byte[4096];
		try (InputStream in = (inZip == null ? System.in :
			Files.newInputStream(inZip, StandardOpenOption.READ));
			 ZipStreamReader zip = new ZipStreamReader(in))
		{
			for (;;)
			{
				System.err.printf("Scanning...%n");
				
				try (ZipStreamEntry entry = zip.nextEntry())
				{
					if (entry == null)
						break;
					
					// Dump info on it
					System.err.printf("Entry: %s%n", entry.name());
					
					if (outPath == null)
						try (HexDumpOutputStream hex = new HexDumpOutputStream(
							System.out))
						{
							System.out.printf(">>> %s%n", entry.name());
							StreamUtils.copy(entry, hex, buf);
						}
					else
					{
						Path dest = outPath.resolve(entry.name());
						Files.createDirectories(dest.getParent());
						
						try (OutputStream wr = Files.newOutputStream(dest,
							StandardOpenOption.WRITE,
							StandardOpenOption.CREATE,
							StandardOpenOption.TRUNCATE_EXISTING))
						{
							StreamUtils.copy(entry, wr, buf);
							wr.flush();
						}
					}
				}
			}
		}
	}
}
