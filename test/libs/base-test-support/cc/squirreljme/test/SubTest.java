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
 * This represents a single sub-test which defines a single test which performs
 * a given test as it runs.
 *
 * @since 2018/03/05
 */
public abstract class SubTest
{
	/** The prefix to snip out. */
	private static final String _SNIP_PREFIX =
		"cc.squirreljme.test.";
	
	/** The name of this test. */
	protected final String testname;
	
	/**
	 * Initializes the sub-test with the given default parameters.
	 *
	 * @param __parms The default parameters to use for the test.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/03/05
	 */
	public SubTest(Parameters... __parms)
		throws NullPointerException
	{
		if (__parms == null)
			throw new NullPointerException("NARG");
		
		// Determine the name of this test
		String testname = this.getClass().getName().replace('$', '.').
			toLowerCase();
		if (testname.startsWith(_SNIP_PREFIX))
			testname = testname.substring(_SNIP_PREFIX.length());
		this.testname = testname;
		
		throw new todo.TODO();
	}
	
	/**
	 * Returns the name of this test.
	 *
	 * @return The name of this test.
	 * @since 2018/03/05
	 */
	public final String testName()
	{
		return this.testname;
	}
}

