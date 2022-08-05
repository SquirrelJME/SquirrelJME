// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.aot.java;

import net.multiphasicapps.tac.TestRunnable;

/**
 * Tests that the class parsing pipe gets initialized and ran.
 *
 * @since 2022/08/04
 */
public class TestClassInitPipe
	extends TestRunnable
{
	/**
	 * {@inheritDoc}
	 * @since 2022/08/04
	 */
	@Override
	public void test()
	{
		this.secondary("key", "value");
	}
}
