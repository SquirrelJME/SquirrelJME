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

import cc.squirreljme.kernel.lib.Library;
import cc.squirreljme.kernel.lib.server.LibrariesProvider;
import cc.squirreljme.kernel.lib.server.LibraryCompilerInput;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;

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
				
				this.registerLibrary(this.loadPath(p));
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
	
	/**
	 * Compiles and creates an installation for the given library.
	 *
	 * @param __lci Input for the compiler.
	 * @param __p The path to the library.
	 * @param __out The output stream where the library will be written to.
	 * @return The compiled library.
	 * @throws IOException On write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/02/11
	 */
	protected abstract Library compileLibrary(LibraryCompilerInput __lci,
		Path __p, OutputStream __out)
		throws IOException, NullPointerException;
	
	/**
	 * Loads the given library path.
	 *
	 * @param __p The path to load.
	 * @return The library for the given path.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/02/12
	 */
	protected abstract Library loadPath(Path __p)
		throws NullPointerException;
	
	/**
	 * {@inheritDoc}
	 * @since 2018/02/11
	 */
	@Override
	protected final Library compileLibrary(LibraryCompilerInput __lci)
		throws NullPointerException
	{
		if (__lci == null)
			throw new NullPointerException("NARG");
		
		// Determine target path
		Path path = this.librarypath.resolve(Integer.toString(__lci.index()));
		
		// Writing may fail
		Path temp = null;
		try
		{
			// Need temporary file so there is no partial compiled binary
			// in the real library location
			temp = Files.createTempFile("squirreljme-compile-", ".bin");
			
			// Compile to temporary location first
			Library rv;
			try (OutputStream out = Files.newOutputStream(temp,
				StandardOpenOption.TRUNCATE_EXISTING,
				StandardOpenOption.WRITE))
			{
				rv = this.compileLibrary(__lci, path, out);
				
				// Try a flush but it could be closed by the sub-stream and
				// this could fail, so ignore it if it does fail
				try
				{
					out.flush();
				}
				catch (IOException e)
				{
				}
			}
			
			// {@squirreljme.error BH06 The compiler did not return a
			// library.}
			if (rv == null)
				throw new RuntimeException("BH06");
			
			// Then move it to the correct location
			Files.createDirectories(path.getParent());
			Files.move(temp, path, StandardCopyOption.REPLACE_EXISTING);
			
			// Done, the library is in its fixed location
			return rv;
		}
		
		// {@squirreljme.error BH05 Read/write error while writing library.}
		catch (IOException e)
		{
			throw new RuntimeException("BH05", e);
		}
		
		// Need to cleanup the temporary file
		finally
		{
			if (temp != null)
				try
				{
					Files.delete(temp);
				}
				catch (IOException e)
				{
				}
		}
	}
}

