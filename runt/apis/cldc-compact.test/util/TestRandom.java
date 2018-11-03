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

import net.multiphasicapps.tac.TestRunnable;
import java.util.Random;

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
		
		for (int i = 0; i < COUNT; i++)
			this.secondary(String.format("boolean-%02d", i),
				rand.nextBoolean());
		
		for (int i = 0; i < COUNT; i++)
			this.secondary(String.format("int-%02d", i),
				rand.nextInt());
		
		for (int i = 0; i < COUNT; i++)
			this.secondary(String.format("long-%02d", i),
				rand.nextLong());
			
		for (int i = 0; i < COUNT; i++)
			this.secondary(String.format("range-%02d", i),
				rand.nextInt(i + 1));
	}
}

