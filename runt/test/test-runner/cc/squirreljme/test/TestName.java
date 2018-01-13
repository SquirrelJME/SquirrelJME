// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.test;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents the name of a test.
 *
 * This class is immutable.
 *
 * @since 2017/03/27
 */
public final class TestName
	implements Comparable<TestName>
{
	/** Name fragments. */
	private final String[] _fragments;
	
	/** String representation of the name. */
	private volatile Reference<String> _string;
	
	/** Prefixed string. */
	private volatile Reference<String> _prefixed;
	
	/**
	 * Initializes the test name.
	 *
	 * @param __n The name fragments to decode.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/27
	 */
	public TestName(String... __n)
		throws NullPointerException
	{
		// Check
		if (__n == null)
			throw new NullPointerException("NARG");
		
		// Splice string by fragments containing dots
		List<String> fragments = new ArrayList<>();
		for (String q : __n)
		{
			// Split fragments
			for (int i = 0, n = q.length(); i < n; i++)
			{
				// Find next dot, or the end of the string
				int nd = q.indexOf('.', i);
				if (nd < 0)
					nd = n;
				
				// Add
				fragments.add(q.substring(i, nd).trim());
				
				// Set to dot position (gets skipped over)
				i = nd;
			}
		}
		
		// Store
		this._fragments = fragments.<String>toArray(
			new String[fragments.size()]);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/03/27
	 */
	@Override
	public int compareTo(TestName __o)
		throws NullPointerException
	{
		// Check
		if (__o == null)
			throw new NullPointerException("NARG");
		
		return toString().compareTo(__o.toString());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/03/27
	 */
	@Override
	public boolean equals(Object __o)
	{
		// Check
		if (!(__o instanceof TestName))
			return false;
		
		return toString().equals(__o.toString());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/03/27
	 */
	@Override
	public int hashCode()
	{
		return toString().hashCode();
	}
	
	/**
	 * Returns the name of this test prefixed by the package prefix.
	 *
	 * @return The prefixed string.
	 * @since 2017/03/27
	 */
	public String prefixed()
	{
		Reference<String> ref = this._prefixed;
		String rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
		{
			// Never end in a dot
			rv = "cc.squirreljme.test." + toString();
			if (rv.charAt(rv.length() - 1) == '.')
				rv = rv.substring(0, rv.length() - 1);
			
			// Cache
			this._prefixed = new WeakReference<>(rv);
		}
		
		return rv;
	}
	
	/**
	 * Resolves a test by the given name as a sub-test.
	 *
	 * @param __s The name of the test to resolve.
	 * @return The name of the test.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/27
	 */
	public TestName resolve(String __s)
		throws NullPointerException
	{
		// Check
		if (__s == null)
			throw new NullPointerException("NARG");
		
		// Make a bigger array
		String[] fragments = this._fragments;
		int n = fragments.length, i;
		String[] rv = new String[n + 1];
		
		// Setup copy
		for (i = 0; i < n; i++)
			rv[i] = fragments[i];
		rv[i] = __s;
		
		// Create
		return new TestName(rv);
	}
	
	/**
	 * Returns the resource name for these tests.
	 *
	 * @return The resource list for these tests.
	 * @since 2017/03/27
	 */
	public String resourceList()
		throws NullPointerException
	{
		return "/" + prefixed().replace('.', '/') + "/tests.lst";
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/03/27
	 */
	@Override
	public String toString()
	{
		Reference<String> ref = this._string;
		String rv;
		
		// Cache?
		if (ref == null || null == (rv = ref.get()))
		{
			// Combine
			StringBuilder sb = new StringBuilder();
			for (String f : this._fragments)
			{
				if (sb.length() > 0)
					sb.append(".");
				sb.append(f);
			}
			
			this._string = new WeakReference<>((rv = sb.toString()));
		}
		
		return rv;
	}
}

