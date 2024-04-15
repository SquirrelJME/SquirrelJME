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
 * Tests that private calls can be done.
 *
 * @since 2018/10/10
 */
public class TestInvokePrivate
	extends TestRunnable
{
	/**
	 * Private call.
	 *
	 * @since 2018/10/10
	 */
	private void doPrivate()
	{
		this.secondary("private", true);
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2018/10/10
	 */
	@Override
	public void test()
	{
		this.doPrivate();
	}
}

