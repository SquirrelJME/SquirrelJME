// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.task;

/**
 * This class manages threads within the system.
 *
 * @since 2019/10/13
 */
public final class ThreadManager
{
	/** This is the bootstrap main thread. */
	public final TaskThread BOOT_THREAD =
		new TaskThread(0);
	
	/**
	 * Creates the specified thread owned by the given task.
	 *
	 * @param __tid The owning TID.
	 * @return The thread which was created.
	 * @since 2019/10/13
	 */
	public final TaskThread createThread(int __tid)
	{
		throw new todo.TODO();
	}
}

