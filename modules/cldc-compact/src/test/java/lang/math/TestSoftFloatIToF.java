// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package lang.math;

import cc.squirreljme.jvm.SoftInteger;
import java.util.Random;
import net.multiphasicapps.tac.TestRunnable;

/**
 * Test soft floating point int to float.
 *
 * @since 2021/04/08
 */
public class TestSoftFloatIToF
	extends TestRunnable
{
	/** The count. */
	private static final int _COUNT =
		128;
	
	/** Quarter of the count. */
	private static final int _QUARTER =
		32;
	
	/** The seed used. */
	private static final long _SEED =
		0xCAFEBABE_DEADBEEFL;
	
	/**
	 * {@inheritDoc}
	 *
	 * @since 2021/04/08
	 */
	@Override
	public void test()
	{
		Random rand = new Random(TestSoftFloatIToF._SEED);
		
		// Do massive conversions
		for (int i = 0; i < TestSoftFloatIToF._COUNT; i++)
		{
			int iVal = (i < TestSoftFloatIToF._QUARTER ? i :
				rand.nextInt());
			float fVal = SoftInteger.toFloat(iVal);//(float)iVal;
			
			this.secondary(String.format("v%03dT%012d", i, iVal)
					.replace('-', 'n'),
				Integer.toString(Float.floatToRawIntBits(fVal),
					16));
		}
	}
}
