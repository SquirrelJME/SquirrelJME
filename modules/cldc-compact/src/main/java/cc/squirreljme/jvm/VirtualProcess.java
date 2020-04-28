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
 * This is a virtualized process within the virtual machine which contains
 * the task context information along with hardware threads that a process
 * owns.
 *
 * @since 2020/04/28
 */
public final class VirtualProcess
{
	/** The task ID of this process, this identifies the context. */
	protected final int taskId;
	
	/**
	 * Initializes the virtual process.
	 *
	 * @param __tid The task ID of this process.
	 * @since 2020/04/28
	 */
	private VirtualProcess(int __tid)
	{
		this.taskId = __tid;
	}
}
