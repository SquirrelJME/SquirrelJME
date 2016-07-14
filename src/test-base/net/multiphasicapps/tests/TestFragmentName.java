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

/**
 * This represents the name of a fragment test.
 *
 * @since 2016/07/12
 */
public class TestFragmentName
	extends __BaseName__
	implements Comparable<TestFragmentName>
{
	/**
	 * Initializes the sub-test fragment name.
	 *
	 * @param __n The name to use.
	 * @since 2016/07/12
	 */
	private TestFragmentName(String __n)
	{
		super(__n);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/12
	 */
	@Override
	public final int compareTo(TestFragmentName __o)
		throws NullPointerException
	{
		// Check
		if (__o == null)
			throw new NullPointerException("NARG");
		
		// Compare name
		return this.string.compareTo(__o.string);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/12
	 */
	@Override
	public final boolean equals(Object __o)
	{
		return super.equals(__o) && (__o instanceof TestFragmentName);
	}
	
	/**
	 * Creates or uses a cached fragment name.
	 *
	 * @param __n The test name.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/12
	 */
	public static TestFragmentName of(String __n)
		throws NullPointerException
	{
		return new TestFragmentName(__n);
	}
}

