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
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import lombok.Getter;
import org.gradle.api.Action;
import org.gradle.api.internal.tasks.testing.TestFramework;
import org.gradle.api.internal.tasks.testing.WorkerTestClassProcessorFactory;
import org.gradle.api.internal.tasks.testing.detection.TestFrameworkDetector;
import org.gradle.api.tasks.Internal;
import org.gradle.api.tasks.testing.TestFrameworkOptions;
import org.gradle.process.internal.worker.WorkerProcessBuilder;

/**
 * Test framework for SquirrelJME.
 *
 * @since 2022/09/11
 */
public class VMTestFramework
	implements TestFramework
{
	/** The name of this framework. */
	public final String declaredDisplayName =
		"SquirrelJME Test Framework";
	
	/** Is this framework immutable? */
	@Internal
	public final boolean immutable =
		false;
	
	/** Our test task. */
	@Internal
	@Getter
	protected final VMModernTestTask task;
	
	/** The classifier. */
	@Internal
	@Getter
	protected final SourceTargetClassifier classifier;
	
	/**
	 * Initializes the test framework.
	 * 
	 * @param __vmTestTask The test task we are using.
	 * @param __classifier The classifier to use.
	 * @since 2022/09/11
	 */
	@Inject
	public VMTestFramework(VMModernTestTask __vmTestTask,
		SourceTargetClassifier __classifier)
	{
		this.task = __vmTestTask;
		this.classifier = __classifier;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/09/11
	 */
	@Override
	public void close()
		throws IOException
	{
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/09/11
	 */
	@Override
	public TestFrameworkDetector getDetector()
	{
		return new VMTestFrameworkDetector(
			VMHelpers.runningTests(this.task.getProject(),
				this.classifier.getSourceSet()).tests);
	}
	
	/**
	 * Is this an immutable test framework?
	 * 
	 * @return If this is immutable.
	 * @since 2022/09/11
	 */
	public boolean getImmutable()
	{
		return this.immutable;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/09/11
	 */
	@Override
	public TestFrameworkOptions getOptions()
	{
		return new TestFrameworkOptions();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/09/11
	 */
	@Override
	public WorkerTestClassProcessorFactory getProcessorFactory()
	{
		// Get the tests we are going to run, since we need to calculate how
		// to run the test, and everything must be serialized through it
		Map<String, CandidateTestFiles> tests = VMHelpers.runningTests(
			this.task.getProject(), this.classifier.getSourceSet()).tests;
		
		// Determine setup for general suite runs
		SuiteRunParameters runSuite = VMTestTaskAction.runSuite(this.task,
			this.classifier);
		
		// Calculate run parameters for each test
		Map<String, TestRunParameters> runParams = new LinkedHashMap<>();
		for (Map.Entry<String, CandidateTestFiles> test : tests.entrySet())
			runParams.put(test.getKey(), VMTestTaskAction.runTest(this.task,
				this.classifier, runSuite, test.getKey(),
				test.getValue()));
		
		// Run the processor, this must be serializable
		return new VMTestFrameworkWorkerTestClassProcessorFactory(
			tests, runParams, this.task.getProject().getName());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/09/11
	 */
	@Override
	public Action<WorkerProcessBuilder> getWorkerConfigurationAction()
	{
		return new VMTestFrameworkWorkerConfigurationAction();
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/09/11
	 */
	@Override
	public List<String> getTestWorkerImplementationModules()
	{
		// No modules are used
		return Collections.emptyList();
	}
}
