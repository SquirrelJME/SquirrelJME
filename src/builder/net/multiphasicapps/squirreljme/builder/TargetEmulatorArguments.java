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
import net.multiphasicapps.zip.blockreader.ZipFile;

/**
 * This class contains the arguments which are needed to setup a target
 * emulator for testing and other such things.
 *
 * @since 2016/07/30
 */
public final class TargetEmulatorArguments
{
	/** The build configuration. */
	protected final BuildConfig config;
	
	/** The bootstrap ZIP. */
	protected final ZipFile zip;
	
	/** The optional alternative executable name. */
	protected final String altexe;
	
	/** Emulator command arguments. */
	protected final String[] args;
	
	/**
	 * Initializes the emulator arguments.
	 *
	 * @param __conf The build configuration.
	 * @param __zip The ZIP which contains the SquirrelJME executable (or
	 * another one).
	 * @param __altexe The alternative executable name, this argument is
	 * optional.
	 * @param __args Arguments to pass to the emulator.
	 * @throws NullPointerException On null arguments, except for optional
	 * ones.
	 * @since 2016/07/30
	 */
	public TargetEmulatorArguments(BuildConfig __conf, ZipFile __zip,
		String __altexe, String[] __args)
		throws NullPointerException
	{
		// Check
		if (__conf == null || __zip == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.config = __conf;
		this.zip = __zip;
		this.altexe = __altexe;
		this.args = (__args == null ? new String[0] : __args.clone());
	}
	
	/**
	 * Returns the target triplet.
	 *
	 * @return The target triplet.
	 * @since 2016/07/30
	 */
	public JITTriplet triplet()
	{
		return this.config.triplet();
	}
}

