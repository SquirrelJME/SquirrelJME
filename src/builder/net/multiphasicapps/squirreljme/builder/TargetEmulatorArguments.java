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

import java.io.InputStream;
import java.io.IOException;
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
		String[] args;
		this.args = (args = (__args == null ? new String[0] : __args.clone()));
		
		// Check
		for (String s : args)
			if (s == null)
				throw new NullPointerException("NARG");
	}
	
	/**
	 * Returns the arguments to the program to be ran.
	 *
	 * @return The arguments to the running program.
	 * @since 2016/07/30
	 */
	public String[] arguments()
	{
		return args.clone();
	}
	
	/**
	 * Returns either the alternative executable name or the default.
	 *
	 * @param __def The default executable name to use.
	 * @return The name of the executable.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/30
	 */
	public String executableName(String __def)
		throws NullPointerException
	{
		// Check
		if (__def == null)
			throw new NullPointerException("NARG");
		
		// Use alternative?
		String alt = this.altexe;
		return (alt != null ? alt : __def);
	}
	
	/**
	 * Returns the full set of arguments to pass to the emulator.
	 *
	 * @param __def The default executable name.
	 * @return The arguments to the emulator.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/30
	 */
	public String[] fullArguments(String __def)
		throws NullPointerException
	{
		// Check
		if (__def == null)
			throw new NullPointerException("NARG");
		
		// Get
		String[] args = this.args;
		int n = args.length;
		
		// Setup new array
		String[] rv = new String[n + 1];
		rv[0] = executableName(__def);
		
		// Pass extra arguments
		for (int i = 0, j = 1; i < n; i++, j++)
			rv[j] = args[i];
		
		// Return
		return rv;
	}
	
	/**
	 * Loads the executable into a byte array and then returns it.
	 *
	 * @param __def The default executable name to use, if an alternative was
	 * not specified.
	 * @return The byte array for the executable.
	 * @since 2016/07/30
	 */
	public byte[] loadExecutable(String __def)
		throws IOException, NullPointerException
	{
		// Check
		if (__def == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
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

