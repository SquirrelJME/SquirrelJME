// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.mp;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.io.IOException;
import java.io.PrintStream;

/**
 * Command line version of the media player.
 *
 * @since 2026/01/04
 */
@SquirrelJMEVendorApi
public class CommandLine
{
	/**
	 * Main entry point.
	 *
	 * @param __args Program arguments.
	 * @throws IOException On read/write errors,
	 * @since 2026/01/04
	 */
	@SquirrelJMEVendorApi
	public static void main(String... __args)
		throws IOException
	{
		if (__args == null || __args.length < 1 || __args[0] == null)
		{
			// Print help
			CommandLine.printHelp();
			
			// Fail
			System.exit(1);
			return;
		}
		
		// Which command was passed?
		switch (__args[0])
		{
				// List directory contents
			case "ls":
			case "list":
				throw Debugging.todo();
				
				// Play a given URL
			case "play":
				throw Debugging.todo();
			
				// Help
			case "help":
				CommandLine.printHelp();
				break;
			
			default:
				System.err.printf("Unknown command: %s", __args[0]);
				
				// print help
				CommandLine.printHelp();
				
				// Fail
				System.exit(1);
				break;		
		}
	}
	
	/**
	 * Prints help text.
	 *
	 * @since 2026/01/04
	 */
	public static void printHelp()
	{
		PrintStream out = System.err;
		
		// Print everything
		out.println("Usage: command (options)");
		out.println("\tlist url -- List contents of the given URL");
		out.println("\tplay url -- Play the given URL");
	}
}
