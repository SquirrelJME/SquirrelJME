// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.tests.narf;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.multiphasicapps.narf.NARFCodeChunks;
import net.multiphasicapps.tests.TestChecker;
import net.multiphasicapps.tests.TestInvoker;

/**
 * This class tests that the code chunk setup works correctly.
 *
 * This contains two uses of the same algorithm, one which uses an
 * {@link ArrayList} and one which uses the {@link NARFCodeChunks}. Both
 * should compute the same (although the {@link ArrayList} is slower and uses
 * much more memory).
 *
 * @since 2016/03/22
 */
public class CodeChunkTests
	implements TestInvoker
{
	/** This is the size of the test buffer. */
	public static final int TEST_BUFFER_SIZE =
		512;
	
	/** Mass removal buffer size. */
	public static final int LOWER_LIMIT =
		4;
	
	/** The seed to use for the random data. */
	public static final long INITIAL_SEED =
		0xBEEF_C0FFEE_CAFEL;
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/22
	 */
	public String invokerName()
	{
		return "narf.codechunks";
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/22
	 */
	public void runTests(TestChecker __tc)
		throws NullPointerException
	{
		// Check
		if (__tc == null)
			throw new NullPointerException();
		
		// A list containing bytes of stuff being added, along with the chunks
		List<Byte> snail = new ArrayList<>();
		NARFCodeChunks bunny = new NARFCodeChunks();
		
		// Initialize PRNG
		Random rand = new Random(INITIAL_SEED);
		
		// Go through 
		int n = TEST_BUFFER_SIZE;
		int trem = 0, krem = 0;
		int tset = 0, kset = 0;
		for (int i = 0; i < n;)
		{
			// Get the position to add or set byte
			int pos = (i == 0 ? 0 : Math.max(0, rand.nextInt(i)));
			int act = (i == 0 ? 0 : rand.nextInt(5));
			byte val = (byte)rand.nextInt();
			
			// Removing data?
			if (act == 4)
			{
				// Count OK removals
				if (snail.remove(pos) == bunny.remove(pos))
					krem++;
				
				// Remove count
				i--;
				
				// More removals were done
				trem++;
			}
			
			// Add or set
			else
			{
				// Adding byte?
				if ((act & 1) == 0)
				{
					snail.add(pos, val);
					bunny.add(pos, val);
				
					// Increase program size now
					i++;
				}
			
				// Just setting it
				else
				{
					if (snail.set(pos, val) == bunny.set(pos, val))
						kset++;
				
					// Total set count
					tset++;
				}
			}
		}
		
		// Check for size equality
		__tc.checkEquals("snailbunny.size", snail.size(), bunny.size());
		
		// Check that sets changed the same values
		__tc.checkEquals("snailbunny.sets", tset, kset);
		
		// Check that removals removed the same values
		__tc.checkEquals("snailbunny.removals", trem, krem);
		
		// Create byte arrays for both sets of code
		byte[] slow = new byte[n];
		byte[] fast = new byte[n];
		
		// Load data into those
		for (int i = 0; i < n; i++)
		{
			slow[i] = snail.get(i);
			fast[i] = bunny.get(i);
		}
		
		// Check equality between them
		__tc.checkEquals("snailbunny.data", slow, fast);
		__tc.checkEquals("snailbunny.data.actual", bunny.actualSize(),
			bunny.actualSize());
		
		// Now remove alot of random bytes from the stuff
		for (int i = TEST_BUFFER_SIZE; i >= LOWER_LIMIT;)
		{
			// Get position to remove
			int pos = rand.nextInt(i);
				
			// Count OK removals
			if (snail.remove(pos) == bunny.remove(pos))
				krem++;
			
			// Remove count
			i--;
			
			// More removals were done
			trem++;
		}
		
		// Check for size equality
		__tc.checkEquals("snailbunny.prunesize", snail.size(), bunny.size());
		
		// Check that removals removed the same values
		__tc.checkEquals("snailbunny.pruneremovals", trem, krem);
		
		// Create byte arrays for both sets of code
		n = Math.min(snail.size(), bunny.size());
		slow = new byte[n];
		fast = new byte[n];
		
		// Load data into those
		for (int i = 0; i < n; i++)
		{
			slow[i] = snail.get(i);
			fast[i] = bunny.get(i);
		}
		
		// Check equality between them
		__tc.checkEquals("snailbunny.prunedata", slow, fast);
		__tc.checkEquals("snailbunny.prunedata.actual", bunny.actualSize(),
			bunny.actualSize());
		
		// Perform quick compaction
		bunny.quickCompact();
		
		// Load data into those
		for (int i = 0; i < n; i++)
			fast[i] = bunny.get(i);
		
		// Check equality between them
		__tc.checkEquals("snailbunny.cprunesize", snail.size(), bunny.size());
		__tc.checkEquals("snailbunny.cprunedata", slow, fast);
		__tc.checkEquals("snailbunny.cprunedata.actual", bunny.actualSize(),
			bunny.actualSize());
	}
}

