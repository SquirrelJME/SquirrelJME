// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.multivm;

import cc.squirreljme.plugin.SquirrelJMEPluginConfiguration;
import cc.squirreljme.plugin.multivm.ident.SourceTargetClassifier;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.concurrent.Callable;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.tasks.SourceSet;

/**
 * Dependencies used for the full SquirrelJME suite.
 *
 * @since 2020/10/17
 */
public class VMFullSuiteDepends
	implements Callable<Iterable<Task>>
{
	/** The task to execute for. */
	protected final Task task;
	
	/** The classifier target used. */
	protected final SourceTargetClassifier classifier;
	
	/**
	 * Initializes the dependency grabber.
	 * 
	 * @param __task The task to run off.
	 * @param __classifier The classifier used.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/10/17
	 */
	public VMFullSuiteDepends(Task __task, SourceTargetClassifier __classifier)
		throws NullPointerException
	{
		if (__task == null || __classifier == null)
			throw new NullPointerException("NARG");
		
		this.task = __task;
		this.classifier = __classifier;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/10/17
	 */
	@Override
	public Iterable<Task> call()
	{
		Project root = this.task.getProject().getRootProject();
		Collection<Task> tasks = new LinkedHashSet<>();
		
		// We need the emulator to be built and working before we can actually
		// run our full suite accordingly
		for (String emulatorProject : this.classifier.getVmType()
			.emulatorProjects(this.classifier.getBangletVariant()))
		{
			Task emulJar = root.project(emulatorProject).getTasks()
				.findByName("jar");
			if (emulJar != null)
				tasks.add(emulJar);
		}
		
		// Which source sets should be used
		List<String> sourceSets;
		if (!this.classifier.isMainSourceSet())
			sourceSets = Arrays.asList(SourceSet.MAIN_SOURCE_SET_NAME,
				this.classifier.getSourceSet());
		else
			sourceSets = Collections.singletonList(
				this.classifier.getSourceSet());
		
		// Go through every single project, and try to use it as a dependency
		for (Project project : root.getAllprojects())
		{
			// Ignore non-SquirrelJME projects
			SquirrelJMEPluginConfiguration config =
				SquirrelJMEPluginConfiguration.configurationOrNull(project);
			if (config == null)
				continue;
			
			// Include whatever libraries are needed for the source set
			for (String sourceSet : sourceSets)
			{
				// Find the associated library task
				Task libTask = project.getTasks().findByName(TaskInitialization
					.task("lib",
						this.classifier.withSourceSet(sourceSet)));
				if (libTask == null)
					continue;
				
				// Use all of their dependencies, if not yet added
				for (VMLibraryTask subDep : new VMRunDependencies(
					libTask.getProject(),
						this.classifier.withSourceSet(sourceSet))
					.call())
					tasks.add(subDep);
			}
		}
		
		return Collections.unmodifiableCollection(tasks);
	}
}
