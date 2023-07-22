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
import java.io.File;
import java.nio.file.Paths;
import javax.inject.Inject;
import lombok.Getter;
import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.provider.Provider;
import org.gradle.api.tasks.Internal;
import org.gradle.jvm.tasks.Jar;

/**
 * This is used to dump the output compilation result of a library.
 *
 * @since 2021/05/16
 */
public class VMDumpLibraryTask
	extends DefaultTask
	implements VMExecutableTask
{
	/** The classifier used. */
	@Internal
	@Getter
	public final SourceTargetClassifier classifier;
	
	/**
	 * Initializes the library dumping task.
	 * 
	 * @param __classifier The source set used.
	 * @param __libTask The library task.
	 * @throws NullPointerException On null arguments.
	 * @since 2021/05/16
	 */
	@Inject
	public VMDumpLibraryTask(SourceTargetClassifier __classifier,
		VMLibraryTask __libTask)
		throws NullPointerException
	{
		if (__classifier == null || __libTask == null)
			throw new NullPointerException("NARG");
			
		Project project = this.getProject();
		Jar baseJar = VMHelpers.jarTask(project, __classifier.getSourceSet());
		
		// These are used at the build stage
		this.classifier = __classifier;
		
		// Set details of this task
		this.setGroup("squirreljme");
		this.setDescription("Dumps the compiled library for debugging.");
		
		// We need to build the library before we can dump it
		this.dependsOn(baseJar,
			__libTask);
			
		// The input is the output of the library task, which is a glob
		Provider<File> file = this.getProject().provider(
			() -> __libTask.getOutputs().getFiles().getSingleFile());
		this.getInputs().file(file);
		
		// The output depends on the task and its source set
		this.getOutputs().file(this.getProject().provider(
			() -> Paths.get(file.get() + ".yml").toFile()));
		this.getOutputs().upToDateWhen(new VMLibraryTaskUpToDate(
			__classifier.getTargetClassifier()));
		
		// Performs the action of the task
		this.doLast(new VMDumpLibraryTaskAction(__classifier));
	}
}
