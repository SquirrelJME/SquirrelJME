// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.multivm;

import cc.squirreljme.plugin.SquirrelJMEPluginConfiguration;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import javax.inject.Inject;
import lombok.Getter;
import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.provider.Provider;
import org.gradle.api.tasks.Internal;
import org.gradle.api.tasks.SourceSet;
import org.gradle.jvm.tasks.Jar;

/**
 * Performs compaction of the library so that it does not have debug
 * information and is smaller.
 *
 * @since 2023/02/01
 */
public class VMCompactLibraryTask
	extends DefaultTask
{
	/** The base JAR. */
	@Internal
	@Getter
	public final Jar baseJar;
	
	/** The source set used. */
	@Internal
	@Getter
	private final String sourceSet;
	
	/**
	 * Initializes the compacting task.
	 * 
	 * @param __sourceSet The source set this belongs to.
	 * @param __baseJar The original source Jar.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/02/01
	 */
	@Inject
	public VMCompactLibraryTask(String __sourceSet,
		Jar __baseJar)
		throws NullPointerException
	{
		if (__sourceSet == null || __baseJar == null)
			throw new NullPointerException("NARG");
		
		Project project = this.getProject();
		
		this.baseJar = __baseJar;
		this.sourceSet = __sourceSet;
		
		// Set details of this task
		this.setGroup("squirreljme");
		this.setDescription(
			"Compacts the library and removes debugging info.");
		
		// Depends on the base Jar but also depends on all the
		// compactLibs for all dependencies
		this.dependsOn(__baseJar, this.getProject().provider(() ->
			VMHelpers.compactLibTaskDepends(this.getProject(),
				this.sourceSet)));
		
		// Only run if the source JAR would run
		this.onlyIf(this::onlyIf);
		
		// The input of this is the original Jar and any Jars and maps that are
		// needed by ProGuard
		this.getInputs().files(this.inputBaseJarPath(),
			this.getProject().provider(() -> {
				Collection<Object> result = new LinkedHashSet<>();
				for (VMCompactLibraryTask compactTask :
						VMHelpers.compactLibTaskDepends(this.getProject(),
							this.sourceSet))
				{
					// If the input Jar changes, we want to update this one
					result.add(compactTask.inputBaseJarPath());
					
					// If the output Jar changes, we want that one as well
					// since the mappings will be different
					result.add(compactTask.outputJarPath());
				}
				
				return result;
			}));
		
		// Inputs are the proguard options, so if this changes we need to
		// do a rebuild!
		this.getInputs().property("squirreljme.proguardOptions",
			this.getProject().provider(() -> Objects.toString(
				VMCompactLibraryTask.__optionsBySourceSet(
					this.getProject(), __sourceSet).get())));
		
		// Error code that is used for the prefix
		this.getInputs().property("squirreljme.javaDocErrorCode",
			this.getProject().provider(() -> Objects.toString(
				SquirrelJMEPluginConfiguration.configuration(project)
					.javaDocErrorCode)));
		
		// Also include the built-in plugin options, in case those change as
		// well!
		this.getInputs().property("squirreljme.proguardOptionsDefault",
			this.getProject().provider(() ->
				Arrays.toString(VMCompactLibraryTaskAction._OPTIMIZATIONS) +
				Arrays.toString(VMCompactLibraryTaskAction._PARSE_SETTINGS) +
				Arrays.toString(VMCompactLibraryTaskAction._TEST_SETTINGS)));
		
		// The output of this JAR is just where it should be placed, this
		// includes the mapping file for incremental mapping
		this.getOutputs().files(this.outputJarPath(), this.outputMapPath());
		
		// Performs the action of the task
		this.doLast(new VMCompactLibraryTaskAction(__sourceSet));
	}
	
	/**
	 * When should this run?
	 * 
	 * @param __task The task to check.
	 * @return If this should run.
	 * @since 2023/02/01
	 */
	private boolean onlyIf(Task __task)
	{
		return this.baseJar.getOnlyIf().isSatisfiedBy(this.baseJar);
	}
	
	/**
	 * Returns the input Jar that will get transformed accordingly.
	 * 
	 * @return The input Jar that we will be transforming.
	 * @since 2023/02/05
	 */
	public final Provider<Path> inputBaseJarPath()
	{
		return this.getProject().provider(() -> this.baseJar.getOutputs()
			.getFiles().getSingleFile().toPath());
	}
	
	/**
	 * Returns the output path of the archive. 
	 * 
	 * @return The output path.
	 * @since 2023/02/01
	 */
	public final Provider<Path> outputJarPath()
	{
		return this.getProject().provider(() ->
			this.getProject().getBuildDir().toPath()
				.resolve("squirreljme").resolve("obfuscated")
				.resolve(this.baseJar.getOutputs().getFiles()
					.getSingleFile().toPath().getFileName()));
	}
	
	/**
	 * Returns the output path of the mapping file.
	 * 
	 * @return The output path of the mapping file.
	 * @since 2023/02/05
	 */
	public final Provider<Path> outputMapPath()
	{
		return this.getProject().provider(() ->
			{
				Path jarOut = this.outputJarPath().get();
				
				return jarOut.resolveSibling(
					VMHelpers.getBaseName(jarOut) + ".map");
			});
	}
	
	/**
	 * Returns the options to use for the given source set.
	 * 
	 * @param __project The project to get from.
	 * @param __sourceSet The source set to use.
	 * @return The provider to the ProGuard options for the source set.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/02/11
	 */
	static Provider<List<String>> __optionsBySourceSet(Project __project,
		String __sourceSet)
		throws NullPointerException
	{
		if (__project == null || __sourceSet == null)
			throw new NullPointerException("NARG");
		
		return __project.provider(() ->
			{
				SquirrelJMEPluginConfiguration projectConfig =
					SquirrelJMEPluginConfiguration.configuration(__project);
				
				switch (__sourceSet)
				{
						// Main sources
					case SourceSet.MAIN_SOURCE_SET_NAME:
						return projectConfig.proGuardOptions;
						
						// Test sources
					case SourceSet.TEST_SOURCE_SET_NAME:
						return projectConfig.proGuardOptionsTest;
					
						// Test fixtures
					case VMHelpers.TEST_FIXTURES_SOURCE_SET_NAME:
						return projectConfig.proGuardOptionsTestFixtures;
				}
				
				return Collections.emptyList();
			});
	}
}
