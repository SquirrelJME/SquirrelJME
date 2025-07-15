// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.standalone.hosted;

import cc.squirreljme.jvm.suite.SuiteUtils;
import cc.squirreljme.runtime.cldc.util.StreamUtils;
import cc.squirreljme.vm.DataContainerLibrary;
import cc.squirreljme.vm.VMClassLibrary;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Utilities for hosted Jars.
 *
 * @since 2025/07/14
 */
public final class HostedUtils
{
	
	/**
	 * Transforms a resource based class library into an actual Jar that
	 * standard virtual machines can use.
	 *
	 * @param __tempJars The path where the JAR should be created.
	 * @param __lib The library to transform.
	 * @param __libName The library name.
	 * @return The resultant path of the library.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/12/03
	 */
	public static Path implodeJar(Path __tempJars,
		VMClassLibrary __lib, String __libName)
		throws IOException, NullPointerException
	{
		if (__tempJars == null || __lib == null || __libName == null)
			throw new NullPointerException("NARG");
		
		// The target path of the output Jar, always end in JAR
		Path resultPath = __tempJars.resolve(
			((SuiteUtils.isAny(__libName) ?
				__libName : __libName + ".jar")));
		
		// Buffer to use for copying data
		byte[] tempBuf = new byte[1048576];
		
		// Extract library
		Path temp = null;
		try
		{
			// Setup new file to write to
			temp = Files.createTempFile("implode", ".jar");
			
			// Copy all the Zip entries accordingly
			try (OutputStream out = Files.newOutputStream(temp,
				StandardOpenOption.CREATE, StandardOpenOption.WRITE,
				StandardOpenOption.TRUNCATE_EXISTING))
			{
				// Just a resource file?
				String[] rcList = __lib.listResources();
				if (rcList.length == 1 &&
					DataContainerLibrary.RESOURCE_NAME.equals(rcList[0]))
				{
					try (InputStream in = __lib.resourceAsStream(rcList[0]))
					{
						StreamUtils.copy(in, out, tempBuf);
					}
				}
				
				// From a Zip
				else
				{
					try (ZipOutputStream zip = new ZipOutputStream(out))
					{
						// Copy all resource data
						for (String rcName : rcList)
						{
							// Setup ZIP entry
							ZipEntry entry = new ZipEntry(rcName);
							zip.putNextEntry(entry);
							
							// Write to it all
							try (InputStream in = __lib.resourceAsStream(
								rcName))
							{
								StreamUtils.copy(in, zip, tempBuf);
							}
							
							// Finish it
							zip.closeEntry();
						}
						
						// Finish the Zip
						zip.finish();
						zip.flush();
					}
				}
			}
			
			// Replace the target file
			Files.move(temp, resultPath,
				StandardCopyOption.REPLACE_EXISTING);
			
			// Use this path
			return resultPath;
		}
		finally
		{
			if (temp != null)
				try
				{
					Files.delete(temp);
				}
				catch (IOException ignored)
				{
				}
		}
	}
}
