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
	/** The group name. */
	protected final TestGroupName group;
	
	/** The sub-test name. */
	protected final TestSubName name;
	
	/** Test results. */
	private final List<Result> _results =
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
	 * Compares two byte arrays to check how they compare to each other.
	 *
	 * @param __f The fragment name.
	 * @param __c The comparison to make.
	 * @param __a The expected array.
	 * @param __b The resulting array.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/12
	 */
	public final void compareByteArrays(TestFragmentName __f,
		TestComparison __c, byte[] __a, byte[] __b)
		throws NullPointerException
	{
		// Check
		if (__f == null || __c == null || __a == null || __b == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	/**
	 * Compares two int values to check how they compare to each other.
	 *
	 * @param __f The fragment name.
	 * @param __c The comparison to make.
	 * @param __a The expected value.
	 * @param __b The resulting value.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/12
	 */
	public final void compareInt(TestFragmentName __f,
		TestComparison __c, int __a, int __b)
		throws NullPointerException
	{
		// Check
		if (__f == null || __c == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	/**
	 * Compares two int arrays to check how they compare to each other.
	 *
	 * @param __f The fragment name.
	 * @param __c The comparison to make.
	 * @param __a The expected array.
	 * @param __b The resulting array.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/12
	 */
	public final void compareIntArrays(TestFragmentName __f,
		TestComparison __c, int[] __a, int[] __b)
		throws NullPointerException
	{
		// Check
		if (__f == null || __c == null || __a == null || __b == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	/**
	 * Compares two object values to check how they compare to each other.
	 *
	 * @param __f The fragment name.
	 * @param __c The comparison to make.
	 * @param __a The expected value.
	 * @param __b The resulting value.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/12
	 */
	public final void compareObject(TestFragmentName __f,
		TestComparison __c, Object __a, Object __b)
		throws NullPointerException
	{
		// Check
		if (__f == null || __c == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	/**
	 * Compares two string values to check how they compare to each other.
	 *
	 * @param __f The fragment name.
	 * @param __c The comparison to make.
	 * @param __a The expected value.
	 * @param __b The resulting value.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/12
	 */
	public final void compareString(TestFragmentName __f,
		TestComparison __c, String __a, String __b)
		throws NullPointerException
	{
		// Check
		if (__f == null || __c == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	/**
	 * This is invoked when an exception is thrown where it should be
	 * treated as failure (and not success).
	 *
	 * @param __f The fragment name.
	 * @param __t The exception that caused failure.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/13
	 */
	public final void failingException(TestFragmentName __f, Throwable __t)
		throws NullPointerException
	{
		// Check
		if (__f == null || __t == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
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
	 * Notes something that is neither a passing nor failing state.
	 *
	 * @param __f The fragment name.
	 * @param __v The value to note.
	 * @throws NullPointerException If no fragment name was specified.
	 * @since 2016/07/12
	 */
	public final void note(TestFragmentName __f, Object __v)
	{
		// Check
		if (__f == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	/**
	 * Initializes a new test result.
	 *
	 * @param __n The name of the test.
	 * @return The result of a test which may be set.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/12
	 */
	public final Result result(String __n)
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
	public final Result result(TestFragmentName __n)
		throws NullPointerException
	{
		// Check
		if (__n == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
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
		List<Result> results = this._results;
		synchronized (results)
		{
			return this._nextid++;
		}
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
		
		/** The test ID. */
		protected final int id;
		
		/** The test status. */
		protected final Status status;
		
		/**
		 * Initializes the result.
		 *
		 * @param __n The fragment name.
		 * @param __dx The test number.
		 * @param __st The test status.
		 * @throws NullPointerException On null arguments.
		 * @since 2016/07/12
		 */
		private Result(TestFragmentName __n, int __dx, Status __st)
			throws NullPointerException
		{
			// Check
			if (__n == null || __st == null)
				throw new NullPointerException("NARG");
			
			// Set
			this.fragment = __n;
			this.id = __dx;
			this.status = __st;
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
		
		/**
		 * Returns the test identifier.
		 *
		 * @return The test identifier.
		 * @since 2016/07/13
		 */
		public final int id()
		{
			return this.id;
		}
		
		/**
		 * Returns the test status.
		 *
		 * @return The test status.
		 * @since 2016/07/13
		 */
		public final Status status()
		{
			return this.status;
		}
	}
	
	/**
	 * This indicates the status of a given test.
	 *
	 * @since 2016/07/13
	 */
	public static enum Status
	{
		/** Test passed. */
		PASS,
		
		/** Test failed. */
		FAIL,
		
		/** End. */
		;
	}
}

