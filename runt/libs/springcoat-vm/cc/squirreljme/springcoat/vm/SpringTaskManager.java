// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.springcoat.vm;

/**
 * This class manages tasks within SpringCoat and is used to launch and
 * provide access to those that are running.
 *
 * @since 2018/11/04
 */
public final class SpringTaskManager
{
	/** The manager for suites. */
	protected final SpringSuiteManager suites;
	
	/**
	 * Initializes the task manager.
	 *
	 * @param __sm The suite manager.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/04
	 */
	public SpringTaskManager(SpringSuiteManager __sm)
		throws NullPointerException
	{
		if (__sm == null)
			throw new NullPointerException("NARG");
		
		this.suites = __sm;
	}
	
	/**
	 * Starts the specified task.
	 *
	 * @param __cp The class path to use.
	 * @param __entry The entry point.
	 * @return The ID of the created task.
	 * @throws NullPointerException On null arguments.
	 * @since 2018/11/04
	 */
	public final int startTask(String[] __cp, String __entry)
		throws NullPointerException
	{
		if (__cp == null || __entry == null)
			throw new NullPointerException("NARG");
		
		throw new todo.TODO();
	}
}

