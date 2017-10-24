// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.unsafe;

/**
 * Internal system process handling.
 *
 * @since 2017/08/11
 */
final class __Ext_systemprocess__
{
	/**
	 * Not used.
	 *
	 * @since 2017/08/11
	 */
	private __Ext_systemprocess__()
	{
	}
	
	/**
	 * This returns the number of CPU threads which are available for usage.
	 *
	 * @return The total number of available CPU threads.
	 * @since 2017/08/29
	 */
	static int cpuThreads()
	{
		return Runtime.getRuntime().availableProcessors();
	}
	
	/**
	 * Creates a new thread, but one which is considered a daemon thread which
	 * is automatically killed when exit is called. This is required because
	 * CLDC has no concept of daemon threads.
	 *
	 * @param __r The method to call when the thread runs.
	 * @param __n The name of the thread.
	 * @return The newly created daemon thread.
	 * @throws NullPointerException On null arguments.
	 * @since 2017/10/24
	 */
	static Thread createDaemonThread(Runnable __r, String __n)
		throws NullPointerException
	{
		Thread rv = new Thread(__r, __n);
		rv.setDaemon(true);
		return rv;
	}
	
	/**
	 * The build system always has the most permission to do anything.
	 *
	 * @return Always {@code true}.
	 * @since 2017/08/11
	 */
	static boolean isLauncher()
	{
		return true;
	}
}

