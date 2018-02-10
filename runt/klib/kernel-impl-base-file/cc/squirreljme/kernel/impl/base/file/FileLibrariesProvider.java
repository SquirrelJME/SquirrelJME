// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.kernel.impl.base.file;

import cc.squirreljme.kernel.lib.server.LibrariesProvider;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * This provides access to installed libraries which have been compiled and
 * stores on the filesystem using the standard SquirrelJME storage areas.
 *
 * @since 2018/01/13
 */
public abstract class FileLibrariesProvider
	extends LibrariesProvider
{
	/** The base path for installed JAR files. */
	protected final Path librarypath;
	
	/**
	 * Initializes the file library provider.
	 *
	 * @since 2018/01/03
	 */
	public FileLibrariesProvider()
	{
		this(StandardPaths.DEFAULT);
	}
	
	/**
	 * Initializes the file library provider using the given paths.
	 *
	 * @param __sp The paths to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/01/31
	 */
	public FileLibrariesProvider(StandardPaths __sp)
		throws NullPointerException
	{
		if (__sp == null)
			throw new NullPointerException("NARG");
		
		Path librarypath;
		this.librarypath = (librarypath = __sp.libraryPath());
		
		// Scan the directory for installed libararies
		try (DirectoryStream<Path> ds = Files.newDirectoryStream(librarypath))
		{
			for (Path p : ds)
			{
				// Only consider files	
				if (!Files.isRegularFile(p))
					continue;
				
				throw new todo.TODO();
			}
		}
		
		// Ignore this as no libraries are actually installed then
		catch (NoSuchFileException e)
		{
		}
		
		// {@squirreljme.error BH01 Could not read installed libraries.}
		catch (IOException e)
		{
			throw new RuntimeException("BH01", e);
		}
	}
}

