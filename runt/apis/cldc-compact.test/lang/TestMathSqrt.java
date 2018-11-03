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

/**
 * Tests the square root of a number.
 *
 * @since 2018/11/03
 */
public class TestMathSqrt
	extends TestRunnable
{
	/**
	 * {@inheritDoc}
	 * @since 2018/11/03
	 */
	@Override
	public void test()
	{
		for (int i = -2; i <= 128; i++)
		{
			long rawbits = Double.doubleToRawLongBits(Math.sqrt(i));
			
			// Debug
			todo.DEBUG.note("%d: %d", i, rawbits);
			
			this.secondary(String.format("value-%03d", i), rawbits);
		}
	}
}

