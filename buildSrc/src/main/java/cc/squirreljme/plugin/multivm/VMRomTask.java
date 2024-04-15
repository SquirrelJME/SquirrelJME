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
import org.gradle.api.provider.Provider;
import org.gradle.api.tasks.Internal;

/**
 * This task is responsible for compiling a combined ROM, if a VM uses one.
 *
 * @since 2020/08/23
 */
public class VMRomTask
	extends DefaultTask
	implements VMBaseTask, VMExecutableTask
{
	/** The classifier used. */
	@Internal
	@Getter
	public final SourceTargetClassifier classifier;
	
	/**
	 * Initializes the library creation task.
	 * 
	 * @param __classifier The classifier used.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/08/07
	 */
	@Inject
	public VMRomTask(SourceTargetClassifier __classifier)
		throws NullPointerException
	{
		if (__classifier == null)
			throw new NullPointerException("NARG");
		
		this.classifier = __classifier;
		
		// Set details of this task
		this.setGroup("squirreljmeGeneral");
		this.setDescription("Builds the combined ROM.");
		
		// The JAR we are compiling has to be built first
		this.dependsOn(new VMRomDependencies(
			this, __classifier));
		
		// Only execute this task in certain cases
		this.onlyIf(new CheckRomShouldBuild(
			__classifier.getTargetClassifier()));
		
		// The inputs of this tasks are all the ROM files to merge
		this.getInputs().files(new VMRomInputs(
			this, __classifier));
		
		// And the output is a primary single file for the ROM
		this.getOutputs().file(this.outputPath());
		
		// Action for performing the actual linkage of the ROM
		this.doLast(new VMRomTaskAction(__classifier));
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
				.outputRomName(this.classifier.getSourceSet(),
					this.classifier.getBangletVariant())));
	}
}
