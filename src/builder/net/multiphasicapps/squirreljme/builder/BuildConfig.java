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

import net.multiphasicapps.squirreljme.jit.base.JITTriplet;

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
	
	/**
	 * Initializes the build configuration.
	 *
	 * @param __trip The target triplet.
	 * @param __jit Include the JIT?
	 * @param __tests Should the tests be included?
	 * @param __altexe The alternative executable name in the output ZIP, this
	 * is not required to be specified.
	 * @throws NullPointerException On null arguments, except for optional
	 * ones.
	 * @since 2016/07/22
	 */
	BuildConfig(JITTriplet __trip,
		boolean __jit, boolean __tests, String __altexe)
		throws NullPointerException
	{
		// Check
		if (__trip == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.triplet = __trip;
		this.jit = __jit;
		this.tests = __tests;
		this.altexename = __altexe;
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

