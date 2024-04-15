// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.multivm;

import org.gradle.api.Action;
import org.gradle.api.Task;

/**
 * Action for {@link DefunctTestTask}.
 *
 * @since 2022/07/10
 */
public class DefunctTestTaskAction
	implements Action<Task>
{
	/**
	 * {@inheritDoc}
	 * @since 2022/07/10
	 */
	@Override
	public void execute(Task __task)
	{		
		throw new RuntimeException("The `test` task is defunct, " +
			"the task `testHosted` must be used instead. Failing.");
	}
}
