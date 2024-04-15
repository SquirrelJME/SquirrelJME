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
import cc.squirreljme.plugin.util.ProjectAndSourceSet;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.tasks.SourceSet;

/**
 * Dependencies for building the ROM, will depend on every library that exists.
 *
 * @since 2020/08/23
 */
public class VMRomDependencies
	implements Callable<Iterable<VMLibraryTask>>
{
	/** The task to execute for. */
	protected final VMRomTask task;
	
	/** The classifier used. */
	protected final SourceTargetClassifier classifier;
	
	/**
	 * Initializes the ROM dependency task.
	 * 
	 * @param __task The task to use.
	 * @param __classifier The classifier used.
	 * @throws NullPointerException on null arguments.
	 * @since 2020/08/23
	 */
	public VMRomDependencies(VMRomTask __task,
		SourceTargetClassifier __classifier)
		throws NullPointerException
	{
		if (__task == null || __classifier == null)
			throw new NullPointerException("NARG");
		
		this.task = __task;
		this.classifier = __classifier;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/08/23
	 */
	@Override
	public Iterable<VMLibraryTask> call()
	{
		return VMRomDependencies.libraries(this.task, this.classifier);
	}
	
	/**
	 * Returns all the libraries that should make up the ROM.
	 * 
	 * @param __task The task to get from.
	 * @param __classifier The classifier used.
	 * @return The libraries that are used as a dependency to build the ROM.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/11/27
	 */
	public static Iterable<VMLibraryTask> libraries(Task __task,
		SourceTargetClassifier __classifier)
		throws NullPointerException
	{
		if (__task == null || __classifier == null)
			throw new NullPointerException("NARG");
		
		// Where all the libraries will go
		Map<ProjectAndSourceSet, VMLibraryTask> result =
			new LinkedHashMap<>();
		
		// This could be recursive
		VMRomDependencies.__libraries(__task, __classifier, result);
		
		return new ArrayList<>(result.values());
	}
	
	/**
	 * Returns all the mapped libraries.
	 * 
	 * @param __task The task to add for.
	 * @param __classifier The classifier used.
	 * @param __result Where all the results are stored.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/08/07
	 */
	private static void __libraries(Task __task,
		SourceTargetClassifier __classifier,
		Map<ProjectAndSourceSet, VMLibraryTask> __result)
		throws NullPointerException
	{
		if (__task == null || __classifier == null || __result == null)
			throw new NullPointerException("NARG");
		
		// If this is a single source set ROM, then the dependencies act a
		// bit differently
		boolean isSingleSourceSet = __classifier.getVmType()
			.isSingleSourceSetRom(__classifier.getBangletVariant());
		
		// Our ROM is only in our source set, so we do not include the
		// libraries which are part of the main or other source set at all
		// The build should go a bit faster for this as it is not waiting
		// for something to happen
		if (!isSingleSourceSet)
		{
			// If we are not on the main source set, we need to include
			// everything the main source set has. For tests for example, we
			// need the main libraries to even test them properly.
			if (!__classifier.isMainSourceSet())
				VMRomDependencies.__libraries(__task,
					__classifier.withSourceSet(SourceSet.MAIN_SOURCE_SET_NAME),
					__result);
			
			// If we are using tests, then we need to include all the test
			// fixtures as well
			if (__classifier.isTestSourceSet())
				VMRomDependencies.__libraries(__task,
					__classifier.withSourceSet(
						VMHelpers.TEST_FIXTURES_SOURCE_SET_NAME), __result);
		}
		
		// Go through all projects and map dependencies
		for (Project project : __task.getProject().getRootProject()
			.getAllprojects())
		{
			// Only consider SquirrelJME projects
			SquirrelJMEPluginConfiguration config =
				SquirrelJMEPluginConfiguration.configurationOrNull(project);
			if (config == null)
				continue;
			
			// Only consider library tasks of this given type
			Task task = project.getTasks().findByName(TaskInitialization
				.task("lib", __classifier));
			if (!(task instanceof VMLibraryTask))
				continue;
			
			// Add the task
			__result.put(new ProjectAndSourceSet(task.getProject(),
				__classifier.getSourceSet()), (VMLibraryTask)task);
		}
	}
}
