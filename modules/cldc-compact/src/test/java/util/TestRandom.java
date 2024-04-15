// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package util;

import java.util.Random;
import net.multiphasicapps.tac.TestRunnable;

/**
 * Tests the random number generator, integer values only.
 *
 * @since 2018/11/02
 */
public class TestRandom
	extends TestRunnable
{
	/** Count for values. */
	public static final int COUNT =
		32;
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/02
	 */
	@Override
	public void test()
	{
		Random rand = new Random(0xCAFEBABE);
		
		for (int i = 0; i < TestRandom.COUNT; i++)
			this.secondary(String.format("boolean-%02d", i),
				rand.nextBoolean());
		
		for (int i = 0; i < TestRandom.COUNT; i++)
			this.secondary(String.format("int-%02d", i),
				rand.nextInt());
		
		for (int i = 0; i < TestRandom.COUNT; i++)
			this.secondary(String.format("long-%02d", i),
				rand.nextLong());
			
		for (int i = 0; i < TestRandom.COUNT; i++)
			this.secondary(String.format("range-%02d", i),
				rand.nextInt(i + 1));
	}
}

