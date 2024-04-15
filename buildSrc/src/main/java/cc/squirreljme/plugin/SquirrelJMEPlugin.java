// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin;

import cc.squirreljme.plugin.multivm.TaskInitialization;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;

/**
 * Plugin for all SquirrelJME operations that are needed in Gradle in order
 * to provide the correct functionality.
 *
 * @since 2020/02/15
 */
public class SquirrelJMEPlugin
	implements Plugin<Project>
{
	/**
	 * {@inheritDoc}
	 * @since 2020/02/15
	 */
	@Override
	public void apply(Project __project)
	{
		// Add configuration for SquirrelJME
		SquirrelJMEPluginConfiguration config = __project
			.getExtensions().<SquirrelJMEPluginConfiguration>create(
			"squirreljme", SquirrelJMEPluginConfiguration.class,
			this, __project);
		
		// List errors in single project
		Task listErrors = __project.getTasks()
			.create("listErrorIds");
		listErrors.setGroup("squirreljme");
		listErrors.setDescription("Lists all of the source error codes.");
		listErrors.doLast((Task __task) ->
			new ErrorListManager(__project).print(System.out));
		
		// Returns the next available error in single project
		Task nextError = __project.getTasks()
			.create("nextErrorId");
		nextError.setGroup("squirreljme");
		nextError.setDescription("Returns the next free error code.");
		nextError.doLast((Task __task) ->
			System.out.println(new ErrorListManager(__project).next()));
		
		// Initialize Virtual Machine tasks for the project
		TaskInitialization.initialize(__project);
		
		// Late initialization, for stuff such as JavaDoc as it relies on
		// essentially the entire project tree to already be known since it
		// operates solely within the context of SquirrelJME and not another
		// JDK we inherit from...
		__project.afterEvaluate(TaskInitialization::lateInitialize);
	}
}
