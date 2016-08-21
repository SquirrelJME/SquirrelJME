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

import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ServiceLoader;
import java.util.Set;

/**
 * This parses the input command line arguments.
 *
 * @since 2016/08/20
 */
class __CommandLine__
{
	/** Emulate after execution? */
	final boolean _doemu;
	
	/** Do not build in a JIT. */
	final boolean _nojit;
	
	/** Run tests. */
	final boolean _tests;
	
	/** Skip building. */
	final boolean _skipbuild;
	
	/** The target to target. */
	final String _target;
	
	/** The output ZIP name. */
	final String _outzipname;
	
	/** The alternative executable name. */
	final String _altexename;
	
	/** The arguments to the emulator. */
	final List<String> _emuargs;
	
	/** The extra projects to include .*/
	final Set<String> _extraprojects;
	
	/** Was the direct emulate option specified? */
	final boolean _directemu;
	
	/** The path to the directly executed binary. */
	final Path _directemupath;
	
	/**
	 * Parses the command line arguments.
	 *
	 * @param __args Input arguments.
	 * @since 2016/08/20
	 */
	__CommandLine__(String... __args)
	{
		// Force to exist
		if (__args == null)
			__args = new String[0];
		
		// Fill in queue for argument handling
		Deque<String> args = new LinkedList<>();
		for (String s : __args)
			args.offerLast(s);
		
		// If using the special -l option then a given binary somewhere is to
		// be read and executed
		if (__args.length > 1 && "-l".equals(__args[0]))
		{
			// These options are enforced
			this._doemu = true;
			this._nojit = true;
			this._tests = false;
			this._skipbuild = true;
			this._outzipname = "";
			this._extraprojects = new HashSet<>();
			this._directemu = true;
			
			// Eat -l
			args.removeFirst();
			
			// {@squirreljme.error DW03 Not enough arguments specified for the
			// -l option.}
			if (args.size() < 2)
				throw new IllegalArgumentException("DW03");
			
			// Target follows
			this._target = args.removeFirst();
			
			// Determine path to the binary to be read
			Path p = Paths.get(args.removeFirst());
			this._directemupath = p;
			
			// Alternative name is the final file's file name (without any
			// starting path)
			this._altexename = p.getFileName().toString();
			
			// And any arguments follow
			List<String> eargs = new ArrayList<>();
			while (!args.isEmpty())
				eargs.add(args.removeFirst());
			this._emuargs = eargs;
			
			// Stop
			return;
		}
		
		// Handle arguments
		boolean doemu = false;
		boolean nojit = false;
		boolean tests = false;
		boolean skipbuild = false;
		String target = null;
		String outzipname = null;
		String altexename = null;
		List<String> emuargs = new ArrayList<>();
		Set<String> extraprojects = new HashSet<>();
		while (!args.isEmpty())
		{
			String a = args.removeFirst();
			
			// Add a project?
			if (a.equals("-a"))
			{
				// {@squirreljme.error DW0z Adding a project requires that
				// one actually be specified.}
				String addthis = args.removeFirst();
				if (addthis == null)
					throw new IllegalArgumentException("DW0z");
				
				// Add it
				extraprojects.add(addthis);
			}
			
			// {@squirreljme.error DW01 Cannot specify -l when other options
			// have been specified.}
			else if (a.equals("-l"))
				throw new IllegalArgumentException("DW01");
			
			// Emulate also?
			else if (a.equals("-e"))
				doemu = true;
			
			// Do not include a JIT?
			else if (a.equals("-n"))
				nojit = true;
			
			// Include tests also?
			else if (a.equals("-t"))
				tests = true;
			
			// Skip building?
			else if (a.equals("-s"))
				skipbuild = true;
			
			// Alternative executable name?
			else if (a.equals("-x"))
			{
				altexename = args.removeFirst();
				
				// {@squirreljme.error DW0u The alternative executable name
				// requires an argument.}
				if (altexename == null)
					throw new IllegalArgumentException("DW0u");
			}
			
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
				
				// Get the output ZIP name.
				if (args.size() >= 1)
					outzipname = args.removeFirst();
				
				// Add emulator arguments
				while (!args.isEmpty())
					emuargs.add(args.removeFirst());
				
				// Stop
				break;
			}
		}
		
		// If no target, print usage
		if (target == null)
		{
			__printUsage(System.out);
			
			// {@squirreljme.error DW0h Not enough arguments.}
			throw new IllegalArgumentException("DW0h");
		}
		
		// Set
		this._doemu = doemu;
		this._nojit = nojit;
		this._tests = tests;
		this._skipbuild = skipbuild;
		this._target = target;
		this._outzipname = outzipname;
		this._altexename = altexename;
		this._emuargs = emuargs;
		this._extraprojects = extraprojects;
		this._directemu = false;
		this._directemupath = null;
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
		__ps.println("Usage: [-a project] [-e] [-n] [-s] [-t] [-x name] " +
			"(target) [squirreljme.zip] [emulator arguments...]");
		__ps.println("Usage: -l (target) executable [arguments...]");
		__ps.println();
		__ps.println("\tThe output ZIP is optionally specified, however");
		__ps.println("\tif emulator arguments are specified the ZIP must");
		__ps.println("\talso be specified.");
		__ps.println();
		__ps.println("\t-a\tInclude the specified project in the target.");
		__ps.println("\t\tMay be specified multiple times.");
		__ps.println("\t-e\tAfter building, emulate the target binary.");
		__ps.println("\t-l\tJust run the specified executable with the");
		__ps.println("\t\tspecified arguments in the target emulator.");
		__ps.println("\t-n\tDo not include a JIT.");
		__ps.println("\t-s\tSkip building and just emulate the ZIP.");
		__ps.println("\t-t\tInclude tests.");
		__ps.println("\t-x\tAlternative name for the binary executable");
		__ps.println("\t\tinstead of using the default name.");
		__ps.println();
		
		// Suggest target header
		__ps.println("Suggested Triplets (Target):");
		__ps.printf("%74s (?)", "Name of Suggestion");
		__ps.println();
		__ps.println();
		
		// Print suggested targets
		for (TargetBuilder tb : ServiceLoader.<TargetBuilder>load(
			TargetBuilder.class))
			for (TargetSuggestion s : tb.suggestedTargets())
			{
				__ps.printf("%-78s%n%74s (%c)", s.triplet(), s.name(),
					(tb.canJIT() ? '+' : '-'));
				__ps.println();
			}
		__ps.println();
		__ps.println(" (+) The JIT is supported.");
		__ps.println(" (-) The JIT is not supported.");
		
		// End
		__ps.println();
	}
}

