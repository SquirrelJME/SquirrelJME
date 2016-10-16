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
 * This represents the name of a sub-test.
 *
 * @since 2016/07/11
 */
public final class TestSubName
	extends __BaseName__
	implements Comparable<TestSubName>
{
	/**
	 * Initializes the sub-test name.
	 *
	 * @param __n The sub-test name.
	 * @since 2016/07/11
	 */
	private TestSubName(String __n)
	{
		super(__n);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/11
	 */
	@Override
	public final int compareTo(TestSubName __o)
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
	 * @since 2016/07/11
	 */
	@Override
	public final boolean equals(Object __o)
	{
		return super.equals(__o) && (__o instanceof TestSubName);
	}
	
	/**
	 * Creates or uses a cached sub name.
	 *
	 * @param __n The test name.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/12
	 */
	public static TestSubName of(String __n)
		throws NullPointerException
	{
		return new TestSubName(__n);
	}
}

