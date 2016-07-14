// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.tests;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.LinkedList;

/**
 * The class represents an individual test to be ran, it allows for argument
 * input along with output results for potential sub-test areas.
 *
 * @since 2016/07/12
 */
public final class IndividualTest
{
	/** The group name. */
	protected final TestGroupName group;
	
	/** The sub-test name. */
	protected final TestSubName name;
	
	/** Test results. */
	private final List<TestResult> _results =
		new ArrayList<>();
	
	/** Next test ID. */
	private volatile int _nextid;
	
	/**
	 * Initializes the individual test with the given sub-name.
	 *
	 * @param __g The test group.
	 * @param __n The sub-test name.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/12
	 */
	IndividualTest(TestGroupName __g, TestSubName __n)
		throws NullPointerException
	{
		// Check
		if (__g == null || __n == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.group = __g;
		this.name = __n;
	}
	
	/**
	 * Returns the group name of the test.
	 *
	 * @return The group name.
	 * @since 2016/07/12
	 */
	public final TestGroupName groupName()
	{
		return this.group;
	}
	
	/**
	 * Initializes a new test result.
	 *
	 * @param __n The name of the test.
	 * @return The result of a test which may be set.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/12
	 */
	public final TestResult result(String __n)
		throws NullPointerException
	{
		return this.result(TestFragmentName.of(__n));
	}
	
	/**
	 * Initializes a new test result.
	 *
	 * @param __n The name of the test fragment.
	 * @return The result of a test which may be set.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/12
	 */
	public final TestResult result(TestFragmentName __n)
		throws NullPointerException
	{
		// Check
		if (__n == null)
			throw new NullPointerException("NARG");
		
		// Lock
		List<TestResult> results = this._results;
		synchronized (results)
		{
			TestResult rv;
			results.add((rv = new TestResult(this.group, this.name,
				__n, __nextId())));
			return rv;
		}
	}
	
	/**
	 * Returns an array of all test results.
	 *
	 * @return The test results.
	 * @since 2016/07/14
	 */
	public final TestResult[] results()
	{
		// Lock
		List<TestResult> results = this._results;
		synchronized (results)
		{
			return results.<TestResult>toArray(new TestResult[results.size()]);
		}
	}
	
	/**
	 * Returns the sub-name of the test.
	 *
	 * @return The test sub-name.
	 * @since 2016/07/12
	 */
	public final TestSubName subName()
	{
		return this.name;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/12
	 */
	@Override
	public String toString()
	{
		throw new Error("TODO");
	}
	
	/**
	 * Returns the next test ID to use.
	 *
	 * @return The next test ID.
	 * @since 2016/07/13
	 */
	private final int __nextId()
	{
		// Lock
		List<TestResult> results = this._results;
		synchronized (results)
		{
			return this._nextid++;
		}
	}
}

