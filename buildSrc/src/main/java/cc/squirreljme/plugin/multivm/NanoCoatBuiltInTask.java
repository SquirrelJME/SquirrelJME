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
import java.nio.file.Files;
import java.nio.file.Path;
import javax.inject.Inject;
import lombok.Getter;
import org.gradle.api.DefaultTask;
import org.gradle.api.provider.Provider;

/**
 * This task is used to generate a built-in RatufaCoat Rom.
 *
 * @since 2023/05/31
 */
public class NanoCoatBuiltInTask
	extends DefaultTask
	implements VMBaseTask
{
	/** The classifier used. */
	@Getter
	private final SourceTargetClassifier classifier;
	
	/**
	 * Initializes the NanoCoat built-in setup.
	 * 
	 * @param __classifier The classifier used.
	 * @param __romTask The task used to create the ROM.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/05/31
	 */
	@Inject
	public NanoCoatBuiltInTask(SourceTargetClassifier __classifier, 
		VMRomTask __romTask)
		throws NullPointerException
	{
		if (__classifier == null || __romTask == null)
			throw new NullPointerException("NARG");
		
		this.classifier = __classifier;
		
		// Set details of this task
		this.setGroup("squirreljmeGeneral");
		this.setDescription("Copies the ROM to NanoCoat.");
		
		// Depends on the ROM being built
		this.dependsOn(__romTask);
		
		// The inputs are the ROM task's output
		this.getInputs().file(this.inputPath(__romTask));
		
		// And the output is a primary single file for the ROM
		this.getOutputs().dirs(this.specificPath(),
			this.sharedPath());
		
		// Actual running of everything
		this.doFirst(new NanoCoatBuiltInCleanTaskAction(__classifier,
			this.romBasePath(), this.specificPath(), this.sharedPath()));
		this.doLast(new NanoCoatBuiltInTaskAction(__classifier));
	}
	
	/**
	 * Returns the input path to use for this task.
	 * 
	 * @param __romTask The ROM task.
	 * @return The output of the task, used for input.
	 * @since 2023/05/31
	 */
	private Provider<Path> inputPath(VMRomTask __romTask)
	{
		return this.getProject().provider(() -> __romTask.getOutputs()
			.getFiles().getSingleFile().toPath());
	}
	
	/**
	 * Returns the base ROM path.
	 *
	 * @return The base ROM path.
	 * @since 2023/09/03
	 */
	public final Provider<Path> romBasePath()
	{
		return this.getProject().provider(() -> this.getProject().getRootDir()
			.toPath().resolve("nanocoat").resolve("rom"));
	}
	
	/**
	 * Returns the shared output path.
	 *
	 * @return The shared output path.
	 * @since 2023/09/03
	 */
	public final Provider<Path> sharedPath()
	{
		return this.getProject().provider(() ->
			this.romBasePath().get().resolve("shared"));
	}
	
	/**
	 * Returns the shared specific path.
	 *
	 * @return The shared specific path.
	 * @since 2023/09/03
	 */
	public final Provider<Path> specificPath()
	{
		return this.getProject().provider(() ->
			this.romBasePath().get().resolve("specific").
				resolve(String.format("%s_%s", this.classifier.getSourceSet(),
					this.classifier.getTargetClassifier().getClutterLevel())));
	}
}
