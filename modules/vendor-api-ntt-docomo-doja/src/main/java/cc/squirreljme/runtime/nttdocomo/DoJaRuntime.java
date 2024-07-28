// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.nttdocomo;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Runtime properties for DoJa applications.
 *
 * @since 2024/07/28
 */
public final class DoJaRuntime
{
	/** Internally set properties. */
	private static final Map<String, String> _PROPERTIES =
		new LinkedHashMap<>();
	
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
	public static String getProperty(String __key)
		throws NullPointerException
	{
		if (__key == null)
			throw new NullPointerException("NARG");
		
		// Get from system first
		String sys = System.getProperty(__key);
		if (sys != null)
			return sys;
		
		// Otherwise from the stored map
		synchronized (DoJaRuntime.class)
		{
			return DoJaRuntime._PROPERTIES.get(__key);
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
}
