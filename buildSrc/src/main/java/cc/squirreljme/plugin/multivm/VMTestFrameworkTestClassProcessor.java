// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.multivm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.gradle.api.internal.tasks.testing.DefaultTestDescriptor;
import org.gradle.api.internal.tasks.testing.DefaultTestMethodDescriptor;
import org.gradle.api.internal.tasks.testing.DefaultTestOutputEvent;
import org.gradle.api.internal.tasks.testing.DefaultTestSuiteDescriptor;
import org.gradle.api.internal.tasks.testing.TestClassProcessor;
import org.gradle.api.internal.tasks.testing.TestClassRunInfo;
import org.gradle.api.internal.tasks.testing.TestCompleteEvent;
import org.gradle.api.internal.tasks.testing.TestResultProcessor;
import org.gradle.api.internal.tasks.testing.TestStartEvent;
import org.gradle.api.tasks.testing.TestOutputEvent;
import org.gradle.api.tasks.testing.TestResult;
import org.gradle.internal.id.IdGenerator;

/**
 * Processor for test classes.
 *
 * @since 2022/09/11
 */
public class VMTestFrameworkTestClassProcessor
	implements TestClassProcessor
{
	/** Tests to run. */
	protected final List<String> runTests =
		new ArrayList<>();
	
	/** The tests that are available. */
	protected final Map<String, CandidateTestFiles> availableTests;
	
	/** The ID generator to use. */
	protected final IdGenerator<?> idGenerator;
	
	/** The name of this project. */
	protected final String projectName;
	
	/** Test result output. */
	private volatile TestResultProcessor _resultProcessor;
	
	/** The thread that is running tests. */
	private volatile Thread _runningThread;
	
	/** Stop running tests? */
	private volatile boolean _stopNow;
	
	/**
	 * Initializes the processor.
	 *
	 * @param __availableTests The tests that are available.
	 * @param __projectName The name of the project.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/09/11
	 */
	public VMTestFrameworkTestClassProcessor(
		Map<String, CandidateTestFiles> __availableTests,
		IdGenerator<?> __idGenerator, String __projectName)
		throws NullPointerException
	{
		if (__availableTests == null || __idGenerator == null ||
			__projectName == null)
			throw new NullPointerException("NARG");
		
		this.availableTests = __availableTests;
		this.idGenerator = __idGenerator;
		this.projectName = __projectName;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/09/11
	 */
	@Override
	public void startProcessing(TestResultProcessor __resultProcessor)
	{
		// Store this for later
		this._resultProcessor = __resultProcessor;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/09/11
	 */
	@Override
	public void processTestClass(TestClassRunInfo __testClass)
	{
		// Remember class for later
		this.runTests.add(__testClass.getTestClassName());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/09/11
	 */
	@Override
	public void stop()
	{
		// Remember thread for later stop
		synchronized (this)
		{
			this._runningThread = Thread.currentThread();
		}
		
		// Stop is a bit of a misnomer, it means stop processing and then run
		// all the tests...
		Map<String, CandidateTestFiles> availableTests = this.availableTests;
		TestResultProcessor resultProcessor = this._resultProcessor;
		
		// Suite for the entire project group
		IdGenerator<?> idGenerator = this.idGenerator;
		DefaultTestSuiteDescriptor suiteDesc =
			new DefaultTestSuiteDescriptor(
				idGenerator.generateId(),
				this.projectName);
		
		// Suite has started
		resultProcessor.started(suiteDesc,
			new TestStartEvent(System.currentTimeMillis()));
		
		// Go through and actually run all the tests
		TestResult.ResultType finalResult = TestResult.ResultType.SKIPPED;
		for (String testName : this.runTests)
		{
			// Check to see if we are stopping testing
			synchronized (this)
			{
				System.err.printf(">> Stopping now!%n");
				
				// If we are forcing a stop, mark as failure
				if (this._stopNow)
				{
					finalResult = TestResult.ResultType.FAILURE;
					break;
				}
			}
			
			System.err.printf(">> TEST: %s%n", testName);
			System.err.flush();
			
			// Start test
			DefaultTestDescriptor desc = new DefaultTestMethodDescriptor(
				idGenerator.generateId(), testName, testName);
			resultProcessor.started(desc,
				new TestStartEvent(System.currentTimeMillis(),
					suiteDesc.getId()));
			
			System.err.printf(">> RUN: %s%n", testName);
			System.err.flush();
			
			// Test output
			resultProcessor.output(desc.getId(),
				new DefaultTestOutputEvent(TestOutputEvent.Destination.StdOut, 
					"Boop"));
			
			// How did this test go?
			TestResult.ResultType testResult = TestResult.ResultType.FAILURE;
			
			// Change the global suite test result here
			switch (testResult)
			{
					// If current state is skipped, then make success
				case SUCCESS:
					if (finalResult == TestResult.ResultType.SKIPPED)
						finalResult = testResult;
					break;
					
					// Otherwise, always mark failure
				case FAILURE:
					finalResult = testResult;
					break;
			}
			
			// Just say it failed for now
			resultProcessor.completed(desc.getId(),
				new TestCompleteEvent(System.currentTimeMillis(),
					testResult));
		}
		
		// If failed, emit a throwable
		if (finalResult == TestResult.ResultType.FAILURE)
			resultProcessor.failure(suiteDesc.getId(),
				new Throwable("Tests failed."));
		
		// Use the final result from all the test runs
		resultProcessor.completed(suiteDesc.getId(),
			new TestCompleteEvent(System.currentTimeMillis(), finalResult));
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/09/11
	 */
	@Override
	public void stopNow()
	{
		// Signal that tests should stop
		synchronized (this)
		{
			// Interrupt thread quickly
			Thread runningThread = this._runningThread;
			if (runningThread != null)
				runningThread.interrupt();
			
			// Do stop now
			this._stopNow = true;
			
			// Make sure to notify on all monitors
			this.notifyAll();
		}
	}
}
