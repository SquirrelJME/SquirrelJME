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
 * This is a matcher which is used to determine which tests to match against.
 *
 * @since 2016/07/12
 */
public final class TestMatcher
{
	/**
	 * Initializes the test matcher.
	 *
	 * @param __m The test to match against.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/07/12
	 */
	public TestMatcher(String __m)
		throws NullPointerException
	{
		// Check
		if (__m == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
}

