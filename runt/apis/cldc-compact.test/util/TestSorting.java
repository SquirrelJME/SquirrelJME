// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package util;

import java.util.Arrays;
import java.util.Random;
import net.multiphasicapps.tac.TestRunnable;

/**
 * Tests that arrays can be sorted.
 *
 * @since 2019/05/09
 */
public class TestSorting
	extends TestRunnable
{
	/** The number of elements to sort through. */
	public static final int COUNT =
		27;
	
	/**
	 * {@inheritDoc}
	 * @since 2019/05/09
	 */
	@Override
	public void test()
	{
		Random rand = new Random(123456789);
		
		// Byte
		byte[] ab = new byte[COUNT];
		for (int i = 0; i < COUNT; i++)
			ab[i] = (byte)rand.nextInt();
		Arrays.sort(ab);
		this.secondary("byte", ab);
		
		// Short
		short[] as = new short[COUNT];
		for (int i = 0; i < COUNT; i++)
			as[i] = (short)rand.nextInt();
		Arrays.sort(as);
		this.secondary("short", as);
		
		// Char
		char[] ac = new char[COUNT];
		for (int i = 0; i < COUNT; i++)
			ac[i] = (char)rand.nextInt();
		Arrays.sort(ac);
		this.secondary("char", ac);
		
		// Integer
		int[] ai = new int[COUNT];
		for (int i = 0; i < COUNT; i++)
			ai[i] = rand.nextInt();
		Arrays.sort(ai);
		this.secondary("int", ai);
	}
}

