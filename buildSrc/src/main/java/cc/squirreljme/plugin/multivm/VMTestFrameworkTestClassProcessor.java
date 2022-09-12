// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.multivm;

import cc.squirreljme.plugin.util.TestResultOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import org.gradle.api.internal.tasks.testing.DefaultTestDescriptor;
import org.gradle.api.internal.tasks.testing.DefaultTestMethodDescriptor;
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
	
	/** The run parameters. */
	protected final Map<String, TestRunParameters> runParameters;
	
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
	 * @param __runParameters The run parameters.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/09/11
	 */
	public VMTestFrameworkTestClassProcessor(
		Map<String, CandidateTestFiles> __availableTests,
		IdGenerator<?> __idGenerator, String __projectName,
		Map<String, TestRunParameters> __runParameters)
		throws NullPointerException
	{
		if (__availableTests == null || __idGenerator == null ||
			__projectName == null || __runParameters == null)
			throw new NullPointerException("NARG");
		
		this.availableTests = __availableTests;
		this.idGenerator = __idGenerator;
		this.projectName = __projectName;
		this.runParameters = __runParameters;
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
	@SuppressWarnings("UseOfProcessBuilder")
	@Override
	public void stop()
	{
		// Stop is a bit of a misnomer, it means stop processing and then run
		// all the tests...
		
		// Remember thread for later stop
		synchronized (this)
		{
			this._runningThread = Thread.currentThread();
		}
		
		Map<String, CandidateTestFiles> availableTests = this.availableTests;
		Map<String, TestRunParameters> runParameters = this.runParameters;
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
			// Threads for processing stream data
			Process process = null;
			Thread stdOutThread = null;
			Thread stdErrThread = null;
			
			// Make sure process and thread are killed at the end
			try
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
				
				// Get parameters for this test run
				TestRunParameters runTest = runParameters.get(testName);
				
				// Start test
				DefaultTestDescriptor desc = new DefaultTestMethodDescriptor(
					idGenerator.generateId(), testName, testName);
				resultProcessor.started(desc,
					new TestStartEvent(System.currentTimeMillis(),
						suiteDesc.getId()));
				
				// Setup process to run
				ProcessBuilder builder = new ProcessBuilder();
				
				// The command we are executing
				builder.command(runTest.getCommandLine());
				
				// Redirect all the outputs we have
				builder.redirectOutput(ProcessBuilder.Redirect.PIPE);
				builder.redirectError(ProcessBuilder.Redirect.PIPE);
				
				// Start the process
				try
				{
					process = builder.start();
				}
				
				// Failed to start?
				catch (IOException __e)
				{
					// Failed to start test
					resultProcessor.failure(suiteDesc.getId(),
						new Throwable("Failed to start test."));
					
					throw new RuntimeException(__e);
				}
				
				// Setup listening buffer threads
				VMTestOutputBuffer stdOut = new VMTestOutputBuffer(
					process.getInputStream(),
					new TestResultOutputStream(resultProcessor, desc.getId(),
						TestOutputEvent.Destination.StdOut),
					false);
				VMTestOutputBuffer stdErr = new VMTestOutputBuffer(
					process.getErrorStream(),
					new TestResultOutputStream(resultProcessor, desc.getId(),
						TestOutputEvent.Destination.StdErr),
					true);
				
				// Setup threads for reading standard output and error
				stdOutThread = new Thread(stdOut, "stdOutReader");
				stdErrThread = new Thread(stdErr, "stdErrReader");
				
				// Start threads so console lines can be read as they appear
				stdOutThread.start();
				stdErrThread.start();
				
				// Run the test, default to failed exit code
				int exitCode = -1;
				try
				{
					// If are stopping, just stop at this point
					if (this._stopNow)
						exitCode = process.exitValue();
					
					// Wait for it to complete, with triggering a timeout
					else if (process.waitFor(3, TimeUnit.MINUTES))
						exitCode = process.exitValue();
				}
				
				// We got interrupted, force continue on!
				catch (IllegalThreadStateException|InterruptedException
					ignored)
				{
					// Forcibly destroy the process if it is alive
					if (process.isAlive())
						process.destroyForcibly();
					
					// Make sure the threads process their output
					stdOutThread.interrupt();
					stdErrThread.interrupt();
				}
				
				// Force completion of the read thread, we cannot continue if
				// the other thread is currently working...
				stdOut.getBytes(stdOutThread);
				stdErr.getBytes(stdErrThread);
				
				// The result determines whether this succeeded, skipped, or
				// failed
				TestResult.ResultType testResult;
				switch (exitCode)
				{
						// Success for zero
					case 0:
						testResult = TestResult.ResultType.SUCCESS;
						break;
						
						// Skipped is just two
					case 2:
						testResult = TestResult.ResultType.SKIPPED;
						break;
						
						// Treat anything else as failure
					default:
						testResult = TestResult.ResultType.FAILURE;
						break;
				}
				
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
				
				// Mark as completed
				resultProcessor.completed(desc.getId(),
					new TestCompleteEvent(System.currentTimeMillis(),
						testResult));
			}
			// Interrupt read/write threads
			finally
			{
				// If our test process is still alive, stop it
				if (process != null)
					if (process.isAlive())
						process.destroyForcibly();
				
				// Stop the standard output thread from running
				if (stdOutThread != null)
					stdOutThread.interrupt();
				
				// Stop the standard error thread from running
				if (stdErrThread != null)
					stdErrThread.interrupt();
			}
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
			// Do stop now
			this._stopNow = true;
			
			// Interrupt thread quickly
			Thread runningThread = this._runningThread;
			if (runningThread != null)
				runningThread.interrupt();
			
			// Make sure to notify on all monitors
			this.notifyAll();
		}
	}
}
