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
import java.util.ArrayList;
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
	
	/** The test options. */
	private final Set<TestOption> _options =
		new HashSet<>();
	
	/** Test matchers to match against. */
	private final Set<TestMatcher> _matchers =
		new HashSet<>();
	
	/**
	 * Initializes the test caller.
	 *
	 * @since 2016/03/03
	 */
	public TestCaller()
	{
	}
	
	/**
	 * Adds a test matcher so that only a specific set of tests run.
	 *
	 * @param __tm The test matcher to use.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/12
	 */
	public final void addMatcher(TestMatcher __tm)
		throws NullPointerException
	{
		if (__tm == null)
			throw new NullPointerException("NARG");
		
		// Lock
		Set<TestMatcher> matcher = this._matchers;
		synchronized (lock)
		{
			throw new Error("TODO");
		}
	}
	
	/**
	 * Checks whether the given option is set.
	 *
	 * @param __to The option to check.
	 * @return {@code true} if it is set.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/12
	 */
	public final boolean hasOption(TestOption __to)
		throws NullPointerException
	{
		// Check
		if (__to == null)
			throw new NullPointerException("NARG");
		
		// Lock
		Set<TestOption> options = _options;
		synchronized (options)
		{
			return options.contains(__to);
		}
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
					return __a.invokerName().toString().compareToIgnoreCase(
						__b.invokerName().toString());
				}
			});
		
		// Start
		output.println("---- tests.start");
		
		// Go through all test services
		Set<TestMatch> ma = matches;
		boolean specific = !ma.isEmpty();
		List<String> subtests = new ArrayList<>();
		Set<TestMatch> wasmatched = new HashSet<>();
		for (TestInvoker ti : alltests)
		{
			// Is this a test which wants to be ran?
			String name = __lower(ti.invokerName().toString());
			if (specific)
			{
				// Must be a match
				boolean matched = false;
				for (TestMatch tm : ma)
					if (tm.isMatchMajor(name)) 
					{
						matched = true;
						break;
					}
				
				// If not matched then do not run it
				if (!matched)
					continue;
			}
			
			// Non-important status output
			output.printf("---- %s%n", name);
			
			// Initialize a new test checker with an initial state
			TestChecker tc = new TestChecker(this, ti);
			tc.setIgnorePass(ignorepass);
			tc.setIgnoreFail(ignorefail);
			tc.setIgnoreException(ignoretoss);
			
			// Get all the tests this has
			wasmatched.clear();
			subtests.clear();
			try
			{
				for (String st : ti.invokerTests())
					subtests.add(__lower(st));
			}
			
			// Failed to execute
			catch (Throwable t)
			{
				tc.setSubTest("");
				tc.exception(t);
				continue;
			}
			
			// Go through all sub-tests
			for (String st : subtests)
			{
				// Set the test used
				tc.setSubTest(st);
				
				// Only running specific tests?
				if (specific)
				{
					// Must be a match
					boolean matched = false;
					for (TestMatch tm : ma)
						if (tm.isMatch(name, st)) 
						{
							matched = true;
							wasmatched.add(tm);
							break;
						}
			
					// If not matched then do not run it
					if (!matched)
						continue;
				}
			
				// Make sure a crashing test does not take out other tests
				try
				{
					// Run the given test
					ti.runTest(tc, st);
				}
		
				// Caught some exception
				catch (Throwable t)
				{
					tc.exception(t);
				}
			}
			
			// There may be free-form tests being ran which do not have
			// explicit sub-tests specified for them. Thus they should be ran
			// regardless
			if (specific)
			{
				// Go through all tests again
				for (TestMatch tm : ma)
					if (tm.isExact(name) && !wasmatched.contains(tm))
						try
						{
							// Set the sub-test used
							String sub = tm.minor;
							tc.setSubTest(sub);
							
							// Run the given test
							ti.runTest(tc, sub);
						}
		
						// Caught some exception
						catch (Throwable t)
						{
							tc.exception(t);
						}
			}
		}
		
		// End
		output.println("---- tests.end");
	}
	
	/**
	 * Sets the specified test option.
	 *
	 * @param __to The option to set.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/12
	 */
	public final void setOption(TestOption __to)
		throws NullPointerException
	{
		// Check
		if (__to == null)
			throw new NullPointerException("NARG");
		
		// Lock
		Set<TestOption> options = _options;
		synchronized (options)
		{
			options.add(__to);
		}
	}
	
	/**
	 * Lower cases the given string using a neutral ASCII encoding which does
	 * not take locale into consideration.
	 *
	 * @param __s The input string to lowercase.
	 * @return The lowercase version of the string.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/05/05
	 */
	private static String __lower(String __s)
		throws NullPointerException
	{
		// Check
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Check for any uppercase characters first
		int n = __s.length();
		int quick = -1;
		for (int i = 0; i < n; i++)
		{
			// Obtain character
			char c = __s.charAt(i);
			
			// If upper case, then stop
			if (c >= 'A' && c <= 'Z')
			{
				quick = i;
				break;
			}
		}
		
		// No upper-case characters
		if (quick < 0)
			return __s;
		
		// Otherwise start building, start with the initial string characters
		// that have no uppercase characters
		StringBuilder sb = new StringBuilder(__s.substring(0, quick));
		
		// Copy the remaining characters lowercasing them
		for (int i = quick; i < n; i++)
		{
			char c = __s.charAt(i);
			
			// If uppercase, make it lower
			if (c >= 'A' && c <= 'Z')
				c = (char)('a' + (c - 'A'));
			
			// Add it
			sb.append(c);
		}
		
		// Return variant of strng
		return sb.toString();
	}
	
	/**
	 * This class is used to test for matching tests.
	 *
	 * @since 2016/05/04
	 */
	public static class TestMatch
		implements Comparable<TestMatch>
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
		 * {@inheritDoc}
		 * @since 2016/05/04
		 */
		@Override
		public int compareTo(TestMatch __o)
		{
			// Compare the major first
			int rv = major.compareTo(__o.major);
			if (rv != 0)
				return rv;
			
			// The minor
			rv = minor.compareTo(__o.minor);
			if (rv != 0)
				return rv;
			
			// The major match type
			rv = majormode.compareTo(__o.majormode);
			if (rv != 0)
				return rv;
			
			// The minor match tpe
			return minormode.compareTo(__o.minormode);
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/05/04
		 */
		@Override
		public boolean equals(Object __o)
		{
			// Must match against self type
			if (!(__o instanceof TestMatch))
				return false;
			
			// Must have the same majors and minors
			TestMatch o = (TestMatch)__o;
			return major.equals(o.major) &&
				minor.equals(o.minor) &&
				majormode.equals(o.majormode) &&
				minormode.equals(o.minormode);
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2016/05/04
		 */
		@Override
		public int hashCode()
		{
			return major.hashCode() ^ majormode.hashCode() ^
				minor.hashCode() ^ minormode.hashCode();
		}
		
		/**
		 * This tests whether the given test matches the specified major and
		 * if it has an exact minor test. This is used to access potentially
		 * freeform tests which have no defined parameters or are pre-specified
		 * in the sub test list.
		 *
		 * @param __maj The major test.
		 * @return {@code true} if this is an exact minor match.
		 * @throws NullPointerException On null arguments.
		 * @since 2016/05/05
		 */
		public boolean isExact(String __maj)
			throws NullPointerException
		{
			// Check
			if (__maj == null)
				throw new NullPointerException("NARG");
			
			// Must be major matched and exactly a minor
			return isMatchMajor(__maj) && minormode == SearchMode.EXACT;
		}
		
		/**
		 * Is the given test a match against this specific test that is
		 * desired to be ran?
		 *
		 * @param __maj The major test identifier.
		 * @param __min The minor test identifier.
		 * @return {@code true} if this is a matching test.
		 * @throws NullPointerException On null arguments.
		 * @since 2016/05/04
		 */
		public boolean isMatch(String __maj, String __min)
			throws NullPointerException
		{
			// Check
			if (__maj == null || __min == null)
				throw new NullPointerException("NARG");
			
			// Must match major and minor
			return isMatchMajor(__maj) && __matches(__min, minor, minormode);
		}
		
		/**
		 * Is the given test a match against the desired test?
		 *
		 * @param __maj The major test identifier.
		 * @return {@code true} if the test is a match.
		 * @since 2016/05/04
		 */
		public boolean isMatchMajor(String __maj)
			throws NullPointerException
		{
			// Check
			if (__maj == null)
				throw new NullPointerException("NARG");
			
			// Match only the major
			return __matches(__maj, major, majormode);
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
		 * Checks whether the specified major and minor matches the fragment
		 * with the specified mode.
		 *
		 * @param __in The input string.
		 * @param __frag The fragment to check against.
		 * @param __mode The mode of comparison.
		 * @return {@code true} if the match is valid.
		 * @throws NullPointerException On null arguments.
		 * @since 2016/05/05
		 */
		private static boolean __matches(String __in, String __frag,
			SearchMode __mode)
			throws NullPointerException
		{
			// Check
			if (__in == null || __frag == null || __mode == null)
				throw new NullPointerException("NARG");
			
			// Depends on the match mode
			switch (__mode)
			{
					// Anything, always succeeds
				case ANY:
					return true;
					
					// Exact
				case EXACT:
					return __in.equals(__frag);
					
					// Contains the given string
				case CONTAINS:
					return __in.indexOf(__frag) > 0;
					
					// Starts with
				case STARTS_WITH:
					return __in.startsWith(__frag);
					
					// Ends with
				case ENDS_WITH:
					return __in.endsWith(__frag);
				
					// Unknown
				default:
					throw new RuntimeException("WTFX");
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

