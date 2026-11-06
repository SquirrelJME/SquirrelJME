// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.demo.hello;

import java.io.PrintStream;

/**
 * Prints a hello message along with information about the virtual machine.
 *
 * @since 2018/09/22
 */
@SuppressWarnings("DuplicateStringLiteralInspection")
public class Hello
{
	/** Redacted properties. */
	private static final String[] _REDACTED =
		new String[]
		{
			"microedition.hostname",
			"microedition.deviceid",
			"microedition.deviceid.uuid",
			"microedition.deviceid.imei",
			"microedition.deviceid.meid",
			"microedition.deviceid.esn",
			"microedition.deviceid.pesn",
			"microedition.deviceid.isunique",
			"microedition.devicevendor.isunique",
		};
	
	/** Properties to print. */
	private static final String[] _PROPERTIES =
		new String[]
		{
			"java.version",
			"java.vendor",
			"java.vendor.email",
			"java.vendor.url",
			"java.vm.name",
			"java.vm.version",
			"java.vm.vendor",
			"java.vm.vendor.email",
			"java.vm.vendor.url",
			"java.runtime.name",
			"java.runtime.version",
			"os.name",
			"os.arch",
			"os.version",
			"user.dir",
			"microedition.encoding",
			"microedition.configuration",
			"microedition.devicevendor",
			"microedition.locale",
			"microedition.profiles",
			"microedition.platform",
			"cc.squirreljme.vm.execpath",
			"cc.squirreljme.vm.freemem",
			"cc.squirreljme.vm.totalmem",
			"cc.squirreljme.vm.maxmem",
			"microedition.commports",
			"microedition.lui",
			"microedition.key",
			"microedition.rms",
			"microedition.media.audio",
			"microedition.io",
			"microedition.io.gcf",
			"microedition.swm",
			"microedition.cellular",
			"microedition.event",
			"microedition.power",
			"file.separator",
		};
	
	/**
	 * Main entry point.
	 *
	 * @param __args Program arguments.
	 * @since 2018/09/22
	 */
	public static void main(String... __args)
	{
		PrintStream out = System.out;
		
		// Print input message if used
		if (__args != null)
		{
			// Print out each message
			int an = __args.length;
			for (int i = 0; i < an; i++)
			{
				// Add space for nicer formatting
				if (i > 0)
					out.print(' ');
				
				out.print(__args[i]);
			}
			
			// Make it nice
			if (an > 0)
				out.println();
		}
		
		// Just a basic message
		else
			out.println("Hello! Squirrels are so cute!");
		
		// Print some information about the virtual machine itself
		for (String p : Hello._PROPERTIES)
		{
			out.print(p);
			out.print(": ");
			
			String v = System.getProperty(p);
			if (v != null)
				out.println(v);
			else
				out.println("<NOT SET>");
		}
		
		// Then redacted properties
		for (String p : Hello._REDACTED)
		{
			out.print(p);
			out.print(": ");
			
			if (System.getProperty(p) != null)
				out.println("[REDACTED]");
			else
				out.println("<NOT SET>");
		}
	}
}

