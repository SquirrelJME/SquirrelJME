// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.test;

/**
 * This is a checker for tests, tests which are run invoke this to make sure
 * that tests operate correctly.
 *
 * @since 2016/03/03
 */
public class TestChecker
{
	/** The test caller. */
	protected final TestCaller caller;
	
	/** The test being invoked. */
	protected final TestInvoker invoker;	
	
	/**
	 * This initializes the test checker.
	 *
	 * @param __tc The test caller.
	 * @param __ti The test invoker.
	 * @throws NullPointerException On null arguments.
	 * @since 2016/03/03
	 */
	public TestChecker(TestCaller __tc, TestInvoker __ti)
		throws NullPointerException
	{
		// Check
		if (__tc == null || __ti == null)
			throw new NullPointerException();
		
		// Set
		caller = __tc;
		invoker = __ti;
	}
}

