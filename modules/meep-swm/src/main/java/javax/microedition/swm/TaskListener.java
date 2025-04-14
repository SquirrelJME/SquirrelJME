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

/**
 * This is called when a task has changed status.
 *
 * @since 2016/06/24
 */
@Api
public interface TaskListener
{
	/**
	 * This is called when a task has changed status.
	 *
	 * @param __t The task which has had its status changed.
	 * @param __status The new status of the task.
	 * @since 2016/06/24
	 */
	@Api
	void notifyStatusUpdate(Task __t, TaskStatus __status);
}

