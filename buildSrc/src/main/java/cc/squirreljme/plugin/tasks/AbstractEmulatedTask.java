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
import cc.squirreljme.plugin.tasks.test.EmulatedTestUtilities;
import java.io.File;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.Callable;
import org.gradle.api.Action;
import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.UnknownTaskException;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.Dependency;
import org.gradle.api.artifacts.ProjectDependency;
import org.gradle.jvm.tasks.Jar;
import org.gradle.process.JavaExecSpec;

/**
 * This is the base task for anything which is emulated.
 *
 * @since 2020/03/06
 */
public abstract class AbstractEmulatedTask
	extends DefaultTask
{
	/** The emulator to use. */
	protected final String emulator;
	
	/** Is this a SummerCoat task? */
	protected final boolean isSummerCoat;
	
	/** Configurations to use. */
	@Deprecated
	private final String[] _configs;
	
	/**
	 * Initializes the base task.
	 *
	 * @param __jar The jar task (the JAR to run).
	 * @param __emulator The emulator to use.
	 * @param __cfgs Configuration items.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/03/06
	 */
	public AbstractEmulatedTask(Task __jar, String __emulator,
		String... __cfgs)
		throws NullPointerException
	{
		if (__jar == null || __emulator == null)
			throw new NullPointerException("No JAR or emulator specified.");
		
		// Only accept these two kinds
		if (!(__jar instanceof Jar) && !(__jar instanceof SummerCoatRomTask))
			throw new IllegalArgumentException("Incompatible dependency: " +
				__jar.getClass());
		
		this.emulator = __emulator;
		this.isSummerCoat = (__jar instanceof SummerCoatRomTask);
		this._configs = (__cfgs == null ? new String[0] : __cfgs.clone());
		
		// Describe this task
		this.setGroup("squirreljme");
		
		// The output of this task is the profiling snapshot
		this.getOutputs().files(
			this.getProject().provider(this::__profilerSnapshotPath));
		this.getOutputs().upToDateWhen(__task -> false);
		
		// This only runs if this is an application
		this.onlyIf(this::__onlyIf);
		
		// Run the task accordingly
		this.doLast(new AbstractEmulatedTask.__ActionTask__());
		
		// This needs the JAR task and the emulation task
		this.dependsOn(__jar,
			(Callable<Object>)this::__findEmulatorJarTask,
			(Callable<Object>)this::__findEmulatorBaseJarTask);
	}
	
	/**
	 * Returns the main class to execute.
	 *
	 * @param __cfg The configuration.
	 * @param __midlet The MIDlet to be ran.
	 * @return The main class.
	 * @since 2020/03/06
	 */
	protected abstract String mainClass(SquirrelJMEPluginConfiguration __cfg,
		JavaMEMidlet __midlet);
	
	/**
	 * Do we use the MIDlet?
	 *
	 * @param __midlet The midlet to use.
	 * @return If we use this.
	 * @since 2020/03/06
	 */
	protected abstract boolean useMidlet(JavaMEMidlet __midlet);
	
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
	 * Recursively scans and obtains dependencies.
	 *
	 * @param __out The output collection.
	 * @param __at The current project currently at.
	 * @since 2020/02/29
	 */
	@Deprecated
	private void __recursiveDependencies(Collection<Path> __out, Project __at)
	{
		this.__recursiveDependencies(__out, __at, new HashSet<>());
	}
	
	/**
	 * Recursively scans and obtains dependencies.
	 *
	 * @param __out The output collection.
	 * @param __at The current project currently at.
	 * @param __eval Projects which have been evaluated.
	 * @since 2020/02/29
	 */
	@Deprecated
	private void __recursiveDependencies(Collection<Path> __out, Project __at,
		Set<Project> __eval)
	{
		// If this is not a SquirrelJME project, ignore
		SquirrelJMEPluginConfiguration config = __at.getExtensions()
			.<SquirrelJMEPluginConfiguration>findByType(
				SquirrelJMEPluginConfiguration.class);
		if (config == null)
			return;
		
		// Always add evaluation for recursion
		if (__eval == null)
			__eval = new HashSet<>();
		
		// Look in these configurations for dependencies, done first so what
		// we depend on has higher priority first
		for (String configurationName : this._configs)
		{
			// The configuration might not even exist
			Configuration buildConfig = __at.getConfigurations()
				.findByName(configurationName);
			if (buildConfig == null)
				continue;
			
			// Recursively scan dependencies
__outer:
			for (Dependency dependency : buildConfig.getDependencies())
			{
				// Only consider modules
				if (!(dependency instanceof ProjectDependency))
					continue;
				
				// If this project was already evaluated stop because this
				// might recurse infinitely
				Project subProject = ((ProjectDependency)dependency)
					.getDependencyProject();
				for (Project check : __eval)
					if (subProject.compareTo(check) == 0)
						continue __outer;
				__eval.add(subProject);
				
				// Recursive search these dependencies
				this.__recursiveDependencies(__out, subProject, __eval);
			}
		}
		
		// Process the output files for the JAR of this project, use the
		// absolute path since there might be hard to find libraries that are
		// relative
		try
		{
			Task targetTask = __at.getTasks().getByName(
				(this.isSummerCoat ? "summerCoatRom" : "jar"));
			
			for (File file : targetTask.getOutputs().getFiles())
				__out.add(file.toPath().toAbsolutePath());
		}
		catch (UnknownTaskException ignored)
		{
			// Ignore this because there is no JAR or SummerCoat task to
			// depend on here
		}
	}
	
	/**
	 * Used to run the emulator.
	 *
	 * @since 2020/02/29
	 */
	private class __ActionTask__
		implements Action<Task>
	{
		__ActionTask__()
		{
		}
		
		/**
		 * {@inheritDoc}
		 * @since 2020/02/29
		 */
		@Override
		public void execute(Task __task)
		{
			// Get current and emulator projects
			Project project = AbstractEmulatedTask.this.getProject();
			Project emuBase = project.getRootProject().
				project(":emulators:emulator-base");
			Project emuCore = project.getRootProject().
				project(":emulators:" + AbstractEmulatedTask.this.
					emulator + "-vm");
			
			// Need this to get the program details
			SquirrelJMEPluginConfiguration config =
				SquirrelJMEPluginConfiguration.configuration(project);
			
			// Execute program
			project.javaexec((JavaExecSpec __spec) ->
				{
					// Are we launching a MIDlet?
					JavaMEMidlet midlet = JavaMEMidlet.find(config.midlets);
					
					// Build arguments to the VM
					Collection<String> args = new LinkedList<>();
					
					// Add emulator
					args.add("-Xemulator:" +
						AbstractEmulatedTask.this.emulator);
					
					// Add snapshot path
					args.add("-Xsnapshot:" + AbstractEmulatedTask.this.
						__profilerSnapshotPath().toPath().toString());
					
					// Determine program classpath
					args.add("-classpath");
					args.add(EmulatedTestUtilities.classpathAsString(
						AbstractEmulatedTask.this.__runClassPath()));
					
					// Add main class to launch
					args.add(AbstractEmulatedTask.this.mainClass(
						config, midlet));
					
					// We need to tell the MIDlet launcher what our main entry
					// point is going to be
					if (midlet != null &&
						AbstractEmulatedTask.this.useMidlet(midlet))
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
