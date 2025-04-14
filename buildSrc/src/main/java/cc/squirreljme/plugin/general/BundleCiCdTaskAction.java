// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.general;

import org.gradle.api.Action;
import org.gradle.api.Task;

/**
 * Task action for bundling from CI/CD.
 *
 * @since 2024/03/04
 */
public class BundleCiCdTaskAction
	implements Action<Task>
{
	/**
	 * {@inheritDoc}
	 * @since 2024/03/04
	 */
	@Override
	public void execute(Task __task)
	{
		throw new Error("TODO");
	}
}
