// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.projects;

import java.nio.file.Path;
import java.nio.file.Paths;
import net.multiphasicapps.squirreljme.bootstrap.base.launcher.
	ResourceAccessor;

/**
 * This represents a binary project which may be launched.
 *
 * @since 2016/10/20
 */
public final class BinaryProject
	extends ProjectInfo
{
	/** The manifest used for the binary. */
	protected final BinaryProjectManifest manifest;
	
	/** The path to the project. */
	protected final Path path;
	
	/**
	 * Initializes the binary project using the given manifest and path.
	 *
	 * @param __man The manifest used.
	 * @param __p The path to the binary.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/21
	 */
	BinaryProject(BinaryProjectManifest __man, Path __p)
		throws NullPointerException
	{
		// Check
		if (__man == null || __p == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.manifest = __man;
		this.path = __p;
	}
	
	/**
	 * Returns the binary manifest of this project.
	 *
	 * @return The binary manifest.
	 * @since 2016/10/20
	 */
	public BinaryProjectManifest binaryManifest()
	{
		throw new Error("TODO");
	}
	
	/**
	 * Returns a resource accessor which may be used by the launcher.
	 *
	 * @return The launcher resource accessor.
	 * @since 2016/10/20
	 */
	public ResourceAccessor launcherResourceAccessor()
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/10/21
	 */
	@Override
	public ProjectName projectName()
	{
		throw new Error("TODO");
	}
}

