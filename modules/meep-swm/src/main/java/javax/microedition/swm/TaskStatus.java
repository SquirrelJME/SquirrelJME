// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package javax.microedition.swm;

import cc.squirreljme.runtime.cldc.annotation.Api;
import cc.squirreljme.runtime.cldc.debug.Debugging;

/**
 * This represents the status of a task.
 *
 * @since 2016/06/24
 */
@Api
public enum TaskStatus
{
	/** Fatally exited. */
	@Api
	EXITED_FATAL,
	
	/** Normal exit. */
	@Api
	EXITED_REGULAR,
	
	/** Terminated. */
	@Api
	EXITED_TERMINATED,
	
	/** Running. */
	@Api
	RUNNING,
	
	/** Failed to start. */
	@Api
	START_FAILED,
	
	/** Starting. */
	@Api
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
		throw Debugging.todo();
		/*switch (__i)
		{
			case 0: return TaskStatus.EXITED_FATAL;
			case 1: return TaskStatus.EXITED_REGULAR;
			case 2: return TaskStatus.EXITED_TERMINATED;
			case 3: return TaskStatus.RUNNING;
			case 4: return TaskStatus.START_FAILED;
			case 5: return TaskStatus.STARTING;
			
			default:
				return TaskStatus.EXITED_FATAL;
		}*/
	}
}

