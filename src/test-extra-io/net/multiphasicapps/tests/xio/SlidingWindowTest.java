// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package net.multiphasicapps.tests.xio;

import java.util.Random;
import net.multiphasicapps.io.SlidingByteWindow;
import net.multiphasicapps.tests.TestChecker;
import net.multiphasicapps.tests.TestInvoker;

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
		8192;
	
	/** Random Seed. */
	public static final long RANDOM_SEED =
		0xC0FFEE_15_407L;
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/08
	 */
	@Override
	public String invokerName()
	{
		return "extraio.slidingwindow";
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/04/08
	 */
	@Override
	public void runTests(TestChecker __tc)
		throws NullPointerException
	{
		// Check
		if (__tc == null)
			throw new NullPointerException();
		
		// Create a sliding window and another buffer of a given size where
		// bytes are to be written to.
		SlidingByteWindow sbw = new SlidingByteWindow(WINDOW_SIZE);
		byte[] raw = new byte[WINDOW_SIZE];
		
		// Setup random number source
		Random rand = new Random(RANDOM_SEED);
		
		// While the window is not yet full
		int totalbytes = 0;
		while (totalbytes < WINDOW_SIZE)
		{
			// Is this a check or a put operation?
			boolean docheck = rand.nextBoolean();
			
			// Check byte in the window
			if (docheck)
				throw new Error("TODO");
			
			// Add byte to the window
			else
			{
				// Byte value to add
				byte add = (byte)rand.nextInt(256);
				
				throw new Error("TODO");
			}
		}
	}
}

