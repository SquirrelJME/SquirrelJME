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
import cc.squirreljme.plugin.util.SingleTaskOutputFile;
import javax.inject.Inject;
import lombok.Getter;
import org.gradle.api.tasks.Internal;
import org.gradle.api.tasks.testing.Test;
import org.gradle.api.tasks.testing.TestTaskReports;
import org.gradle.workers.WorkerExecutor;

/**
 * Not Described.
 *
 * @since 2022/09/11
 */
public class VMModernTestTask
	extends Test
	implements VMBaseTask, VMExecutableTask
{
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
	public VMModernTestTask(WorkerExecutor __executor,
		SourceTargetClassifier __classifier, VMLibraryTask __libTask)
		throws NullPointerException
	{
		if (__executor == null || __classifier == null || __libTask == null)
			throw new NullPointerException("NARG");
		
		// These are used at the test stage
		this.classifier = __classifier;
		
		// Set details of this task
		this.setGroup("squirreljme");
		this.setDescription("Runs the various automated tests (modern).");
		
		// Use our custom test framework?
		this.getTestFrameworkProperty().set(
			new VMTestFramework(this, __classifier));
		
		// Depends on the library to exist first along with the emulator
		// itself
		this.dependsOn(this.getProject().provider(
				new VMRunDependencies(this.getProject(), __classifier)),
			new VMEmulatorDependencies(this.getProject(),
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
		
		// Maximum forks, limited accordingly
		this.setMaxParallelForks(VMTestTaskAction.maxParallelTests());
		
		// Change location of JUnit XML reports, to match legacy output
		TestTaskReports reports = this.getReports();
		reports.getJunitXml().getOutputLocation().set(
			VMHelpers.testResultXmlDir(this.getProject(), __classifier)
			.get().toFile());
		
		// Each individual test case has its own output, as is traditional for
		// SquirrelJME. This ends up being easier to read and is much better
		// when there are parameters to tests.
		reports.getJunitXml().setOutputPerTestCase(true);
		
		// Always show streams
		this.getTestLogging().setShowStandardStreams(true);
		
		// For extra debugging
		this.getTestLogging().setShowExceptions(true);
		this.getTestLogging().setShowStackTraces(true);
		this.getTestLogging().setShowCauses(true);
	}
}
