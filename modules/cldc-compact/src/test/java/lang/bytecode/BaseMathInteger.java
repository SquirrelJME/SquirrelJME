// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package lang.bytecode;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import java.util.Random;
import net.multiphasicapps.tac.TestRunnable;

/**
 * Tests base integer math.
 *
 * @since 2023/07/16
 */
@SuppressWarnings("unused")
public abstract class BaseMathInteger
	extends TestRunnable
{
	/**
	 * Calculates the given value.
	 * 
	 * @param __a A value.
	 * @param __b B value.
	 * @return The calculated value.
	 * @since 2023/07/16
	 */
	public abstract int calc(int __a, int __b);
	
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
			for (int b = -8; b <= 8; b++)
				this.__calc(a, b);
		
		// Setup PRNG to test lots of values on
		Random random = new Random(0xDEADBEEF);
		for (int i = 0; i < 100; i++)
			this.__calc(random.nextInt(), random.nextInt());
	}
	
	/**
	 * Calculates the given value.
	 *
	 * @param __a A value.
	 * @param __b B value.
	 * @since 2023/07/16
	 */
	private void __calc(int __a, int __b)
	{
		String key = BaseMathInteger.__secondaryKey(__a, __b);
		try
		{
			this.secondary(key, this.calc(__a, __b));
		}
		catch (ArithmeticException __e)
		{
			this.secondary(key, __e);
		}
	}
	
	/**
	 * Builds a secondary key name.
	 * 
	 * @param __a A value.
	 * @param __b B value.
	 * @return The resultant key string.
	 * @since 2023/07/16
	 */
	private static String __secondaryKey(int __a, int __b)
	{
		return String.format("%d_%d", __a, __b);
	}
}
