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
import cc.squirreljme.plugin.swm.JavaMEMidlet;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Objects;
import java.util.Set;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.Dependency;
import org.gradle.api.artifacts.ProjectDependency;
import org.gradle.api.provider.Provider;
import org.gradle.api.tasks.SourceSet;
import org.gradle.jvm.tasks.Jar;

/**
 * Helpers for the multi-VM handlers.
 *
 * @since 2020/08/07
 */
public final class MultiVMHelpers
{
	/** Main configurations. */
	private static final String[] _MAIN_CONFIGS =
		new String[]{"api", "implementation"};
	
	/** Test configurations. */
	private static final String[] _TEST_CONFIGS =
		new String[]{"testApi", "testImplementation"};
	
	/* Copy buffer size. */
	public static final int COPY_BUFFER =
		4096;
	
	/**
	 * Not used.
	 * 
	 * @since 2020/08/07
	 */
	private MultiVMHelpers()
	{
	}
	
	/**
	 * Returns a collection of the tests that are available.
	 * 
	 * @param __project The project to check.
	 * @param __sourceSet The source set for the project.
	 * @return The list of available tests.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/08/07
	 */
	public static Collection<String> availableTests(Project __project,
		String __sourceSet)
		throws NullPointerException
	{
		if (__project == null || __sourceSet == null)
			throw new NullPointerException("NARG");
		
		throw new Error("TODO");
	}
	
	/**
	 * Returns the cache directory of the project.
	 * 
	 * @param __project The project to get the cache directory of.
	 * @param __vmType The virtual machine being used.
	 * @param __sourceSet The source set for the library, as there might be
	 * duplicates between them potentially.
	 * @return The path provider to the project cache directory.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/08/15
	 */
	public static Provider<Path> cacheDir(Project __project,
		VirtualMachineSpecifier __vmType, String __sourceSet)
		throws NullPointerException
	{
		if (__project == null || __vmType == null)
			throw new NullPointerException("NARG");
		
		return __project.provider(() -> __project.getBuildDir().toPath()
			.resolve("squirreljme").resolve("vm-" + __sourceSet + "-" +
				__vmType.vmName(VMNameFormat.LOWERCASE)));
	}
	

	/**
	 * Returns the class path as a string.
	 *
	 * @param __paths Class paths.
	 * @return The class path as a string.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/08/21
	 */
	public static String classpathAsString(Path... __paths)
		throws NullPointerException
	{
		if (__paths == null)
			throw new NullPointerException("NARG");
		
		return MultiVMHelpers.classpathAsString(Arrays.asList(__paths));
	}
	
	/**
	 * Returns the class path as a string.
	 *
	 * @param __paths Class paths.
	 * @return The class path as a string.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/02/29
	 */
	public static String classpathAsString(Iterable<Path> __paths)
		throws NullPointerException
	{
		if (__paths == null)
			throw new NullPointerException("NARG");
		
		StringBuilder sb = new StringBuilder();
		
		for (Path path : __paths)
		{
			if (sb.length() > 0)
				sb.append(File.pathSeparatorChar);
			sb.append(path);
		}
		
		return sb.toString();
	}
	
	/**
	 * Copies from the input into the output.
	 * 
	 * @param __in The input.
	 * @param __out The output.
	 * @throws IOException On read/write errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/08/15
	 */
	public static void copy(InputStream __in, OutputStream __out)
		throws IOException, NullPointerException
	{
		if (__in == null || __out == null)
			throw new NullPointerException("NARG");
		
		byte[] buf = new byte[MultiVMHelpers.COPY_BUFFER];
		for (;;)
		{
			int rc = __in.read(buf);
			
			if (rc < 0)
				return;
			
			__out.write(buf, 0, rc);
		}
	}
	
	/**
	 * Returns the task that creates the JAR.
	 * 
	 * @param __project The project to get from.
	 * @param __sourceSet The source set.
	 * @return The jar task used, or {@code null} if not found.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/08/07
	 */
	public static Jar jarTask(Project __project, String __sourceSet)
		throws NullPointerException
	{
		if (__project == null || __sourceSet == null)
			throw new NullPointerException("NARG");
		
		switch (__sourceSet)
		{
			case SourceSet.MAIN_SOURCE_SET_NAME:
				return (Jar)__project.getTasks().getByName("jar");
			
			case SourceSet.TEST_SOURCE_SET_NAME:
				return (Jar)__project.getTasks().getByName("testJar");
			
			default:
				throw new IllegalStateException("Unknown sourceSet: " +
					__sourceSet);
		}
	}
	
	/**
	 * Returns the main class to execute.
	 *
	 * @param __cfg The configuration.
	 * @param __midlet The MIDlet to be ran.
	 * @return The main class.
	 * @throws NullPointerException If {@code __cfg} is {@code null}.
	 * @since 2020/03/06
	 */
	public static String mainClass(SquirrelJMEPluginConfiguration __cfg,
		JavaMEMidlet __midlet)
		throws NullPointerException
	{
		if (__cfg == null)
			throw new NullPointerException("NARG");
		
		// We either run the MIDlet or we do not
		return (__midlet != null ?
			"javax.microedition.midlet.__MainHandler__" :
			Objects.requireNonNull(__cfg.mainClass,
			"No main class in project."));
	}
	
	/**
	 * Returns the project classpath.
	 *
	 * @param __project The project.
	 * @return The class path.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/02/29
	 */
	public static Iterable<File> projectRuntimeClasspath(Project __project)
		throws NullPointerException
	{
		if (__project == null)
			throw new NullPointerException("No project specified.");
		
		return __project.getConfigurations().
			getByName("runtimeClasspath").getFiles();
	}
	
	/**
	 * Resolves tasks from the projects and tasks.
	 * 
	 * @param <T> The class to resolve as.
	 * @param __class The class to resolve as.
	 * @param __project The project to latch onto for lookup.
	 * @param __in The input project and task names.
	 * @return An iterable which has the projects resolved.
	 * @throws NullPointerException On null arguments.
	 */
	public static <T extends Task> Iterable<T> resolveProjectTasks(
		Class<T> __class, Project __project, Iterable<ProjectAndTaskName> __in)
		throws NullPointerException
	{
		if (__project == null || __in == null)
			throw new NullPointerException("NARG");
		
		Collection<T> result = new LinkedList<>();
		
		// Map projects and tasks back into tasks
		for (ProjectAndTaskName depend : __in)
			result.add(__class.cast(__project.project(depend.project)
				.getTasks().getByName(depend.task)));
		
		return Collections.unmodifiableCollection(result);
	}
	
	/**
	 * Returns the path of the all the JARs which make up the classpath for
	 * running an executable.
	 * 
	 * @param __task The task to get for.
	 * @param __sourceSet The source set used.
	 * @param __vmType The virtual machine type.
	 * @return An array of paths containing the class path of execution.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/08/20
	 */
	public static Path[] runClassPath(MultiVMExecutableTask __task,
		String __sourceSet, VirtualMachineSpecifier __vmType)
		throws NullPointerException
	{
		if (__task == null || __sourceSet == null || __vmType == null)
			throw new NullPointerException("NARG");
		
		// Get tasks that are used for dependency running
		Iterable<MultiVMLibraryTask> tasks =
			MultiVMHelpers.<MultiVMLibraryTask>resolveProjectTasks(
				MultiVMLibraryTask.class, __task.getProject(), MultiVMHelpers
					.runClassTasks(__task.getProject(), __sourceSet,
						__vmType));
		
		// Get the outputs of these, as they will be used. Ensure the order is
		// kept otherwise execution may be non-deterministic and could break.
		Set<Path> classPath = new LinkedHashSet<>();
		for (MultiVMLibraryTask vmLib : tasks)
			classPath.add(vmLib.getOutputs().getFiles().getSingleFile()
				.toPath());
		
		return classPath.toArray(new Path[classPath.size()]);
	}
	
	/**
	 * Returns the task dependencies to get outputs from that would be
	 * considered a part of the project's class path used at execution time.
	 * 
	 * @param __project The task to get from.
	 * @param __sourceSet The source set used.
	 * @param __vmType The virtual machine information.
	 * @return The direct run dependencies for the task.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/08/15
	 */
	public static Collection<ProjectAndTaskName> runClassTasks(
		Project __project, String __sourceSet,
		VirtualMachineSpecifier __vmType)
		throws NullPointerException
	{
		return MultiVMHelpers.runClassTasks(__project, __sourceSet, __vmType,
			null);
	}
	
	/**
	 * Returns the task dependencies to get outputs from that would be
	 * considered a part of the project's class path used at execution time.
	 * 
	 * @param __project The task to get from.
	 * @param __sourceSet The source set used.
	 * @param __vmType The virtual machine information.
	 * @param __did Projects that have been processed.
	 * @return The direct run dependencies for the task.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/08/15
	 */
	public static Collection<ProjectAndTaskName> runClassTasks(
		Project __project, String __sourceSet,
		VirtualMachineSpecifier __vmType, Set<ProjectAndTaskName> __did)
		throws NullPointerException
	{
		if (__project == null || __sourceSet == null || __vmType == null)
			throw new NullPointerException("NARG");
		
		// Make sure this is always set
		if (__did == null)
			__did = new HashSet<>();
		
		// If this process was already processed, ignore it
		ProjectAndTaskName selfProjectTask = ProjectAndTaskName.of(__project,
			TaskInitialization.task("lib", __sourceSet, __vmType));
		if (__did.contains(selfProjectTask))
			return Collections.emptySet();
		
		Set<ProjectAndTaskName> result = new LinkedHashSet<>();
		
		// If we are testing then we depend on the main TAC library, otherwise
		// we will not be able to do any actual testing
		boolean isTesting = __sourceSet.equals(SourceSet.TEST_SOURCE_SET_NAME);
		if (isTesting)
		{
			// Depend on TAC
			result.addAll(MultiVMHelpers.runClassTasks(
				__project.findProject(":modules:tac"),
				SourceSet.MAIN_SOURCE_SET_NAME, __vmType, __did));
			
			// Depend on our main project as we will be testing it
			result.addAll(MultiVMHelpers.runClassTasks(__project,
				SourceSet.MAIN_SOURCE_SET_NAME, __vmType, __did));
		}
		
		// Go through the configurations to yank in the dependencies as needed
		for (String config : (isTesting ? MultiVMHelpers._TEST_CONFIGS :
			MultiVMHelpers._MAIN_CONFIGS))
		{
			// The configuration may be optional
			Configuration foundConfig = __project.getConfigurations()
				.findByName(config);
			if (foundConfig == null)
				continue;
			
			// Handle dependencies
			for (Dependency depend : foundConfig.getDependencies())
			{
				// Only consider projects
				if (!(depend instanceof ProjectDependency))
					continue;
				
				Project sub = ((ProjectDependency)depend)
					.getDependencyProject();
				
				// Only consider SquirrelJME projects
				SquirrelJMEPluginConfiguration squirreljmeConf =
					SquirrelJMEPluginConfiguration.configurationOrNull(sub);
				if (squirreljmeConf == null)
					continue;
				
				// Otherwise, handle the dependencies
				result.addAll(MultiVMHelpers.runClassTasks(sub, 
					SourceSet.MAIN_SOURCE_SET_NAME, __vmType, __did));
			}
		}
		
		// Finally add our own library for usages
		result.add(selfProjectTask);
		
		// Ignore our own project
		__did.add(selfProjectTask);
		
		// Debug as needed
		__project.getLogger().debug("Run Depends: {}", result);
		
		return Collections.unmodifiableCollection(result);
	}
}
