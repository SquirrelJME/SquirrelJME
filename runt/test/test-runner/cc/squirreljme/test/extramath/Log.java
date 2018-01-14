// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.test.extramath;

import cc.squirreljme.test.TestDefaultFunction;
import cc.squirreljme.test.TestFunction;
import cc.squirreljme.test.TestResult;
import net.multiphasicapps.math.ExtraMath;

/**
 * This tests the logarithms in the math library.
 *
 * @since 2017/07/25
 */
public class Log
	implements TestFunction
{
	/**
	 * {@inheritDoc}
	 * @since 2017/07/25
	 */
	@Override
	public void run(TestResult __r)
		throws Throwable
	{
		// Natural
		__r.result("log(2.0F)", ExtraMath.log(2.0F));
		__r.result("log(16.0F)", ExtraMath.log(16.0F));
		__r.result("log(100.0F)", ExtraMath.log(100.0F));
		__r.result("log(333.0F)", ExtraMath.log(333.0F));
		__r.result("log(12345678.87654321F)",
			ExtraMath.log(12345678.87654321F));
		
		__r.result("log(2.0D)", ExtraMath.log(2.0D));
		__r.result("log(16.0D)", ExtraMath.log(16.0D));
		__r.result("log(100.0D)", ExtraMath.log(100.0D));
		__r.result("log(333.0D)", ExtraMath.log(333.0D));
		__r.result("log(12345678.87654321D)",
			ExtraMath.log(12345678.87654321D));
		
		// Base 2
		__r.result("log2(2.0F)", ExtraMath.log2(2.0F));
		__r.result("log2(16.0F)", ExtraMath.log2(16.0F));
		__r.result("log2(100.0F)", ExtraMath.log2(100.0F));
		__r.result("log2(333.0F)", ExtraMath.log2(333.0F));
		__r.result("log2(12345678.87654321F)",
			ExtraMath.log2(12345678.87654321F));
		
		__r.result("log2(2.0D)", ExtraMath.log2(2.0D));
		__r.result("log2(16.0D)", ExtraMath.log2(16.0D));
		__r.result("log2(100.0D)", ExtraMath.log2(100.0D));
		__r.result("log2(333.0D)", ExtraMath.log2(333.0D));
		__r.result("log2(12345678.87654321D)",
			ExtraMath.log2(12345678.87654321D));
		
		// Base 10
		__r.result("log10(2.0F)", ExtraMath.log10(2.0F));
		__r.result("log10(16.0F)", ExtraMath.log10(16.0F));
		__r.result("log10(100.0F)", ExtraMath.log10(100.0F));
		__r.result("log10(333.0F)", ExtraMath.log10(333.0F));
		__r.result("log10(12345678.87654321F)",
			ExtraMath.log10(12345678.87654321F));
		
		__r.result("log10(2.0D)", ExtraMath.log10(2.0D));
		__r.result("log10(16.0D)", ExtraMath.log10(16.0D));
		__r.result("log10(100.0D)", ExtraMath.log10(100.0D));
		__r.result("log10(333.0D)", ExtraMath.log10(333.0D));
		__r.result("log10(12345678.87654321D)",
			ExtraMath.log10(12345678.87654321D));
	}
}

