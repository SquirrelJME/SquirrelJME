// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.multivm;

import cc.squirreljme.plugin.SquirrelJMEPluginConfiguration;
import java.util.Collection;
import java.util.LinkedList;
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
	
	/** The source set used. */
	protected final String sourceSet;
	
	/** The virtual machine creating for. */
	protected final VMSpecifier vmType;
	
	/**
	 * Initializes the ROM dependency task.
	 * 
	 * @param __task The task to use.
	 * @param __sourceSet The source set.
	 * @param __vmType The virtual machine type.
	 * @throws NullPointerException on null arguments.
	 * @since 2020/08/23
	 */
	public VMRomDependencies(VMRomTask __task, String __sourceSet,
		VMSpecifier __vmType)
		throws NullPointerException
	{
		if (__task == null || __sourceSet == null || __vmType == null)
			throw new NullPointerException("NARG");
		
		this.task = __task;
		this.sourceSet = __sourceSet;
		this.vmType = __vmType;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/08/23
	 */
	@Override
	public Iterable<VMLibraryTask> call()
	{
		return VMRomDependencies.libraries(this.task,
			this.sourceSet, this.vmType);
	}
	
	/**
	 * Returns all of the libraries that should make up the ROM.
	 * 
	 * @param __task The task to get from.
	 * @param __sourceSet The source set to use.
	 * @param __vmType The VM type running for.
	 * @return The libraries that are used as a dependency to build the ROM.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/11/27
	 */
	public static Iterable<VMLibraryTask> libraries(Task __task,
		String __sourceSet, VMSpecifier __vmType)
		throws NullPointerException
	{
		if (__task == null || __sourceSet == null || __vmType == null)
			throw new NullPointerException("NARG");
		
		// If we are not on the main source set, we need to include everything
		// the main source set has. For tests for example, we need the main
		// libraries to even test them properly.
		Collection<VMLibraryTask> rv = new LinkedList<>();
		if (!SourceSet.MAIN_SOURCE_SET_NAME.equals(__sourceSet))
			for (VMLibraryTask task : VMRomDependencies.libraries(__task,
				SourceSet.MAIN_SOURCE_SET_NAME, __vmType))
				rv.add(task);
		
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
				.task("lib", __sourceSet, __vmType));
			if (!(task instanceof VMLibraryTask))
				continue;
			
			// Add the task
			rv.add((VMLibraryTask)task);
		}
		
		return rv;
	}
}
