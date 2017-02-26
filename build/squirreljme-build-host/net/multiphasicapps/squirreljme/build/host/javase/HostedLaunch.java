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
			
		// DEBUG -- Remove this
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try (InflaterInputStream in = new InflaterInputStream(
			new HexInputStream(new ByteArrayInputStream((
"7d525d4fd360147e5ed6ad5dadda0d068aa07c6a87c04451d12228bb6109892624238b579535a3"+
"a46b97da69fc09defa4bd48b8eb8c41fe08f329ed3762e0c422fce79cf799fe739e7bca77ffefe"+
"fa0d6013551525dc95714fc518fb391512e6395850b0c87e49c6b28a3ceeb379c019834d398f15"+
"3c54318155366b2ad65151f048c1868cc7329e08e4b61dcf0977043246b92e2055fda62d903db1"+
"5dd7179830ca07a7d627abe25a5eab7218068ed73209d5b61c4f60d2787ff19655b29ffdc06d6a"+
"98c494c0d42864afebb84d3b10181fde544facc03a0eed80499b1a9ee219b5444df0f1b9406108"+
"ad79a1dd4a805b1a5ee025434c0ddb9896f14ac30e7635dcc6b48659cebcd6f0067b02e28804a9"+
"2b01759f873bf2e3401f0abffd706a1f87349d51e5217256a7637b0459330e4641e6c56749a732"+
"47f08357b9022f1935aea7385e58b7dcae1defa22630437d5c4153423f499d1be2f0cbc7d06e93"+
"84dfa5514a09dff12bef081912deb6dae6e0e5cfa705e40e472e6db674d90875ccd34f54027f12"+
"046f97ec2d8a0ae405f9ec4a0fe2470cc8f30ad2eb2d82f3b7dac758a3874c1f5243dfef211b21"+
"579423287de41bbadf831ae1dab7085a84ebdf63c657dcc14caab28e4c5c64e1276ea44267b849"+
"6c7d8e2867d0e948c408854107b3ff3bd8483b584e89c5e225558be38942521aff00").getBytes()))))
		{
			byte buf[] = new byte[32];
			for (;;)
			{
				int rv = in.read(buf);
				if (rv < 0)
					break;
				baos.write(buf, 0, rv);
			}
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
		
		try (InputStream in = new ByteArrayInputStream(baos.toByteArray()))
		{
			System.err.println("<<<");
			for (;;)
			{
				int rv = in.read();
				if (rv < 0)
					break;
				System.err.printf("%02x", rv);
			}
			System.err.println(">>>");
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
		
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

