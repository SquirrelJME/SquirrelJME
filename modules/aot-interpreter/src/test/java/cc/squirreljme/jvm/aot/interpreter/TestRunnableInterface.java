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
 * Tests that interpreted classes can be run, such as one that implements
 * {@link Runnable} as an interface.
 *
 * @since 2022/09/11
 */
public class TestRunnableInterface
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
