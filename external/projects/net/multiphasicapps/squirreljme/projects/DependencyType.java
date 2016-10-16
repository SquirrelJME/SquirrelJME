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

import net.multiphasicapps.squirreljme.java.manifest.JavaManifestKey;

/**
 * This represents the type of dependency that is used.
 *
 * @since 2016/10/11
 */
public enum DependencyType
{
	/** Required dependency. */
	REQUIRED("X-SquirrelJME-Depends", false, false, true, true),
	
	/** Optional. */
	OPTIONAL("X-SquirrelJME-Optional", true, true, true, true),
	
	/** Internal dependency. */
	INTERNAL("X-SquirrelJME-Internal", false, true, true, false),
	
	/** External dependency. */
	EXTERNAL("X-SquirrelJME-External", true, true, false, true),
	
	/** Co-dependencies. */
	CODEPEND("X-SquirrelJME-CoDepends", false, false, false, false),
	
	/** End. */
	;
	
	/** The key used for the property. */
	protected final JavaManifestKey key;
	
	/** Is this optional? */
	protected final boolean optional;
	
	/** Is this required for build? */
	protected final boolean buildrequires;
	
	/** Candidate for midlets/liblets? */
	protected final boolean midlet;
	
	/** Candidate for class-path? */
	protected final boolean classpath;
	
	/**
	 * Initializes the dependency type.
	 *
	 * @param __key The key used to obtain the value.
	 * @param __opt Is this considered optional?
	 */
	private DependencyType(String __key, boolean __opt, boolean __br,
		boolean __mid, boolean __cp)
		throws NullPointerException
	{
		// Check
		if (__key == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.key = new JavaManifestKey(__key);
		this.optional = __opt;
		this.buildrequires = __br;
		this.midlet = __mid;
		this.classpath = __cp;
	}
	
	/**
	 * Does a build require this kind of dependency or can it be built
	 * following it?
	 *
	 * @return {@code true} if it is needed for building.
	 * @since 2016/10/11
	 */
	public final boolean doesBuildRequire()
	{
		return this.buildrequires;
	}
	
	/**
	 * Is this dependency considered optional?
	 *
	 * @return {@code true} if an optional dependency.
	 * @since 2016/10/11
	 */
	public final boolean isOptional()
	{
		return this.optional;
	}
	
	/**
	 * The attribute key used.
	 *
	 * @return The key used.
	 * @since 2016/10/11
	 */
	public final JavaManifestKey key()
	{
		return this.key;
	}
	
	/**
	 * Does this output to the "Class-Path" attribute?
	 *
	 * @return {@code true} if it outputs to the "Class-Path" attribute.
	 * @since 2016/10/11
	 */
	public final boolean outputsToClassPath()
	{
		return this.classpath;
	}
	
	/**
	 * Does this output to the "LIBlet/MIDlet-Dependency-#" attribute?
	 *
	 * @return {@code true} if it becomes a midlet/liblet dependency.
	 * @since 2016/10/11
	 */
	public final boolean outputsToMidlet()
	{
		return this.midlet;
	}
}

