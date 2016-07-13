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
	
	/** Group comparison string. */
	protected final String compgroup;
	
	/** Sub-test comparison string. */
	protected final String compsub;
	
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
		TestGroupName group;
		TestSubName sub;
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
		
		// Set
		this.group = group;
		this.sub = sub;
		
		// Wildcards?
		this.wildgroup = __wildcard(group);
		this.wildsub = __wildcard(sub);
		
		// Setup comparison forms
		this.compgroup = this.wildgroup.__base(group);
		this.compsub = this.wildsub.__base(sub);
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
	 * Checks whether the given name matches the given type and name.
	 *
	 * @param __self The actual string to compare with.
	 * @param __type The current wildcard type.
	 * @param __to The target comparison.
	 * @return {@code true} if it matches.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/13
	 */
	final boolean __matches(String __self, __WildType__ __type,
		__BaseName__ __to)
		throws NullPointerException
	{
		// Check
		if (__self == null || __type == null || __to == null)
			throw new NullPointerException("NARG");
		
		// Get target string
		String to = __to.toString();
		
		// Depends on the type
		switch (__type)
		{
				// Exact match
			case EXACT:
				return to.equals(__self);
				
				// Always matches
			case ANY:
				return true;
				
				// Starts with
			case STARTS_WITH:
				return to.startsWith(__self);
				
				// Ends with
			case ENDS_WITH:
				return to.endsWith(__self);
				
				// Contains
			case CONTAINS:
				return to.indexOf(__self) >= 0;
			
				// Unknown, fail
			default:
				throw new RuntimeException(String.format("OOPS %s", __type));
		}
	}
	
	/**
	 * Checks whether the given test matches
	 *
	 * @param __tc The owning test caller.
	 * @param __ti The test invoker.
	 * @param __tf The test family to check.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/13
	 */
	final void __possiblyRunTests(TestCaller __tc, TestInvoker __ti,
		TestFamily __tf)
		throws NullPointerException
	{
		// Check
		if (__tc == null || __ti == null || __tf == null)
			throw new NullPointerException("NARG");
		
		// Not testing group?
		if (!__matches(this.compgroup, this.wildgroup, __tf.groupName()))
			return;
		
		// See if any sub-tests match
		String compsub = this.compsub;
		__WildType__ wildsub = this.wildsub;
		boolean rantest = false;
		for (TestSubName sn : __tf)
		{
			// Does not match?
			if (!__matches(compsub, wildsub, sn))
				continue;
			
			// A test has run so do not try running a non-default one
			rantest = true;
			
			// Run test
			__tc.__runTest(__ti, __tf, sn);
		}
		
		// If this point was reached then it could be an explicit sub-test
		// which is not of a default one.
		if (!rantest)
			__tc.__runTest(__ti, __tf, this.sub);
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
			return __WildType__.EXACT;
		
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
		return __WildType__.EXACT;
	}
	
	/**
	 * The type of wildcard used.
	 *
	 * @since 2016/07/12
	 */
	private static enum __WildType__
	{
		/** No wildcard used. */
		EXACT,
		
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
		
		/**
		 * Returns the base for with the given comparison.
		 *
		 * @param __n The name to utilize.
		 * @return The string to compare with.
		 * @throws NullPointerException On null arguments
		 * @since 2016/07/13
		 */
		private final String __base(__BaseName__ __n)
			throws NullPointerException
		{
			// Check
			if (__n == null)
				throw new NullPointerException("NARG");
			
			// Depends
			String s = __n.toString();
			int n = s.length();
			switch (this)
			{
				case EXACT: return s;
				case ANY: return "";
				case STARTS_WITH: return s.substring(0, n - 1);
				case ENDS_WITH: return s.substring(1);
				case CONTAINS: return s.substring(1, n - 1);
				
					// Unknown
				default:
					throw new RuntimeException(String.format("OOPS %s", this));
			}
		}
	}
}

