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
	 * Returns the group which this chore is within.
	 *
	 * @return The current chore group.
	 * @since 2017/12/08
	 */
	public abstract ChoreGroup group();
}

