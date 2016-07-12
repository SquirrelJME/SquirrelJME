// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.tests;

import java.io.PrintStream;

/**
 * This is the default test caller which handles command line arguments and
 * outputs test results to standard output.
 *
 * @since 2016/07/12
 */
public class DefaultTestCaller
	extends TestCaller
{
	/** Test results output. */
	protected final PrintStream output;
	
	/**
	 * Initializes the default test caller with the given output and input
	 * arguments.
	 *
	 * @param __ps The output for test results.
	 * @param __args The arguments to the test.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/12
	 */
	public DefaultTestCaller(PrintStream __ps, String... __args)
		throws NullPointerException
	{
		// Check
		if (__ps == null)
			throw new NullPointerException("NARG");
		
		// Set
		output = __ps;
		
		// Handle some arguments?
		if (__args != null)
			for (String arg : __args)
				if (arg != null)
					switch (arg)
					{
							// Print usage
						case "-?":
						case "-help":
						case "--help":
							__usage();
							
							// {@squirreljme.error AG02 Help was printed.}
							throw new IllegalArgumentException("AG02");
						
							// Ignore passes
						case "-ip":
							setOption(TestOption.IGNORE_PASS);
							break;
							
							// Ignore failures
						case "-if":
							setOption(TestOption.IGNORE_FAIL);
							break;
							
							// Ignore tossed exceptions
						case "-ie":
						case "-it":
						case "-ix":
							setOption(TestOption.IGNORE_EXCEPTION);
							break;
						
							// Unknown, treat as test to run
						default:
							// {@squirreljme.error AG01 Unknown command line
							// switch. (The unknown switch)}
							if (arg.startsWith("-"))
								throw new IllegalArgumentException(
									String.format("AG01 %s", arg));
							
							matches.add(new TestMatch(__lower(arg)));
							break;
					}
	}
	
	/**
	 * Returns the target print stream.
	 *
	 * @return The target print stream.
	 * @since 2016/03/03
	 */
	public PrintStream printStream()
	{
		return output;
	}
	
	/**
	 * Runs all tests by default using the command line interface.
	 *
	 * @param __args Program arguments.
	 * @since 2016/03/03
	 */
	public static void main(String... __args)
	{
		// Create a new test caller
		TestCaller tc = new TestCaller(System.out, __args);
		
		// Run tests
		tc.runTests();
	}
	
	/**
	 * Prints how the test caller is used.
	 *
	 * @since 2016/05/04
	 */
	private static void __usage()
	{
		// Much faster
		PrintStream o = System.err;
		
		throw new Error("TODO");
	}
}

