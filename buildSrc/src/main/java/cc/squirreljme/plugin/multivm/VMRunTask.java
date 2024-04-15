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
import javax.inject.Inject;
import lombok.Getter;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.Internal;

/**
 * Used to run the virtual machine.
 *
 * @since 2020/08/07
 */
public class VMRunTask
	extends DefaultTask
	implements VMBaseTask, VMExecutableTask
{
	/** The classifier used. */
	@Internal
	@Getter
	protected final SourceTargetClassifier classifier;
	
	/**
	 * Initializes the task.
	 * 
	 * @param __classifier The classifier used.
	 * @param __libTask The task used to create libraries, this may be directly
	 * depended upon.
	 * @since 2020/08/07
	 */
	@Inject
	public VMRunTask(SourceTargetClassifier __classifier,
		VMLibraryTask __libTask)
		throws NullPointerException
	{
		if (__classifier == null || __libTask == null)
			throw new NullPointerException("NARG");
		
		// These are used when running
		this.classifier = __classifier;
		
		// Set details of this task
		this.setGroup("squirreljme");
		this.setDescription("Executes the program to start running it.");
		
		// This task depends on the various VM libraries of this class
		// depending on the dependencies along with the emulator being
		// available as well
		this.dependsOn(this.getProject().provider(
			new VMRunDependencies(this, __classifier)),
			new VMEmulatorDependencies(this,
				__classifier.getTargetClassifier()));
		
		// Only run if entry points are valid
		this.onlyIf(new CheckForEntryPoints());
		
		// Performs the action of the task
		this.doLast(new VMRunTaskAction(__classifier));
	}
}
