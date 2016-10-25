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
public final class MasterDirectory
{
	/**
	 * This is the global project list which may be used to use a pre-existing
	 * list so that there is no cost for reloading the same list.
	 */
	private static volatile MasterDirectory _GLOBAL_LIST;
	
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
	public MasterDirectory(Path __b, Path __s)
		throws IOException, NullPointerException
	{
		// Check
		if (__b == null || __s == null)
			throw new NullPointerException("NARG");
		
		// Parse directories
		this.binaries = new BinaryDirectory(this, __b);
		this.sources = new SourceDirectory(this, __s);
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
	
	/**
	 * This returns the first {@link MasterDirectory} which has been instantiated.
	 *
	 * @return The first initialized project directory, or {@code null} if one
	 * was never initialized.
	 * @since 2016/09/29
	 */
	public static MasterDirectory getGlobalProjectList()
	{
		return MasterDirectory._GLOBAL_LIST;
	}
}

