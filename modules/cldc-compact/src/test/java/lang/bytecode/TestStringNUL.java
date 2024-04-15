// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package lang.bytecode;

import net.multiphasicapps.tac.TestRunnable;

/**
 * Tests that NULs in {@link String} properly appear.
 *
 * @since 2023/07/27
 */
public class TestStringNUL
	extends TestRunnable
{
	/** The set of strings to test. */
	private static final String[] _STRINGS =
		new String[]{"\0", "\0\0", "squirrels\0are\0cute", "hello\0"};
	
	/**
	 * {@inheritDoc}
	 * @since 2023/07/27
	 */
	@Override
	public void test()
	{
		for (int i = 0, n = TestStringNUL._STRINGS.length; i < n; i++)
		{
			String str = TestStringNUL._STRINGS[i];
			
			this.secondary("length" + i, str.length());
			this.secondary("first" + i, str.indexOf(0));
			this.secondary("last" + i, str.lastIndexOf(0));
		}
	}
}
