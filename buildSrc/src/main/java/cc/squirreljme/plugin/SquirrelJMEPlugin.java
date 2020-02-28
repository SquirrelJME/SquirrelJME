// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
//     Copyright (C) Multi-Phasic Applications <multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.plugins.JavaPluginConvention;
import org.gradle.api.tasks.SourceSet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.LinkedList;
import java.util.List;

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
		__project.getExtensions().<SquirrelJMEPluginConfiguration>create(
			"squirreljme", SquirrelJMEPluginConfiguration.class,
			this, __project);
		
		// Inject manifest properties
		Task injectManTask = __project.getTasks().
			create("__injectManifest");
		injectManTask.setGroup("squirreljme");
		injectManTask.doLast(new InjectManifestTask(__project));
		
		// The manifest must be done before the JAR is built
		Task jarTask = __project.getTasks().getByName("jar");
		jarTask.dependsOn(injectManTask);
		
		// Launch application in the SpringCoat VM!
		Task launchSpring = __project.getTasks()
			.create("runSpringCoat");
		launchSpring.setGroup("squirreljme");
		launchSpring.setDescription("Runs via SquirrelJME SpringCoat.");
		launchSpring.dependsOn(jarTask, ":emulators:springcoat-vm:jar");
		launchSpring.onlyIf((Task __task) ->
			SquirrelJMEPluginConfiguration.isApplication(__project));
		launchSpring.doLast((Task __task) ->
			new __RunSpringCoatApplication__(__project).run());
		
		// Building of SummerCoat ROM
		Task buildROM = __project.getTasks().
			create("jarSummerCoatROM");
		buildROM.setGroup("squirreljme");
		buildROM.setDescription("Builds SquirrelJME SummerCoat ROM.");
		buildROM.dependsOn(jarTask);
		buildROM.doLast((Task __task) ->
			{throw new Error("TODO");});
		
		// Launch application in the SummerCoat VM!
		Task launchSummer = __project.getTasks()
			.create("runSummerCoat");
		launchSummer.setGroup("squirreljme");
		launchSummer.setDescription("Runs via SquirrelJME SummerCoat.");
		launchSummer.dependsOn(buildROM, ":emulators:summercoat-vm:jar");
		launchSpring.onlyIf((Task __task) ->
			SquirrelJMEPluginConfiguration.isApplication(__project));
		launchSummer.doLast((Task __task) ->
			new __RunSummerCoatApplication__(__project).run());
		
		// List error codes used by projects
		Task listErrorCodes = __project.getTasks()
			.create("listErrorCodes");
		listErrorCodes.setGroup("squirreljme");
		listErrorCodes.setDescription("Lists error code prefixes.");
		listErrorCodes.doLast((Task __task) ->
			new ErrorCodeManager(__project.getRootProject())
				.print(System.out));
		
		// Determine the next error code that is available
		Task nextErrorCode = __project.getTasks()
			.create("nextErrorCode");
		nextErrorCode.setGroup("squirreljme");
		nextErrorCode.setDescription("Returns the next free error code.");
		nextErrorCode.doLast((Task __task) ->
			System.out.println(new ErrorCodeManager(__project.getRootProject())
				.next()));
		
		// List errors in single project
		Task listErrors = __project.getTasks()
			.create("listErrors");
		listErrors.setGroup("squirreljme");
		listErrors.setDescription("Lists all of the source error codes.");
		listErrors.doLast((Task __task) ->
			new ErrorListManager(__project).print(System.out));
		
		// Returns the next available error in single project
		Task nextError = __project.getTasks()
			.create("nextError");
		nextError.setGroup("squirreljme");
		nextError.setDescription("Returns the next free error code.");
		nextError.doLast((Task __task) ->
			System.out.println(new ErrorListManager(__project).next()));
		
		// Un-MIME the resource files (main code)
		Task unMimeResources = __project.getTasks()
			.create("unMimeResources");
		unMimeResources.setGroup("squirreljme");
		unMimeResources.setDescription("MIME decodes resources.");
		inputAllResourceFiles(unMimeResources, __project, "main");
		unMimeResources.doLast(
			new UnMimeResourcesTask(__project, "main"));
			
		// Resource processing task
		Task processResources = __project.getTasks().
			getByName("processResources");
		processResources.dependsOn(unMimeResources);
		
		// Un-MIME the resource files (main code)
		Task unMimeTestResources = __project.getTasks()
			.create("unMimeTestResources");
		unMimeTestResources.setGroup("squirreljme");
		unMimeTestResources.setDescription("MIME decodes test resources.");
		inputAllResourceFiles(unMimeTestResources, __project, "test");
		unMimeTestResources.doLast(
			new UnMimeResourcesTask(__project, "test"));
		
		// Generate test resources
		Task genTestMeta = __project.getTasks()
			.create("generateTestMetadata");
		genTestMeta.setGroup("squirreljme");
		genTestMeta.setDescription("Generates extra test resources.");
		genTestMeta.dependsOn(unMimeTestResources);
		inputAllSourceFiles(genTestMeta, __project, "test");
		genTestMeta.doLast((Task __task) ->
			this.__generateTestMetadata(__project));
		
		// Test resource processing task
		Task processTestResources = __project.getTasks().
			getByName("processTestResources");
		processTestResources.dependsOn(genTestMeta);
	}
	
	/**
	 * Generates needed test metadata.
	 *
	 * @param __project The project to run.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/02/23
	 */
	private void __generateTestMetadata(Project __project)
		throws NullPointerException
	{
		if (__project == null)
			throw new NullPointerException("No project specified.");
		
		// Where our resources go
		Path genResourceRoot = __project.getBuildDir().toPath()
			.resolve("generated-test-resources");
		
		// Process source files
		try
		{
			// Make sure the directory exists
			Files.createDirectories(genResourceRoot);
			
			// Discovered test
			List<String> tests = new LinkedList<>();
			
			// If there are no tests, then do not bother with this step
			Path srcRoot = __project.getProjectDir().toPath()
				.resolve("src").resolve("test").resolve("java");
			if (!Files.exists(srcRoot))
				return;
			
			// Walk the file
			Files.walk(srcRoot).forEach(
				(Path __visit) ->
				{
					// Ignore directories
					if (Files.isDirectory(__visit))
						return;
					
					// Only consider Java source files
					if (!__visit.toString().endsWith(".java"))
						return;
					
					// Only consider tests of a certain name
					String fileName = __visit.getFileName().toString();
					if (!fileName.startsWith("Test") &&
						!fileName.startsWith("Do"))
						return;
					
					// Store the class name
					String baseName = srcRoot.relativize(__visit).toString();
					tests.add(baseName.substring(
						0, baseName.length() - ".java".length())
						.replace('\\', '.')
						.replace('/', '.'));
				});
				
			// Make services directory
			Path servicesPath = genResourceRoot.resolve("META-INF")
				.resolve("services");
			Files.createDirectories(servicesPath);
			
			// Write test file
			Files.write(
				servicesPath.resolve("net.multiphasicapps.tac.TestInterface"),
				tests, StandardOpenOption.WRITE, StandardOpenOption.CREATE,
				StandardOpenOption.TRUNCATE_EXISTING);
		}
		catch (IOException e)
		{
			throw new RuntimeException(String.format(
				"Failed to generate metadata for %s.",
				__project.getName()), e);
		}
	}
	
	/**
	 * Sets the input of a task to be all the source files.
	 *
	 * @param __task The task to define.
	 * @param __project The project.
	 * @param __group Which group are we concerned with?
	 * @throws NullPointerException On null arguments.
	 * @since 2020/02/27
	 */
	public static void inputAllSourceFiles(Task __task, Project __project,
		String __group)
		throws NullPointerException
	{
		if (__task == null || __project == null || __group == null)
			throw new NullPointerException("No task or project specified.");
		
		__task.getInputs().files(__project.getConvention().getPlugin(
			JavaPluginConvention.class).getSourceSets().
			getByName(__group).getAllJava());
	}
	
	/**
	 * Sets the input of a task to be all the resource files.
	 *
	 * @param __task The task to define.
	 * @param __project The project.
	 * @param __group Which group are we concerned with?
	 * @throws NullPointerException On null arguments.
	 * @since 2020/02/27
	 */
	public static void inputAllResourceFiles(Task __task, Project __project,
		String __group)
		throws NullPointerException
	{
		if (__task == null || __project == null || __group == null)
			throw new NullPointerException("No task or project specified.");
		
		__task.getInputs().files(__project.getConvention().getPlugin(
			JavaPluginConvention.class).getSourceSets().
			getByName(__group).getResources());
	}
}
