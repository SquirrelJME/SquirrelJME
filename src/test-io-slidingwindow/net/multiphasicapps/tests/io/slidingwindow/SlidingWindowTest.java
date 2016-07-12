// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.tests.io.slidingwindow;

import java.util.Arrays;
import java.util.Random;
import net.multiphasicapps.io.slidingwindow.SlidingByteWindow;
import net.multiphasicapps.tests.IndividualTest;
import net.multiphasicapps.tests.InvalidTestException;
import net.multiphasicapps.tests.TestChecker;
import net.multiphasicapps.tests.TestGroupName;
import net.multiphasicapps.tests.TestFamily;

/**
 * This tests the sliding byte window to make sure that it can correctly
 * access data which is in the past (which is used by the inflate
 * algorithm).
 *
 * @since 2016/04/08
 */
public class SlidingWindowTest
	implements TestInvoker
{
	/** The test size of the sliding window. */
	public static final int WINDOW_SIZE =
		512;
	
	/** The number of bytes that should be written (to test sliding). */
	public static final int WRITE_TOTAL =
		1024;
	
	/** Maximum failure count. */
	public static final int MAX_FAILURES =
		8;
	
	/** Random Seed. */
	public static final long RANDOM_SEED =
		0xC0FFEE_15_407L;
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/08
	 */
	@Override
	public void runTest(IndividualTest __t)
		throws NullPointerException, Throwable
	{
		// Check
		if (__t == null)
			throw new NullPointerException();
		
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
		
		// Create a sliding window and another buffer of a given size where
		// bytes are to be written to.
		SlidingByteWindow sbw = new SlidingByteWindow(WINDOW_SIZE);
		byte[] raw = new byte[WINDOW_SIZE];
		
		// Setup random number source
		Random rand = new Random(seed);
		
		// While the window is not yet full
		int totalbytes = 0, checknum = 0;
		int checkpass = 0, checkfail = 0;
		int written = 0;
		while (written < WRITE_TOTAL && checkfail < MAX_FAILURES)
		{
			// Is this a check or a put operation?
			boolean docheck = (rand.nextInt(15) == 0);
			
			// Check byte in the window, but only if the window is past a
			// minimum size
			if (docheck && totalbytes >= 4)
			{
				// How far back? And how much?
				int ago = rand.nextInt(totalbytes - 1) + 1;
				int len = Math.max(1, rand.nextInt(ago));
				
				// Allocate destination buffers
				byte[] a = new byte[len];
				byte[] b = new byte[len];
				
				// From the byte array
				__ago(raw, ago, a, 0, len, totalbytes);
				
				// From the real window
				sbw.get(ago, b, 0, len);
				
				// The arrays must be equal
				checknum++;
				if (!Arrays.equals(a, b))
					checkfail++;
				else
					checkpass++;
			}
			
			// Add byte to the window
			else
			{
				// Byte value to add
				byte add = (byte)rand.nextInt(256);
				
				// Add to the end of the window
				sbw.append(add);
				
				// At the window maximum? Then all bytes need to be shifted
				// down
				if (totalbytes >= WINDOW_SIZE)
				{
					// Shift down
					for (int i = 0; i < totalbytes - 1; i++)
						raw[i] = raw[i + 1];
					
					// Write at the end
					raw[totalbytes - 1] = add;
				}
				
				// Still below the max
				else
					raw[totalbytes++] = add;
				
				// Increase write count
				written++;
			}
		}
		
		// Setup final entire buffer setup
		byte[] rawli = new byte[totalbytes];
		byte[] fulla = new byte[totalbytes];
		byte[] fullb = new byte[totalbytes];
		
		// Copy raw data
		for (int i = 0; i < totalbytes; i++)
			rawli[i] = raw[i];
		
		// From the byte array
		__ago(raw, totalbytes, fulla, 0, totalbytes, totalbytes);
		
		// From the sliding window
		sbw.get(totalbytes, fullb, 0, totalbytes);
		
		// Check for no failures
		if (checkfail != 0)
			__tc.checkEquals(0, checkfail);
		
		// Check all the bytes against the raw window
		else
			__tc.checkEquals(fulla, fullb);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/08
	 */
	@Override
	public TestFamily testFamily()
	{
		return new TestFamily(
			"net.multiphasicapps.io.slidingwindow.SlidingByteWindow",
			Long.toString(RANDOM_SEED));
	}
	
	/**
	 * Obtains a bunch of bytes near the end of the sliding window.
	 *
	 * @param __src The source array.
	 * @param __ago The number of bytes to look in the past in.
	 * @param __dest The destination array.
	 * @param __o The write offset in the destination.
	 * @param __l The number of bytes to copy.
	 * @param __end The total number of buffer bytes.
	 * @since 2016/04/08
	 */
	private void __ago(byte[] __src, int __ago, byte[] __dest, int __o,
		int __l, int __end)
		throws NullPointerException
	{
		// Check
		if (__src == null || __dest == null)
			throw new NullPointerException("NARG");
		
		// Go through all bytes
		for (int i = 0; i < __l; i++)
			__dest[__o + i] = __src[(__end - __ago) + i];
	}
}

