// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package lang;

import java.util.Random;
import net.multiphasicapps.tac.TestRunnable;

/**
 * Tests the square root of a number.
 *
 * @since 2018/11/03
 */
public class TestMathSqrt
	extends TestRunnable
{
	/** Random sequence count. */
	public static final int COUNT =
		32;
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/03
	 */
	@Override
	public void test()
	{
		// Linear sequence of values
		for (int i = -2; i <= 128; i++)
			this.secondary(String.format("value-%03d", i),
				Double.doubleToRawLongBits(Math.sqrt(i)));
		
		// Random sequence of bits, generate NaN and such
		Random rand = new Random(0xDEADBEEF);
		for (int i = 0; i < COUNT; i++)
		{
			long raw = rand.nextLong();
			this.secondary(String.format("random-%d", raw),
				Double.doubleToRawLongBits(Math.sqrt(
					Double.longBitsToDouble(raw))));
		}
	}
}

