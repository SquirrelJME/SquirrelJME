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
 * This represents the group that a test is within.
 *
 * @since 2016/07/10
 */
public final class TestGroupName
	extends __BaseName__
	implements Comparable<TestGroupName>
{
	/**
	 * Initializes the test group name.
	 *
	 * @param __n The test group name.
	 * @since 2016/07/11
	 */
	private TestGroupName(String __n)
	{
		super(__n);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/07/11
	 */
	@Override
	public final int compareTo(TestGroupName __o)
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
		return super.equals(__o) && (__o instanceof TestGroupName);
	}
	
	/**
	 * Creates or uses a cached group name.
	 *
	 * @param __n The test name.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/12
	 */
	public static TestGroupName of(String __n)
		throws NullPointerException
	{
		return new TestGroupName(__n);
	}
}

