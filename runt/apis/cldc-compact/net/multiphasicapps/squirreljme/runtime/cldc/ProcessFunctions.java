// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.runtime.cldc;

/**
 * This contains functions which are used to provide access to the process.
 *
 * @since 2017/11/10
 */
public abstract class ProcessFunctions
{
	/**
	 * This exits the virtual machine using the specifed exit code according
	 * to the specification of {@link Runtime#exit(int)}.
	 *
	 * @param __e The exit code to use.
	 * @since 2016/08/07
	 */
	public abstract void exit(int __e);
	
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
	public final Thread createDaemonThread(Runnable __r, String __n)
		throws NullPointerException
	{
		if (__r == null || __n == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

