// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.test;

import java.io.InputStream;

/**
 * Contains example classes that can be used for testing.
 *
 * @since 2022/08/14
 */
public enum ExampleClass
{
	/** Blank class which just contains an empty constructor. */
	BLANK("Blank.class"),
	
	/** Contains a basic counting for loop. */
	BASIC_FOR_LOOP("BasicForLoop.class"),
	
	/** Monitor enter then exit. */
	MONITOR_ENTER_EXIT("MonitorEnterExit.class"),
	
	/* End. */
	;
	
	/** The resource name used. */
	public final String resourceName;
	
	/**
	 * Sets the example class.
	 * 
	 * @param __resourceName The resource name.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/08/14
	 */
	ExampleClass(String __resourceName)
		throws NullPointerException
	{
		if (__resourceName == null)
			throw new NullPointerException("NARG");
		
		this.resourceName = __resourceName;
	}
	
	/**
	 * Returns the name of the example class.
	 * 
	 * @return The name of this class.
	 * @since 2022/08/14
	 */
	public final String className()
	{
		String resourceName = this.resourceName;
		if (resourceName.endsWith(".class"))
			return resourceName.substring(0,
				resourceName.length() - ".class".length());
		return resourceName;
	}
	
	/**
	 * Returns the stream to the given class.
	 * 
	 * @return The loaded class stream.
	 * @since 2022/08/14
	 */
	public InputStream load()
	{
		return ExampleClass.class
			.getResourceAsStream("test-classes/" + this.resourceName);
	}
}
