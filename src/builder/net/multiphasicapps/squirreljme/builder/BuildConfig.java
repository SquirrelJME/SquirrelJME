// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.builder;

import java.nio.file.Path;
import net.multiphasicapps.squirreljme.jit.base.JITTriplet;
import net.multiphasicapps.squirreljme.jit.JITConfig;
import net.multiphasicapps.squirreljme.jit.JITConfigBuilder;
import net.multiphasicapps.squirreljme.jit.JITOutputConfig;
import net.multiphasicapps.squirreljme.projects.PackageInfo;
import net.multiphasicapps.squirreljme.projects.PackageList;

/**
 * This is used to configure what should be built.
 *
 * @since 2016/07/22
 */
public final class BuildConfig
{
	/** The target triplet. */
	protected final JITTriplet triplet;
	
	/** Should the JIT be included? */
	protected final boolean jit;
	
	/** Should the tests be included? */
	protected final boolean tests;
	
	/** Alternative executable name. */
	protected final String altexename;
	
	/** The package list. */
	protected final PackageList plist;
	
	/** Extra projects to add. */
	private final PackageInfo[] _extraprojects;
	
	/** The JIT configuration used. */
	volatile JITConfig _jitconf;
	
	/**
	 * Initializes the build configuration.
	 *
	 * @param __trip The target triplet.
	 * @param __jit Include the JIT?
	 * @param __tests Should the tests be included?
	 * @param __altexe The alternative executable name in the output ZIP, this
	 * is not required to be specified.
	 * @param __ep Extra projects to include.
	 * @throws NullPointerException On null arguments, except for optional
	 * ones.
	 * @since 2016/07/22
	 */
	BuildConfig(JITTriplet __trip,
		boolean __jit, boolean __tests, String __altexe, String[] __ep,
		PackageList __pl)
		throws NullPointerException
	{
		// Check
		if (__trip == null || __pl == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.triplet = __trip;
		this.jit = __jit;
		this.tests = __tests;
		this.altexename = __altexe;
		this.plist = __pl;
		
		// Find projects
		int n = __ep.length;
		PackageInfo[] extraprojects = new PackageInfo[n];
		for (int i = 0; i < n; i++)
		{
			// Find package
			String s = __ep[i];
			PackageInfo pk = plist.get(s);
			
			// {@squirreljme.error DW09 The specified project to be included
			// in the output binary does not exist. (The missing project)}
			if (pk == null)
				throw new IllegalArgumentException(String.format("DW09 %s",
					s));
			
			// Set
			extraprojects[i] = pk;
		}
		this._extraprojects = extraprojects;
	}
	
	/**
	 * Returns the alternative name for the executable to use.
	 *
	 * @return The alternatvie executable name, may be {@code null}.
	 * @since 2016/07/30
	 */
	public final String alternativeExecutableName()
	{
		return this.altexename;
	}
	
	/**
	 * Returns any extra projects which should be included.
	 *
	 * @return The extra projects to include.
	 * @since 2016/08/16
	 */
	public final PackageInfo[] extraProjects()
	{
		return this._extraprojects.clone();
	}
	
	/**
	 * Include tests in the output?
	 *
	 * @return {@code true} if tests should be included.
	 * @since 2016/07/22
	 */
	public final boolean includeTests()
	{
		return this.tests;
	}
	
	/**
	 * Returns the package list.
	 *
	 * @return The package list.
	 * @since 2016/09/02
	 */
	public final PackageList packageList()
	{
		return this.plist;
	}
	
	/**
	 * Returns the triplet.
	 *
	 * @return The triplet.
	 * @since 2016/07/22
	 */
	public final JITTriplet triplet()
	{
		return this.triplet;
	}
}

