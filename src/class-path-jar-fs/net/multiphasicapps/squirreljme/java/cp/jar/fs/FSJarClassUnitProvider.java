// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.classpath.jar.fs;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import net.multiphasicapps.squirreljme.classpath.ClassUnit;
import net.multiphasicapps.squirreljme.classpath.ClassUnitProvider;
import net.multiphasicapps.squirreljme.classpath.jar.JarClassUnit;
import net.multiphasicapps.squirreljme.classpath.jar.JarClassUnitProvider;

/**
 * This provides access to JARs which exist on the file system.
 *
 * @since 2016/05/25
 */
public class FSJarClassUnitProvider
	extends JarClassUnitProvider
{
	/** The path to use when searching for JARs. */
	protected final Path path;
	
	/**
	 * Initializes the file system class unit provider which searches in the
	 * specified path.
	 *
	 * @param __p The path to locate class units for.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/25
	 */
	public FSJarClassUnitProvider(Path __p)
		throws NullPointerException
	{
		// Check
		if (__p == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.path = __p;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/28
	 */
	@Override
	protected JarClassUnit createClassUnit(String __k)
		throws NullPointerException
	{
		// Check
		if (__k == null)
			throw new NullPointerException("NARG");
		
		// Create
		return new FSJarClassUnit(this.path.resolve(__k));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/26
	 */
	@Override
	protected Collection<String> keyCollection()
	{
		// Output keys
		Set<String> rv = new HashSet<>();
		
		// Go through all directories and get the info
		try (DirectoryStream<Path> ds = Files.newDirectoryStream(path))
		{
			// Build keys
			for (Path p : ds)
			{
				// Ignore directories
				if (Files.isDirectory(p))
					continue;
				
				// Add otherwise
				rv.add(p.getFileName().toString());
			}
		}
		
		// Ignore
		catch (IOException e)
		{
		}
		
		// Return the set
		return rv;
	}
}

