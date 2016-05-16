// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.kernel.jvm.javase;

import net.multiphasicapps.squirreljme.kernel.ConsoleUserInterface;
import net.multiphasicapps.squirreljme.kernel.Kernel;

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
		Kernel kern;
		ConsoleUserInterface cui = new ConsoleUserInterface(
			(kern = new JVMJavaSEKernel(__args)));
		
		// Create thread for the console interface
		kern.newThread(cui);
		
		// Block until all workers are terminated
		kern.untilThreadless();
	}
}

