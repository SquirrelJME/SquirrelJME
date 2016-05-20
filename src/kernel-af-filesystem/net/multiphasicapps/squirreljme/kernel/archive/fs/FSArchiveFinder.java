// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel.archive.fs;

import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import net.multiphasicapps.squirreljme.kernel.archive.Archive;
import net.multiphasicapps.squirreljme.kernel.archive.ArchiveFinder;
import net.multiphasicapps.squirreljme.kernel.Kernel;
import net.multiphasicapps.util.unmodifiable.UnmodifiableList;

/**
 * This contains the archive finder which supports JAR files and utilizes
 * the host filesystem to load them.
 *
 * @since 2016/05/18
 */
public class FSArchiveFinder
	extends ArchiveFinder
{
	/** The directories that contains JAR files. */
	private final Path[] _paths;
	
	/** The archives which are available for execution . */
	private final List<FSArchive> _archives =
		new ArrayList<>();
	
	/** Unmodifiable view. */
	private final List<FSArchive> _unmod =
		UnmodifiableList.<FSArchive>of(this._archives);
	
	/** The string cache. */
	private volatile Reference<String> _string;
	
	/** Has this been refreshed? */
	private volatile boolean _refreshed;
	
	/** Refresh number. */
	
	/**
	 * Initializes the file system based archive finder using the default
	 * system properties and such.
	 *
	 * @param __k The owning kernel.
	 * @since 2016/05/18
	 */
	public FSArchiveFinder(Kernel __k)
	{
		this(__k, __defaultPaths(__k));
	}
	
	/**
	 * Initializes the file system based archive finder.
	 *
	 * @param __k The owning kernel for filesystem access.
	 * @param __p The paths containing JAR files.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/18
	 */
	public FSArchiveFinder(Kernel __k, Path... __p)
		throws NullPointerException
	{
		super(__k);
		
		// Check
		if (__p == null || __p.length <= 0)
			throw new NullPointerException("NARG");
		
		// Set
		Path[] paths = __p.clone();
		this._paths = paths;
		for (Path p : paths)
			if (p == null)
				throw new NullPointerException("NARG");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/18
	 */
	@Override
	public List<? extends FSArchive> archives()
	{
		return this._unmod;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/18
	 */
	@Override
	public void refresh()
	{
		// Lock
		Path[] paths = this._paths;
		List<FSArchive> unmod = this._unmod;;
		synchronized (unmod)
		{
			// Get target paths
			List<FSArchive> archives = this._archives;
			
			// To remove old unused archives
			Set<FSArchive> got = new HashSet<>();
			
			// Walk through all given paths
			for (Path r : paths)
			{
				// Open stream
				try (DirectoryStream<Path> ds = Files.newDirectoryStream(r))
				{
					// Go through all files in the directory
					for (Path p : ds)
					{
						// Ignore directories
						if (Files.isDirectory(p))
							continue;
						
						// Get the filename
						String fn = p.getFileName().toString();
						
						// Find the last and first dot
						int ld = fn.lastIndexOf('.');
						int fd = fn.indexOf('.');
						
						// Add to the list of archives? If it is either foo.jar
						// or jar.foo (Amiga)
						if (((ld >= 0 &&
							fn.substring(ld + 1).equalsIgnoreCase("jar")) ||
							(fd >= 0 &&
							fn.substring(0, fd).equalsIgnoreCase("jar"))))
						{
							// Only add it once
							int n = archives.size();
							boolean has = false;
							for (int i = 0; i < n; i++)
							{
								FSArchive vv = archives.get(i);	
								
								// Is this same path?
								if (vv.path().equals(p))
								{
									// Already got it
									has = true;
									got.add(vv);
									break;
								}
							}
							
							// Do not have an archive for it
							if (!has)
							{
								FSArchive cr = new FSArchive(p);
								got.add(cr);
							}
						}
					}
				}
				
				// Ignore, but warn on it
				catch (IOException e)
				{
					e.printStackTrace(System.err);
				}
			}
			
			// Remove any archives associated with paths which are no longer
			// in the given directories
			Iterator<FSArchive> it = archives.iterator();
			while (it.hasNext())
			{
				FSArchive vv = it.next();
				
				// Do not have this archive?
				if (!got.contains(vv))
					it.remove();
			}
			
			// Sort the paths alphabetically
			Collections.<FSArchive>sort(archives, new Comparator<FSArchive>()
				{
					/**
					 * {@inheritDoc}
					 * @since 2016/05/20
					 */
					@Override
					public int compare(FSArchive __a, FSArchive __b)
					{
						return __a.toString().compareToIgnoreCase(
							__b.toString());
					}
				});
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/18
	 */
	@Override
	public String toString()
	{
		// Get
		Reference<String> ref = _string;
		String rv;
		
		// Create?
		if (ref == null || null == (rv = ref.get()))
			_string = new WeakReference<>((rv = "File System " +
				"(Dirs: " + this._paths.length + ")"));
		
		// Return
		return rv;
	}
	
	/**
	 * Initializes default paths used to locate JAR files.
	 *
	 * @param __k The kernel to obtain properties and the filesystem from.
	 * @return The paths to locate JARs within.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/18
	 */
	private static Path[] __defaultPaths(Kernel __k)
		throws NullPointerException
	{
		return new Path[]{Paths.get(".")};
	}
}

