// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package lang;

import net.multiphasicapps.tac.TestRunnable;
import java.util.Random;

/**
 * Tests the log of a number.
 *
 * @since 2018/11/03
 */
public class TestMathLog
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
	@SuppressWarnings({"deprecated"})
	public void test()
	{
		// Linear sequence of values
		for (int i = -2; i <= 128; i++)
			this.secondary(String.format("value-%03d", i),
				Double.doubleToRawLongBits(Math.log(i)));
		
		// Random sequence of bits, generate NaN and such
		Random rand = new Random(0xDEADBEEF);
		for (int i = 0; i < COUNT; i++)
		{
			long raw = rand.nextLong();
			this.secondary(String.format("random-%d", raw),
				Double.doubleToRawLongBits(Math.log(
					Double.longBitsToDouble(raw))));
		}
	}
}


