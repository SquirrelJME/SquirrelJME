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

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.nio.file.Path;
import java.nio.file.Paths;
import net.multiphasicapps.squirreljme.kernel.archive.Archive;
import net.multiphasicapps.squirreljme.kernel.archive.ArchiveFinder;
import net.multiphasicapps.squirreljme.kernel.Kernel;

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
	
	/** The string cache. */
	private volatile Reference<String> _string;
	
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

