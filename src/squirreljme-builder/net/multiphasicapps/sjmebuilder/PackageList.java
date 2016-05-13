// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.sjmebuilder;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.AbstractMap;
import java.util.Collections;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import net.multiphasicapps.collections.MissingCollections;

/**
 * This contains the list of packages which are available for compilation and
 * potential running.
 *
 * @since 2016/02/28
 */
public class PackageList
	extends AbstractMap<String, PackageInfo>
{
	/** Output JAR directory. */
	protected final Path outdir;
	
	/** Input source directory. */
	protected final Path srcdir;
	
	/** Available packages. */
	protected final Map<String, PackageInfo> packages;
	
	/**
	 * Initializes the package list.
	 *
	 * @param __out Output JAR directory.
	 * @param __src Source directory.
	 * @throws IOException If the mapping of packages could not be determined.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/02/28
	 */
	public PackageList(Path __out, Path __src)
		throws IOException, NullPointerException
	{
		// Check
		if (__out == null || __src == null)
			throw new NullPointerException("NARG");
		
		// Set
		outdir = __out;
		srcdir = __src;
		
		// Target package list
		Map<String, PackageInfo> to = new HashMap<>();
		
		// Get a directory stream
		try (DirectoryStream<Path> ds = Files.newDirectoryStream(srcdir))
		{
			// Go through all files
			for (Path file : ds)
			{
				// Ignore self
				if (Files.isSameFile(srcdir, file))
					continue;
				
				// Ignore non-directories
				if (!Files.isDirectory(file))
					continue;
				
				// If the parent is not the source root, ignore
				if (!Files.isSameFile(srcdir, file.getParent()))
					continue;
				
				// Setup package
				PackageInfo pi = new PackageInfo(outdir, file);
				
				// Add it but only if valid
				if (pi.isValid())
					to.put(pi.toString(), pi);
			}
		}
		
		// Lock
		packages = MissingCollections.<String, PackageInfo>unmodifiableMap(to);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/02/28
	 */
	@Override
	public Set<Map.Entry<String, PackageInfo>> entrySet()
	{
		return packages.entrySet();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/02/28
	 */
	@Override
	public PackageInfo get(Object __k)
	{
		return packages.get(__k);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/02/28
	 */
	@Override
	public int size()
	{
		return packages.size();
	}
}

