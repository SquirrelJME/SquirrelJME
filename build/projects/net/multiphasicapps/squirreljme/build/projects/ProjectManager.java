// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.build.projects;

import java.io.InputStream;
import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import net.multiphasicapps.squirreljme.java.manifest.JavaManifest;
import net.multiphasicapps.squirreljme.java.manifest.JavaManifestAttributes;

/**
 * This class is used to manage projects which represent modules that
 * SquirrelJME contains. Each module could be part of the build system, an API
 * which is implemented by SquirrelJME, or a library/midlet which may be
 * included in a target environment.
 *
 * @since 2016/10/28
 */
public class ProjectManager
{
	/**
	 * Initializes the project manager.
	 *
	 * @param __bin The binary path.
	 * @param __src The root of the SquirrelJME tree.
	 * @throws IOException On read/write errors.
	 * @since 2016/10/28
	 */
	public ProjectManager(Path __bin, Path __src)
		throws IOException, NullPointerException
	{
		// Check
		if (__bin == null || __src == null)
			throw new NullPointerException("NARG");
		
		// Seed tree with initial set
		Map<NamespaceType, Set<Path>> namespacetree = new LinkedHashMap<>();
		for (NamespaceType v : NamespaceType.values())
			namespacetree.put(v, new LinkedHashSet<Path>());
		
		// Go through all source directories and determine which paths are
		// associated with namespaces.
		try (DirectoryStream<Path> ds = Files.newDirectoryStream(__src))
		{
			for (Path p : ds)
			{
				// Ignore non-directories
				if (!Files.isDirectory(p))
					continue;
				
				// Load in manifest
				try (InputStream in = Channels.newInputStream(FileChannel.open(
					p.resolve("NAMESPACE.MF"), StandardOpenOption.READ)))
				{
					JavaManifest man = new JavaManifest(in);
					
					// Get the type of namespace this is
					JavaManifestAttributes attr = man.getMainAttributes();
					String rtype = attr.getValue(
						"X-SquirrelJME-Namespace-Type");
					if (rtype == null)
					{
						// {@squirreljme.error AT02 There is a namespace
						// manifest, however it lacks a type and as such it
						// will ignored. (The path to the namespace)}
						System.err.printf("AT02 %s%n", p);
						
						// Ignore
						continue;
					}
					
					// Look it up
					NamespaceType type = NamespaceType.of(rtype.trim());
					
					// Store it
					namespacetree.get(type).add(p);
				}
				
				// Ignore
				catch (NoSuchFileException e)
				{
					continue;
				}
			}
		}
	}
	
	/**
	 * Returns the list of suite hashes currently available.
	 *
	 * @return An array of suite hashes.
	 * @since 2016/11/20
	 */
	public int[] suiteHashes()
	{
		throw new Error("TODO");
	}
}

