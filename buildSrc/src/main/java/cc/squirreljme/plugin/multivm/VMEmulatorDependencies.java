// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.multivm;

import cc.squirreljme.plugin.multivm.ident.SourceTargetClassifier;
import cc.squirreljme.plugin.multivm.ident.TargetClassifier;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.TaskContainer;

/**
 * This class is used to provide the dependency lookup for the emulator
 * backend, this is so the emulator is built.
 *
 * @since 2020/08/16
 */
public final class VMEmulatorDependencies
	implements Callable<Iterable<Task>>
{
	/** The task referencing this. */
	protected final VMBaseTask task;
	
	/** The target classifier used. */
	protected final TargetClassifier targetClassifier;
	
	/**
	 * Initializes the dependencies.
	 * 
	 * @param __task The task to reference.
	 * @param __classifier The classifier used.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/08/16
	 */
	public VMEmulatorDependencies(VMBaseTask __task,
		TargetClassifier __classifier)
		throws NullPointerException
	{
		if (__task == null || __classifier == null)
			throw new NullPointerException("NARG");
		
		this.task = __task;
		this.targetClassifier = __classifier;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/08/16
	 * @return
	 */
	@Override
	public Iterable<Task> call()
	{
		Project root = this.task.getProject().getRootProject();
		
		// Need tasks for all the source emulators
		Set<Task> rv = new LinkedHashSet<>();
		for (String emulatorProject : this.targetClassifier.getVmType()
			.emulatorProjects(this.targetClassifier.getBangletVariant()))
		{
			Project emuProject = root.project(emulatorProject);
			TaskContainer emuTasks = emuProject.getTasks();
			TaskContainer emuBase =
				root.project(":emulators:emulator-base").getTasks();
			
			// Build projects that are needed to run the emulator
			for (ProjectAndTaskName task : VMHelpers.runClassTasks(emuProject,
				new SourceTargetClassifier(SourceSet.MAIN_SOURCE_SET_NAME,
				this.targetClassifier)))
			{
				Project taskProject = root.project(task.project);
				
				// We need to depend on the classes and JAR for the emulator
				// projects
				rv.add(taskProject.getTasks().getByName("classes"));
				rv.add(taskProject.getTasks().getByName("jar"));
			}
			
			// Add base emulator projects and such, so that they are forced
			rv.add(emuTasks.getByName("jar"));
			rv.add(emuTasks.getByName("assemble"));
			rv.add(emuBase.getByName("jar"));
			rv.add(emuBase.getByName("assemble"));
			if (null != emuBase.findByName("assembleDebug"))
				rv.add(emuBase.getByName("assembleDebug"));
			if (null != emuBase.findByName("assembleRelease"))
				rv.add(emuBase.getByName("assembleRelease"));
		}
		
		return new ArrayList<>(rv);
	}
}
