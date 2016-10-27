// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * New bootstrap build system.
 *
 * @since 2016/10/26
 */
public class NewBootstrap
	implements Runnable
{
	/** The binary path. */
	protected final Path binarypath;
	
	/** The source path. */
	protected final Path sourcepath;
	
	/** The input launch arguments. */
	protected final String[] launchargs;
	
	/** Projects available for usage. */
	protected final Map<String, BuildProject> projects;
	
	/**
	 * Initializes the bootstrap base.
	 *
	 * @param __bin The binary output directory.
	 * @param __src The source input namespace directories.
	 * @param __args Arguments to the bootstrap.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/26
	 */
	public NewBootstrap(Path __bin, Path __src, String[] __args)
		throws IOException, NullPointerException
	{
		// Check
		if (__bin == null || __src == null || __args == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.binarypath = __bin;
		this.sourcepath = __src;
		this.launchargs = __args.clone();
		
		// Load all projects in the build directory
		Map<String, BuildProject> projects = new LinkedHashMap<>();
		try (DirectoryStream<Path> ds =
			Files.newDirectoryStream(__src.resolve("build")))
		{
			// Go through all directories
			for (Path p : ds)
			{
				// Must be a directory
				if (!Files.isDirectory(p))
					continue;
			
				// See if the manifest exists
				Path man = p.resolve("META-INF").resolve("MANIFEST.MF");
				if (!Files.exists(man))
					continue;
			
				// Load project
				BuildProject bp = new BuildProject(p);
				projects.put(bp.projectName(), bp);
			}
		}
		this.projects = projects;
	}
		
	/**
	 * {@inheritDoc}
	 * @since 2016/10/26
	 */
	@Override
	public void run()
	{
		throw new Error("TODO");
	}
	
	/**
	 * Main entry point for the new bootstrap system.
	 *
	 * @param __args Program arguments.
	 * @throws IOException On any read/write errors.
	 * @since 2016/10/26
	 */
	public static void main(String... __args)
		throws IOException
	{
		// Get directories for input and output
		Path bin = Paths.get(System.getProperty(
			"net.multiphasicapps.squirreljme.bootstrap.binary")),
			src = Paths.get(System.getProperty(
			"net.multiphasicapps.squirreljme.bootstrap.source"));
		
		// Only build?
		NewBootstrap nb = new NewBootstrap(bin, src, __args);
		
		// Run it
		nb.run();
	}
	
	/**
	 * This represents a single project which may be built.
	 *
	 * @since 2016/10/27
	 */
	public static class BuildProject
	{
		/**
		 * Initializes the build project.
		 *
		 * @param __b The project base path.
		 * @throws IOException On read/write errors.
		 * @throws NullPointerException On null arguments.
		 * @since 2016/10/27
		 */
		BuildProject(Path __b)
			throws IOException, NullPointerException
		{
			// Check
			if (__b == null)
				throw new NullPointerException("NARG");
			
			throw new Error("TODO");
		}
		
		/**
		 * Returns the name of this project.
		 *
		 * @return The project name.
		 * @since 2016/10/27
		 */
		public String projectName()
		{
			throw new Error("TODO");
		}
	}
}

