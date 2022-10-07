// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.multivm;

import java.nio.file.Path;
import javax.inject.Inject;
import lombok.Getter;
import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.provider.Provider;
import org.gradle.api.tasks.Internal;
import org.gradle.jvm.tasks.Jar;

/**
 * This task is responsible for creating a library that is used for the task
 * execution.
 *
 * @since 2020/08/07
 */
public class VMLibraryTask
	extends DefaultTask
	implements VMExecutableTask
{
	/** The source set used. */
	@Internal
	@Getter
	public final String sourceSet;
	
	/** The virtual machine type. */
	@Internal
	@Getter
	public final VMSpecifier vmType;
	
	/** The base JAR. */
	@Internal
	@Getter
	public final Jar baseJar;
	
	/**
	 * Initializes the library creation task.
	 * 
	 * @param __sourceSet The source set used.
	 * @param __vmType The virtual machine type.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/08/07
	 */
	@Inject
	public VMLibraryTask(String __sourceSet,
		VMSpecifier __vmType)
		throws NullPointerException
	{
		if (__sourceSet == null || __vmType == null)
			throw new NullPointerException("NARG");
			
		Project project = this.getProject();
		Jar baseJar = VMHelpers.jarTask(project, __sourceSet);
		this.baseJar = baseJar;
		
		// These are used at the build stage
		this.sourceSet = __sourceSet;
		this.vmType = __vmType;
		
		// Set details of this task
		this.setGroup("squirreljme");
		this.setDescription("Compiles/constructs the library for execution.");
		
		// The JAR we are compiling has to be built first
		this.dependsOn(baseJar,
			new VMLibraryTaskDependencies(this, this.vmType));
		
		// Only run if the JAR would run
		this.onlyIf(this::onlyIf);
		
		// The input of this task is the JAR that was created
		this.getInputs().file(baseJar.getArchiveFile());
		
		// The output depends on the task and its source set
		this.getOutputs().files(
			this.getProject().provider(() -> this.__taskOutputFile()));
		this.getOutputs().upToDateWhen(
			new VMLibraryTaskUpToDate(this.vmType));
		
		// Performs the action of the task
		this.doLast(new VMLibraryTaskAction(__sourceSet, __vmType));
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
			this.getProject(), this.vmType, this.sourceSet).get()
			.resolve(this.vmType.outputLibraryName(this.getProject(),
			this.sourceSet)));
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
