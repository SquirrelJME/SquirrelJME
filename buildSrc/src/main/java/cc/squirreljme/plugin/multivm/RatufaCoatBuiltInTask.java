// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
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
import org.gradle.api.provider.Provider;

/**
 * This task is used to generate a built-in RatufaCoat ROm.
 *
 * @since 2021/02/25
 */
public class RatufaCoatBuiltInTask
	extends DefaultTask
	implements VMBaseTask
{
	/** The classifier used. */
	@Getter
	private final SourceTargetClassifier classifier;
	
	/**
	 * Initializes the full suite task.
	 * 
	 * @param __classifier The classifier used.
	 * @param __romTask The task used to create the ROM.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/02/25
	 */
	@Inject
	public RatufaCoatBuiltInTask(SourceTargetClassifier __classifier, 
		VMRomTask __romTask)
		throws NullPointerException
	{
		if (__classifier == null || __romTask == null)
			throw new NullPointerException("NARG");
		
		this.classifier = __classifier;
		
		// Set details of this task
		this.setGroup("squirreljmeGeneral");
		this.setDescription("Builds the combined ROM.");
		
		// Depends on the ROM being built
		this.dependsOn(__romTask);
		
		// The inputs are the ROM task's output
		this.getInputs().file(this.inputPath(__romTask));
		
		// And the output is a primary single file for the ROM
		this.getOutputs().file(this.outputPath());
		
		// Actual running of everything
		this.doLast(new RatufaCoatBuiltInTaskAction(__classifier));
	}
	
	/**
	 * Returns the input path to use for this task.
	 * 
	 * @param __romTask The ROM task.
	 * @return The output of the task, used for input.
	 * @since 2021/02/25
	 */
	private Provider<Path> inputPath(VMRomTask __romTask)
	{
		return this.getProject().provider(() -> __romTask.getOutputs()
			.getFiles().getSingleFile().toPath());
	}
	
	/**
	 * Returns the output path of the C file. 
	 * 
	 * @return The output path.
	 * @since 2021/02/25
	 */
	public final Provider<Path> outputPath()
	{
		return this.getProject().provider(() -> this.getProject().getRootDir()
			.toPath().resolve("ratufacoat").resolve("build")
			.resolve(TaskInitialization.task(
				"builtin", this.classifier) + ".c"));
	}
}
