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

import java.util.Arrays;
import net.multiphasicapps.tac.TestSupplier;

/**
 * Test that binary search works properly.
 *
 * @since 2018/10/28
 */
public class TestArrayBinarySearch
	extends TestSupplier<Long>
{
	/** The array length. */
	public static final int LENGTH =
		31;
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/28
	 */
	@Override
	public Long test()
	{
		long rv = 0;
		
		// Setup array with values
		int[] values = new int[LENGTH];
		for (int i = 0; i < LENGTH; i++)
			values[i] = i * 2;
		
		// Search for all values
		for (int i = 0; i < LENGTH * 2; i++)
		{
			// Find the value
			int res = Arrays.binarySearch(values, i);
			
			// Value is even, it will be in the array
			int want;
			if ((i & 1) == 0)
				want = i / 2;
			
			// Is odd, will not be in the array
			else
				want = -((i / 2) + 2);
			
			// Is this our expected value?
			if (res == want)
				rv |= (1L << (long)i);
			
			todo.DEBUG.note("%s -> %s should be %s", i, res, want);
		}
		
		return rv;
	}
}

