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
import java.nio.file.Path;
import java.util.LinkedHashSet;
import java.util.Set;
import net.multiphasicapps.squirreljme.java.manifest.JavaManifest;
import net.multiphasicapps.squirreljme.java.manifest.JavaManifestAttributes;
import net.multiphasicapps.squirreljme.java.manifest.JavaManifestKey;
import net.multiphasicapps.util.empty.EmptySet;
import net.multiphasicapps.util.unmodifiable.UnmodifiableSet;

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
	 * @return The resulting binary from the compilation.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/12/19
	 */
	final Path __compile(Path __dest, Set<ProjectBinary> __deps)
		throws IOException, NullPointerException
	{
		// Check
		if (__dest == null || __deps == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
}

