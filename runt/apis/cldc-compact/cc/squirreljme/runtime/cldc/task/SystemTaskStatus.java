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
@Deprecated
public enum SystemTaskStatus
{
	/** Exit with fatal error. */
	@Deprecated
	EXITED_FATAL,
	
	/** Normal exit. */
	@Deprecated
	EXITED_REGULAR,

	/** Terminated. */
	@Deprecated
	EXITED_TERMINATED,

	/** Running. */
	@Deprecated
	RUNNING,

	/** Failed to start. */
	@Deprecated
	START_FAILED,

	/** Starting. */
	@Deprecated
	STARTING,
	
	/** End. */
	;
}

