// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.midlet;

import cc.squirreljme.jvm.suite.Profile;
import cc.squirreljme.jvm.suite.SuiteVersion;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import javax.microedition.midlet.MIDlet;

/**
 * Runtime properties for MEEP applications.
 *
 * @since 2025/04/18
 */
@SquirrelJMEVendorApi
public final class MeepRuntime
{
	/** The cached DoJa version. */
	private static volatile SuiteVersion _VERSION;
	
	/**
	 * Not used.
	 *
	 * @since 2024/07/28
	 */
	private MeepRuntime()
	{
	}
	
	/**
	 * Returns the MEEP version.
	 *
	 * @return The MEEP version.
	 * @since 2025/04/09
	 */
	@SquirrelJMEVendorApi
	public static SuiteVersion version()
	{
		// Already cached?
		SuiteVersion version = MeepRuntime._VERSION;
		if (version != null)
			return version;
		
		// Are we running MIDlets?
		MIDlet midlet = ActiveMidlet.optional();
		if (midlet == null)
			version = new SuiteVersion(8, 0);
		else
		{
			// Setup version based on the current profile
			String profile = midlet.getAppProperty("MicroEdition-Profile");
			if (profile != null && !profile.isEmpty())
			{
				profile = profile.trim();
				if (profile.startsWith("MEEP-") ||
					profile.startsWith("MIDP-"))
					version = new Profile(profile).version();
			}
			else
				version = new SuiteVersion(8, 0);
		}
		
		// Cache and use it
		MeepRuntime._VERSION = version;
		return version;
	}
	
	/**
	 * Checks if the MEEP version is before the given version.
	 *
	 * @param __major The major version.
	 * @param __minor The minor version.
	 * @return If this is before the given DoJa version.
	 * @since 2025/04/18
	 */
	@SquirrelJMEVendorApi
	public static boolean versionBefore(int __major, int __minor)
	{
		return !MeepRuntime.version().atLeast(__major, __minor);
	}
	
	/**
	 * Checks if the MEEP version is at least the given version.
	 *
	 * @param __major The major version.
	 * @param __minor The minor version.
	 * @return If this is at least the given DoJa version.
	 * @since 2025/04/18
	 */
	@SquirrelJMEVendorApi
	public static boolean versionLeast(int __major, int __minor)
	{
		return MeepRuntime.version().atLeast(__major, __minor);
	}
}
