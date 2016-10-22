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
import net.multiphasicapps.squirreljme.java.manifest.JavaManifestAttributes;
import net.multiphasicapps.squirreljme.java.manifest.JavaManifestKey;

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
	 * @throws InvalidProjectException If the manifest does not refer to a
	 * valid binary project.
	 * @throws NotAProjectException If the manifest does not refer to a
	 * project.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/21
	 */
	public BinaryProjectManifest(InputStream __is)
		throws IOException, InvalidProjectException, NotAProjectException,
			NullPointerException
	{
		this(new JavaManifest(Objects.<InputStream>requireNonNull(__is)));
	}
	
	/**
	 * Parses the specified manifest.
	 *
	 * @param __man The binary manifest to parse.
	 * @throws InvalidProjectException If the manifest does not refer to a
	 * valid binary project.
	 * @throws NotAProjectException If the manifest does not refer to a
	 * project.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/21
	 */
	public BinaryProjectManifest(JavaManifest __man)
		throws InvalidProjectException, NotAProjectException,
			NullPointerException
	{
		// Check
		if (__man == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.manifest = __man;
		
		// Depends on the attributes
		JavaManifestAttributes attr = __man.getMainAttributes();
		
		// {@squirreljme.error CI03 The project is being ignored because it has
		// be flagged as such.}
		if (Boolean.valueOf(
			attr.getValue("X-SquirrelJME-IgnoreBinaryProject")))
			throw new NotAProjectException("CI03");
		
		// {@squirreljme.error CI02 Old-style SquirrelJME binary projects
		// (before September 2016) are not supported. (The old-style project
		// name)}
		String depname;
		if (null != (depname = attr.getValue("X-SquirrelJME-Name")))
			throw new NotAProjectException(String.format("CI02 %s", depname));
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

