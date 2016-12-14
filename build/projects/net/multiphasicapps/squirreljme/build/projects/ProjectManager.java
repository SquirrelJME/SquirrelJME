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
import net.multiphasicapps.util.unmodifiable.UnmodifiableMap;

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
	/** The mapping of project names to projects. */
	protected final Map<ProjectName, Object> projects;
	
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
		
		// Scan source directory for project paths
		Map<NamespaceType, Set<Path>> namespacetree = __scanSources(null,
			__src);
		
		throw new Error("TODO");
	}
	
	/**
	 * Reads a manifest from the given path.
	 *
	 * @param __p The path to read from.
	 * @return The manifest that was read.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/11/24
	 */
	static final JavaManifest __readManifest(Path __p)
		throws IOException, NullPointerException
	{
		// Check
		if (__p == null)
			throw new NullPointerException("NARG");
		
		// Open the file and read it
		try (InputStream in = Channels.newInputStream(FileChannel.open(
			__p, StandardOpenOption.READ)))
		{
			return new JavaManifest(in);
		}
	}
	
	/**
	 * Scans the specified directory for source namespace layouts and returns
	 * the mapping of source projects.
	 *
	 * @param __dest The destination map to place values in, if the value is
	 * {@code null} then it is initialized.
	 * @param __root The root directory of the source tree to scan for
	 * namespaces and projects.
	 * @throws IOException On read errors.
	 * @throws NullPointerException If no root was specified.
	 * @since 2016/12/14
	 */
	static final Map<NamespaceType, Set<Path>> __scanSources(
		Map<NamespaceType, Set<Path>> __dest, Path __root)
		throws IOException, NullPointerException
	{
		// Check
		if (__root == null)
			throw new NullPointerException("NARG");
		
		// Create map as needed
		if (__dest == null)
			__dest = new LinkedHashMap<>();
		
		// Go through all source directories and determine which paths are
		// associated with namespaces.
		try (DirectoryStream<Path> ds = Files.newDirectoryStream(__root))
		{
			for (Path p : ds)
			{
				// Ignore non-directories
				if (!Files.isDirectory(p))
					continue;
				
				// Load in manifest
				try
				{
					JavaManifest man = ProjectManager.__readManifest(
						p.resolve("NAMESPACE.MF"));
					
					// Get the type of namespace this is
					JavaManifestAttributes attr = man.getMainAttributes();
					String rtype = attr.getValue(
						"Namespace-Type");
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
					
					// Go through this namespace and load sub-projects
					Set<Path> sub = __dest.get(type);
					try (DirectoryStream<Path> ss =
						Files.newDirectoryStream(p))
					{
						for (Path s : ss)
						{
							// Only access directories
							if (!Files.isDirectory(s) ||
								!Files.exists(s.resolve("META-INF").
									resolve("MANIFEST.MF")))
								continue;
							
							// Create entry?
							if (sub == null)
								__dest.put(type,
									(sub = new LinkedHashSet<Path>()));
							
							// Add project
							sub.add(s);
						}
					}
				}
				
				// Ignore
				catch (NoSuchFileException e)
				{
					continue;
				}
			}
		}
		
		return __dest;
	}
}

