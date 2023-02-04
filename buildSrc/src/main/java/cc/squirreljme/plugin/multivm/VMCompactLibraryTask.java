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
import cc.squirreljme.plugin.multivm.ident.SourceTargetClassifier;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Objects;
import javax.inject.Inject;
import lombok.Getter;
import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.provider.Provider;
import org.gradle.api.tasks.Internal;
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
		this.setDescription("Compacts the library and removes debugging.");
		
		// Depends on the base Jar but also depends on all the
		// compactLibs for all dependencies
		this.dependsOn(__baseJar, this.getProject().provider(() ->
			VMHelpers.compactLibTaskDepends(this.getProject(),
				this.sourceSet)));
		
		// Only run if the source JAR would run
		this.onlyIf(this::onlyIf);
		
		// The input of this is the original Jar
		this.getInputs().file(__baseJar.getOutputs()
			.getFiles().getSingleFile());
		
		// Inputs are the proguard options, so if this changes we need to
		// do a rebuild!
		this.getInputs().property("squirreljme.proguardOptions",
			this.getProject().provider(() -> Objects.toString(
				SquirrelJMEPluginConfiguration.configuration(project)
					.proGuardOptions)));
		
		// Remember if the shrinking option has changed
		this.getInputs().property("squirreljme.noShrinking",
			this.getProject().provider(() -> Boolean.toString(
				SquirrelJMEPluginConfiguration.configuration(project)
					.noShrinking)));
		
		// Also include the built-in plugin options, in case those change as
		// well!
		this.getInputs().property("squirreljme.proguardOptionsDefault",
			this.getProject().provider(() -> Arrays.toString(
				VMCompactLibraryTaskAction._PARSE_SETTINGS)));
		
		// The output of this JAR is just where it should be placed
		this.getOutputs().files(
			this.getProject().provider(() -> this.outputPath()));
		
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
	 * Returns the output path of the archive. 
	 * 
	 * @return The output path.
	 * @since 2023/02/01
	 */
	public final Provider<Path> outputPath()
	{
		return this.getProject().provider(() ->
			this.getProject().getBuildDir().toPath()
				.resolve("squirreljme").resolve("compact")
				.resolve(this.baseJar.getOutputs().getFiles()
					.getSingleFile().toPath().getFileName()));
	}
}
