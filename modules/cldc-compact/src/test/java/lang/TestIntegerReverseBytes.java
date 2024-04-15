// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package lang;

import net.multiphasicapps.tac.TestRunnable;

/**
 * Tests {@link Integer#reverseBytes(int)}).
 *
 * @since 2021/02/03
 */
public class TestIntegerReverseBytes
	extends TestRunnable
{
	/**
	 * {@inheritDoc}
	 * @since 2021/02/03
	 */
	@Override
	public void test()
		throws Throwable
	{
		this.secondary("first", Integer.reverseBytes(0xAABBCCDD));
		this.secondary("undo", Integer.reverseBytes(
			Integer.reverseBytes(0xAABBCCDD)));
		
		this.secondary("eeeeffff", Integer.reverseBytes(0xEEEEFFFF));
	}
}
