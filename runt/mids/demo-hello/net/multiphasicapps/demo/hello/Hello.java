// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.demo.hello;

import java.io.PrintStream;

/**
 * Prints a hello message along with information about the virtual machine.
 *
 * @since 2018/09/22
 */
public class Hello
{
	/** Properties to print. */
	private static String[] _PROPERTIES =
		new String[]
		{
			"java.version",
			"java.vendor",
			"java.vendor.email",
			"java.vendor.url",
			"java.vm.version",
			"java.vm.name",
			"java.vm.vendor",
			"java.vm.vendor.email",
			"java.vm.vendor.url",
			"java.runtime.name",
			"java.runtime.version",
			"os.name",
			"os.arch",
			"os.version",
			"microedition.locale",
			"microedition.profiles",
		};
	
	/**
	 * Main entry point.
	 *
	 * @param __args Program arguments.
	 * @since 2018/09/22
	 */
	public static final void main(String... __args)
	{
		PrintStream out = System.out;
		
		// Print input message if used
		int an;
		if (__args != null && (an = __args.length) > 0)
		{
			for (int i = 0; i < an; i++)
			{
				// Add space for nicer formatting
				if (i > 0)
					out.print(' ');
				
				out.print(__args[i]);
			}
			
			// Make it nice
			out.println();
		}
		
		// Just a basic message
		else
			out.println("Hello! Squirrels are so cute!");
		
		// Print some information about the virtual machine itself
		for (String p : Hello._PROPERTIES)
		{
			String v = System.getProperty(p);
			if (v != null)
			{
				out.print(p);
				out.print(": ");
				out.println(v);
			}
		}
	}
}

