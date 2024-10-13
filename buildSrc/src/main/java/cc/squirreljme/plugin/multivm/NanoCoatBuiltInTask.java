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
import java.util.concurrent.Callable;
import javax.inject.Inject;
import lombok.Getter;
import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.api.provider.Provider;

/**
 * This task is used to generate a built-in RatufaCoat Rom.
 *
 * @since 2023/05/31
 */
public class NanoCoatBuiltInTask
	extends DefaultTask
	implements VMBuiltInTask
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
		NanoCoatBuiltInTaskInput input = new NanoCoatBuiltInTaskInput(
			__classifier, __romTask);
		this.getInputs().file(input);
		
		// And the output is a primary single file for the ROM and its source
		Provider<Path> romBase = NanoCoatBuiltInTask.romBasePath(this);
		NanoCoatBuiltInTaskOutput outJar = new NanoCoatBuiltInTaskOutput(
			__classifier, romBase, false, false);
		NanoCoatBuiltInTaskOutput outSrc = new NanoCoatBuiltInTaskOutput(
			__classifier, romBase, true, false);
		this.getOutputs().files(outJar, outSrc);
		
		// Tests?
		NanoCoatBuiltInTaskOutput outTest;
		if (!__classifier.isTestSourceSet())
			outTest = null;
		else
		{
			outTest = new NanoCoatBuiltInTaskOutput(
				__classifier, romBase, false, true);
			this.getOutputs().files(outTest);
		}
		
		// Actual running of everything
		this.doLast(new NanoCoatBuiltInTaskAction(input, outJar, outSrc,
			outTest));
	}
	
	/**
	 * Returns the base ROM path.
	 *
	 * @param __any Any task.
	 * @return The base ROM path.
	 * @throws NullPointerException On null arguments.
	 * @since 2023/09/03
	 */
	public static Provider<Path> romBasePath(Task __any)
		throws NullPointerException
	{
		if (__any == null)
			throw new NullPointerException("NARG");
		
		return __any.getProject().provider(() ->
			__any.getProject().getRootDir().toPath()
				.resolve("nanocoat").resolve("rom"));
	}
}
