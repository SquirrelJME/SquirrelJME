// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package util;

import net.multiphasicapps.tac.TestRunnable;
import java.util.Random;

/**
 * Test random number generator, floating point values only.
 *
 * @since 2018/11/02
 */
public class TestRandomFloat
	extends TestRunnable
{
	/** Count for values. */
	public static final int COUNT =
		32;
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/02
	 */
	@Override
	public void test()
	{
		Random rand = new Random(0xCAFED00D);
		
		for (int i = 0; i < COUNT; i++)
			this.secondary(String.format("double-%02d", i),
				Double.doubleToRawLongBits(rand.nextDouble()));
		
		for (int i = 0; i < COUNT; i++)
			this.secondary(String.format("float-%02d", i),
				Float.floatToRawIntBits(rand.nextFloat()));
		
		for (int i = 0; i < COUNT; i++)
			this.secondary(String.format("gauss-%02d", i),
				Double.doubleToRawLongBits(rand.nextGaussian()));
	}
}

