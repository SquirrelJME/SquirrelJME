// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.build.host.javase;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import net.multiphasicapps.io.hex.HexInputStream;
import net.multiphasicapps.io.inflate.InflaterInputStream;
import net.multiphasicapps.squirreljme.build.projects.FileChannelBlockAccessor;
import net.multiphasicapps.squirreljme.java.manifest.JavaManifest;
import net.multiphasicapps.squirreljme.java.manifest.JavaManifestAttributes;
import net.multiphasicapps.squirreljme.java.manifest.JavaManifestKey;
import net.multiphasicapps.zip.blockreader.ZipBlockEntry;
import net.multiphasicapps.zip.blockreader.ZipBlockReader;

/**
 * This is a hosted launch which runs the first MIDlet detected.
 *
 * @since 2017/02/24
 */
public class HostedLaunch
{
	/**
	 * Main entry point.
	 *
	 * @param __args Program arguments.
	 * @since 2017/02/24
	 */
	public static void main(String... __args)
	{
		// Initialize if missing
		if (__args == null)
			__args = new String[0];
		
		// {@squirreljme.error BM0b Insufficient number of arguments.}
		if (__args.length < 1)
			throw new IllegalArgumentException("BM0b");
		
		// Load the target ZIP to the read manifest
		Path p = Paths.get(__args[0]);
		try (FileChannel fc = FileChannel.open(p, StandardOpenOption.READ);
			ZipBlockReader zip = new ZipBlockReader(
				new FileChannelBlockAccessor(fc)))
		{
			// Read the manifest
			ZipBlockEntry entry = zip.get("META-INF/MANIFEST.MF");
			JavaManifest man;
			try (InputStream in = entry.open())
			{
				man = new JavaManifest(in);
			}
			JavaManifestAttributes attr = man.getMainAttributes();
			
			// {@squirreljme.error BM0d The JAR to launch does not specify any
			// midlets.}
			String midletval = attr.get(new JavaManifestKey("MIDlet-1"));
			if (midletval == null)
				throw new RuntimeException("BM0d");
			
			throw new Error("TODO");
		}
		
		// {@squirreljme.error BM0c Failed to read the manifest.}
		catch (IOException e)
		{
			throw new RuntimeException("BM0c", e);
		}
	}
}

