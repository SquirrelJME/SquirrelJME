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

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * This represents a family of tests that are available for execution.
 *
 * @since 2016/07/12
 */
public final class TestFamily
	implements Comparable<TestFamily>, Iterable<TestSubName>
{
	/** The test group. */
	protected final TestGroupName group;
	
	/** The sub-tests in this group. */
	private final TestSubName[] _subs;
	
	/** String representation. */
	private volatile Reference<String> _string;
	
	/**
	 * Initializes the test family names and details.
	 *
	 * @param __g The group of the test.
	 * @param __s The sub-tests that are default for this family.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/12
	 */
	public TestFamily(String __g, String... __s)
		throws NullPointerException
	{
		this(TestGroupName.of(__g), __stringArrayToSubs(__s));
	}
	
	/**
	 * Initializes the test family.
	 *
	 * @param __g The group of the test.
	 * @param __s The sub-tests that are default for this family.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/12
	 */
	public TestFamily(TestGroupName __g, TestSubName... __s)
		throws NullPointerException
	{
		// Check
		if (__g == null)
			throw new NullPointerException("NARG");
		
		// Set
		this.group = __g;
		this._subs = (__s = (__s == null ? new TestSubName[0] : __s.clone()));
		
		// Check
		for (TestSubName s : __s)
			if (s == null)
				throw new NullPointerException("NARG");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/12
	 */
	@Override
	public final int compareTo(TestFamily __b)
	{
		// Compare group first
		int rv = this.group.compareTo(__b.group);
		if (rv != 0)
			return rv;
		
		// Then the subs
		TestSubName[] a = this._subs, b = __b._subs;
		int na = a.length, nb = b.length;
		int min = Math.min(na, nb);
		for (int i = 0; i < min; i++)
		{
			rv = a[i].compareTo(b[i]);
			if (rv != 0)
				return rv;
		}
		
		// Compare size
		if (na < nb)
			return -1;
		else if (na > nb)
			return 1;
		return 0;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/12
	 */
	@Override
	public final boolean equals(Object __o)
	{
		// Check
		if (!(__o instanceof TestFamily))
			return false;
		
		// Cast
		TestFamily o = (TestFamily)__o;
		return this.group.equals(o.group) &&
			Arrays.equals(this._subs, o._subs);
	}
	
	/**
	 * Returns the test group name.
	 *
	 * @return The test group name.
	 * @since 2016/07/13
	 */
	public final TestGroupName groupName()
	{
		return this.group;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/12
	 */
	@Override
	public final int hashCode()
	{
		int rv = 1;
		for (TestSubName s : this._subs)
			rv = (31 * rv) + s.hashCode();
		return rv ^ this.group.hashCode();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/12
	 */
	@Override
	public final Iterator<TestSubName> iterator()
	{
		return new Iterator<TestSubName>()
			{
				/** Iteration limit. */
				protected final int limit =
					TestFamily.this._subs.length;
				
				/** Next index. */
				private volatile int _index;
				
				/**
				 * {@inheritDoc}
				 * @since 2016/07/12
				 */
				@Override
				public boolean hasNext()
				{
					return this._index < this.limit;
				}
				
				/**
				 * {@inheritDoc}
				 * @since 2016/07/12
				 */
				@Override
				public TestSubName next()
				{
					// {@squirreljme.error AG03 No more sub-tests.}
					int next = this._index;
					if (next >= this.limit)
						throw new NoSuchElementException("AG03");
					
					// Set next
					this._index = next + 1;
					
					// Return test
					return TestFamily.this._subs[next];
				}
				
				/**
				 * {@inheritDoc}
				 * @since 2016/07/12
				 */
				@Override
				public void remove()
				{
					// {@squirreljme.error AG04 Sub tests cannot be removed.}
					throw new UnsupportedOperationException("AG04");
				}
			};
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/12
	 */
	@Override
	public final String toString()
	{
		// Get
		Reference<String> ref = this._string;
		String rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
		{
			// Build
			StringBuilder sb = new StringBuilder(this.group.toString());
			sb.append(":[");
			boolean comma = false;
			for (TestSubName s : this._subs)
			{
				if (comma)
					sb.append(", ");
				comma = true;
				
				// Add test
				sb.append(s.toString());
			}
			sb.append(']');
			
			// Finish
			this._string = new WeakReference<>((rv = sb.toString()));
		}
		
		// Return it
		return rv;
	}
	
	/**
	 * Converts an array of string into an array of sub-tests.
	 *
	 * @param __s The string of input tests.
	 * @return The array representation of sub-tests.
	 * @since 2016/07/12
	 */
	private static TestSubName[] __stringArrayToSubs(String... __s)
	{
		// Not used
		if (__s == null)
			return null;
		
		// Setup new
		int n = __s.length;
		TestSubName[] rv = new TestSubName[n];
		for (int i = 0; i < n; i++)
			rv[i] = TestSubName.of(__s[i]);
		
		// Return it
		return rv;
	}
}

