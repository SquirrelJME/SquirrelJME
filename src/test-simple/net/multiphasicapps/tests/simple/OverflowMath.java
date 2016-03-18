// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.tests.simple;

import net.multiphasicapps.tests.TestChecker;
import net.multiphasicapps.tests.TestInvoker;

/**
 * This tests for some overflowing operations.
 *
 * @since 2016/03/03
 */
public class OverflowMath
	implements TestInvoker
{
	/**
	 * {@inheritDoc}
	 * @since 2016/03/03
	 */
	public String invokerName()
	{
		return "simple.overflowmath";
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/03
	 */
	public void runTests(TestChecker __tc)
		throws NullPointerException
	{
		// Check
		if (__tc == null)
			throw new NullPointerException();
		
		// (int) The minimum minus one is the maximum
		__tc.checkEquals("intminsuboneismax",
			Integer.MAX_VALUE, __minIntValue() - 1);
		
		// (int) Underflowed value minus one is greater than itself
		__tc.checkEquals("intminsubonegreaterthanmax",
			true, (__minIntValue() - 1) >= __minIntValue());
		
		// (long) The minimum minus one is the maximum
		__tc.checkEquals("longminsuboneismax",
			Long.MAX_VALUE, __minLongValue() - 1L);
		
		// (long) Underflowed value minus one is greater than itself
		__tc.checkEquals("longminsubonegreaterthanmax",
			true, (__minLongValue() - 1) >= __minLongValue());
	}
	
	/**
	 * Returns the minimum int value.
	 *
	 * @return The minimal int value.
	 * @since 2016/03/03
	 */
	private static int __minIntValue()
	{
		return new Integer(Integer.MIN_VALUE);
	}
	
	/**
	 * Returns the minimum long value.
	 *
	 * @return The minimal long value.
	 * @since 2016/03/03
	 */
	private static long __minLongValue()
	{
		return new Long(Long.MIN_VALUE);
	}
}

