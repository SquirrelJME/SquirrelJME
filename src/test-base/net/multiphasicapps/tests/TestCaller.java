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
import java.util.Deque;
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
public abstract class TestCaller
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
	
	/** Tests which have run. */
	private final Deque<IndividualTest> _tests =
		new LinkedList<>();
	
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
		Set<TestMatcher> matchers = this._matchers;
		synchronized (matchers)
		{
			matchers.add(__tm);
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
	public final void runTests()
	{
		// Obtain all test services that are available
		List<TestInvoker> runtests = new LinkedList<>();
		ServiceLoader<TestInvoker> serviceloader = this.serviceloader;
		synchronized (serviceloader)
		{
			for (TestInvoker ti : serviceloader)
				runtests.add(ti);
		}
		
		// Prevent matchers from being changed
		Set<TestMatcher> matchers = this._matchers;
		synchronized (matchers)
		{
			// No matchers?
			boolean nomatch = matchers.isEmpty();
			
			// Go through all services
			for (TestInvoker ti : runtests)
			{
				// Cache family
				TestFamily tf = ti.testFamily();
				
				// Just running all tests
				if (nomatch)
					throw new Error("TODO");
				
				// See if any test matcher matches
				else
					for (TestMatcher tm : matchers)
						tm.__possiblyRunTests(this, ti, tf);
			}
		}
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
	 * Runs a single test.
	 *
	 * @param __ti The test invoker.
	 * @param __tf The input test family.
	 * @param __sn The sub-test name to run.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/13
	 */
	final void __runTest(TestInvoker __ti, TestFamily __tf, TestSubName __sn)
		throws NullPointerException
	{
		// Check
		if (__ti == null || __tf == null || __sn == null)
			throw new NullPointerException("NARG");
		
		// Setup test
		IndividualTest it = new IndividualTest(__tf.groupName(), __sn);
		
		// Need to catch all exceptions
		try (IndividualTest.Result r = it.result("*exec*"))
		{
			// Run the test
			try
			{
				__ti.runTest(it);
				
				// Mark test run a success
				r.success();
			}
		
			// Any caught exceptions means failure
			catch (Throwable t)
			{
				// Report failure
				r.failingException(t);
			}
		}
		
		// Add to tests which have run
		Deque<IndividualTest> tests = this._tests;
		synchronized (tests)
		{
			tests.offerLast(it);
		}
	}
}

