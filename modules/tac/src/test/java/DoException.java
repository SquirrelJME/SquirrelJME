// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

import net.multiphasicapps.tac.TestRunnable;

/**
 * Tests a basic exception.
 *
 * @since 2018/10/06
 */
public class DoException
	extends TestRunnable
{
	/**
	 * {@inheritDoc}
	 * @since 2018/10/06
	 */
	@Override
	public void test()
	{
		// Expected to fail
		throw new RuntimeException("Oopsie!");
	}
}

