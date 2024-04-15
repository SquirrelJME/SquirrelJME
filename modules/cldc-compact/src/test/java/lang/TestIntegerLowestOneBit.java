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
import net.multiphasicapps.tac.TestSupplier;

/**
 * Tests {@link Integer#lowestOneBit(int)}.
 *
 * @since 2020/10/29
 */
public class TestIntegerLowestOneBit
	extends TestSupplier<int[]>
{
	/** The number of times to try. */
	private static final int _COUNT = 128;
	
	/**
	 * {@inheritDoc}
	 * @since 2020/10/29
	 */
	@Override
	public int[] test()
	{
		Random rand = new Random(0xDEAD_BEEF);
		
		int[] result = new int[TestIntegerLowestOneBit._COUNT];
		for (int i = 0; i < TestIntegerLowestOneBit._COUNT; i++)
			result[i] = Integer.lowestOneBit((i < 32 ? (1 << i) : rand.nextInt(
				Math.abs(1 + (1 << (i & 31))))));
				
		this.secondary("zero", Integer.lowestOneBit(0));
		this.secondary("min", Integer.lowestOneBit(Integer.MIN_VALUE));
		this.secondary("max", Integer.lowestOneBit(Integer.MAX_VALUE));
		
		return result;
	}
}
