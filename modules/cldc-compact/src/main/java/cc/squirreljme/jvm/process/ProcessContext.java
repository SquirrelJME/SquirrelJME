// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.process;

/**
 * This contains the context for a process along with all of its state.
 *
 * @since 2020/04/18
 */
public class ProcessContext
{
	/** The task ID for this process. */
	protected final int taskId;
	
	/**
	 * Initializes the process context.
	 *
	 * @param __taskId The Task ID of the process.
	 * @throws IllegalArgumentException If the task ID is that of the
	 * supervisor process.
	 * @since 2020/04/18
	 */
	public ProcessContext(int __taskId)
		throws IllegalArgumentException
	{
		// {@squirreljme.error ZZ45 Cannot create a process with the supervisor
		// task Id.}
		if (__taskId == 0)
			throw new IllegalArgumentException("ZZ45");
		
		this.taskId = __taskId;
	}
}
