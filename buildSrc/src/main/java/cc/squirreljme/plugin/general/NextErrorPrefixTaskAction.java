// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.general;

import cc.squirreljme.plugin.ErrorCodeManager;
import org.gradle.api.Action;
import org.gradle.api.Task;

/**
 * Action for getting the next error prefix.
 *
 * @since 2020/08/22
 */
public class NextErrorPrefixTaskAction
	implements Action<Task>
{
	/**
	 * {@inheritDoc}
	 * @since 2020/08/22
	 */
	@Override
	public void execute(Task __task)
	{
		System.out.println(new ErrorCodeManager(
			__task.getProject().getRootProject()).next());
	}
}
