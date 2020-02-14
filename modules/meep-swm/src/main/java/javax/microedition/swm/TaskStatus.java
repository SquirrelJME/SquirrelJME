// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
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
	
	/**
	 * Maps ordinal back into task status.
	 *
	 * @param __i Index to get.
	 * @return The task status.
	 * @since 2018/11/04
	 */
	static final TaskStatus __of(int __i)
	{
		switch (__i)
		{
			case 0: return EXITED_FATAL;
			case 1: return EXITED_REGULAR;
			case 2: return EXITED_TERMINATED;
			case 3: return RUNNING;
			case 4: return START_FAILED;
			case 5: return STARTING;
			
			default:
				return EXITED_FATAL;
		}
	}
}

