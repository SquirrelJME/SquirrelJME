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
	/** The first random seed to try. */
	public static final long RANDOM_SEED_A =
		0xCAFE_F00DL;
	
	/** The second random seed to try. */
	public static final long RANDOM_SEED_B =
		0xF00D_CAFEL;
	
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
		return Arrays.<String>asList(Long.toString(RANDOM_SEED_A),
			Long.toString(RANDOM_SEED_B));
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
			return new RuntimeException(e);
		}
		
		// Initialize generator
		Random ran = new Random(seed);
		
		throw new Error("TODO");
	}
}

