// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.util;

import java.nio.file.Path;

/**
 * Utilities regarding test detection.
 *
 * @since 2020/08/28
 */
public class TestDetection
{
	/**
	 * Not used.
	 * 
	 * @since 2020/08/28
	 */
	private TestDetection()
	{
	}
	
	/**
	 * Is this considered a test?
	 *
	 * @param __path The path to check.
	 * @return If this is considered a test.
	 * @since 2020/02/28
	 */
	public static boolean isTest(Path __path)
		throws NullPointerException
	{
		if (__path == null)
			throw new NullPointerException("NARG");
			
		// Only consider source files
		String baseName = __path.getFileName().toString();
		if (!baseName.endsWith(".java") && !baseName.endsWith(".j"))
			return false;
		
		// Use class name calculation
		return TestDetection.isTest(__path.getFileName().toString());
	}
	
	/**
	 * Is this considered a test?
	 *
	 * @param __className The class name to check.
	 * @return If this is considered a test.
	 * @since 2020/08/28
	 */
	public static boolean isTest(String __className)
		throws NullPointerException
	{	
		if (__className == null)
			throw new NullPointerException("NARG");
		
		// Get base name of the file
		String base = __className.substring(0,
			__className.lastIndexOf('.'));
		
		return (base.startsWith("Do") || base.startsWith("Test") ||
			base.endsWith("Test"));
	}
}
