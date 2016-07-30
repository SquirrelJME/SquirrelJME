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
	
	/**
	 * Initializes the emulator arguments.
	 *
	 * @param __conf The build configuration.
	 * @param __zip The ZIP which contains the SquirrelJME executable (or
	 * another one).
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/30
	 */
	public TargetEmulatorArguments(BuildConfig __conf, ZipFile __zip)
		throws NullPointerException
	{
		// Check
		if (__conf == null || __zip == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.config = __conf;
		this.zip = __zip;
	}
}

