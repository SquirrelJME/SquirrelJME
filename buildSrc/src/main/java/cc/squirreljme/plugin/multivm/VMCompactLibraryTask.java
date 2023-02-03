// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.multivm;

import cc.squirreljme.plugin.multivm.ident.SourceTargetClassifier;
import java.nio.file.Path;
import javax.inject.Inject;
import lombok.Getter;
import org.gradle.api.DefaultTask;
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
		
		this.baseJar = __baseJar;
		this.sourceSet = __sourceSet;
		
		// Set details of this task
		this.setGroup("squirreljme");
		this.setDescription("Compacts the library and removes debugging.");
		
		// The base Jar has to be done first
		this.dependsOn(__baseJar);
		
		// Only run if the source JAR would run
		this.onlyIf(this::onlyIf);
		
		// The input of this is the original Jar
		this.getInputs().file(__baseJar.getOutputs()
			.getFiles().getSingleFile());
		
		// The output of this JAR is just where it should be placed
		this.getOutputs().files(
			this.getProject().provider(() -> this.outputPath()));
		/*this.getOutputs().upToDateWhen(
			new VMLibraryTaskUpToDate(__classifier.getTargetClassifier()));*/
		
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
				.resolve(String.format("%s.%s.jar",
					this.getProject().getName(),
					this.sourceSet)));
	}
}
