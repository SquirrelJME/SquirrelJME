// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) 2013-2016 Steven Gawroriski <steven@multiphasicapps.net>
//     Copyright (C) 2013-2016 Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU Affero General Public License v3+, or later.
// For more information see license.mkd.
// ---------------------------------------------------------------------------

package javax.microedition.swm;

/**
 * This represents the status of a task.
 *
 * @since 2016/06/24
 */
public enum TaskStatus
{
	/** Fatally exited. */
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

