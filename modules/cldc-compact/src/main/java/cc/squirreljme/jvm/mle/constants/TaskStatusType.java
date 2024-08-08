// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.jvm.mle.constants;

import cc.squirreljme.runtime.cldc.annotation.SquirrelJMEVendorApi;

/**
 * This represents the status of a task.
 *
 * @since 2020/07/02
 */
@SquirrelJMEVendorApi
public interface TaskStatusType
{
	/** The task has exited. */
	@SquirrelJMEVendorApi
	byte EXITED =
		0;
	
	/** The task is alive. */
	@SquirrelJMEVendorApi
	byte ALIVE =
		1;
	
	/** The number of status types. */
	@SquirrelJMEVendorApi
	byte NUM_TASK_STATUSES =
		2;
}
