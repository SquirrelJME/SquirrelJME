// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.pvmjvm;

import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * This is the main class for the para-virtual machine.
 *
 * @since 2016/06/16
 */
public class Main
{
	/**
	 * Main entry point.
	 *
	 * @param __args The main arguments.
	 * @since 2016/06/16
	 */
	public static void main(String... __args)
	{
		// Must exist
		if (__args == null)
			__args = new String[0];
		
		// Some messages
		PrintStream out = System.out;
		out.println("SquirrelJME Paravirtual Machine");
		out.println(" Copyright (C) 2013-2016 Steven Gawroriski " +
			"<steven@multiphasicapps.net>");
		out.println(" Copyright (C) 2013-2016 Multi-Phasic Applications " +
			"<multiphasicapps.net>");
		out.println("SquirrelJME is under the GNU GPLv3 or later.");
		out.println();
		
		// Initialize the virtual machine
		PVM pvm = new PVM(Paths.get(System.getProperty("user.dir")), __args);
	}
}

