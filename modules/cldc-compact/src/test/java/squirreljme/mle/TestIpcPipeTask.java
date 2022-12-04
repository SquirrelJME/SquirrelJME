// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package squirreljme.mle;

import net.multiphasicapps.tac.TestRunnable;

/**
 * Tests IPC pipe communication from a sub-task.
 *
 * @since 2022/12/03
 */
public class TestIpcPipeTask
	extends TestRunnable
{
	/**
	 * {@inheritDoc}
	 * @since 2022/12/03
	 */
	@Override
	public void test()
	{
		this.secondary("key", "value");
	}
}
