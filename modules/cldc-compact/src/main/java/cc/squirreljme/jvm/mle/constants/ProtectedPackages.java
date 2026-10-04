// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.constants;

/**
 * This class contains all of the packages which are protected and are not
 * permitted to have additional classes added within.
 *
 * @since 2020/06/21
 */
public final class ProtectedPackages
{
	/**
	 * Not used.
	 * 
	 * @since 2020/06/21
	 */
	private ProtectedPackages()
	{
	}
	
	/**
	 * Checks to see if the package is protected, if it is then no other
	 * JAR may add to it. This will be recursively checked so any package
	 * under a given one will not be permitted.
	 * 
	 * @param __name The package name, must be in binary name form.
	 * @return If this package is protected.
	 * @throws IllegalArgumentException If the name contains a period as this
	 * means it potentially is being called incorrectly.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/06/21
	 */
	public static boolean isProtectedPackage(String __name)
		throws IllegalArgumentException, NullPointerException
	{
		if (__name == null)
			throw new NullPointerException("NARG");
		
		/* {@squirreljme.error ZZ37 Package contains a period. (The name)} */
		if (__name.indexOf('.') >= 0)
			throw new IllegalArgumentException("ZZ37 " + __name);
		
		// Try these
		switch (__name)
		{
				// These packages must never be changed
			case "cc/squirreljme/jvm":
			case "java/lang":
				return true;
		}
		
		// No more packages to check
		int lastSlash = __name.lastIndexOf('/');
		if (lastSlash < 0)
			return false;
		
		// Recurse into parent packages
		return ProtectedPackages.isProtectedPackage(
			__name.substring(0, lastSlash));
	}
}
