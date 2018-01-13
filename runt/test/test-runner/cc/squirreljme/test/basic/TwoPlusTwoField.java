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
 * This adds two plus two. This reads from a field so that the compiler does
 * not optimize the constant (it performs the actual add operation).
 *
 * @since 2017/03/27
 */
public class TwoPlusTwoField
	implements TestDefaultFunction, TestFunction
{
	/** Contains zero. */
	private static volatile int ZERO;
	
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
		int two = ZERO | 2;
		__r.result("result", two + two);
	}
}

