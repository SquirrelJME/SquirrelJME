// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.multivm;

import cc.squirreljme.plugin.tasks.AdditionalManifestPropertiesTask;
import cc.squirreljme.plugin.tasks.GenerateTestsListTask;
import cc.squirreljme.plugin.tasks.JasminAssembleTask;
import cc.squirreljme.plugin.tasks.MimeDecodeResourcesTask;
import cc.squirreljme.plugin.tasks.TestsJarManifestTask;
import cc.squirreljme.plugin.tasks.TestsJarTask;
import java.util.ArrayList;
import java.util.Iterator;
import org.gradle.api.GradleException;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.UnknownDomainObjectException;
import org.gradle.api.plugins.JavaPluginConvention;
import org.gradle.api.provider.Provider;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.TaskContainer;
import org.gradle.jvm.tasks.Jar;

/**
 * This is used to initialize the Gradle tasks for projects accordingly.
 *
 * @since 2020/08/07
 */
public final class TaskInitialization
{
	/** Source sets that are used. */
	private static final String[] _SOURCE_SETS =
		new String[]{SourceSet.MAIN_SOURCE_SET_NAME,
			VMHelpers.TEST_FIXTURES_SOURCE_SET_NAME,
			SourceSet.TEST_SOURCE_SET_NAME};
	
	/**
	 * Not used.
	 * 
	 * @since 2020/08/07
	 */
	private TaskInitialization()
	{
	}
	
	/**
	 * Initializes the project for the tasks and such 
	 * 
	 * @param __project The project to initialize for.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/08/07
	 */
	public static void initialize(Project __project)
		throws NullPointerException
	{
		if (__project == null)
			throw new NullPointerException("NARG");
		
		// Initialize or both main classes and such
		for (String sourceSet : TaskInitialization._SOURCE_SETS)
			TaskInitialization.initialize(__project, sourceSet);
		
		// Disable the test task, since it is non-functional
		// However this might fail
		try
		{
			__project.getTasks().replace("test", DefunctTestTask.class);
		}
		catch (IllegalStateException|GradleException e)
		{
			__project.getLogger().debug("Could not defunct test task.", e);
		}
		
		Task check = __project.getTasks().getByName("check");
		for (Iterator<Object> it = check.getDependsOn().iterator();
			it.hasNext();)
		{
			// Get the root item, if a provider of one
			Object item = it.next();
			if (item instanceof Provider)
				item = ((Provider<?>)item).get();
			
			// Only consider tasks
			if (!(item instanceof Task))
				continue;
			
			// Remove the test task, since we do not want it to run here
			if ("test".equals(((Task)item).getName()))
				it.remove();
		}
	}
	
	/**
	 * Initializes the source set for the given project.
	 * 
	 * @param __project The project to initialize for.
	 * @param __sourceSet The source set to be initialized.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/08/07
	 */
	public static void initialize(Project __project, String __sourceSet)
		throws NullPointerException
	{
		if (__project == null || __sourceSet == null)
			throw new NullPointerException("NARG");
		
		// Used for Jasmin and Mime Decoding tasks
		Task processResources = __project.getTasks()
			.getByName(TaskInitialization.task(
				"process", __sourceSet, "resources"));
			
		// Generate the list of tests that are available (only tests)
		if (__sourceSet.equals(SourceSet.TEST_SOURCE_SET_NAME))
			__project.getTasks().create("generateTestsList",
				GenerateTestsListTask.class, processResources);
		
		// The current Jar Task
		String jarTaskName = TaskInitialization.task(
			"", __sourceSet, "jar");
		Jar jarTask = (Jar)__project.getTasks()
			.findByName(jarTaskName);
		
		// If it does not exist, create it
		if (jarTask == null)
		{
			// We need to know how to make the classes
			Task classes = __project.getTasks()
				.getByName(TaskInitialization.task(
					"", __sourceSet, "classes"));
			
			// Create task for making the Jar
			jarTask = (Jar)__project.getTasks()
				.create("testJar", TestsJarTask.class,
				classes, processResources);
		}
		
		// Correct name of the Jar archive
		String normalJarName;
		if (__sourceSet.equals(SourceSet.MAIN_SOURCE_SET_NAME))
			normalJarName = __project.getName() + ".jar";
		else
			normalJarName = __project.getName() + "-" + __sourceSet + ".jar";
		jarTask.getArchiveFileName().set(normalJarName);
		
		// Jasmin assembling
		__project.getTasks().create(TaskInitialization.task(
				"assemble", __sourceSet, "jasmin"),
			JasminAssembleTask.class,
			__sourceSet,
			processResources);
		
		// Mime Decoding
		__project.getTasks().create(TaskInitialization.task(
				"mimeDecode", __sourceSet, "resources"),
			MimeDecodeResourcesTask.class, SourceSet.MAIN_SOURCE_SET_NAME,
			processResources);
			
		// Add SquirrelJME properties to the manifest
		Class<? extends Task> propertyTask;
		if (__sourceSet.equals(SourceSet.TEST_SOURCE_SET_NAME))
			propertyTask = TestsJarManifestTask.class;
		else
			propertyTask = AdditionalManifestPropertiesTask.class;
		__project.getTasks().create(TaskInitialization.task(
				"additional", __sourceSet, "jarProperties"),
			propertyTask, jarTask, processResources, __sourceSet);
		
		// Initialize for each VM
		for (VMType vmType : VMType.values())
			TaskInitialization.initialize(__project, __sourceSet, vmType);
	}
	
	/**
	 * Initializes the virtual machine for the given project's sourceset.
	 * 
	 * @param __project The project to initialize for.
	 * @param __sourceSet The source set.
	 * @param __vmType The virtual machine type.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/08/07
	 */
	public static void initialize(Project __project, String __sourceSet,
		VMSpecifier __vmType)
		throws NullPointerException
	{
		if (__project == null || __sourceSet == null || __vmType == null)
			throw new NullPointerException("NARG");
		
		// Everything will be working on these tasks
		TaskContainer tasks = __project.getTasks();
		
		// Make sure the source set exists first
		try
		{
			__project.getConvention().getPlugin(JavaPluginConvention.class)
				.getSourceSets().getByName(__sourceSet);
		}
		catch (UnknownDomainObjectException e)
		{
			__project.getLogger().debug(String.format(
				"Could not find sourceSet %s in project %s (available: %s)",
				__sourceSet, __project.getPath(), new ArrayList<>(
					__project.getConvention()
					.getPlugin(JavaPluginConvention.class).getSourceSets())),
				e);
		}
		
		// Library that needs to be constructed so execution happens properly
		VMLibraryTask libTask = tasks.create(
			TaskInitialization.task("lib", __sourceSet, __vmType),
			VMLibraryTask.class, __sourceSet, __vmType);
		
		// Is dumping available?
		if (__vmType.hasDumping())
			tasks.create(
				TaskInitialization.task("dump", __sourceSet, __vmType),
				VMDumpLibraryTask.class, __sourceSet, __vmType, libTask);
		
		// Running the target
		if (__sourceSet.equals(SourceSet.MAIN_SOURCE_SET_NAME))
			tasks.create(
				TaskInitialization.task("run", __sourceSet, __vmType),
				VMRunTask.class, __sourceSet, __vmType, libTask);
		
		// Testing the target
		else if (__sourceSet.equals(SourceSet.TEST_SOURCE_SET_NAME))
			tasks.create(
				TaskInitialization.task("test", __sourceSet, __vmType),
				VMTestTask.class, __sourceSet, __vmType, libTask);
	}
	
	/**
	 * Initializes the full-suite run which selects every API and library
	 * module available, along with allowing an external 3rd library classpath
	 * launching.
	 * 
	 * @param __project The root project.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/10/17
	 */
	public static void initializeFullSuiteTask(Project __project)
		throws NullPointerException
	{
		if (__project == null)
			throw new NullPointerException("NARG");
		
		for (VMType vmType : VMType.values())
			for (String sourceSet : TaskInitialization._SOURCE_SETS)
				TaskInitialization.initializeFullSuiteTask(__project,
					sourceSet, vmType);
	}
	
	/**
	 * Initializes the full-suite run which selects every API and library
	 * module available, along with allowing an external 3rd library classpath
	 * launching.
	 * 
	 * @param __project The root project.
	 * @param __sourceSet Source set used.
	 * @param __vmType The virtual machine type.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/10/17
	 */
	private static void initializeFullSuiteTask(Project __project,
		String __sourceSet, VMType __vmType)
		throws NullPointerException
	{
		if (__project == null || __sourceSet == null || __vmType == null)
			throw new NullPointerException("NARG");
		
		// Standard ROM
		__project.getTasks().create(
			TaskInitialization.task("full", __sourceSet, __vmType),
			VMFullSuite.class, __sourceSet, __vmType);
	}
	
	/**
	 * Initializes ROM tasks for the given base project.
	 * 
	 * @param __project The root project.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/08/23
	 */
	public static void romTasks(Project __project)
		throws NullPointerException
	{
		if (__project == null)
			throw new NullPointerException("NARG");
			
		// Initialize or both main classes and such
		for (String sourceSet : TaskInitialization._SOURCE_SETS)
			for (VMType vmType : VMType.values())
				TaskInitialization.romTasks(__project, sourceSet, vmType);
	}
	
	/**
	 * Initializes ROM tasks for the given base project.
	 * 
	 * @param __project The root project.
	 * @param __sourceSet The source set to use.
	 * @param __vmType The virtual machine type used.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/08/23
	 */
	private static void romTasks(Project __project, String __sourceSet,
		VMType __vmType)
		throws NullPointerException
	{
		if (__project == null || __sourceSet == null || __vmType == null)
			throw new NullPointerException("NARG");
			
		// Everything will be working on these tasks
		TaskContainer tasks = __project.getTasks();
		
		// Does the VM utilize ROMs?
		if (__vmType.hasRom())
		{
			String baseName = TaskInitialization.task("rom",
				__sourceSet, __vmType);
			VMRomTask rom = tasks.create(baseName,
				VMRomTask.class, __sourceSet, __vmType);
			
			// Full RatufaCoat Built-In
			__project.getTasks().create(baseName + "RatufaCoat",
				RatufaCoatBuiltInTask.class,  __sourceSet, __vmType, rom);
		}
	}
	
	/**
	 * Builds a name for a task, without the virtual machine type.
	 * 
	 * @param __name The task name.
	 * @param __sourceSet The source set for the task base.
	 * @return A string representing the task.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/12/19
	 */
	public static String task(String __name, String __sourceSet)
		throws NullPointerException
	{
		return TaskInitialization.task(__name, __sourceSet, "");
	}
	
	/**
	 * Builds a name for a task, without the virtual machine type.
	 * 
	 * @param __name The task name.
	 * @param __sourceSet The source set for the task base.
	 * @param __suffix The task suffix.
	 * @return A string representing the task.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/08/07
	 */
	public static String task(String __name, String __sourceSet,
		String __suffix)
		throws NullPointerException
	{
		if (__name == null || __sourceSet == null || __suffix == null)
			throw new NullPointerException("NARG");
		
		// We need to later determine how the suffix works
		String baseName;
		
		// If this is the main source set, never include the source set as
		// it becomes implied. Additionally, if the name and the source set
		// are the same, reduce the confusion so there is no "testTestHosted".
		if (__sourceSet.equals(SourceSet.MAIN_SOURCE_SET_NAME) ||
			__sourceSet.equals(__name) || __sourceSet.isEmpty())
			baseName = __name;
		
		// Otherwise, include it
		else
		{
			// If just the source set, then just keep that lowercase
			if (__name.isEmpty())
				baseName = __sourceSet;
			else
				baseName = __name +
					TaskInitialization.uppercaseFirst(__sourceSet);
		}
		
		// If there is no suffix, just return the base
		if (__suffix.isEmpty())
			return baseName;
		
		// If there is no base, just return the suffix
		if (baseName.isEmpty())
			return __suffix;
		
		// Otherwise, perform needed capitalization
		// "additionalJarProperties" or "additionalTestJarProperties"
		return baseName + TaskInitialization.uppercaseFirst(__suffix);
	}
	
	/**
	 * Builds a name for a task.
	 * 
	 * @param __name The task name.
	 * @param __sourceSet The source set for the task base.
	 * @param __vmType The type of virtual machine used.
	 * @return A string representing the task.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/08/07
	 */
	public static String task(String __name, String __sourceSet,
		VMSpecifier __vmType)
		throws NullPointerException
	{
		if (__name == null || __sourceSet == null || __vmType == null)
			throw new NullPointerException("NARG");
		
		return TaskInitialization.task(__name, __sourceSet) +
			__vmType.vmName(VMNameFormat.PROPER_NOUN);
	}
	
	/**
	 * Uppercases the first character of a string.
	 * 
	 * @param __input The input string.
	 * @return The string with the first character uppercased.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/08/07
	 */
	public static String uppercaseFirst(String __input)
		throws NullPointerException
	{
		if (__input == null)
			throw new NullPointerException("NARG");
		
		if (__input.isEmpty())
			return __input;
		
		return Character.toUpperCase(__input.charAt(0)) +
			__input.substring(1);
	}
}
