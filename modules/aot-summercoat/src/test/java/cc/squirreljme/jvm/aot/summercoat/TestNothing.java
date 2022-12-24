// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.aot.summercoat;

import cc.squirreljme.runtime.cldc.debug.Debugging;
import net.multiphasicapps.tac.TestRunnable;

/**
 * Does not do anything, should just run the class in the executed engine
 * and then return.
 *
 * @since 2022/12/24
 */
public class TestNothing
	extends TestRunnable
{
	/**
	 * {@inheritDoc}
	 * @since 2022/12/24
	 */
	@Override
	public void test()
		throws Throwable
	{
	}
}
