// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.tac.runner;

import java.io.PrintStream;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

/**
 * Main entry class for the TAC runner system.
 *
 * @since 2018/10/17
 */
public class Main
{
	/**
	 * Main entry point.
	 *
	 * @param __args Arguments, used to filter tests.
	 * @since 2018/10/17
	 */
	public static void main(String... __args)
	{
		// Load arguments
		if (__args == null)
			__args = new String[0];
		Deque<String> args = new LinkedList<>();
		for (String s : __args)
			if (s != null)
				args.add(s);
		
		// Load the database
		Database db = Database.build();
		
		// List tests?
		if ("-l".equals(args.peekFirst()))
		{
			// Remove it!
			args.pollFirst();
			
			// Banner to standard error
			System.err.println("Available tests:");
			
			// Output the tests to standard output
			PrintStream out = System.out;
			for (SingleUnit u : db)
				out.println(u.fullName());
			
			// Stop
			return;
		}
		
		// Running specific tests? These get crimped accordingly
		Set<String> specific = new HashSet<>(),
			endwild = new HashSet<>(),
			startwild = new HashSet<>();
		while (!args.isEmpty())
		{
			// Ignore nulls
			String s = args.pollFirst();
			if (s == null)
				continue;
			
			// Crimp to simplify it
			s = SingleUnit.__crimpName(s);
			
			// Ending wildcard? Run all tests
			if (s.endsWith("*"))
				endwild.add(s.substring(0, s.length() - 1));
			
			// Starts with wildcard?
			else if (s.startsWith("*"))
				startwild.add(s.substring(1));
			
			// Run specific test
			else
				specific.add(s);
		}
		
		// If there is no specific test, just ignore it
		if (specific.isEmpty())
			specific = null;
		if (endwild.isEmpty())
			endwild = null;
		if (startwild.isEmpty())
			startwild = null;
		
		// Checking for tests?
		boolean check = (specific != null || endwild != null ||
			startwild != null);
		
		// Report for tests to run
		Report report = new Report();
		
		// Run each test
		int total = 0,
			pass = 0,
			fail = 0;
		boolean hasfailed = false;
		for (SingleUnit su : db)
		{
			String fn = su.fullName();
			
			// Check if we want to run this test
			if (check)
			{
				boolean found = false;
				
				// Is this a specific test being run?
				if (specific != null && specific.contains(fn))
					found = true;
				
				// Ends in a wildcard? Run multiple tests?
				if (!found && endwild != null)
					for (String prefix : endwild)
						if (fn.startsWith(prefix))
						{
							found = true;
							break;
						}
				
				// Starts with a wildcard?
				if (!found && startwild != null)
					for (String suffix : startwild)
						if (fn.endsWith(suffix))
						{
							found = true;
							break;
						}
				
				// Not found, ignore!
				if (!found)
					continue;
			}
			
			// Run the test
			System.err.printf("Running %s...%n", fn);
			long startns = System.nanoTime();
			boolean passed = su.run();
			
			// Keep track
			total++;
			if (!passed)
			{
				fail++;
				hasfailed = true;
			}
			else
				pass++;
			
			// How long did this take?
			long durns = System.nanoTime() - startns;
			
			// Send to the report for later usage
			report.add(fn, passed, durns);
		}
		
		// Note it
		System.err.printf("Ran %d tests: %d passed, %d failed.%n",
			total, pass, fail);
		
		// Generate report
		report.generate(System.out, ReportType.JUNIT);
		
		// Exit with failure if there are bad tests
		if (hasfailed)
			System.exit(1);
	}
}

