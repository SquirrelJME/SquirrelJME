// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package net.multiphasicapps.squirreljme.runtime.cldc;

/**
 * This represents the status of a task.
 *
 * @since 2017/12/10
 */
public interface SystemTaskStatus
{
	/** Exit with fatal error. */
	public static final int EXITED_FATAL =
		1;
	
	/** Normal exit. */
	public static final int EXITED_REGULAR =
		2;

	/** Terminated. */
	public static final int EXITED_TERMINATED =
		3;

	/** Running. */
	public static final int RUNNING =
		4;

	/** Failed to start. */
	public static final int START_FAILED =
		5;

	/** Starting. */
	public static final int STARTING =
		6;
}

