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

import net.multiphasicapps.tac.TestSupplier;

/**
 * Tests the creation of a new thread.
 *
 * @since 2018/11/20
 */
public class TestThreadNew
	extends TestSupplier<Boolean>
{
	/** Did things? */
	volatile boolean _didstuff;
	
	/**
	 * {@inheritDoc}
	 * @since 2018/11/20
	 */
	@Override
	public Boolean test()
	{
		// Create and start thread to set the boolean flag
		new Thread(new __Task__()).start();
		
		// Run thread and wait, this should result in did being performed
		try
		{
			Thread.sleep(1000);
		}
		catch (InterruptedException e)
		{
		}
		
		// Did stuff
		return this._didstuff;
	}
	
	/**
	 * The task to run.
	 *
	 * @since 2018/11/20
	 */
	final class __Task__
		implements Runnable
	{
		/**
		 * {@inheritDoc}
		 * @since 2018/11/20
		 */
		@Override
		public void run()
		{
			// Just set as true!
			TestThreadNew.this._didstuff = true;
		}
	}
}

