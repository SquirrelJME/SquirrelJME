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

import java.io.IOException;
import java.nio.file.Path;

/**
 * This represents the project directory which contains binaries for
 * execution and sources for compilation.
 *
 * @since 2016/10/20
 */
public final class ProjectDirectory
{
	/** The binary directory. */
	protected final BinaryDirectory binaries;
	
	/** The source directory. */
	protected final SourceDirectory sources;
	
	/**
	 * Initializes the project directory listing.
	 *
	 * @param __b The binary project directory, used for binary output.
	 * @param __s The source project directory, containing namespaces.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/20
	 */
	public ProjectDirectory(Path __b, Path __s)
		throws IOException, NullPointerException
	{
		// Check
		if (__b == null || __s == null)
			throw new NullPointerException("NARG");
		
		// Parse directories
		this.binaries = new BinaryDirectory(this, __b);
		this.source = new SourceDirectory(this, __s);
	}
	
	/**
	 * Returns the directory that contains binary projects.
	 *
	 * @return The binary directory.
	 * @since 2016/10/20
	 */
	public BinaryDirectory binaries()
	{
		return this.binaries;
	}
	
	/**
	 * Returns the directory which contains source projects.
	 *
	 * @return The source directory.
	 * @since 2016/10/20
	 */
	public SourceDirectory sources()
	{
		return this.sources;
	}
}

