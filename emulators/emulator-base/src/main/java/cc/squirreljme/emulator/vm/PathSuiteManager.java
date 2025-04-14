// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.emulator.vm;

import cc.squirreljme.jvm.suite.SuiteUtils;
import cc.squirreljme.vm.InMemoryClassLibrary;
import cc.squirreljme.vm.VMClassLibrary;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.LinkedHashSet;
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
					if (SuiteUtils.isAny(fn))
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
	 * @since 2023/12/18
	 */
	@Override
	public int libraryId(VMClassLibrary __lib)
		throws IllegalArgumentException, NullPointerException
	{
		if (__lib == null)
			throw new NullPointerException("NARG");
		
		Map<String, VMClassLibrary> cache = this._cache;
		synchronized (cache)
		{
			// The library must have been previously loaded first
			if (!cache.values().contains(__lib))
				throw new IllegalArgumentException(
					"Unknown library: " + __lib);
			
			Path path = __lib.path();
			return (path != null ? path.hashCode() : __lib.name().hashCode());
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
			
			// Exists but it is not valid, verbosely fail
			catch (IOException e)
			{
				e.printStackTrace();
				
				cache.put(__s, null);
				return null;
			}
		}
	}
}

