// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.tests.cldc.java.lang;

import net.multiphasicapps.tests.TestChecker;
import net.multiphasicapps.tests.TestInvoker;

/**
 * Tests integers.
 *
 * @since 2016/05/04
 */
public class TestInteger
	implements TestInvoker
{
	/**
	 * {@inheritDoc}
	 * @since 2016/05/05
	 */
	@Override
	public String invokerName()
	{
		return "java.lang.Integer";
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/05
	 */
	@Override
	public Iterable<String> invokerTests()
	{
		throw new Error("TODO");
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/05
	 */
	@Override
	public void runTest(TestChecker __tc, String __st)
		throws NullPointerException
	{
		// Check
		if (__tc == null || __st == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
}

