// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.runtime.cldc.task;

/**
 * This represents the status of a task.
 *
 * @since 2017/12/10
 */
public enum SystemTaskStatus
{
	/** Exit with fatal error. */
	EXITED_FATAL,
	
	/** Normal exit. */
	EXITED_REGULAR,

	/** Terminated. */
	EXITED_TERMINATED,

	/** Running. */
	RUNNING,

	/** Failed to start. */
	START_FAILED,

	/** Starting. */
	STARTING,
	
	/** End. */
	;
}

