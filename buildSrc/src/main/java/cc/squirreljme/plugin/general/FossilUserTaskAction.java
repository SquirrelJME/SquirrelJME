// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.general;

import cc.squirreljme.plugin.util.FossilExe;
import org.gradle.api.Action;
import org.gradle.api.Task;

/**
 * The action for {@link FossilUserTask}.
 *
 * @since 2022/07/10
 */
class FossilUserTaskAction
	implements Action<Task>
{
	/**
	 * {@inheritDoc}
	 *
	 * @since 2022/07/10
	 */
	@Override
	public void execute(Task __task)
	{
		__task.getLogger().lifecycle(
			"Fossil user is: " + FossilExe.instance().currentUser());
	}
}
