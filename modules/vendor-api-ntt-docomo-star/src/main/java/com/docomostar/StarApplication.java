// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package com.docomostar;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.midlet.CleanupHandler;

@Api
public abstract class StarApplication
{
	/** Application was launched from the launcher. */
	@Api
	public static final int LAUNCHED_FROM_LAUNCHER =
		8;
	
	/** Exception that was thrown on termination. */
	volatile Throwable _terminateException;
	
	/**
	 * Indicates that the application has started.
	 * 
	 * @param __launchType The type of application launch this was, will
	 * have the prefix of {@code LAUNCHED_}.
	 * @since 2022/02/28
	 */
	@Api
	public abstract void started(int __launchType);
	
	/**
	 * Terminates the application.
	 * 
	 * @since 2022/02/28
	 */
	@Api
	public final void terminate()
	{
		// Run all cleanup handlers
		CleanupHandler.runAll();
		
		// We need to exit the VM ourselves here
		System.exit((this._terminateException != null ? 1 : 0));
	}
}
