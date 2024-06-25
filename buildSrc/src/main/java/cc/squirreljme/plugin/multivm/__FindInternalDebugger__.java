// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.multivm;

import java.util.concurrent.Callable;
import org.gradle.api.Project;
import org.gradle.api.Task;

/**
 * Finds the internal debugger.
 *
 * @since 2024/06/24
 */
final class __FindInternalDebugger__
	implements Callable<Task>
{
	/** The project used. */
	protected final Project project;
	
	/**
	 * Finds the internal debugger.
	 *
	 * @param __project The project to base from.
	 * @throws NullPointerException On null arguments.
	 * @since 2024/06/24
	 */
	__FindInternalDebugger__(Project __project)
		throws NullPointerException
	{
		if (__project == null)
			throw new NullPointerException("NARG");
		
		this.project = __project;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2024/06/24
	 */
	@Override
	public Task call()
	{
		return this.project.getRootProject()
			.project(":tools:squirreljme-debugger")
			.getTasks().getByName("shadowJar");
	}
}
