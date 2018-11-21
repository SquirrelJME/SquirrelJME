// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package lang;

import net.multiphasicapps.tac.TestRunnable;

/**
 * Tests that the current thread holds the lock.
 *
 * @since 2018/11/21
 */
public class TestHoldLock
	extends TestRunnable
{
	/**
	 * {@inheritDoc}
	 * @since 2018/11/21
	 */
	@Override
	public void test()
	{
		this.secondary("before", Thread.holdsLock(this));
		
		synchronized (this)
		{
			this.secondary("during", Thread.holdsLock(this));
		}
		
		this.secondary("after", Thread.holdsLock(this));
	}
}

