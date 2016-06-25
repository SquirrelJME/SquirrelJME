// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.sjmebuilder;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import net.multiphasicapps.sjmepackages.PackageList;

/**
 * This is the main entry point for the builder.
 *
 * @since 2016/06/24
 */
public class Main
{
	/**
	 * Main entry point.
	 *
	 * @param __args Main program arguments.
	 * @since 2016/06/24
	 */
	public static void main(String... __args)
	{
		// Must exist
		if (__args == null)
			__args = new String[0];
		
		// Fill in queue for argument handling
		Deque<String> args = new LinkedList<>();
		for (String s : __args)
			args.offerLast(s);
		
		// Handle arguments
		boolean dosim = false;
		String target = null;
		List<String> simargs = new ArrayList<>();
		while (!args.isEmpty())
		{
			String a = args.removeFirst();
			
			// Simulate also?
			if (a.equals("-S") || a.equals("-s"))
				dosim = true;
			
			// {@squirreljme.error DW02 Unknown command line argument.
			// (The argument)}
			else if (a.startsWith("-"))
				throw new IllegalArgumentException(String.format("DW02 %s",
					a));
			
			// Get the target
			else
			{
				// Set
				target = a.trim();
				
				// {@squirreljme.error DW03 No arguments must follow the
				// target when not simulating.}
				if (!dosim && !args.isEmpty())
					throw new IllegalArgumentException("DW03");
				
				// Add remaining arguments to the simulator
				else
					while (!args.isEmpty())
						simargs.add(args.removeFirst());
				
				// Stop
				break;
			}
		}
		
		// {@squirreljme.error DW04 Usage: [-S] (target) [simulator arguments];
		// The target is the arch.os.variant to build a native executable for.
		// The -S switch may
		// be specified to also simulate the output resulting binary in which
		// case the arguments to the simulator may be passed following the
		// target.}
		if (target == null)
			throw new IllegalArgumentException("DW04");
		
		System.err.printf("DEBUG -- %s %s %s%n", dosim, target, simargs);
		
		// Split the triplet
		int deca = target.indexOf('.');
		int decb = (deca >= 0 ? target.indexOf('.', deca + 1) : -1);
		
		// {@squirreljme.error DW05 The twin or triplet must be in the form
		// of {@code arch.os} or {@code arch.os.variant}. (The input target)}
		if (deca < 0)
			throw new IllegalArgumentException(String.format("DW05 %s",
				target));
		
		// Split
		String arch = target.substring(0, deca).trim(),
			os = target.substring(deca + 1,
				(decb >= 0 ? decb : target.length())).trim(),
			var = (decb >= 0 ? target.substring(decb + 1) : "generic");
		
		System.err.printf("DEBUG -- %s %s %s%n", arch, os, var);
		
		// Load the package list
		PrintStream out = System.out;
		PackageList plist;
		try
		{
			out.println("Loading the package lists...");
			plist = new PackageList(Paths.get(System.getProperty("user.dir")),
				null);
		}
		
		// {@squirreljme.error DW01 Failed to load the package list.}
		catch (IOException e)
		{
			throw new RuntimeException("DW01", e);
		}
		
		// Could fail on perhaps a bad disk or malformed file
		try
		{
			// Setup builder
			out.println("Setting up build...");
			Builder b = new Builder(plist, arch, os, var);
		
			// Perform the build
			out.println("Building...");
			b.build();
		
			throw new Error("TODO");
		}
		
		// {@squirreljme.error DW07 Failed to build for the target due to an
		// IOException.}
		catch (IOException e)
		{
			throw new RuntimeException("DW07", e);
		}
	}
}

