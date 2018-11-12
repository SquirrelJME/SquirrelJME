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

import java.util.Random;
import net.multiphasicapps.tac.TestSupplier;

/**
 * Tests the number of bits in values.
 *
 * @since 2018/11/11
 */
public class TestBitCount
	extends TestSupplier<Integer>
{
	/** The number of values to count. */
	public static final int COUNT =
		32;
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/11
	 */
	@Override
	public Integer test()
	{
		Random rand = new Random(0xDEADBEEF);
		
		int rv = 0;
		for (int i = 0; i < COUNT; i++)
		{
			int test = rand.nextInt(),
				bits = Integer.bitCount(test);
			
			this.secondary("" + test, bits);
			
			rv += bits;
		}
		
		return rv;
	}
}

