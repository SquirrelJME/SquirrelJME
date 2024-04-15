// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package lang;

import net.multiphasicapps.tac.TestRunnable;

/**
 * Tests {@link StringBuilder#delete(int, int)}.
 *
 * @since 2022/06/29
 */
public class TestStringBuilderDelete
	extends TestRunnable
{
	/** The test string. */
	private static final String _TEST_STRING =
		"Squirrels are so cute and adorable!";
	
	/**
	 * {@inheritDoc}
	 * @since 2022/06/29
	 */
	@Override
	public void test()
	{
		int strLen = TestStringBuilderDelete._TEST_STRING.length();
		
		for (boolean overFill : new boolean[]{false, true})
		{
			// Nothing
			this.__check(overFill, 0, 0);
			
			// Everything to length
			this.__check(overFill, 0, strLen);
			
			// Everything and more
			this.__check(overFill, 0, Integer.MAX_VALUE);
			
			// Off the end shifted a bit
			this.__check(overFill, 5, strLen);
			
			// And way off the end
			this.__check(overFill, 5, Integer.MAX_VALUE);
			
			// In the middle
			this.__check(overFill, 5, 10);
			
			// Reverse order
			this.__check(overFill, 10, 5);
			
			// Negative
			this.__check(overFill, -1, strLen);
			
			// Another negative
			this.__check(overFill, 0, -5);
			
			// Both negative
			this.__check(overFill, -10, -5);
			
			// Same as above but swapped negatives
			this.__check(overFill, -5, -10);
			
			// At the very end
			this.__check(overFill, strLen, strLen);
			
			// At very end with an extra
			this.__check(overFill, strLen, strLen + 1);
			
			// Past the end
			this.__check(overFill, strLen + 1, strLen + 1);
			
			// Past the end but with extra
			this.__check(overFill, strLen + 1, strLen + 2);
		}
	}
	
	/**
	 * Checks the given string and deletion sequence.
	 *
	 * @param __overFill Over fill the buffer with capacity?
	 * @param __from The from index.
	 * @param __to The to index.
	 * @since 2022/06/29
	 */
	private void __check(boolean __overFill, int __from, int __to)
	{
		String key = String.format("str_%d_%d%s", __from, __to,
			(__overFill ? "_o" : ""));
		
		try
		{
			// Are we overfilling with capacity?
			StringBuilder sb;
			if (__overFill)
			{
				sb = new StringBuilder(128);
				sb.append(TestStringBuilderDelete._TEST_STRING);
			}
			else
				sb = new StringBuilder(TestStringBuilderDelete._TEST_STRING);
			
			// Perform the deletion
			this.secondary(key, sb.delete(__from, __to).toString());
		}
		catch (IndexOutOfBoundsException e)
		{
			this.secondary(key, e);
		}
	}
}
