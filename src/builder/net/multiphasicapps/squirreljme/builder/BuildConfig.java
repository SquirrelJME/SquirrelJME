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
	
	/** Emulate after building? */
	protected final boolean emulate;
	
	/** Should the JIT be included? */
	protected final boolean jit;
	
	/** Should the tests be included? */
	protected final boolean tests;
	
	/** Arguments to the emulator. */
	private final String[] _emulatorargs;
	
	/**
	 * Initializes the build configuration.
	 *
	 * @param __trip The target triplet.
	 * @param __emu Should this system be emulated following the build?
	 * @param __args Arguments to the emulator.
	 * @param __jit Include the JIT?
	 * @param __tests Should the tests be included?
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/22
	 */
	BuildConfig(JITTriplet __trip, boolean __emu, String[] __args,
		boolean __jit, boolean __tests)
		throws NullPointerException
	{
		// Check
		if (__trip == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.triplet = __trip;
		this.emulate = __emu;
		this._emulatorargs = (__args != null ? __args.clone() : new String[0]);
		this.jit = __jit;
		this.tests = __tests;
	}
	
	/**
	 * Perform emulation after a successful build?
	 *
	 * @return {@code true} if emulation should be performed.
	 * @since 2016/07/22
	 */
	public final boolean doEmulation()
	{
		return this.emulate;
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

