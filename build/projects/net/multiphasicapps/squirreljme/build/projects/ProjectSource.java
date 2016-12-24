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

import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.LinkedHashSet;
import java.util.Set;
import net.multiphasicapps.squirreljme.build.base.FileDirectory;
import net.multiphasicapps.squirreljme.build.base.SourceCompiler;
import net.multiphasicapps.squirreljme.build.base.SourceCompilerProvider;
import net.multiphasicapps.squirreljme.java.manifest.JavaManifest;
import net.multiphasicapps.squirreljme.java.manifest.JavaManifestAttributes;
import net.multiphasicapps.squirreljme.java.manifest.JavaManifestKey;
import net.multiphasicapps.util.empty.EmptySet;
import net.multiphasicapps.util.unmodifiable.UnmodifiableSet;
import net.multiphasicapps.zip.streamwriter.ZipStreamWriter;

/**
 * This represents the base for the class which represents the source code
 * of a given project.
 *
 * @since 2016/12/14
 */
public abstract class ProjectSource
	extends ProjectBase
{
	/** The property used for dependencies. */
	private static final JavaManifestKey _DEPENDS_PROPERTY =
		new JavaManifestKey("X-SquirrelJME-Depends");
	
	/** The manifest for the source code. */
	protected final JavaManifest manifest;
	
	/** Dependencies of this project. */
	private volatile Reference<Set<Project>> _depends;
	
	/**
	 * Initializes the source representation.
	 *
	 * @param __pr The project owning this.
	 * @param __fp The path to the source code.
	 * @throws IOException On read errors.
	 * @since 2016/12/14
	 */
	ProjectSource(Project __pr, Path __fp)
		throws IOException
	{
		super(__pr, __fp);
		
		// Load the manifest
		JavaManifest manifest = ProjectManager.__readManifest(
			this.path.resolve("META-INF").resolve("MANIFEST.MF"));
		this.manifest = manifest;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/12/18
	 */
	@Override
	public final void dependencies(Set<Project> __out)
		throws InvalidProjectException, NullPointerException
	{
		// Check
		if (__out == null)
			throw new NullPointerException("NARG");
		
		// Since dependencies are very much fixed, just cache them so that
		// it does not need to be calculated/checked every time
		Reference<Set<Project>> ref = this._depends;
		Set<Project> rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
		{
			// Target
			rv = new LinkedHashSet<>();
			
			// Parse configuration/profile to auto-depend on CLDC and/or
			// MIDP required APIs
			System.err.println("TODO -- Parse configuration/profile.");
			
			// Parse dependency property
			String attr = this.manifest.getMainAttributes().
				get(_DEPENDS_PROPERTY);
			if (attr != null)
			{
				throw new Error("TODO");
			}
			
			// Cache it
			this._depends = new WeakReference<>(rv);
		}
		
		// Fill projects
		for (Project p : rv)
			__out.add(p);
	}
	
	/**
	 * Compiles the source code for this project.
	 *
	 * @param __dest The destination path where the binary should be placed.
	 * @param __deps The binary dependencies needed for it to compile.
	 * @throws InvalidProjectException If the project could not be compiled.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/12/19
	 */
	final void __compile(Path __dest, Set<ProjectBinary> __deps)
		throws InvalidProjectException, IOException, NullPointerException
	{
		// Check
		if (__dest == null || __deps == null)
			throw new NullPointerException("NARG");
		
		// Locate compiler
		SourceCompiler sc;
		try
		{
			sc = SourceCompilerProvider.newInstance();
		}
		
		// {@squirreljme.error AT08 Could not compile the project because
		// no compiler is available.}
		catch (RuntimeException e)
		{
			throw new InvalidProjectException("AT08", e);
		}
		
		// Need to access directories during compilation
		Path tempdir = null, tempjar = null;
		try (FileDirectory fd = openFileDirectory();
			__CloseableList__<FileDirectory> bds = new __CloseableList__<>())
		{
			// Need a place to store files and where to write the output JAR
			String sname = name().toString();
			tempjar = Files.createTempFile(sname, "-build.jar");
			tempdir = Files.createTempDirectory(sname);
			
			// Add source
			sc.addSourceDirectory(fd);
			
			// Load dependency into directories
			for (ProjectBinary db : __deps)
			{
				// Load
				FileDirectory bfd = db.openFileDirectory();
				bds.add(bfd);
				
				// Set where classes exist
				sc.addClassDirectory(bfd);
			}
			
			// Add files to be compiled
			for (String s : fd)
				if (__isValidSource(s))
					sc.addSource(s);
			
			// Target Java 7 as usual, with debugging as needed and
			// add some other options
			sc.setCompileOptions("-source", "1.7", "-target", "1.7", "-g",
				"-Xlint:deprecation", "-Xlint:unchecked");
			
			// {@squirreljme.error AT0a Failed to compile the given source
			// project. (The name of this project)}
			if (!sc.compile())
				throw new InvalidProjectException(String.format("AT0a %s",
					sname));
			
			// Need to package the binary
			try (ZipStreamWriter zsw = new ZipStreamWriter(
				Channels.newOutputStream(FileChannel.open(tempjar,
				StandardOpenOption.WRITE))))
			{
				if (true)
					throw new Error("TODO");
			}
			
			// Replace the JAR
			Files.move(tempjar, __dest, StandardCopyOption.REPLACE_EXISTING);
		}
		
		// Always clear temporary files
		finally
		{
			// Delete temporary JAR if it exists
			if (tempjar != null)
				try
				{
					Files.delete(tempjar);
				}
				catch (IOException e)
				{
					e.printStackTrace();
				}
			
			throw new Error("TODO");
		}
	}
	
	/**
	 * Checks whether this file is a valid source code name.
	 *
	 * @param __s The source file name to check.
	 * @return {@code true} if the source code is valid.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/12/24
	 */
	private static boolean __isValidSource(String __s)
		throws NullPointerException
	{
		// Check
		if (__s == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
}

