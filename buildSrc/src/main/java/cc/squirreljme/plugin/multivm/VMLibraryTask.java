// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.multivm;

import cc.squirreljme.plugin.multivm.ident.SourceTargetClassifier;
import java.nio.file.Path;
import javax.inject.Inject;
import lombok.Getter;
import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.internal.AbstractTask;
import org.gradle.api.provider.Provider;
import org.gradle.api.tasks.Internal;
import org.gradle.api.tasks.bundling.AbstractArchiveTask;
import org.gradle.jvm.tasks.Jar;

/**
 * This task is responsible for creating a library that is used for the task
 * execution.
 *
 * @since 2020/08/07
 */
public class VMLibraryTask
	extends DefaultTask
	implements VMBaseTask, VMExecutableTask
{
	/** The base JAR. */
	@Internal
	@Getter
	public final AbstractTask baseJar;
	
	/** The classifier used. */
	@Internal
	@Getter
	private final SourceTargetClassifier classifier;
	
	/**
	 * Initializes the library creation task.
	 * 
	 * @param __classifier The classifier used.
	 * @param __baseJar The task with the Jar output.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/08/07
	 */
	@Inject
	public VMLibraryTask(SourceTargetClassifier __classifier,
		AbstractTask __baseJar)
		throws NullPointerException
	{
		if (__classifier == null || __baseJar == null)
			throw new NullPointerException("NARG");
		
		// These are used at the build stage
		this.baseJar = __baseJar;
		this.classifier = __classifier;
		
		// Set details of this task
		this.setGroup("squirreljme");
		this.setDescription("Compiles/constructs the library for execution.");
		
		// The JAR we are compiling has to be built first
		// We also need the virtual machine library compiler as well
		this.dependsOn(__baseJar, new VMLibraryTaskDependencies(this,
			__classifier.getTargetClassifier()));
		 
		// Only run if the JAR would run
		this.onlyIf(this::onlyIf);
		
		// The input of this task is the JAR that was created
		this.getInputs().file(this.getProject().provider(() ->
			VMHelpers.onlyFile(__baseJar.getOutputs().getFiles(),
				"jar")));
		
		// The output depends on the task and its source set
		this.getOutputs().files(
			this.getProject().provider(() -> this.__taskOutputFile()));
		this.getOutputs().upToDateWhen(
			new VMLibraryTaskUpToDate(__classifier.getTargetClassifier()));
		
		// Performs the action of the task
		this.doLast(new VMLibraryTaskAction(__classifier));
	}
	
	/**
	 * When should this run?
	 * 
	 * @param __task The task to check.
	 * @return If this should run.
	 * @since 2022/05/20
	 */
	private boolean onlyIf(Task __task)
	{
		return this.baseJar.getOnlyIf().isSatisfiedBy(this.baseJar);
	}
	
	/**
	 * Returns the output path of the archive. 
	 * 
	 * @return The output path.
	 * @since 2020/08/07
	 */
	public final Provider<Path> outputPath()
	{
		return this.getProject().provider(() -> VMHelpers.cacheDir(
			this.getProject(), this.classifier).get()
			.resolve(this.classifier.getVmType()
				.outputLibraryName(this.getProject(),
					this.classifier.getSourceSet())));
	}
	
	/**
	 * Returns the output file for this task.
	 * 
	 * @return The output file for this task.
	 * @since 2022/05/20
	 */
	private Object __taskOutputFile()
	{
		if (this.onlyIf(this))
			return this.outputPath();
		return null;
	}
}
