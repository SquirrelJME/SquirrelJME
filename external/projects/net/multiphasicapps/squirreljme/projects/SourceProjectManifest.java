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
 * This is a manifest that is used for source projects.
 *
 * @since 2016/10/21
 */
public final class SourceProjectManifest
{
	/** The manifest used. */
	protected final JavaManifest manifest;
	
	/**
	 * Reads a manifest from the given input stream.
	 *
	 * @param __is The stream containing the manifest data.
	 * @throws IOException On read errors.
	 * @throws InvalidProjectException If the manifest does not refer to a
	 * valid source project.
	 * @throws NotAProjectException If the manifest does not refer to a
	 * project.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/21
	 */
	public SourceProjectManifest(InputStream __is)
		throws IOException, InvalidProjectException, NotAProjectException,
			NullPointerException
	{
		this(new JavaManifest(Objects.<InputStream>requireNonNull(__is)));
	}
	
	/**
	 * Parses the specified manifest.
	 *
	 * @param __man The source manifest to parse.
	 * @throws InvalidProjectException If the manifest does not refer to a
	 * valid source project.
	 * @throws NotAProjectException If the manifest does not refer to a
	 * project.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/10/21
	 */
	public SourceProjectManifest(JavaManifest __man)
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
		
		// {@squirreljme.error CI04 Old-style SquirrelJME source projects
		// (before September 2016) are not supported. (The old-style project
		// name)}
		String depname;
		if (null != (depname =
			attr.get(new JavaManifestKey("X-SquirrelJME-Name"))))
			throw new NotAProjectException(String.format("CI04 %s", depname));
	}
}

