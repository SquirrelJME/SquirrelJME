// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc;

import cc.squirreljme.jvm.mle.RuntimeShelf;
import cc.squirreljme.jvm.mle.constants.PhoneModelType;
import cc.squirreljme.jvm.mle.constants.VMDescriptionType;
import cc.squirreljme.jvm.suite.SuiteVersion;

/**
 * Contains information on SquirrelJME.
 *
 * @since 2018/12/05
 */
@SuppressWarnings("unused")
public final class SquirrelJME
{
	/** The major version of SquirrelJME. */
	public static final byte MAJOR_VERSION =
		0;
	
	/** The minor version of SquirrelJME. */
	public static final byte MINOR_VERSION =
		3;
	
	/** The release version of SquirrelJME. */
	public static final byte RELEASE_VERSION =
		0;
	
	/** The version of this SquirrelJME runtime. */
	public static final String RUNTIME_VERSION =
		"0.3.0";
	
	/** The microedition platform. */
	public static final String MICROEDITION_PLATFORM =
		"SquirrelJME/0.3.0";
	
	/** The cached API version. */
	private static volatile SuiteVersion _API_VERSION;
	
	/** The cached Runtime version. */
	private static volatile SuiteVersion _RUNTIME_VERSION;
	
	/**
	 * Not used.
	 * 
	 * @since 2022/02/14
	 */
	private SquirrelJME()
	{
	}
	
	/**
	 * Returns the {@code microedition.platform} property of the machine.
	 *
	 * @param __phoneModel The current phone model.
	 * @since 2022/02/14
	 */
	public static String platform(int __phoneModel)
	{
		// This can vary
		String base = SquirrelJME.MICROEDITION_PLATFORM;
		switch (__phoneModel)
		{
			case PhoneModelType.NTT_DOCOMO_D503I:
				return base + " D503i";
			
			case PhoneModelType.NTT_DOCOMO_F503I:
				return base + " F503i";
			
			case PhoneModelType.NTT_DOCOMO_SO503I:
				return base + " So503i";
			
			case PhoneModelType.NTT_DOCOMO_P503I:
				return base + " P503i";
		}
		
		// Unknown
		return base;
	}
	
	/**
	 * Returns the SquirrelJME API version.
	 *
	 * @return The SquirrelJME API version.
	 * @since 2026/10/03
	 */
	public static SuiteVersion versionApi()
	{
		// Already cached?
		SuiteVersion version = SquirrelJME._API_VERSION;
		if (version != null)
			return version;
		
		// Use the API version from the VM, or fallback to a "nothing" version
		String desc = RuntimeShelf.vmDescription(
			VMDescriptionType.SQUIRRELJME_API_VERSION);
		if (desc != null && !desc.isEmpty())
			version = new SuiteVersion(desc);
		else
			version = new SuiteVersion("0.0.0");
		
		// Cache and use it
		SquirrelJME._API_VERSION = version;
		return version;
	}
	
	/**
	 * Checks if the SquirrelJME API version is before the given version.
	 *
	 * @param __major The major version.
	 * @param __minor The minor version.
	 * @return If this is before the given SquirrelJME API version.
	 * @since 2026/10/03
	 */
	public static boolean versionApiBefore(int __major, int __minor)
	{
		return SquirrelJME.versionApi().atLeast(__major, __minor);	
	}
	
	/**
	 * Checks if the SquirrelJME API version is at least the given version.
	 *
	 * @param __major The major version.
	 * @param __minor The minor version.
	 * @return If this is at least the given SquirrelJME API version.
	 * @since 2026/10/03
	 */
	public static boolean versionApiLeast(int __major, int __minor)
	{
		return SquirrelJME.versionApi().atLeast(__major, __minor);
	}
	
	/**
	 * Checks if the API version and the runtime version are compatible
	 * with the current version. This can be used to determine if an actual
	 * native API is available.
	 *
	 * @return If the version is compatible with the current runtime and
	 * API versions.
	 * @since 2026/10/03
	 */
	public static boolean versionCompatible()
	{
		SuiteVersion apiVer = SquirrelJME.versionApi();
		return SquirrelJME.versionCompatible(SquirrelJME.MAJOR_VERSION,
				SquirrelJME.MINOR_VERSION) &&
			SquirrelJME.versionCompatible(apiVer.major(), apiVer.minor());
	}
	
	/**
	 * Checks if the API version and the runtime version are compatible
	 * with the given version. This can be used to determine if an actual
	 * native API is available.
	 *
	 * @param __major The major version to check.
	 * @param __minor The minor version to check.
	 * @return If the version is compatible.
	 * @since 2026/10/03
	 */
	public static boolean versionCompatible(int __major, int __minor)
	{
		// Get both version types
		SuiteVersion api = SquirrelJME.versionApi();
		SuiteVersion runtime = SquirrelJME.versionRuntime();
		
		// Note that for API versions, odd numbers are always rounded up to
		// the next even number as they are development builds. Thus, 0.3.0
		// is the development build for 0.4.0, so anything in 0.4.0 should
		// loosely be in 0.3.0.
		if ((__minor % 2) != 0)
			return api.atLeast(__major, __minor - 1) &&
				runtime.atLeast(__major, __minor);
		return api.atLeast(__major, __minor) &&
			runtime.atLeast(__major, __minor);
	}
	
	/**
	 * Returns the SquirrelJME Runtime version.
	 *
	 * @return The SquirrelJME Runtime version.
	 * @since 2026/10/03
	 */
	public static SuiteVersion versionRuntime()
	{
		// Already cached?
		SuiteVersion version = SquirrelJME._RUNTIME_VERSION;
		if (version != null)
			return version;
		
		// This is always the value defined here
		version = new SuiteVersion(SquirrelJME.RUNTIME_VERSION);
		
		// Cache and use it
		SquirrelJME._RUNTIME_VERSION = version;
		return version;
	}
	
	/**
	 * Checks if the SquirrelJME Runtime version is before the given version.
	 *
	 * @param __major The major version.
	 * @param __minor The minor version.
	 * @return If this is before the given SquirrelJME Runtime version.
	 * @since 2026/10/03
	 */
	public static boolean versionRuntimeBefore(int __major, int __minor)
	{
		return SquirrelJME.versionRuntime().atLeast(__major, __minor);
	}
	
	/**
	 * Checks if the SquirrelJME Runtime version is at least the given version.
	 *
	 * @param __major The major version.
	 * @param __minor The minor version.
	 * @return If this is at least the given SquirrelJME Runtime version.
	 * @since 2026/10/03
	 */
	public static boolean versionRuntimeLeast(int __major, int __minor)
	{
		return SquirrelJME.versionRuntime().atLeast(__major, __minor);
	}
}

