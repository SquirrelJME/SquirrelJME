// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.build.project;

import java.io.InputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import net.multiphasicapps.squirreljme.java.manifest.JavaManifest;
import net.multiphasicapps.squirreljme.java.manifest.JavaManifestAttributes;

/**
 * This represents a single source project which contains the source code for
 * a single project.
 *
 * @since 2017/10/31
 */
public final class Source
{
	/** The name of the source. */
	protected final SourceName name;
	
	/** The path to the source code root. */
	protected final Path root;
	
	/** The manifest for the source code. */
	protected final JavaManifest manifest;
	
	/**
	 * Initializes the project source.
	 *
	 * @param __name The name of the source.
	 * @param __p The path to the source code.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/31
	 */
	public Source(SourceName __name, Path __p)
		throws IOException, NullPointerException
	{
		if (__name == null || __p == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.name = __name;
		this.root = __p;
		
		// Load manifest
		try (InputStream in = Files.newInputStream(__p.resolve("META-INF").
			resolve("MANIFEST.MF"), StandardOpenOption.READ))
		{
			this.manifest = new JavaManifest(in);
		}
	}
}

