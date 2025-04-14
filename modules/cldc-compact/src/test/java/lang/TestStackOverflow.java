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
import net.multiphasicapps.tac.UntestableException;

/**
 * Tests that the stack overflows, eventually...
 *
 * @since 2023/07/09
 */
public class TestStackOverflow
	extends TestRunnable
{
	/** When to just give up testing this situation. */
	private static final int _RECURSION_LIMIT =
		128;
	
	/**
	 * {@inheritDoc}
	 * @since 2023/07/09
	 */
	@Override
	public void test()
	{
		TestStackOverflow.__overflow(0);
	}
	
	/**
	 * Calls itself to overflow.
	 * 
	 * @param __at The index of the call.
	 * @since 2023/07/09
	 */
	@SuppressWarnings("InfiniteRecursion")
	public static void __overflow(int __at)
	{
		if (__at >= TestStackOverflow._RECURSION_LIMIT)
			throw new UntestableException();
		
		TestStackOverflow.__overflow(__at + 1);
	}
}
