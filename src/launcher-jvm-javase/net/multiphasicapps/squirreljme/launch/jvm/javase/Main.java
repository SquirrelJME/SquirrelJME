// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.launch.jvm.javase;

import net.multiphasicapps.squirreljme.launch.ConsoleUserInterface;

/**
 * Main entry point for the Java SE JVM launcher interface kernel.
 *
 * @since 2016/05/14
 */
public class Main
{
	/**
	 * Main entry point.
	 *
	 * @param __args Program arguments.
	 * @since 2016/05/14
	 */
	public static void main(String... __args)
	{
		// Initialize the main launcher and run the main loop
		ConsoleUserInterface cui = new ConsoleUserInterface(
			new JVMJavaSEKernel(__args));
	}
}

