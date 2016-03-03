// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.test;

import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;
import java.util.ServiceLoader;

/**
 * This calls all tests which are known about which are listed as a service
 * for tests.
 *
 * @since 2016/03/03
 */
public class TestCaller
{
	/** Loader for tests. */
	protected final ServiceLoader<TestInvoker> serviceloader =
		ServiceLoader.<TestInvoker>load(TestInvoker.class);
	
	/** Test results output. */
	protected final PrintStream output;
	
	/**
	 * Initializes the test caller.
	 *
	 * @param __ps The output where test results are placed.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/03
	 */
	public TestCaller(PrintStream __ps)
		throws NullPointerException
	{
		// Check
		if (__ps == null)
			throw new NullPointerException();
		
		// Set
		output = __ps;
	}
	
	/**
	 * Runs available tests.
	 *
	 * @since 2016/03/03
	 */
	public void runTests()
	{
		// Start
		System.err.println("---- tests.start");
		
		// Go through all test services
		for (TestInvoker ti : serviceloader)
		{
			// Non-important status output
			System.err.printf("---- %s%n", ti.invokerName());
			
			// Setup the checker
			TestChecker tc = new TestChecker(this, ti);
			
			// Run all the tests
			ti.runTests(tc);
		}
		
		// End
		System.err.println("---- tests.end");
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
		TestCaller tc = new TestCaller(System.out);
		
		// Run tests
		tc.runTests();
	}
}

