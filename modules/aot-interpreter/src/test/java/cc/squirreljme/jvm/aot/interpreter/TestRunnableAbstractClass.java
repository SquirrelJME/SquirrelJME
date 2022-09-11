// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.interpreter;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import net.multiphasicapps.tac.TestRunnable;

/**
 * Tests that an interpreted abstract class works.
 *
 * @since 2022/09/11
 */
public class TestRunnableAbstractClass
	extends TestRunnable
{
	/**
	 * {@inheritDoc}
	 * @since 2022/09/11
	 */
	@Override
	public void test()
		throws Throwable
	{
		throw Debugging.todo();
	}
}
