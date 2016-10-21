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

import java.io.InputStream;
import java.io.IOException;
import java.util.Objects;
import net.multiphasicapps.squirreljme.java.manifest.JavaManifest;

/**
 * This provides a wrapper around the binary project manifest and is used to
 * help determine what a binary project actually is (so it may be correctly
 * used).
 *
 * @since 2016/10/20
 */
public final class BinaryProjectManifest
{
	/** The manifest used. */
	protected final JavaManifest manifest;
	
	/**
	 * Reads a manifest from the given input stream.
	 *
	 * @param __is The stream containing the manifest data.
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/21
	 */
	public BinaryProjectManifest(InputStream __is)
		throws IOException, NullPointerException
	{
		this(new JavaManifest(Objects.<InputStream>requireNonNull(__is)));
	}
	
	/**
	 * Parses the specified manifest.
	 *
	 * @param __is The binary manifest to parse.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/21
	 */
	public BinaryProjectManifest(JavaManifest __man)
		throws NullPointerException
	{
		// Check
		if (__man == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.manifest = __man;
	}
	
	/**
	 * Returns the classical main entry point of the project.
	 *
	 * @return The classic main entry point, or {@code null} if there is none.
	 * @since 2016/10/20
	 */
	public String mainClass()
	{
		throw new Error("TODO");
	}
}

