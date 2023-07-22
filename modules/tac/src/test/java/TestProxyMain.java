// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

import cc.squirreljme.runtime.cldc.debug.Debugging;
import net.multiphasicapps.tac.TestRunnable;

/**
 * Tests that a proxy main is called when specified for the test which performs
 * different main handling accordingly.
 *
 * @since 2022/09/07
 */
public class TestProxyMain
	extends TestRunnable
{
	/**
	 * {@inheritDoc}
	 * @since 2022/09/07
	 */
	@Override
	public void test()
		throws Throwable
	{
		synchronized (ProxyMain.class)
		{
			this.secondary("called", ProxyMain.called);
		}
	}
}
