// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm;

import cc.squirreljme.jvm.task.TaskManager;

/**
 * This class contains all of the globals which are used by the supervisor.
 *
 * @since 2019/10/06
 */
public final class Globals
{
	/** Manager for tasks. */
	private static TaskManager _TASK_MANAGER;
	
	/**
	 * Not used.
	 *
	 * @since 2019/10/06
	 */
	private Globals()
	{
	}
	
	/**
	 * Gets the task manager.
	 *
	 * @return The task manager.
	 * @since 2019/10/06
	 */
	public static final TaskManager getTaskManager()
	{
		TaskManager rv = Globals._TASK_MANAGER;
		if (rv == null)
			Globals._TASK_MANAGER = (rv = new TaskManager());
		return rv;
	}
}

