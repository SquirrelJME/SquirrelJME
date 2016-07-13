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

/**
 * This is a matcher which is used to determine which tests to match against.
 *
 * This also is used to indicate any non-default tests that should be run if
 * there are no wildcards in the string.
 *
 * @since 2016/07/12
 */
public final class TestMatcher
{
	/** The group to match against. */
	protected final TestGroupName group;
	
	/** The sub-test to match against. */
	protected final TestSubName sub;
	
	/** Wildcard group type. */
	protected final __WildType__ wildgroup;
	
	/** Wildcard sub type. */
	protected final __WildType__ wildsub;
	
	/**
	 * Initializes the test matcher.
	 *
	 * @param __m The test to match against.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/12
	 */
	public TestMatcher(String __m)
		throws NullPointerException
	{
		// Check
		if (__m == null)
			throw new NullPointerException("NARG");
		
		// If there is an at sign then this includes a sub-test
		int at = __m.indexOf('@');
		if (at >= 0)
		{
			group = TestGroupName.of(__m.substring(0, at));
			sub = TestSubName.of(__m.substring(at + 1));
		}
		
		// Otherwise, it does not, use any match for sub-tests
		else
		{
			group = TestGroupName.of(__m);
			sub = TestSubName.of("*");
		}
		
		// Wildcards?
		wildgroup = __wildcard(group);
		wildsub = __wildcard(sub);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/12
	 */
	@Override
	public final boolean equals(Object __o)
	{
		// Check
		if (!(__o instanceof TestMatcher))
			return false;
		
		// Cast
		TestMatcher o = (TestMatcher)__o;
		return this.group.equals(o.group) &&
			this.sub.equals(o.sub) &&
			this.wildgroup.equals(o.wildgroup) &&
			this.wildsub.equals(o.wildsub);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/12
	 */
	@Override
	public final int hashCode()
	{
		return this.group.hashCode() ^
			this.sub.hashCode() ^
			this.wildgroup.hashCode() ^
			this.wildsub.hashCode();
	}
	
	/**
	 * Detects the type of wildcard to use.
	 *
	 * @param __bn The name to decode.
	 * @return The wildcard type used.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/12
	 */
	private static final __WildType__ __wildcard(__BaseName__ __bn)
		throws NullPointerException
	{
		// Check
		if (__bn == null)
			throw new NullPointerException("NARG");
		
		// Get string length
		String s = __bn.toString();
		int n = s.length();
		
		// Empty is no match
		if (n <= 0)
			return __WildType__.NONE;
		
		// Single character is any if just *
		char first = s.charAt(0);
		boolean fstar = (first == '*');
		if (n == 1 && fstar)
			return __WildType__.ANY;
		
		// Otherwise find the last
		char last = s.charAt(n - 1);
		boolean lstar = (last == '*');
		
		// *foo*
		if (fstar && lstar)
			return __WildType__.CONTAINS;
		
		// *foo
		else if (fstar)
			return __WildType__.ENDS_WITH;
		
		// foo*
		else if (lstar)
			return __WildType__.STARTS_WITH;
		
		// No wildcard
		return __WildType__.NONE;
	}
	
	/**
	 * The type of wildcard used.
	 *
	 * @since 2016/07/12
	 */
	private static enum __WildType__
	{
		/** No wildcard used. */
		NONE,
		
		/** Any. */
		ANY,
		
		/** Starts with. */
		STARTS_WITH,
		
		/** Ends with. */
		ENDS_WITH,
		
		/** Contains. */
		CONTAINS,
		
		/** End. */
		;
	}
}

