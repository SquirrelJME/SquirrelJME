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
import java.nio.file.Path;
import net.multiphasicapps.squirreljme.java.manifest.JavaManifest;
import net.multiphasicapps.squirreljme.java.manifest.JavaManifestAttributes;

/**
 * This represents the base for the class which represents the source code
 * of a given project.
 *
 * @since 2016/12/14
 */
public abstract class ProjectSource
	extends ProjectBase
{
	/** The manifest for the source code. */
	protected final JavaManifest manifest;
	
	/** The root directory of the source. */
	protected final Path root;
	
	/**
	 * Initializes the source representation.
	 *
	 * @param __pr The project owning this.
	 * @param __fp The path to the source code.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/12/14
	 */
	ProjectSource(Project __pr, Path __fp)
		throws IOException, NullPointerException
	{
		super(__pr);
		
		// Check
		if (__fp == null)
			throw new NullPointerException("NARG");
		
		// Load the manifest
		JavaManifest manifest = ProjectManager.__readManifest(
			__fp.resolve("META-INF").resolve("MANIFEST.MF"));
		this.manifest = manifest;
		
		// Set
		this.root = __fp;
	}
}

