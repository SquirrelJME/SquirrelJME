// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.build.projects;

import java.io.InputStream;
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.nio.file.Path;
import java.util.Set;
import net.multiphasicapps.squirreljme.build.base.FileDirectory;
import net.multiphasicapps.squirreljme.java.manifest.JavaManifest;
import net.multiphasicapps.squirreljme.java.manifest.JavaManifestAttributes;
import net.multiphasicapps.squirreljme.java.manifest.JavaManifestException;
import net.multiphasicapps.squirreljme.java.manifest.JavaManifestKey;

/**
 * This represents the base for a binary project which is a compiled form
 * of an existing project.
 *
 * @since 2016/12/14
 */
public abstract class ProjectBinary
	extends ProjectBase
{
	/** The manifest for this binary. */
	private volatile Reference<JavaManifest> _manifest;
	
	/**
	 * Initializes the project binary.
	 *
	 * @param __p The project owning this.
	 * @param __fp The path to the project's binary.
	 * @throws IOException If the specified path is not valid for a binary.
	 * @since 2016/12/17
	 */
	ProjectBinary(Project __p, Path __fp)
		throws IOException
	{
		super(__p, __fp);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/01/20
	 */
	@Override
	public JavaManifest binaryManifest()
		throws InvalidProjectException
	{
		// The binary manifest is the manifest
		return manifest();
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
		
		// Need these for parsing
		JavaManifestAttributes attr = manifest().getMainAttributes();
		
		// Add common dependencies
		__commonDependencies(__out);
		
		// TODO
		System.err.println("TODO -- Determine binary depends.");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/12/27
	 */
	@Override
	public final JavaManifest manifest()
		throws InvalidProjectException
	{
		Reference<JavaManifest> ref = this._manifest;
		JavaManifest rv;
		
		// Needs loading?
		if (ref == null || null == (rv = ref.get()))
			try (FileDirectory fd = openFileDirectory();
				InputStream is = fd.open("META-INF/MANIFEST.MF"))
			{
				this._manifest = new WeakReference<>(
					(rv = new JavaManifest(is)));
			}
		
			// {@squirreljme.error AT0d Could not parse the manifest for
			// the given project. (The project name)}
			catch (JavaManifestException|IOException e)
			{
				throw new InvalidProjectException(String.format("AT0d %s",
					name()), e);
			}
		
		// Return
		return rv;
	}
}

