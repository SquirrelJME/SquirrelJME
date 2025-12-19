// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.midlet;

import cc.squirreljme.jvm.launch.IModeProperty;
import cc.squirreljme.jvm.suite.Profile;
import cc.squirreljme.jvm.suite.SuiteVersion;
import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Runtime properties for DoJa applications.
 *
 * @since 2024/07/28
 */
@SquirrelJMEVendorApi
public final class DoJaRuntime
{
	/** Key which specifies the launch type. */
	@SquirrelJMEVendorApi
	public static final String LAUNCH_TYPE =
		"X-SquirrelJME-DoJa-LaunchType";
	
	/** Internally set properties. */
	private static final Map<String, String> _PROPERTIES =
		new LinkedHashMap<>();
	
	/** The cached DoJa version. */
	private static volatile SuiteVersion _VERSION;
	
	/** Is this DoJa? */
	private static volatile boolean _isDoJa;
	
	/**
	 * Not used.
	 *
	 * @since 2024/07/28
	 */
	private DoJaRuntime()
	{
	}
	
	/**
	 * Gets a key from internal storage.
	 *
	 * @param __key The key to get.
	 * @return The resultant value, will be {@code null} if there is none.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/07/28
	 */
	@SquirrelJMEVendorApi
	public static String getProperty(String __key)
		throws NullPointerException
	{
		if (__key == null)
			throw new NullPointerException("NARG");
		
		// Get from system first
		String sys = System.getProperty(
			IModeProperty.DOJA_PROFILE_PROPERTY + "." + __key);
		if (sys != null)
			return sys;
		
		// Otherwise from the stored map
		synchronized (DoJaRuntime.class)
		{
			return DoJaRuntime._PROPERTIES.get(__key);
		}
	}
	
	/**
	 * Is this DoJa?
	 *
	 * @return If this is DoJa.
	 * @since 2025/06/03
	 */
	@SquirrelJMEVendorApi
	public static boolean isDoJa()
	{
		synchronized (DoJaRuntime.class)
		{
			return DoJaRuntime._isDoJa;
		}
	}
	
	/**
	 * Puts a property into the internal mapping.
	 *
	 * @param __key The key.
	 * @param __value The value.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/07/28
	 */
	@SquirrelJMEVendorApi
	public static void putProperty(String __key, String __value)
		throws NullPointerException
	{
		if (__key == null || __value == null)
			throw new NullPointerException("NARG");
		
		synchronized (DoJaRuntime.class)
		{
			DoJaRuntime._PROPERTIES.put(__key, __value);
		}
	}
	
	/**
	 * Sets that this is DoJa.
	 *
	 * @param __set The value to set.
	 * @since 2025/06/03
	 */
	@SquirrelJMEVendorApi
	public static void setDoJa(boolean __set)
	{
		synchronized (DoJaRuntime.class)
		{
			DoJaRuntime._isDoJa = __set;
		}
	}
	
	/**
	 * Returns the DoJa version.
	 *
	 * @return The DoJa version.
	 * @since 2025/04/09
	 */
	@SquirrelJMEVendorApi
	public static SuiteVersion version()
	{
		// Already cached?
		SuiteVersion version = DoJaRuntime._VERSION;
		if (version != null)
			return version;
		
		// Setup version based on the current profile
		String profile = DoJaRuntime.getProperty(
			IModeProperty.ADF_PROPERTY_PREFIX);
		if (profile != null && !profile.isEmpty())
			version = new Profile(profile.trim()).version();
		else
			version = new SuiteVersion(5, 1);
		
		// Cache and use it
		DoJaRuntime._VERSION = version;
		return version;
	}
	
	/**
	 * Checks if the DoJa version is before the given version.
	 *
	 * @param __major The major version.
	 * @param __minor The minor version.
	 * @return If this is before the given DoJa version.
	 * @since 2025/04/09
	 */
	@SquirrelJMEVendorApi
	public static boolean versionBefore(int __major, int __minor)
	{
		return DoJaRuntime.isDoJa() &&
			!DoJaRuntime.version().atLeast(__major, __minor);	
	}
	
	/**
	 * Checks if the DoJa version is at least the given version.
	 *
	 * @param __major The major version.
	 * @param __minor The minor version.
	 * @return If this is at least the given DoJa version.
	 * @since 2025/04/09
	 */
	@SquirrelJMEVendorApi
	public static boolean versionLeast(int __major, int __minor)
	{
		return DoJaRuntime.isDoJa() &&
			DoJaRuntime.version().atLeast(__major, __minor);	
	}
}
