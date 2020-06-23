// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

/**
 * This class manages the native bindings.
 *
 * @since 2020/02/25
 */
public final class NativeBinding
{
	static
	{
		// Find the library to load
		String libName = System.mapLibraryName("emulator-base");
		
		// Copy resource to the output
		Path tempDir = null,
			libFile = null;
		try (InputStream in = NativeBinding.class.
			getResourceAsStream("/" + libName))
		{
			if (in == null)
				throw new RuntimeException(String.format(
					"Library %s not found in resource.", libName));
			
			// Store the library as a given file
			tempDir = Files.createTempDirectory("squirreljme-lib");
			libFile = tempDir.resolve(libName);
			
			// Write to the disk as we can only load there
			try (OutputStream out = Files.newOutputStream(libFile,
					StandardOpenOption.CREATE,
					StandardOpenOption.TRUNCATE_EXISTING,
					StandardOpenOption.WRITE))
			{
				// Store here
				byte[] buf = new byte[4096];
				for (;;)
				{
					int rc = in.read(buf);
					
					if (rc < 0)
						break;
					
					out.write(buf, 0, rc);
				}
				
				// Make sure it is on the disk
				out.flush();
			}
		}
		catch (IOException e)
		{
			// Try to clear the file
			try
			{
				if (libFile != null)
					Files.delete(libFile);
				
				if (tempDir != null)
					Files.delete(tempDir);
			}
			catch (IOException f)
			{
				e.addSuppressed(f);
			}
			
			throw new RuntimeException("Could not copy native library.", e);
		}
		
		// Attempt cleanup at shutdown.
		Runtime.getRuntime().addShutdownHook(
			new PathCleanup(libFile, tempDir));
		
		// Try loading the library now
		System.load(libFile.toString());
		
		// Bind methods
		if (NativeBinding.__bindMethods() != 0)
			throw new RuntimeException("Could not bind methods!");
	}
	
	/**
	 * Binds methods accordingly.
	 *
	 * @since 2020/02/26
	 */
	private static native int __bindMethods();
}
