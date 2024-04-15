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
 * The task action for {@link FossilExeVersionTask}.
 *
 * @since 2022/07/10
 */
class FossilExeVersionTaskAction
	implements Action<Task>
{
	@Override
	public void execute(Task __task)
	{
		__task.getLogger().lifecycle(
			"Fossil version is: " + FossilExe.instance().version());
	}
}
