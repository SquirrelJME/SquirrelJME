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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ServiceLoader;
import java.util.Set;

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
	
	/** Ignoring passing tests? */
	protected final boolean ignorepass;
	
	/** Ignoring failing tests? */
	protected final boolean ignorefail;
	
	/** Ignoring tossed exceptions? */
	protected final boolean ignoretoss;
	
	/** The acceptable test matches to perform. */
	protected final Set<TestMatch> matches =
		new HashSet<>();
	
	/** The specific tests to run. */
	@Deprecated
	protected final Set<String> runtests =
		new HashSet<>();
	
	/**
	 * Initializes the test caller.
	 *
	 * @param __ps The output where test results are placed.
	 * @param __args Program arguments.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/03
	 */
	public TestCaller(PrintStream __ps, String... __args)
		throws NullPointerException
	{
		// Check
		if (__ps == null)
			throw new NullPointerException("NARG");
		
		// Set
		output = __ps;
		
		// Handle some arguments?
		boolean ignpass = false;
		boolean ignfail = false;
		boolean igntoss = false;
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
							ignpass = true;
							break;
							
							// Ignore failures
						case "-if":
							ignfail = true;
							break;
							
							// Ignore tossed exceptions
						case "-ie":
						case "-it":
						case "-ix":
							igntoss = true;
							break;
						
							// Unknown, treat as test to run
						default:
							// {@squirreljme.error AG01 Unknown command line
							// switch. (The unknown switch)}
							if (arg.startsWith("-"))
								throw new IllegalArgumentException(
									String.format("AG01 %s", arg));
							
							matches.add(new TestMatch(arg));
							runtests.add(arg);
							break;
					}
		
		// Set
		ignorepass = ignpass;
		ignorefail = ignfail;
		ignoretoss = igntoss;
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
	 * Runs available tests.
	 *
	 * @since 2016/03/03
	 */
	public void runTests()
	{
		// Find all tests first
		output.println("---- tests.find");
		List<TestInvoker> alltests = new LinkedList<>();
		for (TestInvoker ti : serviceloader)
			alltests.add(ti);
		
		// Sort all of them
		output.println("---- tests.sort");
		Collections.<TestInvoker>sort(alltests, new Comparator<TestInvoker>()
			{
				/**
				 * {@inheritDoc}
				 * @since 2016/03/03
				 */
				@Override
				public int compare(TestInvoker __a, TestInvoker __b)
				{
					return __a.invokerName().compareToIgnoreCase(
						__b.invokerName());
				}
			});
		
		// Start
		output.println("---- tests.start");
		
		// Go through all test services
		for (TestInvoker ti : alltests)
		{
			// Is this a test which wants to be ran?
			String name = ti.invokerName();
			if (!runtests.isEmpty())
				if (!runtests.contains(name))
					continue;
			
			// Non-important status output
			output.printf("---- %s%n", name);
			
			// Initialize a new test checker with an initial state
			TestChecker tc = new TestChecker(this, ti);
			tc.setIgnorePass(ignorepass);
			tc.setIgnoreFail(ignorefail);
			tc.setIgnoreException(ignoretoss);
			
			// Make sure a crashing test does not take out other tests
			try
			{
				// Run all the tests
				ti.runTests(tc);
			}
			
			// Caught some exception
			catch (Throwable t)
			{
				tc.exception("", t);
			}
		}
		
		// End
		output.println("---- tests.end");
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
		// Must faster
		PrintStream o = System.err;
	}
	
	/**
	 * This class is used to test for matching tests.
	 *
	 * @since 2016/05/04
	 */
	public static class TestMatch
	{
		/** The major test to compare with. */
		protected final String major;
		
		/** The major mode. */
		protected final SearchMode majormode;
		
		/** The minor test to compare with. */
		protected final String minor;
		
		/** The minor mode. */
		protected final SearchMode minormode;
		
		/**
		 * Initializes a test match.
		 *
		 * @param __form The input form to check.
		 * @throws NullPointerException On null arguments.
		 * @since 2016/05/04
		 */
		public TestMatch(String __form)
			throws NullPointerException
		{
			// Check
			if (__form == null)
				throw new NullPointerException("NARG");
			
			// Split base forms
			String a, b;
			int atdx = __form.indexOf('@');
			if (atdx >= 0)
			{
				b = __form.substring(atdx + 1);
				
				// If this is just an @ followed by something then it is an
				// any match.
				a = (atdx == 0 ? "*" : __form.substring(0, atdx));
			}
			
			// No at sign, just a major is used
			else
			{
				a = __form;
				
				// The minor is the wildcard always
				b = "*";
			}
			
			// Get major details
			SearchMode[] mm = new SearchMode[1];
			major = __get(a, mm);
			majormode = mm[0];
			
			// Get minor details
			minor = __get(b, mm);
			minormode = mm[0];
		}
		
		/**
		 * Gets the major string sequence to check against and its search mode.
		 *
		 * @param __s The input form.
		 * @param __m The search mode to perform.
		 * @return The comparison string.
		 * @throws NullPointerException On null arguments.
		 * @since 2016/05/04
		 */
		private static String __get(String __s, SearchMode[] __m)
			throws NullPointerException
		{
			// Check
			if (__s == null || __m == null)
				throw new NullPointerException("NARG");
			
			// Asterisks?
			int len = __s.length();
			boolean astart = __s.startsWith("*");
			boolean aend = __s.endsWith("*");
			
			// Any wildcard match?
			if (len <= 1 && astart)
			{
				__m[0] = SearchMode.ANY;
				return "";
			}
			
			// Contains?
			else if (astart && aend)
			{
				__m[0] = SearchMode.CONTAINS;
				return __s.substring(1, len - 1);
			}
			
			// Starts with?
			else if (!astart && aend)
			{
				__m[0] = SearchMode.STARTS_WITH;
				return __s.substring(0, len - 1);
			}
			
			// Ends with?
			else if (astart && !aend)
			{
				__m[0] = SearchMode.ENDS_WITH;
				return __s.substring(1, len);
			}
			
			// Otherwise exact match
			else
			{
				__m[0] = SearchMode.EXACT;
				return __s;
			}
		}
		
		/**
		 * This is the type of comparison to make.
		 *
		 * @since 2016/05/04
		 */
		public static enum SearchMode
		{
			/** Any match. */
			ANY,
			
			/** Exact match. */
			EXACT,
			
			/** Contains. */
			CONTAINS,
			
			/** Starts with. */
			STARTS_WITH,
			
			/** Ends with. */
			ENDS_WITH,
			
			/** End. */
			;
		}
	}
}

