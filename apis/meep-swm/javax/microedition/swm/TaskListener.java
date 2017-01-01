// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.swm;

/**
 * This is called when a task has changed status.
 *
 * @since 2016/06/24
 */
public interface TaskListener
{
	/**
	 * This is called when a task has changed status.
	 *
	 * @param __t The task which has had its status changed.
	 * @param __status The new status of the task.
	 * @since 2016/06/24
	 */
	public abstract void notifyStatusUpdate(Task __t, TaskStatus __status);
}

