// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.builder;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import net.multiphasicapps.sjmepackages.PackageInfo;
import net.multiphasicapps.sjmepackages.PackageList;
import net.multiphasicapps.squirreljme.java.manifest.JavaManifest;
import net.multiphasicapps.squirreljme.java.manifest.JavaManifestAttributes;
import net.multiphasicapps.squirreljme.jit.base.JITTriplet;

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
		boolean doemu = false;
		boolean nojit = false;
		boolean tests = false;
		String target = null;
		List<String> emuargs = new ArrayList<>();
		while (!args.isEmpty())
		{
			String a = args.removeFirst();
			
			// Emulate also?
			if (a.equals("-e"))
				doemu = true;
			
			// Do not include a JIT?
			else if (a.equals("-n"))
				nojit = true;
			
			// Include tests also?
			else if (a.equals("-t"))
				tests = true;
			
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
				if (!doemu && !args.isEmpty())
					throw new IllegalArgumentException("DW03");
				
				// Add remaining arguments to the simulator
				else
					while (!args.isEmpty())
						emuargs.add(args.removeFirst());
				
				// Stop
				break;
			}
		}
		
		// Output
		PrintStream out = System.out;
		
		// If no target, print usage
		if (target == null)
		{
			__printUsage(out);
			
			// {@squirreljme.error DW0h Not enough arguments.}
			throw new IllegalArgumentException("DW0h");
		}
		
		// Setup build configuration
		BuildConfig config = new BuildConfig(new JITTriplet(target), doemu,
			emuargs.<String>toArray(new String[emuargs.size()]), !nojit,
			tests);
		
		throw new Error("TODO");
		/*
		// Load the package list
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
		
		// {@squirreljme.error DW04 Usage: [-e] [-n] [-t] (target) [simulator
		// arguments];
		// The target is the arch.os.variant to build a native executable for.
		// The -e switch may
		// be specified to also emulate the output resulting binary in which
		// case the arguments to the emulator may be passed following the
		// target. The -n switch disables the JIT. The -t switch includes the
		// tests.}
		if (target == null)
		{
			// Print all detected targets and architectures (with their
			// variants)
			out.println();
			__printDetected(plist, out);
			
			// Fail
			throw new IllegalArgumentException("DW04");
		}
		
		// Could fail on perhaps a bad disk or malformed file
		try
		{
			// Setup builder
			out.println("Setting up build...");
			Builder b = new Builder(plist, new JITTriplet(target));
		
			// Perform the build
			out.println("Building...");
			b.build();
			
			// If simulating, run it in the simulator
			if (doemu)
				throw new Error("TODO");
		}
		
		// {@squirreljme.error DW07 Failed to build for the target due to an
		// IOException.}
		catch (IOException e)
		{
			throw new RuntimeException("DW07", e);
		}
		*/
	}
	
	/**
	 * Prints usage information.
	 *
	 * @param __ps The output print stream.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/22
	 */
	private static void __printUsage(PrintStream __ps)
		throws NullPointerException
	{
		// Check
		if (__ps == null)
			throw new NullPointerException("NARG");
		
		// Print header
		__ps.println("Usage: [-e] [-n] [-t] (target) [emulator arguments...]");
		__ps.println();
		__ps.println("\t-e\tAfter building, emulate the target binary.");
		__ps.println("\t-n\tDo not include a JIT.");
		__ps.println("\t-t\nInclude tests.");
		__ps.println();
		
		// Print suggested targets
		__ps.println("Suggested targets:");
		for (TargetBuilder tb : ServiceLoader.<TargetBuilder>load(
			TargetBuilder.class))
			for (TargetSuggestion s : tb.suggestedTargets())
				__ps.printf("%-40s %37s%n", s.triplet(), s.name());
	}
}

