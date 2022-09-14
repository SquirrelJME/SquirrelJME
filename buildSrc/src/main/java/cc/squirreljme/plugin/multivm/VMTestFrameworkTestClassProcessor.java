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
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
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
	
	/** Test result output. */
	protected final AtomicReference<TestResultProcessor> resultProcessor =
		new AtomicReference<>();
	
	/** The tests that are available. */
	protected final Map<String, CandidateTestFiles> availableTests;
	
	/** The ID generator to use. */
	protected final IdGenerator<?> idGenerator;
	
	/** The name of this project. */
	protected final String projectName;
	
	/** The run parameters. */
	protected final Map<String, TestRunParameters> runParameters;
	
	/** The thread that is running tests. */
	private volatile Thread _runningThread;
	
	/** Stop running tests? */
	private volatile boolean _stopNow;
	
	/** The final test result. */
	private volatile TestResult.ResultType _finalResult =
		TestResult.ResultType.SKIPPED;
	
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
		// Store this for late
		synchronized (this.resultProcessor)
		{
			this.resultProcessor.set(__resultProcessor);
		}
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
		// Stop is a bit of a misnomer, it means stop processing and then run
		// all the tests...
		
		// Remember thread for later stop
		synchronized (this)
		{
			this._runningThread = Thread.currentThread();
		}
		
		AtomicReference<TestResultProcessor> resultProcessor =
			this.resultProcessor;
		
		// Suite for the entire project group
		IdGenerator<?> idGenerator = this.idGenerator;
		DefaultTestSuiteDescriptor suiteDesc =
			new DefaultTestSuiteDescriptor(
				idGenerator.generateId(),
				this.projectName);
		
		// Suite has started
		VMTestFrameworkTestClassProcessor.resultAction(resultProcessor,
			(__rp) -> __rp.started(suiteDesc,
			new TestStartEvent(System.currentTimeMillis())));
		
		// Go through and actually run all the tests
		for (String testName : this.runTests)
		{
			// Do not run any more tests?
			synchronized (this)
			{
				if (this._stopNow)
					break;
			}
			
			// Run test and get its result
			TestResult.ResultType result = this.__runSingleTest(suiteDesc,
				testName);
			
			// Change the global suite test result here
			switch (result)
			{
				// If current state is skipped, then make success
				case SUCCESS:
					if (this._finalResult == TestResult.ResultType.SKIPPED)
						this._finalResult = result;
					break;
				
				// Otherwise, always mark failure
				case FAILURE:
					this._finalResult = result;
					break;
			}
		}
		
		// If failed, emit a throwable
		TestResult.ResultType finalResult = this._finalResult;
		if (finalResult == TestResult.ResultType.FAILURE)
			VMTestFrameworkTestClassProcessor.resultAction(resultProcessor,
				(__rp) -> __rp.failure(suiteDesc.getId(),
					new Throwable("Tests failed.")));
		
		// Use the final result from all the test runs
		VMTestFrameworkTestClassProcessor.resultAction(resultProcessor,
			(__rp) -> __rp.completed(suiteDesc.getId(),
				new TestCompleteEvent(System.currentTimeMillis(),
					finalResult)));
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
			
			// Make this invalid
			synchronized (this.resultProcessor)
			{
				this.resultProcessor.set(null);
			}
			
			// Interrupt thread quickly
			Thread runningThread = this._runningThread;
			if (runningThread != null)
				runningThread.interrupt();
			
			// Make sure to notify on all monitors
			this.notifyAll();
		}
	}
	
	/**
	 * Runs the test and gives the result of it.
	 * 
	 * @param __suiteDesc The suite descriptor.
	 * @param __testName The name of the test.
	 * @return The result of the test.
	 * @since 2022/09/11
	 */
	@SuppressWarnings("UseOfProcessBuilder")
	private TestResult.ResultType __runSingleTest(
		DefaultTestSuiteDescriptor __suiteDesc, String __testName)
	{
		Map<String, TestRunParameters> runParameters = this.runParameters;
		AtomicReference<TestResultProcessor> resultProcessor =
			this.resultProcessor;
		IdGenerator<?> idGenerator = this.idGenerator;
		
		// Default to failure
		TestResult.ResultType testResult = TestResult.ResultType.FAILURE;
		
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
				// If we are forcing a stop, mark as failure
				if (this._stopNow)
					return TestResult.ResultType.FAILURE;
			}
			
			// Get parameters for this test run
			TestRunParameters runTest = runParameters.get(__testName);
			
			// Start test
			DefaultTestDescriptor desc = new DefaultTestMethodDescriptor(
				idGenerator.generateId(), __testName, __testName);
			VMTestFrameworkTestClassProcessor.resultAction(resultProcessor,
				(__rp) -> __rp.started(desc,
					new TestStartEvent(System.currentTimeMillis(),
						__suiteDesc.getId())));
			
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
				VMTestFrameworkTestClassProcessor.resultAction(resultProcessor,
					(__rp) -> __rp.failure(__suiteDesc.getId(),
						new Throwable("Failed to start test.")));
				
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
			
			// Mark as completed
			TestResult.ResultType finalResult = testResult;
			VMTestFrameworkTestClassProcessor.resultAction(resultProcessor,
				(__rp) -> __rp.completed(desc.getId(),
				new TestCompleteEvent(System.currentTimeMillis(),
					finalResult)));
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
		
		return testResult;
	}
	
	/**
	 * Runs the given test result processor action, assuming that it is still
	 * a valid one. When {@link #stopNow()} then any processor that did exist
	 * must not be used ever again.
	 * 
	 * @param __atom The atomic to check on.
	 * @param __doThis Do this action.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/09/11
	 */
	@SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
	public static void resultAction(
		AtomicReference<TestResultProcessor> __atom,
		Consumer<TestResultProcessor> __doThis)
		throws NullPointerException
	{
		if (__atom == null || __doThis == null)
			throw new NullPointerException("NARG");
		
		synchronized (__atom)
		{
			TestResultProcessor processor = __atom.get();
			if (processor != null)
				__doThis.accept(processor);
		}
	}
}
