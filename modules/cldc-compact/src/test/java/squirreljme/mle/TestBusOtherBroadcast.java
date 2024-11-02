// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package squirreljme.mle;

import net.multiphasicapps.tac.TestRunnable;

/**
 * Not Described.
 *
 * @since 2024/10/15
 */
public class TestBusOtherBroadcast
	extends TestRunnable
{
	/**
	 * {@inheritDoc}
	 *
	 * @since 2024/10/15
	 */
	@Override
	public void test()
	{
		this.secondary("key", "value");
	}
}
