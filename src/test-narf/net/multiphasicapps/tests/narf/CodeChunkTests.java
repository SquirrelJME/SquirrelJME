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
		for (int i = 0; i < n;)
		{
			// Get the position to add or set byte
			int pos = (i == 0 ? 0 : rand.nextInt(i));
			boolean add = (i == 0 ? true : rand.nextBoolean());
			byte val = (byte)rand.nextInt();
			
			// Adding byte?
			if (add)
			{
				snail.add(pos, val);
				bunny.add(pos, val);
				
				// Increase program size now
				i++;
			}
			
			// Just setting it
			else
			{
				snail.set(pos, val);
				bunny.set(pos, val);
			}
		}
		
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
		__tc.checkEquals("snailbunny", slow, fast);
	}
}

