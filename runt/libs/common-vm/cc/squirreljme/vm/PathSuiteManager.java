// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm;

import java.io.InputStream;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import net.multiphasicapps.zip.streamreader.ZipStreamReader;

/**
 * This is a suite manager which uses the filesystem.
 *
 * @since 2018/12/08
 */
public final class PathSuiteManager
	implements VMSuiteManager
{
	/** The base lib directory. */
	protected final Path libpath;
	
	/** Cache of loaded libraries. */
	private final Map<String, VMClassLibrary> _cache =
		new HashMap<>();
	
	/**
	 * Initializes the suite manager.
	 *
	 * @param __lp The library path.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/12/09
	 */
	public PathSuiteManager(Path __lp)
		throws NullPointerException
	{
		if (__lp == null)
			throw new NullPointerException("NARG");
		
		this.libpath = __lp;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/08
	 */
	@Override
	public final String[] listLibraryNames()
	{
		// Could fail
		try
		{
			Set<String> rv = new LinkedHashSet<>();
			
			// Go through directory entries
			try (DirectoryStream<Path> dir = Files.newDirectoryStream(
				this.libpath))
			{
				for (Path p : dir)
				{
					// Ignore directories
					if (Files.isDirectory(p))
						continue;
					
					String fn = p.getFileName().toString();
					if (fn.endsWith(".jar") || fn.endsWith(".JAR"))
						rv.add(fn);
				}
			}
			
			return rv.<String>toArray(new String[rv.size()]);
		}
		
		// Was not found, ignore
		catch (NoSuchFileException e)
		{
			return new String[0];
		}
		
		// Could not load!
		catch (IOException e)
		{
			e.printStackTrace();
			
			return new String[0];
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/12/08
	 */
	@Override
	public final VMClassLibrary loadLibrary(String __s)
		throws NullPointerException
	{
		if (__s == null)
			throw new NullPointerException("NARG");
		
		Map<String, VMClassLibrary> cache = this._cache;
		synchronized (cache)
		{
			VMClassLibrary rv = cache.get(__s);
			if (rv != null)
				return rv;
			
			// Pre-cached to not exist
			if (cache.containsKey(__s))
				return null;
			
			// Open file and stream the ZIP
			try (InputStream in = Files.newInputStream(
				this.libpath.resolve(__s), StandardOpenOption.READ);
				ZipStreamReader zsr = new ZipStreamReader(in))
			{
				cache.put(__s, (rv = InMemoryClassLibrary.loadZip(__s, zsr)));
				return rv;
			}
			
			// Does not exist, stop
			catch (NoSuchFileException e)
			{
				cache.put(__s, null);
				return null;
			}
			
			// {@squirreljme.error AK08 
			catch (IOException e)
			{
				throw new VMException("AK08", e);
			}
		}
	}
}

