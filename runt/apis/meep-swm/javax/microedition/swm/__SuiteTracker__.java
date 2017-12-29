// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.swm;

import net.multiphasicapps.squirreljme.runtime.cldc.SystemCall;

/**
 * This performs the installation and tracks progress.
 *
 * @since 2017/12/28
 */
final class __SuiteTracker__
	extends SuiteManagementTracker
{
	/** The installer for the thread. */
	protected final SuiteInstaller installer;
	
	/** The thread where installation is happening. */
	protected final Thread thread;
	
	/** The suite when it is installed. */
	volatile Suite _suite;
	
	/**
	 * Initializes the tracker.
	 *
	 * @param __i The owning installer.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/12/28
	 */
	__SuiteTracker__(SuiteInstaller __i)
		throws NullPointerException
	{
		if (__i == null)
			throw new NullPointerException("NARG");
		
		this.installer = __i;
		
		// Setup thread which performs the actual installation
		Thread thread = new Thread(new __Runner__(),
			"SquirrelJME-Suite-Installer");
		SystemCall.setDaemonThread(thread);
		this.thread = thread;
		thread.start();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2016/06/24
	 */
	@Override
	public Suite getSuite()
	{
		return this._suite;
	}
	
	/**
	 * This thread performs the installation.
	 *
	 * @since 2017/12/28
	 */
	private final class __Runner__
		implements Runnable
	{
		/**
		 * {@inheritDoc}
		 * @since 2017/12/28
		 */
		@Override
		public void run()
		{
			throw new todo.TODO();
		}
	}
}

