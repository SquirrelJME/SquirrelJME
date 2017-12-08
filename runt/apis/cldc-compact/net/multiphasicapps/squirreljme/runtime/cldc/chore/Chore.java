// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.runtime.cldc.chore;

/**
 * This class represents a chore which is used to identify and store
 * information about tasks running on the system.
 *
 * @since 2017/12/08
 */
public abstract class Chore
{
	/** Exit with fatal error. */
	public static final int STATUS_EXITED_FATAL =
		0;
	
	/** Normal exit. */
	public static final int STATUS_EXITED_REGULAR =
		1;

	/** Terminated. */
	public static final int STATUS_EXITED_TERMINATED =
		2;

	/** Running. */
	public static final int STATUS_RUNNING =
		3;

	/** Failed to start. */
	public static final int STATUS_START_FAILED =
		4;

	/** Starting. */
	public static final int STATUS_STARTING =
		5;
	
	/**
	 * Returns the group which this chore is within.
	 *
	 * @return The current chore group.
	 * @since 2017/12/08
	 */
	public abstract ChoreGroup group();
	
	/**
	 * Returns the amount of used memory.
	 *
	 * @return The amount of memory used.
	 * @since 2017/12/08
	 */
	public abstract long memoryUsed();
	
	/**
	 * Returns the priority of the chore.
	 *
	 * @return The chore priority, the lower the value the higher the
	 * priority.
	 * @since 2017/12/08
	 */
	public abstract int priority();
	
	/**
	 * Returns the status of the task.
	 *
	 * @return The task status.
	 * @since 2017/12/08
	 */
	public abstract int status();
	
	/**
	 * Returns {@code true} if this is a system chore.
	 *
	 * @return {@code true} if this is a system chore.
	 * @since 2017/12/08
	 */
	public final boolean isSystem()
	{
		return this.group().isSystem();
	}
}

