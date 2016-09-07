// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.projects;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import net.multiphasicapps.util.sorted.SortedTreeMap;
import net.multiphasicapps.util.unmodifiable.UnmodifiableMap;
import net.multiphasicapps.zip.blockreader.ZipFile;

/**
 * This contains a mapping of every package which is available to SquirrelJME.
 *
 * @since 2016/06/15
 */
public class PackageList
	extends AbstractMap<PackageName, PackageInfo>
{
	/** The mapping of packages. */
	protected final Map<PackageName, PackageInfo> packages;
	
	/**
	 * This initializes the package list.
	 *
	 * @param __j The directory containing pre-built JAR files, if {@code null}
	 * then binary packages are not available.
	 * @param __s The directories containing source packages, if {@code null}
	 * then source packages are not available.
	 * @throws IOException If there is an error reading the package list.
	 * @throws NullPointerException If both arguments are null
	 * @since 2016/06/15
	 */
	public PackageList(Path __j, Path... __s)
		throws IOException, NullPointerException
	{
		// Check
		if (__j == null && __s == null)
			throw new NullPointerException("NARG");
		
		// The target map
		Map<PackageName, PackageInfo> target = new SortedTreeMap<>();
		
		// Go through binary JAR files
		if (__j != null)
			try (DirectoryStream<Path> ds = Files.newDirectoryStream(__j))
			{
				// Go through all files
				for (Path p : ds)
				{
					// Ignore directories
					if (Files.isDirectory(p))
						continue;
				
					// Open file
					try (FileChannel fc = FileChannel.open(p,
						StandardOpenOption.READ))
					{
						// Open as ZIP
						ZipFile zip = ZipFile.open(fc);
					
						// Load package information
						PackageInfo pi = new PackageInfo(this, p, zip);
					
						// Add to mapping
						target.put(pi.name(), pi);
					}
				
					// Not a valid ZIP or package, ignore
					catch (IOException|InvalidPackageException e)
					{
						continue;
					}
				}
			}
		
		// Lock
		this.packages = UnmodifiableMap.<PackageName, PackageInfo>of(target);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/06/19
	 */
	@Override
	public boolean containsKey(Object __o)
	{
		if (__o instanceof String)
			return this.packages.containsKey(new PackageName((String)__o));
		return this.packages.containsKey(__o);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/06/15
	 */
	@Override
	public Set<Map.Entry<PackageName, PackageInfo>> entrySet()
	{
		return this.packages.entrySet();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/06/19
	 */
	@Override
	public PackageInfo get(Object __o)
	{
		if (__o instanceof String)
			return this.packages.get(new PackageName((String)__o));
		return this.packages.get(__o);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/06/19
	 */
	@Override
	public int size()
	{
		return this.packages.size();
	}
}

