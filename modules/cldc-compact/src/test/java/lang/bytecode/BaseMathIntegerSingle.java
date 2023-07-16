// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package lang.bytecode;

import java.util.Random;
import net.multiphasicapps.tac.TestRunnable;

/**
 * Tests base integer math, but only with a single value.
 *
 * @since 2023/07/16
 */
@SuppressWarnings("unused")
public abstract class BaseMathIntegerSingle
	extends TestRunnable
{
	/**
	 * Calculates the given value.
	 * 
	 * @param __a A value.
	 * @return The calculated value.
	 * @since 2023/07/16
	 */
	public abstract int calc(int __a);
	
	/**
	 * {@inheritDoc}
	 * @since 2023/07/16
	 */
	@Override
	public void test()
		throws Throwable
	{
		// Test base constants, for sanity
		for (int a = -8; a <= 8; a++)
			this.secondary(BaseMathIntegerSingle.__secondaryKey(a),
				this.calc(a));
		
		// Setup PRNG to test lots of values on
		Random random = new Random(0xDEADBEEF);
		for (int i = 0; i < 100; i++)
		{
			int a = random.nextInt();
			
			this.secondary(BaseMathIntegerSingle.__secondaryKey(a),
				this.calc(a));
		}
	}
	
	/**
	 * Builds a secondary key name.
	 * 
	 * @param __a A value.
	 * @return The resultant key string.
	 * @since 2023/07/16
	 */
	private static String __secondaryKey(int __a)
	{
		return Integer.toString(__a);
	}
}
