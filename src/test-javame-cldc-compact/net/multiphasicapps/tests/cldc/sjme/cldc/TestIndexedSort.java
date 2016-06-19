// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.tests.cldc.sjme.cldc;

import java.util.Arrays;
import java.util.Random;
import net.multiphasicapps.squirreljme.cldc.IndexedComparator;
import net.multiphasicapps.squirreljme.cldc.IndexedSort;
import net.multiphasicapps.tests.TestChecker;
import net.multiphasicapps.tests.TestInvoker;

/**
 * This tests that the index based sort operates correctly.
 *
 * @since 2016/06/18
 */
public class TestIndexedSort
	implements TestInvoker
{
	/**
	 * {@inheritDoc}
	 * @since 2016/06/18
	 */
	@Override
	public String invokerName()
	{
		return "net.multiphasicapps.squirreljme.cldc.IndexedSort";
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/06/18
	 */
	@Override
	public Iterable<String> invokerTests()
	{
		return Arrays.<String>asList(Long.toString(0xCAFE_F00DL),
			Long.toString(0xF00D_CAFEL),
			Long.toString(0x19890706L),
			Long.toString(0x201606182129L));
	}
	
	
	/**
	 * {@inheritDoc}
	 * @since 2016/06/18
	 */
	@Override
	public void runTest(TestChecker __tc, String __st)
		throws NullPointerException
	{
		// Decode the seed to use
		long seed;
		try
		{
			seed = Long.decode(__st);
		}
		
		// Bad number
		catch (NumberFormatException e)
		{
			throw new RuntimeException(e);
		}
		
		// Initialize generator
		Random rand = new Random(seed);
		
		// Create array using the test size and fill it with random data
		int n = 32 + rand.nextInt(32);
		int[] test = new int[n];
		for (int i = 0; i < n; i++)
			test[i] = rand.nextInt();
		
		// Run the sorting algorithm on it to get the index order
		int[] dxo = IndexedSort.<int[]>sort(test, 0, n,
			new IndexedComparator<int[]>()
			{
				/**
				 * {@inheritDoc}
				 * @since 2016/06/18
				 */
				@Override
				public int compare(int[] __q, int __a, int __b)
				{
					// Get A and B values
					int a = __q[__a];
					int b = __q[__b];
					
					// Compare
					if (a < b)
						return -1;
					else if (a > b)
						return 1;
					else
						return 0;
				}
			});
		
		// Go through the sorted indices to make sure the values
		// are in sequential order
		int last = Integer.MIN_VALUE;
		boolean failed = false;
		int[] act = new int[n];
		for (int i = 0; i < n; i++)
		{
			// Get value here
			int v = test[dxo[i]];
			act[i] = v;
			
			// Bad?
			failed = (v < last);
			
			// Store current
			last = v;
		}
		
		// Selection sort, which is rather slow
		int[] sortedtest = new int[n];
		boolean[] inserted = new boolean[n];
		for (int i = 0; i < n; i++)
		{
			// Find the lowest non-inserted value
			int j, tv, y = Integer.MAX_VALUE, q = -1;
			for (j = 0; j < n; j++)
				if (!inserted[j] && (tv = test[j]) < y)
				{
					y = tv;
					q = j;
				}
			
			// Insert
			inserted[q] = true;
			sortedtest[i] = y;
		}
		
		// Check that the sorted values match
		__tc.checkEquals(sortedtest, act);
	}
}

