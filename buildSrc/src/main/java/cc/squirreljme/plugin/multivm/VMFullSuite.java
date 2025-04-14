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
 * Task for running the full-suite of SquirrelJME.
 *
 * @since 2020/10/17
 */
public class VMFullSuite
	extends DefaultTask
	implements VMBaseTask, VMExecutableTask
{
	/** The source target classifier. */
	@Internal
	@Getter
	protected final SourceTargetClassifier classifier;
	
	/**
	 * Initializes the full suite task.
	 * 
	 * @param __classifier The target classifier used.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/10/17
	 */
	@Inject
	public VMFullSuite(SourceTargetClassifier __classifier)
		throws NullPointerException
	{
		if (__classifier == null)
			throw new NullPointerException("NARG");
		
		this.classifier = __classifier;
		
		// Runs the entire API/Library suite of SquirrelJME to run a given
		// application
		this.setGroup("squirreljmeGeneral");
		this.setDescription("Runs the full suite of SquirrelJME Modules.");
		
		// This always runs, no matter what
		this.onlyIf(new AlwaysTrue());
		this.getOutputs().upToDateWhen(new AlwaysFalse());
		
		// This depends on everything!
		this.dependsOn(new VMFullSuiteDepends(this, __classifier),
			new VMEmulatorDependencies(this.getProject(),
				__classifier.getTargetClassifier()));
		
		// Actual running of everything
		this.doLast(new VMFullSuiteTaskAction(__classifier));
	}
}
