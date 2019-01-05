// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.vm.summercoat;

/**
 * This represents the current state of a task.
 *
 * @since 2019/01/05
 */
public enum TaskState
{
	/** Task is starting. */
	STARTING,
	
	/** Task is running. */
	RUNNING,
	
	/** Task has exited. */
	EXITED,
	
	/** End. */
	;
	
	/**
	 * Is this state considered exited?
	 *
	 * @return If the state is considered to be exited.
	 * @since 2019/01/05
	 */
	public final boolean isExited()
	{
		return this == EXITED;
	}
}

