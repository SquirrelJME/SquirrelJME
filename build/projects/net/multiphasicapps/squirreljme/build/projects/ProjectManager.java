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
	/** The API manager. */
	protected final APIManager apiman;
	
	/** The application manager. */
	protected final ApplicationManager appman;
	
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
		
		// Seed tree with initial sets
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
				try
				{
					JavaManifest man = ProjectManager.__readManifest(
						p.resolve("NAMESPACE.MF"));
					
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
					
					// Go through this namespace and load sub-projects
					Set<Path> sub = namespacetree.get(type);
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
		
		// Initialize the API manager for provided interfaces
		this.apiman = new APIManager(this, 
			namespacetree.get(NamespaceType.APIS));
		
		// Setup application manager using the midlet and liblet namespaces
		this.appman = new ApplicationManager(this,
			namespacetree.get(NamespaceType.LIBLETS),
			namespacetree.get(NamespaceType.MIDLETS));
	}
	
	/**
	 * This is used to provide access to all of the APIs which are
	 * available.
	 *
	 * @return The API manager.
	 * @since 2016/12/04
	 */
	public final APIManager apis()
	{
		return this.apiman;
	}
	
	/**
	 * Returns the application manager.
	 *
	 * @return The application manager.
	 * @since 2016/11/20
	 */
	public final ApplicationManager applications()
	{
		return this.appman;
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
}

