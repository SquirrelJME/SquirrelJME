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
import java.util.List;

/**
 * This represents a result of a test fragment, since there may be a need
 * for multiple tests to exist for a given sub-test.
 *
 * @since 2016/07/12
 */
public class TestResult
	implements AutoCloseable
{
	/** The fragment name. */
	protected final TestFragmentName fragment;
	
	/** The test ID. */
	protected final int id;
	
	/** The group this test is in. */
	protected final TestGroupName group;
	
	/** The sub name of the test. */
	protected final TestSubName sub;
	
	/** Object to lock for test results. */
	private final Object _lock =
		new Object();
	
	/** Data points for a given test (there may be zero or more). */
	private final List<Object> _data =
		new ArrayList<>(3);
	
	/** The result of a given test. */
	private volatile TestPassState _status;
	
	/** Was a result given? */
	private volatile boolean _done;
	
	/** The associated exception. */
	private volatile Throwable _exception;
	
	/**
	 * Initializes the result.
	 *
	 * @param __g The group name of this test.
	 * @param __s The sub-name of this test.
	 * @param __n The fragment name.
	 * @param __dx The test number.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/12
	 */
	TestResult(TestGroupName __g, TestSubName __s, TestFragmentName __n,
		int __dx)
		throws NullPointerException
	{
		// Check
		if (__g == null || __s == null || __n == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.group = __g;
		this.sub = __s;
		this.fragment = __n;
		this.id = __dx;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/13
	 */
	@Override
	public void close()
	{
		// Lock
		synchronized (this._lock)
		{
			// Automatically fail if no state was given
			if (this._status == null)
				this._status = TestPassState.FAIL;
		}
	}

	/**
	 * Compares two byte arrays to check how they compare to each other.
	 *
	 * @param __c The comparison to make.
	 * @param __a The expected array.
	 * @param __b The resulting array.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/12
	 */
	public final void compareByteArrays(TestComparison __c, byte[] __a,
		byte[] __b)
		throws NullPointerException
	{
		// Check
		if (__c == null || __a == null || __b == null)
			throw new NullPointerException("NARG");
		
		// Lock
		List<Object> data = this._data;
		synchronized (this._lock)
		{
			// Done
			__setDone();
			
			// Clone both arrays
			byte[] a = __a.clone();
			byte[] b = __b.clone();
			
			// Set status
			this._status = __c.__passState(__byteArrayCompare(a, b));
			
			// Add data points
			data.add(__c);
			data.add(a);
			data.add(b);
			
			// No more results
			close();
		}
	}

	/**
	 * Compares two int values to check how they compare to each other.
	 *
	 * @param __c The comparison to make.
	 * @param __a The expected value.
	 * @param __b The resulting value.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/12
	 */
	public final void compareInt(TestComparison __c, int __a, int __b)
		throws NullPointerException
	{
		// Check
		if (__c == null)
			throw new NullPointerException("NARG");
	
		// Lock
		List<Object> data = this._data;
		synchronized (this._lock)
		{
			// Done
			__setDone();
			
			// Compare
			int comp;
			if (__a < __b)
				comp = -1;
			else if (__a > __b)
				comp = 1;
			else
				comp = 0;
			
			// Set status
			this._status = __c.__passState(comp);
				
			// Add data points
			data.add(__c);
			data.add(__a);
			data.add(__b);
			
			// No more results
			close();
		}
	}

	/**
	 * Compares two int arrays to check how they compare to each other.
	 *
	 * @param __c The comparison to make.
	 * @param __a The expected array.
	 * @param __b The resulting array.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/12
	 */
	public final void compareIntArrays(TestComparison __c, int[] __a,
		int[] __b)
		throws NullPointerException
	{
		// Check
		if (__c == null || __a == null || __b == null)
			throw new NullPointerException("NARG");
	
		// Lock
		List<Object> data = this._data;
		synchronized (this._lock)
		{
			// Done
			__setDone();
			
			// Clone both arrays
			int[] a = __a.clone();
			int[] b = __b.clone();
			
			// Set status
			this._status = __c.__passState(__intArrayCompare(a, b));
			
			// Add data points
			data.add(__c);
			data.add(a);
			data.add(b);
		
			// No more results
			close();
		}
	}

	/**
	 * Compares two object values to check how they compare to each other.
	 *
	 * An input of {@code null} is considered less than a non-{@code null}
	 * value.
	 *
	 * @param __c The comparison to make.
	 * @param __a The expected value.
	 * @param __b The resulting value.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/12
	 */
	@SuppressWarnings({"unchecked"})
	public final void compareObject(TestComparison __c, Object __a,
		Object __b)
		throws NullPointerException
	{
		// Check
		if (__c == null)
			throw new NullPointerException("NARG");
	
		// Lock
		List<Object> data = this._data;
		synchronized (this._lock)
		{
			// Done
			__setDone();
			
			// Only a single side is null?
			boolean na = (__a == null);
			boolean nb = (__b == null);
			int comp;
			TestPassState autoset = null;
			if (na && !nb)
				comp = -1;
			
			// Right side is null
			else if (!na && nb)
				comp = 1;
			
			// Both sides are null
			else if (!na && !nb)
				comp = 0;
			
			// Requires more work
			else
			{
				// Both sides are comparable
				if (__a instanceof Comparable && __b instanceof Comparable)
					comp = ((Comparable)__a).compareTo((Comparable)__b);
				
				// Equality only
				else
				{
					// Automatically fail since the values cannot compare to
					// each other.
					if (__c != TestComparison.EQUALS &&
						__c != TestComparison.NOT_EQUALS)
					{
						comp = Integer.MIN_VALUE;
						autoset = TestPassState.FAIL;
					}
					
					// Check equality
					else
						comp = (__a.equals(__b) ? 0 : -1);
				}
			}
			
			// Set status
			if (autoset != null)
				this._status = autoset;
			else
				this._status = __c.__passState(comp);
				
			// Add data points
			data.add(__c);
			data.add(__a);
			data.add(__b);
		
			// No more results
			close();
		}
	}

	/**
	 * Compares two string values to check how they compare to each other.
	 *
	 * @param __c The comparison to make.
	 * @param __a The expected value.
	 * @param __b The resulting value.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/12
	 */
	public final void compareString(TestComparison __c, String __a,
		String __b)
		throws NullPointerException
	{
		compareObject(__c, __a, __b);
	}
	
	/**
	 * Returns any data which is associated with this test.
	 *
	 * @return The data points, may be an empty array if there is no data.
	 * @since 2016/07/14
	 */
	public final Object[] data()
	{
		// Lock
		List<Object> data = this._data;
		synchronized (this._lock)
		{
			return data.<Object>toArray(new Object[data.size()]);
		}
	}
	
	/**
	 * Returns associated exception information.
	 *
	 * @return An associated exception or {@code null} if none was thrown.
	 * @since 2016/07/14
	 */
	public final Throwable exception()
	{
		// Lock
		synchronized (this._lock)
		{
			return this._exception;
		}
	}
	
	/**
	 * This is invoked when an exception is thrown where it should be
	 * treated as failure (and not success).
	 *
	 * @param __t The exception that caused failure.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/13
	 */
	public final void failingException(Throwable __t)
		throws NullPointerException
	{
		// Check
		if (__t == null)
			throw new NullPointerException("NARG");
	
		// Lock
		synchronized (this._lock)
		{
			// Done
			__setDone();
			
			// Force failure
			this._status = TestPassState.FAIL;
			
			// Set exception
			this._exception = __t;
		
			// No more results
			close();
		}
	}
	
	/**
	 * Returns the fragment name being used.
	 *
	 * @return The fragment name.
	 * @since 2016/07/12
	 */
	public final TestFragmentName fragmentName()
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
	 * Notes something that is neither a passing nor failing state.
	 *
	 * @param __v The value to note.
	 * @since 2016/07/12
	 */
	public final void note(Object __v)
	{
		// Lock
		List<Object> data = this._data;
		synchronized (this._lock)
		{
			// Done
			__setDone();
			
			// Set note and associate data
			this._status = TestPassState.NOTE;
			data.add(__v);
		
			// No more results
			close();
		}
	}
	
	/**
	 * Returns the test status.
	 *
	 * @return The test status.
	 * @throws IllegalStateException If the test has no result.
	 * @since 2016/07/13
	 */
	public final TestPassState status()
		throws IllegalStateException
	{
		// Lock
		synchronized (this._lock)
		{
			// {@squirreljme.error AG05 The result of the test is not
			// known. (The test group; The sub-test; The test fragment)}
			TestPassState rv = this._status;
			if (!this._done	|| rv == null)
				throw new IllegalStateException(String.format(
					"AG05 %s %s %s", this.group,
					this.sub, this.fragment));
			
			// Return it
			return rv;
		}
	}
	
	/**
	 * Marks that the test was a success with no further information.
	 *
	 * @since 2016/07/13
	 */
	public final void success()
	{
		// Lock
		synchronized (this._lock)
		{
			// Done
			__setDone();
			
			// Force success
			this._status = TestPassState.PASS;
		
			// No more results
			close();
		}
	}
	
	/**
	 * Sets that the test is done.
	 *
	 * @throws IllegalStateException If it is already done.
	 * @since 2016/07/13
	 */
	private final void __setDone()
		throws IllegalStateException
	{
		// Lock
		synchronized (this._lock)
		{
			// Mark done
			this._done = true;
		}
	}
	
	/**
	 * Compares two byte arrays to see how they relate.
	 *
	 * @param __a The first array.
	 * @param __b The second array.
	 * @return The result of the comparison.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/14
	 */
	private static int __byteArrayCompare(byte[] __a, byte[] __b)
		throws NullPointerException
	{
		// Check
		if (__a == null || __b == null)
			throw new NullPointerException("NARG");	
		
		// Compare values
		int na = __a.length, nb = __b.length, min = Math.min(na, nb);
		for (int i = 0; i < min; i++)
		{
			// Compare both values
			byte va = __a[i], vb = __b[i];
			int comp;
		
			if (va < vb)
				return -1;
			else if (va > vb)
				return 1;
		}
		
		// Now compare the length
		if (na < nb)
			return -1;
		else if (na > nb)
			return 1;
		
		// Equal
		return 0;
	}
	
	/**
	 * Compares two integer arrays to see how they relate.
	 *
	 * @param __a The first array.
	 * @param __b The second array.
	 * @return The result of the comparison.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/14
	 */
	private static int __intArrayCompare(int[] __a, int[] __b)
		throws NullPointerException
	{
		// Check
		if (__a == null || __b == null)
			throw new NullPointerException("NARG");	
		
		// Compare values
		int na = __a.length, nb = __b.length, min = Math.min(na, nb);
		for (int i = 0; i < min; i++)
		{
			// Compare both values
			int va = __a[i], vb = __b[i];
			int comp;
		
			if (va < vb)
				return -1;
			else if (va > vb)
				return 1;
		}
		
		// Now compare the length
		if (na < nb)
			return -1;
		else if (na > nb)
			return 1;
		
		// Equal
		return 0;
	}
}

