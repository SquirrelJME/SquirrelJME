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

import groovy.lang.Closure;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.file.FileCollection;
import org.gradle.api.file.FileSystemLocation;
import org.gradle.api.file.FileTree;
import org.gradle.api.java.archives.Attributes;
import org.gradle.api.plugins.JavaPluginConvention;
import org.gradle.api.provider.Provider;
import org.gradle.api.specs.Spec;
import org.gradle.api.tasks.SourceSet;
import org.gradle.api.tasks.SourceSetContainer;
import org.gradle.api.tasks.TaskDependency;
import org.gradle.jvm.tasks.Jar;
import org.gradle.language.jvm.tasks.ProcessResources;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

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
		injectManTask.doLast((Task __task) ->
			this.__configureManifest(__project));
		
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
			__isApplication(__project));
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
			__isApplication(__project));
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
		
		// Generate test resources
		Task genTestMeta = __project.getTasks()
			.create("generateTestMetadata");
		genTestMeta.setGroup("squirreljme");
		genTestMeta.setDescription("Generates extra test resources.");
		genTestMeta.doLast((Task __task) ->
			this.__generateTestMetadata(__project));
		
		// Resource processing task
		Task processTestResources = __project.getTasks().
			getByName("processTestResources");
		processTestResources.dependsOn(genTestMeta);
	}
	
	/**
	 * Performs manifest configuration.
	 *
	 * @param __project The project being adjusted.
	 * @since 2020/02/15
	 */
	private void __configureManifest(Project __project)
	{
		// Find our config
		SquirrelJMEPluginConfiguration config = __project.getExtensions()
			.<SquirrelJMEPluginConfiguration>getByType(
				SquirrelJMEPluginConfiguration.class);
		
		// Obtain the manifest to modify
		Jar jar = (Jar)__project.getTasks().getByName("jar");
		Attributes attributes = jar.getManifest().getAttributes();
		
		// Project name is used internally for dependency lookup
		attributes.put("X-SquirrelJME-InternalProjectName",
			__project.getName());
		
		// Standard Application
		if (config.swmType == JavaMEMidletType.APPLICATION)
		{
			attributes.put("MIDlet-Name", config.swmName);
			attributes.put("MIDlet-Vendor", config.swmVendor);
			attributes.put("MIDlet-Version",
				__project.getVersion().toString());
			
			// Main class of entry?
			if (config.mainClass != null)
				attributes.put("Main-Class", config.mainClass);
			
			// Add any defined MIDlets
			int midletId = 1;
			for (JavaMEMidlet midlet : config.midlets)
				attributes.put("MIDlet-" + (midletId++), midlet.toString());
			
			// Ignored in the launcher?
			if (config.ignoreInLauncher)
				attributes.put("X-SquirrelJME-NoLauncher", true);
		}
		
		// Library defining classes
		if (config.swmType == JavaMEMidletType.LIBRARY)
		{
			attributes.put("LIBlet-Name", config.swmName);
			attributes.put("LIBlet-Vendor", config.swmVendor);
			attributes.put("LIBlet-Version",
				__project.getVersion().toString());
		}
		
		// SquirrelJME defined APIs
		if (config.swmType == JavaMEMidletType.API)
		{
			attributes.put("X-SquirrelJME-API-Name", config.swmName);
			attributes.put("X-SquirrelJME-API-Vendor", config.swmVendor);
			attributes.put("X-SquirrelJME-API-Version",
				__project.getVersion().toString());
			
			// Configurations defined?
			if (!config.definedConfigurations.isEmpty())
				attributes.put("X-SquirrelJME-DefinedConfigurations",
					__delimate(config.definedConfigurations, ' '));
			
			// Profiles defined?
			if (!config.definedProfiles.isEmpty())
				attributes.put("X-SquirrelJME-DefinedProfiles",
					__delimate(config.definedProfiles, ' '));
			
			// Standards defined?
			if (!config.definedStandards.isEmpty())
				attributes.put("X-SquirrelJME-DefinedStandards",
					__delimate(config.definedStandards, ' '));
		}
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
			.resolve("generated-resources");
		
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
	 * Turns an iterable into a string with the given delimeter.
	 *
	 * @param __it The iteration to convert.
	 * @param __delim The delimiter.
	 * @return The converted string.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/02/15
	 */
	private static String __delimate(Iterable<?> __it, char __delim)
		throws NullPointerException
	{
		if (__it == null)
			throw new NullPointerException("NARG");
		
		// Build output string
		StringBuilder sb = new StringBuilder();
		for (Object o : __it)
		{
			// Add delimeter?
			if (sb.length() > 0)
				sb.append(__delim);
			
			// Add object
			sb.append(o);
		}
		
		return sb.toString();
	}
	
	/**
	 * Is this a SquirrelJME application?
	 *
	 * @param __project The project to check.
	 * @return If it is an application.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/02/16
	 */
	private static boolean __isApplication(Project __project)
		throws NullPointerException
	{
		if (__project == null)
			throw new NullPointerException("No project specified.");
			
		// Find our config
		SquirrelJMEPluginConfiguration config = __project.getExtensions()
			.<SquirrelJMEPluginConfiguration>getByType(
				SquirrelJMEPluginConfiguration.class);
		
		return config.swmType == JavaMEMidletType.APPLICATION;
	}
}
