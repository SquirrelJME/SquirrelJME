// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.general;

import cc.squirreljme.plugin.multivm.TaskInitialization;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.tasks.TaskContainer;

/**
 * This is a plugin that performs general SquirrelJME tasks and is mostly used
 * for extra utilities that are not needed by the build at all. Examples of
 * this would be managing the blog or otherwise.
 *
 * @since 2020/06/24
 */
public class GeneralSquirrelJMEPlugin
	implements Plugin<Project>
{
	/**
	 * {@inheritDoc}
	 * @since 2020/06/24
	 */
	@Override
	public void apply(Project __project)
	{
		TaskContainer tasks = __project.getTasks();
		
		// Print Fossil executable path
		FossilExeTask exeTask = tasks.create("fossilExe",
			FossilExeTask.class);
		
		// Print Fossil executable path
		tasks.create("fossilVersion",
			FossilExeVersionTask.class, exeTask);
		
		// Print Fossil user
		tasks.create("fossilUser",
			FossilUserTask.class, exeTask);
		
		// Developer note
		tasks.create("developerNote",
			DeveloperNoteTask.class, exeTask);
		
		// Recreate the developer note calendar
		tasks.create("recreateDeveloperNoteCalendar",
			RecreateDeveloperNoteCalendarTask.class, exeTask);
		
		// List error prefixes used by projects
		tasks.create("listErrorPrefixes",
			ListErrorPrefixTask.class);
		
		// Determine the next error prefix that is available
		tasks.create("nextErrorPrefix",
			NextErrorPrefixTask.class);
		
		// Setup ROM tasks, only once
		TaskInitialization.romTasks(__project);
		
		// Initialize the full suite tasks
		TaskInitialization.initializeFullSuiteTask(__project);
		
		// Bundling the build from CI/CD
		tasks.create("bundleCiCd",
			BundleCiCdTask.class);
	}
}
