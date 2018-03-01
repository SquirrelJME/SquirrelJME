// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.test.basic;

import cc.squirreljme.test.TestDefaultFunction;
import cc.squirreljme.test.TestFunction;
import cc.squirreljme.test.TestResult;

/**
 * This adds two plus two. This reads from a method so that the compiler does
 * not optimize the constant (it performs the actual add operation).
 *
 * @since 2017/03/27
 */
public class TwoPlusTwoMethod
	implements TestDefaultFunction, TestFunction
{
	/**
	 * {@inheritDoc}
	 * @since 2017/03/27
	 */
	@Override
	public void defaultRun(TestResult __r)
		throws Throwable
	{
		__r.result("result", 4);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2017/03/27
	 */
	@Override
	public void run(TestResult __r)
		throws Throwable
	{
		// Do some work to prevent the compiler from optimizing it
		__r.result("result", __add(2, 2));
	}
	
	/**
	 * Adds two values
	 *
	 * @param __a A.
	 * @param __b B.
	 * @return The result of addition
	 * @since 2017/03/27
	 */
	private static int __add(int __a, int __b)
	{
		return __a + __b;
	}
}

