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
import cc.squirreljme.plugin.util.SingleTaskOutputFile;
import javax.inject.Inject;
import lombok.Getter;
import org.gradle.api.DefaultTask;
import org.gradle.api.tasks.Internal;
import org.gradle.workers.WorkerExecutor;

/**
 * Used to test the virtual machine, generates test results from the run.
 *
 * @since 2020/08/07
 */
public class VMLegacyTestTask
	extends DefaultTask
	implements VMBaseTask, VMExecutableTask
{
	/** Property for running single test. */
	public static final String SINGLE_TEST_PROPERTY =
		"test.single";
	
	/** Second property for test. */
	public static final String SINGLE_TEST_PROPERTY_B =
		"single.test";
	
	/** The classifier used. */
	@Internal
	@Getter
	protected final SourceTargetClassifier classifier;
	
	/**
	 * Initializes the task.
	 * 
	 * @param __executor The executor for the task.
	 * @param __classifier The classifier used.
	 * @param __libTask The task used to create libraries, this may be directly
	 * depended upon.
	 * @since 2020/08/07
	 */
	@Inject
	public VMLegacyTestTask(WorkerExecutor __executor,
		SourceTargetClassifier __classifier, VMLibraryTask __libTask)
		throws NullPointerException
	{
		if (__executor == null || __classifier == null || __libTask == null)
			throw new NullPointerException("NARG");
			
		// These are used at the test stage
		this.classifier = __classifier;
		
		// Set details of this task
		this.setGroup("squirreljme");
		this.setDescription("Runs the various automated tests (legacy).");
		
		// Depends on the library to exist first along with the emulator
		// itself
		this.dependsOn(this.getProject().provider(
			new VMRunDependencies(this, __classifier)),
			new VMEmulatorDependencies(this,
				__classifier.getTargetClassifier()));
		
		// Add the entire JAR as input, so that if it changes for any reason
		// then all tests should be considered invalid and rerun
		// All the input source files to be tested
		this.getInputs().files(
			this.getProject().provider(
				new SingleTaskOutputFile(__libTask)),
			this.getProject().provider(
				new VMTestInputs(this, __classifier.getSourceSet())));
		
		// All the test results that are created
		this.getOutputs().files(this.getProject().provider(
			new VMTestOutputs(this, __classifier)));
		
		// Add additional testing to see if our test run will not be up-to-
		// date when we run these. Also, this is never up-to-date if
		// test.single/single.test is used because we do not want to
		// interfere with the caching or not running tests in such
		// situations.
		this.getOutputs().upToDateWhen((__task) ->
			{
				return new VMRunUpToDateWhen(__classifier)
						.isSatisfiedBy(__task) &&
					!VMHelpers.runningTests(__task.getProject(),
						__classifier.getSourceSet()).isSingle;
			});
		
		// Only run if there are actual tests to run
		this.onlyIf(new CheckForTests(__classifier.getSourceSet()));
		
		// Performs the action of the task
		this.doLast(new VMTestTaskAction(__executor, __classifier));
	}
}
