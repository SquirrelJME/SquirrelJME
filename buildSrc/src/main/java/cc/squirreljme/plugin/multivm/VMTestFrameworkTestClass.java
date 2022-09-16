// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.multivm;

/**
 * Represents a test class.
 *
 * @since 2022/09/14
 */
public final class VMTestFrameworkTestClass
{
	/** The class name this is in. */
	public String className;
	
	/** The variant. */
	public String variant;
	
	/** The normal test. */
	public String normal;
	
	/**
	 * Initializes the class name reference.
	 * 
	 * @param __testName The name of the test.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/09/14
	 */
	public VMTestFrameworkTestClass(String __testName)
		throws NullPointerException
	{
		if (__testName == null)
			throw new NullPointerException("NARG");
		
		// Split off from the test and the class
		int lastAt = __testName.lastIndexOf('@');
		int lastSlash = __testName.lastIndexOf('.');
		
		// Determine the actual class name and variant
		this.normal = __testName;
		this.className = (lastAt >= 0 && lastAt > lastSlash ?
			__testName.substring(0, lastAt) : __testName);
		this.variant = (lastAt >= 0 && lastAt > lastSlash ?
			__testName.substring(lastAt + 1) : null);
	}
}
