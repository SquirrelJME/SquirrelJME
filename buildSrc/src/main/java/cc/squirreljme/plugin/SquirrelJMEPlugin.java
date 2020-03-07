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

import cc.squirreljme.plugin.tasks.AdditionalManifestPropertiesTask;
import cc.squirreljme.plugin.tasks.GenerateTestsListTask;
import cc.squirreljme.plugin.tasks.MimeDecodeResourcesTask;
import cc.squirreljme.plugin.tasks.RunEmulatedTask;
import cc.squirreljme.plugin.tasks.RunNativeTask;
import cc.squirreljme.plugin.tasks.RunTestEmulatedTask;
import cc.squirreljme.plugin.tasks.TestInVMTask;
import cc.squirreljme.plugin.tasks.TestsJarManifestTask;
import cc.squirreljme.plugin.tasks.TestsJarTask;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.tasks.SourceSet;

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
		
		// Test classes
		Task testClasses = __project.getTasks().
			getByName("testClasses");
		
		// Resource processing tasks
		Task processResources = __project.getTasks().
			getByName("processResources");
		Task processTestResources = __project.getTasks().
			getByName("processTestResources");
		
		// JAR Tasks
		Task jarTask = __project.getTasks().getByName("jar");
		
		// Run native application
		Task rna = __project.getTasks().create("runNative",
			RunNativeTask.class, jarTask);
		
		// Mime Decode Resources
		Task mmr = __project.getTasks().create("mimeDecodeResources",
			MimeDecodeResourcesTask.class, SourceSet.MAIN_SOURCE_SET_NAME,
			processResources);
		Task tmr = __project.getTasks().create("mimeDecodeTestResources",
			MimeDecodeResourcesTask.class, SourceSet.TEST_SOURCE_SET_NAME,
			processTestResources);
		
		// Generate the list of tests that are available
		Task gtl = __project.getTasks().create("generateTestsList",
			GenerateTestsListTask.class, processTestResources);
			
		// Build test JAR
		Task testJar = __project.getTasks()
			.create("testJar", TestsJarTask.class,
			testClasses, processTestResources);
			
		// Add SquirrelJME properties to the manifest
		__project.getTasks().create("additionalTestJarProperties",
			TestsJarManifestTask.class,
			testJar, processTestResources);
		
		// Add SquirrelJME properties to the manifest
		Task sjp = __project.getTasks().create("additionalJarProperties",
			AdditionalManifestPropertiesTask.class, jarTask, processResources);
		
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
			
		// Run emulated program
		Task esp = __project.getTasks().create("runSpringCoat",
			RunEmulatedTask.class,
			jarTask, "springcoat");
		Task esu = __project.getTasks().create("runSummerCoat",
			RunEmulatedTask.class,
			jarTask, "summercoat");
		
		// Run emulated tests
		Task tsp = __project.getTasks().create("testSpringCoat",
			TestInVMTask.class,
			testJar, "springcoat");
		Task tsu = __project.getTasks().create("testSummerCoat",
			TestInVMTask.class,
			testJar, "summercoat");
	}
}
