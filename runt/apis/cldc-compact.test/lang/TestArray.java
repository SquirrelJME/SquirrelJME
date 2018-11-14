// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package lang;

import java.util.Random;
import net.multiphasicapps.tac.TestRunnable;

/**
 * This tests that arrays work properly.
 *
 * @since 2018/11/14
 */
public class TestArray
	extends TestRunnable
{
	/** Storage count. */
	public static final int COUNT =
		8;
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/14
	 */
	@Override
	public void test()
	{
		Random rand = new Random(0x4C6F7665596F7521L);
		
		// Store values
		boolean az[] = new boolean[COUNT];
		for (int i = 0; i < COUNT; i++)
			az[i] = rand.nextBoolean();
		
		byte ab[] = new byte[COUNT];
		for (int i = 0; i < COUNT; i++)
			ab[i] = (byte)rand.nextInt();
		
		short as[] = new short[COUNT];
		for (int i = 0; i < COUNT; i++)
			as[i] = (short)rand.nextInt();
		
		char ac[] = new char[COUNT];
		for (int i = 0; i < COUNT; i++)
			ac[i] = (char)rand.nextInt();
		
		int ai[] = new int[COUNT];
		for (int i = 0; i < COUNT; i++)
			ai[i] = rand.nextInt();
		
		long al[] = new long[COUNT];
		for (int i = 0; i < COUNT; i++)
			al[i] = rand.nextLong();
		
		float af[] = new float[COUNT];
		for (int i = 0; i < COUNT; i++)
			af[i] = rand.nextFloat();
		
		double ad[] = new double[COUNT];
		for (int i = 0; i < COUNT; i++)
			ad[i] = rand.nextDouble();
		
		// Read all values
		for (int i = 0; i < COUNT; i++)
			this.secondary("boolean-" + i, az[i]);
		
		for (int i = 0; i < COUNT; i++)
			this.secondary("byte-" + i, ab[i]);
		
		for (int i = 0; i < COUNT; i++)
			this.secondary("short-" + i, as[i]);
		
		for (int i = 0; i < COUNT; i++)
			this.secondary("char-" + i, ac[i]);
		
		for (int i = 0; i < COUNT; i++)
			this.secondary("int-" + i, ai[i]);
		
		for (int i = 0; i < COUNT; i++)
			this.secondary("long-" + i, al[i]);
		
		for (int i = 0; i < COUNT; i++)
			this.secondary("float-" + i, Float.floatToRawIntBits(af[i]));
		
		for (int i = 0; i < COUNT; i++)
			this.secondary("double-" + i, Double.doubleToRawLongBits(ad[i]));
	}
}

