// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.launch;

/**
 * Utilities for the suite scanner.
 *
 * @since 2024/01/06
 */
public final class ScannerUtils
{
	/**
	 * Not used.
	 * 
	 * @since 2024/01/06
	 */
	private ScannerUtils()
	{
	}
	
	/**
	 * Returns a sibling file with the same base name but a differing
	 * extension.
	 * 
	 * @param __jar The other Jar to check.
	 * @param __ext The extension to map to.
	 * @return The sibling file based on the extension.
	 * @since 2023/04/10
	 */
	public static String siblingByExt(String __jar, String __ext)
	{
		boolean lower = __jar.endsWith(".jar");
		boolean upper = __jar.endsWith(".JAR");
		
		// Does not end with it? Just append it
		if (!lower && !upper)
			return __jar + __ext;
		
		// Remove old extension and just append the new one
		String baseName = __jar.substring(0, __jar.length() - 4);
		if (upper)
			return baseName + __ext.toUpperCase();
		return baseName + __ext;
	}
}
