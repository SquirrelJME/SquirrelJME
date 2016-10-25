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
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Set;
import java.util.Objects;
import net.multiphasicapps.squirreljme.java.manifest.JavaManifest;
import net.multiphasicapps.squirreljme.java.manifest.JavaManifestAttributes;
import net.multiphasicapps.squirreljme.java.manifest.JavaManifestKey;
import net.multiphasicapps.util.sorted.SortedTreeSet;

/**
 * This is a manifest that is used for source projects.
 *
 * @since 2016/10/21
 */
public final class SourceProjectManifest
{
	/** The manifest used. */
	protected final JavaManifest manifest;
	
	/** The project name. */
	protected final String name;
	
	/** Dependencies of this project. */
	private volatile Reference<SourceDependency[]> _depends;
	
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
		if (null != (depname = attr.getValue("X-SquirrelJME-Name")))
			throw new NotAProjectException(String.format("CI04 %s", depname));
		
		// {@squirreljme.error CI06 No project name was specified.}
		String name = attr.getValue("X-SquirrelJME-SourceName");
		if (name == null)
			throw new InvalidProjectException("CI06");
		this.name = name;
	}
	
	/**
	 * Returns the dependencies of the given project.
	 *
	 * @return The dependencies of the project.
	 * @since 2016/10/25
	 */
	public SourceDependency[] dependencies()
	{
		Reference<SourceDependency[]> ref = this._depends;
		SourceDependency[] rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
		{
			// Need attributes
			JavaManifestAttributes attr = this.manifest.getMainAttributes();
			
			// Load dependencies
			Set<SourceDependency> b = new SortedTreeSet<>();
			for (int i = 1; i < Integer.MAX_VALUE; i++)
			{
				// Get key
				JavaManifestKey k = new JavaManifestKey(
					String.format("X-SquirrelJME-Dependency-%d", i));
				String v = attr.get(k);
				
				// Stop
				if (v == null)
					break;
				
				// Decode
				b.add(new SourceDependency(v));
			}
			
			// Store
			rv = b.<SourceDependency>toArray(new SourceDependency[b.size()]);
			this._depends = new WeakReference<>(rv);
		}
		
		// Defensive copy
		return rv.clone();
	}
	
	/**
	 * Returns the name of the project.
	 *
	 * @return The project name.
	 * @since 2016/10/21
	 */
	public String projectName()
	{
		return this.name;
	}
}

