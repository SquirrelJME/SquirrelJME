// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm;

/**
 * This is the manager for the hardware threads which exist within the VM.
 *
 * @since 2020/04/28
 */
public final class HardwareThread
{
	/** The unique identifier for this thread, this is a unique value. */
	protected final int threadId;
	
	/** The task ID this thread falls under. */
	protected final int taskId;
	
	/**
	 * Initializes the hardware thread information.
	 *
	 * @param __thread The thread ID.
	 * @param __task The task this hardware thread is associated with.
	 * @since 2020/04/28
	 */
	HardwareThread(int __thread, int __task)
	{
		this.threadId = __thread;
		this.taskId = __task;
	}
}
