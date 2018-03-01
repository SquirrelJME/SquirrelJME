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

/**
 * This contains the profile of a test and its results.
 *
 * @since 2017/03/28
 */
public class TestProfile
{
	/**
	 * Gets the result for the specified test with the given name.
	 *
	 * @param __n The name of the test to get the result for.
	 * @return The result of this test.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/03/28
	 */
	public TestResult get(TestName __n)
		throws NullPointerException
	{
		// Check
		if (__n == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

