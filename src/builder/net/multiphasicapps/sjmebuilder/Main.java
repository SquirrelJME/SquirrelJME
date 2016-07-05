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
import java.util.Collections;
import java.util.Comparator;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import net.multiphasicapps.sjmepackages.PackageInfo;
import net.multiphasicapps.sjmepackages.PackageList;
import net.multiphasicapps.squirreljme.java.manifest.JavaManifest;
import net.multiphasicapps.squirreljme.java.manifest.JavaManifestAttributes;
import net.multiphasicapps.squirreljme.jit.JITTriplet;

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
		
		// {@squirreljme.error DW04 Usage: [-S] (target) [simulator arguments];
		// The target is the arch.os.variant to build a native executable for.
		// The -S switch may
		// be specified to also simulate the output resulting binary in which
		// case the arguments to the simulator may be passed following the
		// target.}
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
		
			throw new Error("TODO");
		}
		
		// {@squirreljme.error DW07 Failed to build for the target due to an
		// IOException.}
		catch (IOException e)
		{
			throw new RuntimeException("DW07", e);
		}
	}
	
	/**
	 * Prints the detected target architectures and operating systems to
	 * the given output stream.
	 *
	 * @param __plist The list of available packages.
	 * @param __ps The output print stream.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/01
	 */
	private static void __printDetected(PackageList __plist, PrintStream __ps)
		throws NullPointerException
	{
		// Check
		if (__plist == null || __ps == null)
			throw new NullPointerException("NARG");
		
		// Go through every package and extract available operating systems
		// and OS variants
		List<String> availos = new ArrayList<>();
		for (PackageInfo pi : __plist.values())
		{
			// Get the manifest
			JavaManifest man = pi.manifest();
			if (man == null)
				continue;
			
			// See if the OS key exists
			JavaManifestAttributes attr = man.getMainAttributes();
			String vkey = attr.get(Builder.TARGET_OS_KEY);
			
			// Add to list if it does
			if (vkey != null)
				availos.add(vkey.trim());
		}
		
		// Sort by name
		Collections.<String>sort(availos);
		
		// Print
		__ps.println("Available Targets:");
		for (String os : availos)
		{
			__ps.print(" * ");
			__ps.println(os);
		}
		__ps.println();
	}
}

