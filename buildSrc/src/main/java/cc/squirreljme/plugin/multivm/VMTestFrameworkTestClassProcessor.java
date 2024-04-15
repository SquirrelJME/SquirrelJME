// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.multivm;

import cc.squirreljme.plugin.util.TestResultOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import org.gradle.api.internal.tasks.testing.DefaultTestClassDescriptor;
import org.gradle.api.internal.tasks.testing.DefaultTestDescriptor;
import org.gradle.api.internal.tasks.testing.DefaultTestMethodDescriptor;
import org.gradle.api.internal.tasks.testing.DefaultTestSuiteDescriptor;
import org.gradle.api.internal.tasks.testing.TestClassProcessor;
import org.gradle.api.internal.tasks.testing.TestClassRunInfo;
import org.gradle.api.internal.tasks.testing.TestCompleteEvent;
import org.gradle.api.internal.tasks.testing.TestResultProcessor;
import org.gradle.api.internal.tasks.testing.TestStartEvent;
import org.gradle.api.tasks.testing.TestFailure;
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
	protected final Map<String, Set<VMTestFrameworkTestClass>> runTests =
		new TreeMap<>();
	
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
		// Build test name for later usage
		String test = __testClass.getTestClassName();
		VMTestFrameworkTestClass testClass =
			new VMTestFrameworkTestClass(test);
		
		// Was there a match at all?
		boolean match = false;
		
		Map<String, Set<VMTestFrameworkTestClass>> runTests = this.runTests;
		for (String availableTest : this.availableTests.keySet())
		{
			VMTestFrameworkTestClass available =
				new VMTestFrameworkTestClass(availableTest);
			
			// Belong to a different class, ignore completely
			if (!testClass.className.equals(available.className))
				continue;
			
			// Is this pure match?
			boolean currentMatch = false;
			currentMatch = testClass.normal.equals(available.normal);
			
			// Is there a variant match?
			if (!currentMatch && testClass.variant != null)
				currentMatch = testClass.variant.equals(available.variant);
			
			// We have a primary sub-variant which matches, but we asked for no
			// secondary sub-variant and there is one... we want to grab it
			if (!currentMatch && testClass.primarySubVariant != null)
				currentMatch = testClass.primarySubVariant
					.equals(available.primarySubVariant) &&
					testClass.secondarySubVariant == null &&
					available.secondarySubVariant != null;
			
			// We did not ask for a variant but there is one, so include the
			// test
			if (!currentMatch && testClass.variant == null &&
				available.variant != null)
				currentMatch = true;
			
			// Matched, so add the test
			if (currentMatch)
				runTests.computeIfAbsent(testClass.className,
						(__k) -> new TreeSet<>())
					.add(new VMTestFrameworkTestClass(availableTest));
			
			// Matching emits a match
			match |= currentMatch;
		}
		
		// If there is no match and a variant was specified, we likely want
		// a slightly different parameter
		if (!match && testClass.variant != null)
		{
			// Remember class for later, sort by classes all together
			runTests.computeIfAbsent(testClass.className,
				(__k) -> new TreeSet<>()).add(testClass);
		}
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
		for (Map.Entry<String, Set<VMTestFrameworkTestClass>> byClass :
			this.runTests.entrySet())
		{
			// Do not run any more classes?
			synchronized (this)
			{
				if (this._stopNow)
					break;
			}
			
			// Start class test
			DefaultTestClassDescriptor classDesc =
				new DefaultTestClassDescriptor(idGenerator.generateId(),
					byClass.getKey(), byClass.getKey());
			VMTestFrameworkTestClassProcessor.resultAction(resultProcessor,
				(__rp) -> __rp.started(classDesc,
					new TestStartEvent(System.currentTimeMillis(),
						suiteDesc.getId())));
			
			// Go through all tests in this class
			TestResult.ResultType classResult = TestResult.ResultType.SKIPPED;
			for (VMTestFrameworkTestClass testName : byClass.getValue())
			{
				// Do not run any more tests?
				synchronized (this)
				{
					if (this._stopNow)
						break;
				}
				
				// We do not know how to run the test, so do nothing
				if (this.runParameters.get(testName.normal) == null)
					continue;
				
				// Run test and get its result
				TestResult.ResultType result = this.__runSingleTest(suiteDesc,
					testName, classDesc);
				
				// Update class based result
				classResult = VMTestFrameworkTestClassProcessor
					.calculateResult(classResult, result);
				
				// Change the global suite test result here
				this._finalResult = VMTestFrameworkTestClassProcessor
					.calculateResult(this._finalResult, result);
			}
			
			// Mark class as completed
			TestResult.ResultType finalClassResult = classResult;
			VMTestFrameworkTestClassProcessor.resultAction(resultProcessor,
				(__rp) -> __rp.completed(classDesc.getId(),
					new TestCompleteEvent(System.currentTimeMillis(),
						finalClassResult)));
		}
		
		// If failed, emit a throwable... if we do not do this then Gradle does
		// not care if a test failed and will just continue on happily like
		// nothing ever happened
		TestResult.ResultType finalResult = this._finalResult;
		if (finalResult == TestResult.ResultType.FAILURE)
			VMTestFrameworkTestClassProcessor.resultAction(resultProcessor,
				(__rp) -> __rp.failure(suiteDesc.getId(),
					TestFailure.fromTestFrameworkFailure(VMTestFrameworkTestClassProcessor
						.messageThrow("Tests have failed."))));
		
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
	 * @param __classDesc The owning class descriptor, since this is a group.
	 * @return The result of the test.
	 * @since 2022/09/11
	 */
	@SuppressWarnings("UseOfProcessBuilder")
	private TestResult.ResultType __runSingleTest(
		DefaultTestSuiteDescriptor __suiteDesc,
		VMTestFrameworkTestClass __testName,
		DefaultTestClassDescriptor __classDesc)
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
			TestRunParameters runTest = runParameters.get(__testName.normal);
			
			// Setup process to run
			ProcessBuilder builder = new ProcessBuilder();
			
			// The command we are executing
			builder.command(runTest.getCommandLine());
			
			// Redirect all the outputs we have
			builder.redirectOutput(ProcessBuilder.Redirect.PIPE);
			builder.redirectError(ProcessBuilder.Redirect.PIPE);
			
			// Start method test
			DefaultTestDescriptor methodDesc = new DefaultTestMethodDescriptor(
				idGenerator.generateId(), __testName.className, 
				(__testName.variant == null ? "test" :
					String.format("test[%s]", __testName.variant)));
			VMTestFrameworkTestClassProcessor.resultAction(resultProcessor,
				(__rp) -> __rp.started(methodDesc,
					new TestStartEvent(System.currentTimeMillis(),
						__classDesc.getId())));
			
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
						TestFailure.fromTestFrameworkFailure(new Throwable("Failed to start test."))));
				
				throw new RuntimeException(__e);
			}
			
			// Setup listening buffer threads
			VMTestOutputBuffer stdOut = new VMTestOutputBuffer(
				process.getInputStream(),
				new TestResultOutputStream(resultProcessor, methodDesc.getId(),
					TestOutputEvent.Destination.StdOut),
				false);
			VMTestOutputBuffer stdErr = new VMTestOutputBuffer(
				process.getErrorStream(),
				new TestResultOutputStream(resultProcessor, methodDesc.getId(),
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
			boolean isDebugging = VMTestTaskAction.isDebugging();
			for (long startTime = System.nanoTime();;)
				try
				{
					// How much time is left? If debugging time never runs
					// out
					long timeLeft = Math.max(VMTestWorkAction.TEST_TIMEOUT -
						(System.nanoTime() - startTime),
						(isDebugging ? 1000L : 0L));
					
					// Did we run out of time?
					// Wait for however long this takes to complete
					if (timeLeft <= 0 || this._stopNow ||
						process.waitFor(timeLeft, TimeUnit.NANOSECONDS))
					{
						exitCode = process.exitValue();
						break;
					}
				}
				
				// We got interrupted or ran out of time, make sure the process
				// stops, and we continue with the result
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
			byte[] stdErrBytes = stdErr.getBytes(stdErrThread);
			
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
			
			// If failed, emit a throwable...
			TestResult.ResultType finalResult = testResult;
			if (finalResult == TestResult.ResultType.FAILURE)
				VMTestFrameworkTestClassProcessor.resultAction(resultProcessor,
					(__rp) -> __rp.failure(methodDesc.getId(),
						TestFailure.fromTestFrameworkFailure(VMTestFrameworkTestClassProcessor
							.messageThrow("Test failed: " +
								__testName.normal))));
			
			// Mark method as completed
			VMTestFrameworkTestClassProcessor.resultAction(resultProcessor,
				(__rp) -> __rp.completed(methodDesc.getId(),
					new TestCompleteEvent(System.currentTimeMillis(),
						finalResult)));
		}
		
		// Interrupt read/write threads and kill the process if it is alive
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
	 * Calculates the test result.
	 * 
	 * @param __input The input result.
	 * @param __modifier The modifier to the result.
	 * @return The new result that should be used.
	 * @since 2022/09/14
	 */
	public static TestResult.ResultType calculateResult(
		TestResult.ResultType __input, TestResult.ResultType __modifier)
	{
		if (__input == null || __modifier == null)
			throw new NullPointerException("NARG");
		
		// Depends on our target result
		switch (__modifier)
		{
				// Keep the old state, this does not cause a change
			case SKIPPED:
				return __input;
				
				// Change skipped to success, but nothing else
			case SUCCESS:
				if (__input == TestResult.ResultType.SKIPPED)
					return __modifier;
				return __input;
			
				// Otherwise, always mark failure
			case FAILURE:
				return __modifier;
		}
		
		throw new Error("OOPS");
	}
	
	/**
	 * Generates a throwable useful for printing the output.
	 * 
	 * @param __output The output.
	 * @return The throwable to use for the message.
	 * @since 2022/09/14
	 */
	public static Throwable messageThrow(byte[] __output)
	{
		return VMTestFrameworkTestClassProcessor.messageThrow(
			(__output == null || __output.length <= 0 ? "" :
				new String(__output, StandardCharsets.UTF_8)));
	}
	
	/**
	 * Generates a throwable useful for printing the output.
	 *
	 * @param __output The output.
	 * @return The throwable to use for the message.
	 * @since 2022/09/14
	 */
	private static Throwable messageThrow(String __output)
	{
		return new VMTestFrameworkThrowableOutput(__output);
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
