// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.tasks;

import cc.squirreljme.plugin.SquirrelJMEPluginConfiguration;
import cc.squirreljme.plugin.multivm.BangletVariant;
import cc.squirreljme.plugin.multivm.ClutterLevel;
import cc.squirreljme.plugin.multivm.TaskInitialization;
import cc.squirreljme.plugin.multivm.VMHelpers;
import cc.squirreljme.plugin.multivm.VMType;
import cc.squirreljme.plugin.multivm.ident.SourceTargetClassifier;
import cc.squirreljme.plugin.multivm.ident.TargetClassifier;
import cc.squirreljme.plugin.swm.JavaMEConfiguration;
import cc.squirreljme.plugin.swm.JavaMEMidlet;
import cc.squirreljme.plugin.swm.JavaMEMidletType;
import cc.squirreljme.plugin.swm.JavaMEProfile;
import cc.squirreljme.plugin.swm.JavaMEStandard;
import cc.squirreljme.plugin.swm.SuiteDependency;
import cc.squirreljme.plugin.swm.SuiteDependencyLevel;
import cc.squirreljme.plugin.swm.SuiteDependencyType;
import cc.squirreljme.plugin.swm.SuiteName;
import cc.squirreljme.plugin.swm.SuiteVendor;
import cc.squirreljme.plugin.swm.SuiteVersion;
import cc.squirreljme.plugin.swm.SuiteVersionRange;
import cc.squirreljme.plugin.util.ProjectAndSourceSet;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.jar.Attributes;
import org.gradle.api.Action;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.artifacts.Configuration;
import org.gradle.api.artifacts.Dependency;
import org.gradle.api.artifacts.ProjectDependency;
import org.gradle.api.tasks.SourceSet;

/**
 * Implements the action for {@link AdditionalManifestPropertiesTask}.
 *
 * @since 2022/08/07
 */
public class AdditionalManifestPropertiesTaskAction
	implements Action<Task>
{
	/** The task output. */
	protected final Path taskOutput;
	
	/** The source set used. */
	protected final String sourceSet;
	
	/**
	 * Initializes the task action.
	 *
	 * @param __taskOutput The task output.
	 * @param __sourceSet The source set used.
	 * @since 2022/08/07
	 */
	public AdditionalManifestPropertiesTaskAction(Path __taskOutput,
		String __sourceSet)
	{
		this.taskOutput = __taskOutput;
		this.sourceSet = __sourceSet;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/08/07
	 */
	@Override
	public void execute(Task __task)
	{
		// Is this the main source set?
		String sourceSet = this.sourceSet;
		boolean isTest = sourceSet.equals(SourceSet.TEST_SOURCE_SET_NAME);
		
		// Get the project and the config details
		Project project = __task.getProject();
		SquirrelJMEPluginConfiguration config =
			SquirrelJMEPluginConfiguration.configuration(project);
		
		// Setup manifest to write into
		java.util.jar.Manifest javaManifest = new java.util.jar.Manifest();
		Attributes attributes = javaManifest.getMainAttributes();
		
		// Set manifest to 1.0
		attributes.put(Attributes.Name.MANIFEST_VERSION, "1.0");
		
		// Determine project name for lookup
		String internalProjectName =
			VMHelpers.projectInternalNameViaSourceSet(project, sourceSet);
		
		// Project name is used internally for dependency lookup
		attributes.putValue("X-SquirrelJME-InternalProjectName",
			internalProjectName);
		
		// Generation really depends on the application type
		// The main sources are whatever, but everything else such as
		// text fixtures is considered a library dependency wise
		// Tests are always considered to be applications that are run
		JavaMEMidletType type =
			config.swmType.normalizeViaSourceSet(sourceSet);
		
		// Add common keys
		attributes.putValue(type.nameKey(),
			VMHelpers.projectSwmNameViaSourceSet(project, sourceSet));
		attributes.putValue(type.vendorKey(), config.swmVendor);
		attributes.putValue(type.versionKey(),
			new SuiteVersion(project.getVersion().toString()).toString());
		
		// Error Prefix
		attributes.putValue("X-SquirrelJME-PrefixCode",
			Objects.toString(config.javaDocErrorCode, "XX"));
		
		// Application
		if (type == JavaMEMidletType.APPLICATION)
		{
			// Add ability for tests to be launched
			if (isTest)
			{
				// SquirrelJME specific indicator that this is for testing
				attributes.putValue("X-SquirrelJME-Tests", "true");
				
				// Main entry point is always the TAC test runner
				attributes.putValue("Main-Class",
					"net.multiphasicapps.tac.MainSuiteRunner");
				
				// Add class path needed to run the test
				attributes.putValue("X-SquirrelJME-Tests-ClassPath",
					VMHelpers.runClassPathAsInternalClassPath(
						__task.getProject(),
						SourceTargetClassifier.builder()
							.sourceSet(SourceSet.TEST_SOURCE_SET_NAME)
							.targetClassifier(TargetClassifier.builder()
								.bangletVariant(BangletVariant.NONE)
								.vmType(VMType.HOSTED)
								.clutterLevel(ClutterLevel.DEBUG)
								.build())
							.build(), true));
			}
			
			// Normal application
			else
			{
				// Main class of entry?
				if (config.mainClass != null)
					attributes.putValue("Main-Class", config.mainClass);
				
				// Add any defined MIDlets
				int midletId = 1;
				for (JavaMEMidlet midlet : config.midlets)
					attributes.putValue("MIDlet-" + (midletId++),
						midlet.toString());
				
				// Ignored in the launcher?
				if (config.ignoreInLauncher)
					attributes.putValue("X-SquirrelJME-NoLauncher",
						"true");
			}
		}
		
		// Library
		else if (type == JavaMEMidletType.LIBRARY)
		{
		}
		
		// API
		else if (type == JavaMEMidletType.API)
		{
			// Configurations defined?
			if (!config.definedConfigurations.isEmpty())
				attributes.putValue(
					"X-SquirrelJME-DefinedConfigurations",
					AdditionalManifestPropertiesTaskAction
						.__delimate(config.definedConfigurations, ' '));
			
			// Profiles defined?
			if (!config.definedProfiles.isEmpty())
				attributes.putValue(
					"X-SquirrelJME-DefinedProfiles",
					AdditionalManifestPropertiesTaskAction
						.__delimate(config.definedProfiles, ' '));
			
			// Standards defined?
			if (!config.definedStandards.isEmpty())
				attributes.putValue(
					"X-SquirrelJME-DefinedStandards",
					AdditionalManifestPropertiesTaskAction
						.__delimate(config.definedStandards, ','));
		}
		
		// Always depend on the main source set if we are a non-main
		// source set
		Set<ProjectAndSourceSet> dependencies = new LinkedHashSet<>();
		if (!sourceSet.equals(SourceSet.MAIN_SOURCE_SET_NAME))
			dependencies.add(new ProjectAndSourceSet(project,
				SourceSet.MAIN_SOURCE_SET_NAME));
			
		// Find dependencies based on their inclusion
		for (String configGroup : Arrays.<String>asList(
			"api", "implementation"))
		{
			// What this configuration is called?
			String configurationName = TaskInitialization.task("",
				this.sourceSet, configGroup);
			
			// The configuration might not even exist
			Configuration buildConfig = project.getConfigurations()
				.findByName(configurationName);
			if (buildConfig == null)
				continue;
			
			// Add all project based dependencies
			for (Dependency dependency : buildConfig.getDependencies())
				if (dependency instanceof ProjectDependency)
				{
					ProjectDependency mod = (ProjectDependency)dependency;
					
					dependencies.add(new ProjectAndSourceSet(mod));
				}
		}
		
		// Put in standard required dependencies
		int[] normalDep = new int[]{1};
		for (ProjectAndSourceSet depend : dependencies)
			AdditionalManifestPropertiesTaskAction.__addDependency(__task,
				false, depend, normalDep,
				attributes, this.sourceSet);
		
		// Add any optional dependencies now, which may or may not exist
		for (Project depend : VMHelpers.optionalDepends(project, sourceSet))
			AdditionalManifestPropertiesTaskAction.__addDependency(__task,
				true,
				new ProjectAndSourceSet(depend,
					SourceSet.MAIN_SOURCE_SET_NAME), normalDep, attributes,
				this.sourceSet);
		
		// Write the manifest output
		try (OutputStream out = Files.newOutputStream(this.taskOutput,
			StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING,
			StandardOpenOption.WRITE))
		{
			javaManifest.write(out);
			
			// Make sure it is really written!
			out.flush();
		}
		catch (IOException e)
		{
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Turns an iterable into a string with the given delimiter.
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
			// Add decimeter?
			if (sb.length() > 0)
				sb.append(__delim);
			
			// Add object
			sb.append(o);
		}
		
		return sb.toString();
	}
	
	/**
	 * Adds the given configuration to the configuration list.
	 * 
	 * @param __attributes The attributes to put into.
	 * @param __config The configuration to add.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/08/09
	 */
	private static void __addConfiguration(Attributes __attributes,
		JavaMEConfiguration __config)
		throws NullPointerException
	{
		if (__attributes == null || __config == null)
			throw new NullPointerException("NARG");
		
		// Do nothing if the configuration is older or the same as we always
		// want the best
		String existing =
			__attributes.getValue("Microedition-Configuration"); 
		if (existing != null &&
			__config.compareTo(new JavaMEConfiguration(existing)) <= 0)
			return;
		
		// Store it
		__attributes.putValue("Microedition-Configuration",
			__config.toString());
	}
	
	/**
	 * Adds a single dependency to a given project.
	 * 
	 * @param __task The current task?
	 * @param __isOptional Is this an optional dependency?
	 * @param __dependency The dependency that this depends on.
	 * @param __depCounter Dependency counter.
	 * @param __attributes The output attributes.
	 * @param __sourceSourceSet The source task source set.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/02/03
	 */
	static void __addDependency(Task __task, boolean __isOptional,
		ProjectAndSourceSet __dependency, int[] __depCounter,
		Attributes __attributes, String __sourceSourceSet)
		throws NullPointerException
	{
		if (__task == null || __dependency == null)
			throw new NullPointerException("NARG");
		
		// Generation really depends on the application type
		Project project = __task.getProject();
		SquirrelJMEPluginConfiguration config =
			SquirrelJMEPluginConfiguration.configuration(project);
		JavaMEMidletType type = config.swmType
			.normalizeViaSourceSet(__sourceSourceSet);
		
		// Which requirement level?
		SuiteDependencyLevel requireLevel = (__isOptional ?
			SuiteDependencyLevel.OPTIONAL : SuiteDependencyLevel.REQUIRED);
		
		// Get the project config
		SquirrelJMEPluginConfiguration subConfig =
			SquirrelJMEPluginConfiguration.configurationOrNull(
				__dependency.dependency);
		if (subConfig == null)
		{
			__task.getLogger().error(String.format(
				"Project %s:%s wants to add %s as a dependency however " +
				"it is not a SquirrelJME module, ignoring.",
				__task.getProject().getPath(),
				__sourceSourceSet, __dependency.dependency.getPath()));
			return;
		}
		
		// The dependency being constructed (which might be added)
		SuiteDependency suiteDependency = null;
		boolean didDepend = false;
		boolean forceDepend = false;
		
		// What is the SWM type of the dependency?
		JavaMEMidletType subSwmType = subConfig.swmType
			.normalizeViaSourceSet(__dependency.sourceSet);
		
		// Nothing can depend on a MIDlet!
		if (subSwmType == JavaMEMidletType.APPLICATION)
		{
			// If this is another project, we cannot just depend on it at all
			// Unless we are testing, then we can bend the rules a little
			if (project.compareTo(__dependency.dependency) != 0 &&
				!__sourceSourceSet.equals(SourceSet.TEST_SOURCE_SET_NAME))
				throw new IllegalArgumentException(String.format(
					"Project %s:%s cannot depend on application %s:%s.",
						project.getPath(), __sourceSourceSet,
						__dependency.dependency.getPath(),
						__dependency.sourceSet));
			
			// Fall and do the default project level dependency...
			__task.getLogger().warn(String.format(
				"Project %s:%s is depending on %s:%s via SquirrelJME means.",
					project.getPath(), __sourceSourceSet,
					__dependency.dependency.getPath(),
					__dependency.sourceSet));
			forceDepend = true;
		}
		
		// This is another library
		else if (subSwmType == JavaMEMidletType.LIBRARY)
		{
			suiteDependency = new SuiteDependency(
				SuiteDependencyType.LIBLET,
				requireLevel,
				new SuiteName(VMHelpers.projectSwmNameViaSourceSet(
					__dependency.dependency, __dependency.sourceSet)),
				new SuiteVendor(subConfig.swmVendor),
				SuiteVersionRange.exactly(new SuiteVersion(
					__dependency.dependency.getVersion().toString())));
		}
		
		// Is otherwise an API
		else
		{
			// Configuration specified?
			if (subConfig.definedConfigurations != null &&
				!subConfig.definedConfigurations.isEmpty())
				try
				{
					if (!config.noEmitConfiguration)
					{
						AdditionalManifestPropertiesTaskAction
							.__addConfiguration(__attributes,
							Collections.max(subConfig.definedConfigurations));
						didDepend = true;
					}
				}
				catch (NoSuchElementException e)
				{
					// Ignore
				}
			
			// Profile specified?
			if (subConfig.definedProfiles != null &&
				!subConfig.definedProfiles.isEmpty())
				try
				{
					AdditionalManifestPropertiesTaskAction.__addProfile(
						__attributes,
						Collections.max(subConfig.definedProfiles));
					didDepend = true;
				}
				catch (NoSuchElementException e)
				{
					// Ignore
				}
			
			// Standard specified? Use that reference... but do not use one
			// if in the event there was a configuration or profile used
			if (!didDepend && subConfig.definedStandards != null &&
				!subConfig.definedStandards.isEmpty())
			{
				// Use the best standard
				JavaMEStandard bestStandard = Collections.max(
					subConfig.definedStandards);
				suiteDependency = new SuiteDependency(
					SuiteDependencyType.STANDARD,
					requireLevel,
					bestStandard.name(),
					bestStandard.vendor(),
					(bestStandard.version() != null ?
						SuiteVersionRange.exactly(bestStandard.version()) :
						null));
			}
		}
		
		// Unknown, do a generic project dependency
		if (forceDepend || (!didDepend && suiteDependency == null))
			suiteDependency = new SuiteDependency(
				SuiteDependencyType.PROPRIETARY,
				requireLevel,
				new SuiteName("squirreljme.project@" +
					VMHelpers.projectInternalNameViaSourceSet(
						__dependency.dependency, __dependency.sourceSet)),
				null,
				null);
		
		// Write out the dependency if one was requested
		if (suiteDependency != null)
		{
			__attributes.putValue(type.dependencyKey(__depCounter[0]++),
				suiteDependency.toString());
		}
	}
	
	/**
	 * Adds the given profile to the profile list.
	 * 
	 * @param __attributes The attributes to add into.
	 * @param __profile The profile to add.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/08/09
	 */
	private static void __addProfile(Attributes __attributes,
		JavaMEProfile __profile)
		throws NullPointerException
	{
		if (__attributes == null || __profile == null)
			throw new NullPointerException("NARG");
		
		// Load all the existing profiles
		Set<JavaMEProfile> existing = new LinkedHashSet<>();
		String rawExisting =
			__attributes.getValue("Microedition-Profile");
		if (rawExisting != null)
			existing.addAll(JavaMEProfile.parseProfiles(rawExisting));
		
		// Add to profile set
		existing.add(__profile);
		
		// Store all of them
		__attributes.putValue("Microedition-Profile",
			JavaMEProfile.toString(existing));
	}
}
