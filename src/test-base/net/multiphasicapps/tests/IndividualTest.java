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
	/** The sub-test name. */
	protected final TestSubName name;
	
	/** Test results. */
	protected final List<Result> results =
		new ArrayList<>();
	
	/**
	 * Initializes the individual test with the given sub-name.
	 *
	 * @param __n The sub-test name.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/12
	 */
	public IndividualTest(TestSubName __n)
		throws NullPointerException
	{
		// Check
		if (__n == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.name = __n;
	}
	
	/**
	 * Initializes a new test result.
	 *
	 * @param __n The name of the test.
	 * @return The result of a test which may be set.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/12
	 */
	public Result result(String __n)
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
	public Result result(TestFragmentName __n)
		throws NullPointerException
	{
		// Check
		if (__n == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	/**
	 * This represents a result of a test fragment, since there may be a need
	 * for multiple tests to exist for a given sub-test.
	 *
	 * @since 2016/07/12
	 */
	public final class Result
	{
		/** The fragment name. */
		protected final TestFragmentName fragment;
		
		/**
		 * Initializes the result.
		 *
		 * @param __n The fragment name.
		 * @throws NullPointerException On null arguments.
		 * @since 2016/07/12
		 */
		private Result(TestFragmentName __n)
			throws NullPointerException
		{
			// Check
			if (__n == null)
				throw new NullPointerException("NARG");
			
			// Set
			this.fragment = __n;
		}
		
		/**
		 * Returns the fragment name being used.
		 *
		 * @return The fragment name.
		 * @since 2016/07/12
		 */
		public final TestFragmentName fragment()
		{
			return this.fragment;
		}
	}
}

