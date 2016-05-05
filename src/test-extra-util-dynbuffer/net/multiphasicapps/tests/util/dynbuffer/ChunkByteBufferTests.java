// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.tests.util.dynbuffer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import net.multiphasicapps.util.dynbuffer.DynamicByteBuffer;
import net.multiphasicapps.tests.InvalidTestException;
import net.multiphasicapps.tests.TestChecker;
import net.multiphasicapps.tests.TestInvoker;

/**
 * This class tests that the code chunk setup works correctly.
 *
 * This contains two uses of the same algorithm, one which uses an
 * {@link ArrayList} and one which uses the {@link DynamicByteBuffer}. Both
 * should compute the same (although the {@link ArrayList} is slower and uses
 * much more memory).
 *
 * @since 2016/03/22
 */
public class ChunkByteBufferTests
	implements TestInvoker
{
	/** This is the size of the test buffer. */
	public static final int TEST_BUFFER_SIZE =
		512;
	
	/** Mass removal buffer size. */
	public static final int LOWER_LIMIT =
		64;
	
	/** The seed to use for the random data. */
	public static final long INITIAL_SEED =
		0xBEEF_C0FFEE_CAFEL;
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/22
	 */
	@Override
	public String invokerName()
	{
		return "net.multiphasicapps.util.dynbuffer.DynamicByteBuffer";
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/05/05
	 */
	@Override
	public Iterable<String> invokerTests()
	{
		return Arrays.<String>asList(Long.toString(INITIAL_SEED));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/03/22
	 */
	@Override
	public void runTest(TestChecker __tc, String __st)
		throws NullPointerException, Throwable
	{
		// Check
		if (__tc == null || __st == null)
			throw new NullPointerException("NARG");
		
		// Extract the used seed
		long seed;
		try
		{
			seed = Long.decode(__st);
		}
		
		// Illegal number
		catch (NumberFormatException e)
		{
			throw new InvalidTestException();
		}
		
		// A list containing bytes of stuff being added, along with the chunks
		List<Byte> snail = new ArrayList<>();
		DynamicByteBuffer bunny = new DynamicByteBuffer();
		
		// Initialize PRNG
		Random rand = new Random(seed);
		
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
		
		// Build a unique key for comparisons
		
		// Check for size equality
		__tc.checkEquals(snail.size(), bunny.size());
		
		// Check that sets changed the same values
		__tc.checkEquals(tset, kset);
		
		// Check that removals removed the same values
		__tc.checkEquals(trem, krem);
		
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
		__tc.checkEquals(slow, fast);
		
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
		__tc.checkEquals(snail.size(), bunny.size());
		
		// Check that removals removed the same values
		__tc.checkEquals(trem, krem);
		
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
		__tc.checkEquals(slow, fast);
		__tc.checkEquals(bunny.actualSize(), bunny.actualSize());
		
		// Perform quick compaction
		bunny.quickCompact();
		
		// Load data into those
		for (int i = 0; i < n; i++)
			fast[i] = bunny.get(i);
		
		// Check equality between them
		__tc.checkEquals(snail.size(), bunny.size());
		__tc.checkEquals(slow, fast);
		__tc.checkEquals(bunny.actualSize(), bunny.actualSize());
	}
}

