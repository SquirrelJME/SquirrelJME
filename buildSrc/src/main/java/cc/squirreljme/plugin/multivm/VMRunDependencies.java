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
import java.util.concurrent.Callable;
import org.gradle.api.Project;

/**
 * This is the set of dependencies for {@link VMRunTask} which takes all
 * the dependencies directly needed in order to run the program.
 * 
 * If the virtual machine is {@link VMSpecifier#hasEmulatorJit()} then
 * SpringCoat libraries will be used instead of the ROM libraries for running.
 *
 * @since 2020/08/15
 */
public final class VMRunDependencies
	implements Callable<Iterable<VMLibraryTask>>
{
	/** The project executing under. */
	protected final Project project;
	
	/** The classifier used. */
	protected final SourceTargetClassifier classifier;
	
	/**
	 * Initializes the provider.
	 * 
	 * @param __project The project working under.
	 * @param __classifier The classifier used.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/08/15
	 */
	public VMRunDependencies(Project __project,
		SourceTargetClassifier __classifier)
		throws NullPointerException
	{
		if (__project == null || __classifier == null)
			throw new NullPointerException("NARG");
		
		this.project = __project;
		this.classifier = __classifier;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/08/15
	 */
	@Override
	public final Iterable<VMLibraryTask> call()
	{
		Project project = this.project;
		
		// If this is emulator that is JIT capable, instead for running
		// load it with SpringCoat's library instead
		boolean emuJit = this.classifier.getTargetClassifier().getVmType()
			.hasEmulatorJit();
		if (emuJit)
			return VMHelpers.<VMLibraryTask>resolveProjectTasks(
				VMLibraryTask.class, project,
				VMHelpers.runClassTasks(project,
					this.classifier.withVmByEmulatedJit(), true));
		
		return VMHelpers.<VMLibraryTask>resolveProjectTasks(
			VMLibraryTask.class, project,
			VMHelpers.runClassTasks(project,
				this.classifier, true));
	}
}
