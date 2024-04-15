// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.suite;

/**
 * General suite utils.
 *
 * @since 2024/01/06
 */
public final class SuiteUtils
{
	/**
	 * Not used.
	 * 
	 * @since 2024/01/06
	 */
	private SuiteUtils()
	{
	}
	
	/**
	 * Returns the base name of the given file name.
	 *
	 * @param __name The name to get the base name of.
	 * @return The resultant base name.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/01/06
	 */
	public static String baseName(String __name)
		throws NullPointerException
	{
		if (__name == null)
			throw new NullPointerException("NARG");
		
		// Already the base name?
		int lastSlash = __name.lastIndexOf('/');
		if (lastSlash < 0)
			return __name;
		
		// Path separator?
		String fileSep = System.getProperty("file.separator");
		if (fileSep.length() == 1)
		{
			int lastSep = __name.lastIndexOf(fileSep.charAt(0));
			if (lastSep >= 0 && lastSep >= lastSlash)
				return __name.substring(lastSep + 1);
			return __name.substring(lastSlash + 1);
		}
		
		// Otherwise place it down
		return __name.substring(lastSlash + 1);
	}
	
	/**
	 * Is this a Jar or resource?
	 *
	 * @param __name The name to check.
	 * @return If it is a Jar or resource.
	 * @since 2024/01/06
	 */
	public static boolean isAny(String __name)
	{
		return SuiteUtils.isJar(__name) || SuiteUtils.isResource(__name);
	}
	
	/**
	 * Is this a Jar?
	 *
	 * @param __name The name to check.
	 * @return If it is a Jar.
	 * @since 2024/01/06
	 */
	public static boolean isJar(String __name)
	{
		return __name.endsWith(".jar") || __name.endsWith(".JAR") ||
			__name.endsWith(".kjx") || __name.endsWith(".KJX");
	}
	
	/**
	 * Is this a resource, that should be included but might not be a Jar?
	 *
	 * @param __name The name to check.
	 * @return If it is a resource.
	 * @since 2024/01/06
	 */
	public static boolean isResource(String __name)
	{
		// Standard Jar
		return __name.endsWith(".jad") || __name.endsWith(".JAD") ||
			
			// i-mode
			__name.endsWith(".jam") || __name.endsWith(".JAM") ||
			__name.endsWith(".adf") || __name.endsWith(".ADF") ||
			__name.endsWith(".sec") || __name.endsWith(".SEC") ||
			__name.endsWith(".sto") || __name.endsWith(".STO") ||
			__name.endsWith(".sp") || __name.endsWith(".SP");
	}
}
