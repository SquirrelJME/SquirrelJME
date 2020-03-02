// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.tasks;

import cc.squirreljme.plugin.SquirrelJMEPluginConfiguration;
import cc.squirreljme.plugin.swm.JavaMEMidlet;
import cc.squirreljme.plugin.swm.JavaMEMidletType;
import groovy.lang.Closure;
import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Objects;
import java.util.concurrent.Callable;
import javax.inject.Inject;
import org.gradle.api.Action;
import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.Dependency;
import org.gradle.api.artifacts.ProjectDependency;
import org.gradle.jvm.tasks.Jar;
import org.gradle.process.JavaExecSpec;

/**
 * Launches the program but runs it in the Virtual Machine.
 *
 * @since 2020/02/29
 */
public class RunEmulatedTask
	extends DefaultTask
{
	/** Main configurations. */
	private static final String[] _MAIN_CONFIGS =
		new String[]{"api", "implementation"};
	
	/** Test configurations. */
	private static final String[] _TEST_CONFIGS =
		new String[]{"testApi", "testImplementation"};
	
	/** The emulator to use. */
	protected final String emulator;
	
	/** Is this for a test? */
	protected final boolean isTest;
	
	/**
	 * Initializes the task.
	 *
	 * @param __jar The JAR task.
	 * @param __emulator The emulator to use.
	 * @param __test Is this for tests?
	 * @since 2020/02/29
	 */
	@Inject
	public RunEmulatedTask(Jar __jar, String __emulator, boolean __test)
	{
		if (__test)
			throw new IllegalArgumentException("Implement test launching!");
		
		this.emulator = __emulator;
		this.isTest = __test;
		
		// Set details of this task
		this.setGroup("squirreljme");
		this.setDescription((__test ? "Tests the program with " +
			__emulator + "." : "Launches the program with the " +
			__emulator + " virtual machine."));
		
		// The output of this task is the profiling snapshot
		this.getOutputs().files(
			this.getProject().provider(this::__profilerSnapshotPath));
		
		// This only runs if this is an application
		this.onlyIf(this::__onlyIf);
		
		// Run the task accordingly
		this.doLast(new RunEmulatedTask.__ActionTask__());
		
		// This needs the JAR task and the emulation task
		this.dependsOn(__jar,
			(Callable<Object>)this::__findEmulatorJarTask,
			(Callable<Object>)this::__findEmulatorBaseJarTask);
	}
	
	/**
	 * Only runs if these conditions are met.
	 *
	 * @param __task Checks this task.
	 * @return If this can run or not.
	 * @since 2020/02/29
	 */
	private boolean __onlyIf(Task __task)
	{
		// Only allow if an application!
		return SquirrelJMEPluginConfiguration.configuration(this.getProject())
			.swmType == JavaMEMidletType.APPLICATION;
	}
	
	/**
	 * Finds the emulator JAR task.
	 *
	 * @return The emulator JAR task.
	 * @since 2020/02/29
	 */
	private Object __findEmulatorBaseJarTask()
	{
		return Objects.requireNonNull(this.getProject().getRootProject().
			findProject(":emulators:emulator-base"),
			"No emulator base?").getTasks().getByName("jar");
	}
	
	/**
	 * Locates the emulator package to base on.
	 *
	 * @return The emulator base.
	 * @since 2020/02/29
	 */
	private Object __findEmulatorJarTask()
	{
		return Objects.requireNonNull(this.getProject().getRootProject().
			findProject(":emulators:" + this.emulator + "-vm"),
			"No emulator?").getTasks().getByName("jar");
	}
	
	/**
	 * Returns the snapshot path for the profiler.
	 *
	 * @return The profiler snapshot path.
	 * @since 2020/02/29
	 */
	File __profilerSnapshotPath()
	{
		return this.getProject().getBuildDir().toPath()
			.resolve(this.emulator + ".nps").toFile();
	}
	
	/**
	 * Returns the run class path.
	 *
	 * @return The run class path.
	 * @since 2020/02/29
	 */
	Iterable<Path> __runClassPath()
	{
		Collection<Path> result = new LinkedHashSet<>();
		
		this.__recursiveDependencies(result, this.getProject());
		
		return result;
	}
	
	/**
	 * Returns the run class path as a string.
	 *
	 * @return The run class path as a string.
	 * @since 2020/02/29
	 */
	String __runClassPathAsString()
	{
		StringBuilder sb = new StringBuilder();
		
		for (Path path : this.__runClassPath())
		{
			if (sb.length() > 0)
				sb.append(File.pathSeparatorChar);
			sb.append(path);
		}
		
		return sb.toString();
	}
	
	/**
	 * Recursively scans and obtains dependencies.
	 *
	 * @param __out The output collection.
	 * @param __at The current project currently at.
	 * @since 2020/02/29
	 */
	final void __recursiveDependencies(Collection<Path> __out, Project __at)
	{
		// If this is not a SquirrelJME project, ignore
		SquirrelJMEPluginConfiguration config = __at.getExtensions()
			.<SquirrelJMEPluginConfiguration>findByType(
				SquirrelJMEPluginConfiguration.class);
		if (config == null)
			return;
		
		// Look in these configurations for dependencies, done first so what
		// we depend on has higher priority first
		for (String configurationName : (this.isTest ?
				RunEmulatedTask._TEST_CONFIGS : RunEmulatedTask._MAIN_CONFIGS))
		{
			// The configuration might not even exist
			Configuration buildConfig = __at.getConfigurations()
				.findByName(configurationName);
			if (buildConfig == null)
				continue;
			
			// Recursively scan dependencies
			for (Dependency dependency : buildConfig.getDependencies())
			{
				// Only consider modules
				if (!(dependency instanceof ProjectDependency))
					continue;
				
				// Recursive search these dependencies
				this.__recursiveDependencies(__out,
					((ProjectDependency)dependency).getDependencyProject());
			}
		}
		
		// Process the output files for the JAR of this project, use the
		// absolute path since there might be hard to find libraries that are
		// relative
		for (File file : ((Jar)__at.getTasks().getByName("jar"))
			.getOutputs().getFiles())
			__out.add(file.toPath().toAbsolutePath());
		
	}
	
	/**
	 * Used to run the emulator.
	 *
	 * @since 2020/02/29
	 */
	private class __ActionTask__
		implements Action<Task>
	{
		/**
		 * {@inheritDoc}
		 * @since 2020/02/29
		 */
		@Override
		public void execute(Task __task)
		{
			// Get current and emulator projects
			Project project = RunEmulatedTask.this.getProject();
			Project emuBase = project.getRootProject().
				project(":emulators:emulator-base");
			Project emuCore = project.getRootProject().
				project(":emulators:" + RunEmulatedTask.this.emulator + "-vm");
			
			// Need this to get the program details
			SquirrelJMEPluginConfiguration config =
				SquirrelJMEPluginConfiguration.configuration(project);
			
			// Execute program
			project.javaexec((JavaExecSpec __spec) ->
				{
					// Are we launching a MIDlet?
					JavaMEMidlet midlet = null;
					if (!config.midlets.isEmpty())
						midlet = config.midlets.get(0);
					
					// Build arguments to the VM
					Collection<String> args = new LinkedList<>();
					
					// Add emulator
					args.add("-Xemulator:" + RunEmulatedTask.this.emulator);
					
					// Add snapshot path
					args.add("-Xsnapshot:" + RunEmulatedTask.this.
						__profilerSnapshotPath().toPath().toString());
					
					// Determine program classpath
					args.add("-classpath");
					args.add(RunEmulatedTask.this.__runClassPathAsString());
					
					// Add main class to launch
					args.add((midlet != null ?
						"javax.microedition.midlet.__MainHandler__" :
						Objects.requireNonNull(config.mainClass,
						"No main class in project.")));
					
					// We need to tell the MIDlet launcher what our main entry
					// point is going to be
					if (midlet != null)
						args.add(midlet.mainClass);
					
					// Use the given arguments
					__spec.args(args);
					
					// We only need the classpath of the emulator because this
					// runs on it
					__spec.classpath(
						((Jar)emuBase.getTasks().getByName("jar"))
							.getOutputs().getFiles(),
						((Jar)emuCore.getTasks().getByName("jar"))
							.getOutputs().getFiles(),
						RunNativeTask.__projectClassPath(emuBase),
						RunNativeTask.__projectClassPath(emuCore));
					
					// Main entry point for the emulator
					__spec.setMain("cc.squirreljme.emulator.vm.VMFactory");
				});
		}
	}
}
