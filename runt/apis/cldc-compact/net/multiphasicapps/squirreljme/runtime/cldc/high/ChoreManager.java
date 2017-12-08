// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.runtime.cldc.high;

/**
 * This is used to manage tasks within the system and is intended to be used
 * by the SWM sub-system.
 *
 * @since 2017/12/07
 */
public abstract class ChoreManager
{
	/** System task flag. */
	public static final int FLAG_SYSTEM =
		0b0000_0000__0000_0100;
	
	/** The mask for priority. */
	public static final int FLAG_PRIORITY_MASK =
		0b0000_0000__0000_0011;
	
	/** Minimum priority. */
	public static final int PRIORITY_MINIMUM =
		0b0000_0000__0000_0000;
	
	/** Normal priority. */
	public static final int PRIORITY_NORMAL =
		0b0000_0000__0000_0001;
	
	/** Maximum priority. */
	public static final int PRIORITY_MAXIMUM =
		0b0000_0000__0000_0010;
	
	/** Undefined priority. */
	public static final int PRIORITY_INVALID =
		0b0000_0000__0000_0011;
	
	/** The mask for status. */
	public static final int FLAG_STATUS_MASK =
		0b0000_0000__0011_1000;
	
	/** Exit with fatal error. */
	public static final int STATUS_EXITED_FATAL =
		0b0000_0000__0000_0000;
	
	/** Normal exit. */
	public static final int STATUS_EXITED_REGULAR =
		0b0000_0000__0000_1000;

	/** Terminated. */
	public static final int STATUS_EXITED_TERMINATED =
		0b0000_0000__0001_0000;

	/** Running. */
	public static final int STATUS_RUNNING =
		0b0000_0000__0001_1000;

	/** Failed to start. */
	public static final int STATUS_START_FAILED =
		0b0000_0000__0010_0000;

	/** Starting. */
	public static final int STATUS_STARTING =
		0b0000_0000__0010_1000;
	
	/**
	 * Returns the flags for the given chore.
	 *
	 * @return A bitfield representing the chore flags, if the chore is not
	 * valid then {@code -1} is returned.
	 * @since 2017/12/07
	 */
	public abstract int flags(int __id);
	
	/**
	 * Returns the approximate number of bytes which are in use by the
	 * process.
	 *
	 * @param __id The process to get the heap size for.
	 * @return The amount of memory being used by the given process.
	 * @since 2017/12/08
	 */
	public abstract long heapUsed(int __id);
	
	/**
	 * Lists the chores which are currently running.
	 *
	 * @param __sys If {@code true} then system chores are included.
	 * @return The identifiers of the chores which are running within
	 * SquirrelJME.
	 * @since 2017/12/07
	 */
	public abstract int[] list(boolean __sys);
}

