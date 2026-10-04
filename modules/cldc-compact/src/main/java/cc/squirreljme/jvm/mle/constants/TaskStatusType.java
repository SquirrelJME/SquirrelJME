// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.constants;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMENativeApi;

/**
 * This represents the status of a task.
 *
 * @since 2020/07/02
 */
@SquirrelJMENativeApi(min = "0.4.0")
public interface TaskStatusType
{
	/** The task has exited. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte EXITED =
		0;
	
	/** The task is alive. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte ALIVE =
		1;
	
	/** The task is alive, but in the background. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte BACKGROUND =
		2;
	
	/** The number of status types. */
	@SquirrelJMENativeApi(min = "0.4.0")
	byte NUM_TASK_STATUSES =
		3;
}
