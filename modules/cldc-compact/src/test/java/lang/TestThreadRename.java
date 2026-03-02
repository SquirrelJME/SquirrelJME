// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package lang;

import cc.squirreljme.jvm.mle.ThreadShelf;
import cc.squirreljme.runtime.cldc.debug.Debugging;
import net.multiphasicapps.tac.TestRunnable;

/**
 * Tests renaming a thread.
 *
 * @since 2026/02/22
 */
public class TestThreadRename
	extends TestRunnable
{
	/**
	 * {@inheritDoc}
	 *
	 * @since 2026/02/22
	 */
	@Override
	public void test()
	{
		// Setup thread
		Thread thread = new Thread(new __Nothing__(), "old");
		ThreadShelf.javaThreadSetDaemon(thread);
		thread.start();
		
		// Short delay to let the thread run
		try
		{
			Thread.sleep(3000);
		}
		catch (InterruptedException ignored)
		{
		}
		
		// Get the old name
		this.secondary("old", thread.getName());
		
		// Set new name
		thread.setName("new");
		
		// Get the new name
		this.secondary("new", thread.getName());
	}
	
	/**
	 * Does nothing.
	 *
	 * @since 2026/02/22
	 */
	static class __Nothing__
		implements Runnable
	{
		/**
		 * {@inheritDoc}
		 * @since 2026/02/22
		 */
		@Override
		public void run()
		{
			for (int i = 0; i < 30; i++)
				try
				{
					Thread.sleep(10_000);
				}
				catch(InterruptedException ignored)
				{
					break;
				}
		}
	}
}
