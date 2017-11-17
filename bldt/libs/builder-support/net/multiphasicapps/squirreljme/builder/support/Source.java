// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.builder.support;

import java.io.InputStream;
import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import net.multiphasicapps.tool.manifest.JavaManifest;
import net.multiphasicapps.tool.manifest.JavaManifestAttributes;

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
	
	/** Dependencies that this source code relies on. */
	private volatile Reference<DependencySet> _dependencies;
	
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
	
	/**
	 * Returns the set of dependencies which are needed for this project to
	 * build and operate correctly.
	 *
	 * @return The set of dependencies.
	 * @since 2017/11/17
	 */
	public final DependencySet dependencies()
	{
		Reference<DependencySet> ref = this._dependencies;
		DependencySet rv;
		
		if (ref == null || null == (rv = ref.get()))
		{
			throw new todo.TODO();
		}
		
		return rv;
	}
	
	/**
	 * Returns the time that the source code was last modified.
	 *
	 * @return The last modification date of the source code.
	 * @throws IOException On read errors.
	 * @since 2017/11/06
	 */
	public long lastModifiedTime()
	{
		throw new todo.TODO();
	}
}

