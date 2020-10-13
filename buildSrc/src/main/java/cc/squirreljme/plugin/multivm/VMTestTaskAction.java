// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.multivm;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.Supplier;
import org.gradle.api.Action;
import org.gradle.api.Task;
import org.gradle.process.JavaExecSpec;
import org.gradle.workers.WorkQueue;
import org.gradle.workers.WorkerExecutor;

/**
 * Performs the testing of the program.
 * 
 * This is responsible for executing the test and outputting JUnit results as
 * well which record the test logs accordingly. This also will record snapshots
 * for profiling and otherwise, if possible.
 * 
 * Entry is via {@code net.multiphasicapps.tac.MainSingleRunner} which is
 * passed the test to load and execute.
 *
 * @since 2020/08/07
 */
public class VMTestTaskAction
	implements Action<Task>
{
	/** The special key for quick finding test results. */
	static final String _SPECIAL_KEY = 
		"XERSQUIRRELJMEXER";
	
	/** The maximum parallel tests that can run at once. */
	private static final int _MAX_PARALLEL_TESTS =
		4;
	
	/** The worker executor. */
	protected final WorkerExecutor executor;
	
	/** The source set used. */
	protected final String sourceSet;
	
	/** The virtual machine type. */
	protected final VMSpecifier vmType;
	
	/** Factory for making specifications. */
	protected final Supplier<JavaExecSpec> specFactory;
	
	/**
	 * Initializes the virtual machine task action.
	 * 
	 * @param __executor The executor for tasks.
	 * @param __specFactory Factory for creating specifications.
	 * @param __sourceSet The source set.
	 * @param __vmType The virtual machine type used.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/08/23
	 */
	public VMTestTaskAction(WorkerExecutor __executor,
		Supplier<JavaExecSpec> __specFactory, String __sourceSet,
		VMSpecifier __vmType)
		throws NullPointerException
	{
		if (__sourceSet == null || __vmType == null)
			throw new NullPointerException("NARG");
		
		this.executor = __executor;
		this.specFactory = __specFactory;
		this.sourceSet = __sourceSet;
		this.vmType = __vmType;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/08/07
	 */
	@SuppressWarnings("UnstableApiUsage")
	@Override
	public void execute(Task __task)
	{
		// Debug
		__task.getLogger().debug("Tests: {}", VMHelpers.runningTests(
			__task.getProject(), this.sourceSet));
		
		// We want our tasks to run from within Gradle
		WorkQueue queue = this.executor.noIsolation();
		
		// We will need this as we cannot pass tasks for execution specs
		// due to a serialization barrier, so we must only pass command line
		// arguments
		Supplier<JavaExecSpec> specFactory = this.specFactory;
		
		// All results will go here
		String sourceSet = this.sourceSet;
		VMSpecifier vmType = this.vmType;
		Path resultDir = VMHelpers.testResultDir(__task.getProject(),
			vmType, sourceSet).get();
		
		// All of the result files will be read afterwards to determine whether
		// this task will pass or fail
		Map<String, Path> xmlResults = new TreeMap<>();
		
		// How many tests should be run be at once?
		int cpuCount = Runtime.getRuntime().availableProcessors();
		int maxParallel = (cpuCount <= 1 ? 1 : Math.min(
			Math.max(2, Runtime.getRuntime().availableProcessors() / 2),
			VMTestTaskAction._MAX_PARALLEL_TESTS));
		
		// Determine the number of tests
		Set<String> testNames = VMHelpers.runningTests(
			__task.getProject(), sourceSet).keySet();
		int numTests = testNames.size();
		
		// Determine system properties to use for testing
		Map<String, String> sysProps = new LinkedHashMap<>();
		if (Boolean.getBoolean("java.awt.headless"))
			sysProps.put("java.awt.headless", "true");
		
		// Execute the tests concurrently but up to the limit, as testing is
		// very intense on CPU
		int runCount = 0;
		int submitCount = 0;
		for (String testName : testNames)
		{
			// Determine the arguments that are used to spawn the JVM
			JavaExecSpec execSpec = specFactory.get();
			vmType.spawnJvmArguments(__task, execSpec,
				VMHelpers.SINGLE_TEST_RUNNER, sysProps,
				VMHelpers.runClassPath((VMExecutableTask)__task,
					sourceSet, vmType), testName);
			
			// Where will the results be read from?
			Path xmlResult = resultDir.resolve(
				VMHelpers.testResultXmlName(testName));
			xmlResults.put(testName, xmlResult);
			
			// Which test number is this?
			int submitId = ++submitCount;
			
			// Submit our work task which should be a simple JVM execute due
			// to the limitations of Gradle workers
			queue.submit(VMTestWorkAction.class, __params ->
				{
					// The test and where the results will go
					__params.getTestName().set(testName);
					__params.getResultFile().set(xmlResult.toFile());
					
					// The command line to execute
					__params.getCommandLine().set(execSpec.getCommandLine());
					
					// Name of the VM for hostname setting
					__params.getVmName()
						.set(vmType.vmName(VMNameFormat.PROPER_NOUN));
					
					// Used for progress tracking
					__params.getCount().set(submitId);
					__params.getTotal().set(numTests);
				});
			
			// Already requested the number of tests to run, so let them
			// finish first
			if (++runCount >= maxParallel)
			{
				// Reset counter so that we can trigger this again
				runCount = 0;
				
				// Wait for the current set to finish
				queue.await();
			}
		}
		
		// Wait for the queue to finish
		queue.await();
		
		// Determine the tests that failed
		Set<String> failedTests = this.__failedTests(xmlResults);
		
		// Print any tests that failed
		for (String testName : failedTests)
			__task.getLogger().error("Failed test: {}", testName);
		
		// If there were failures, then fail this task with an exception
		if (!failedTests.isEmpty())
			throw new RuntimeException(
				"There were failing tests: " + failedTests);
	}
	
	/**
	 * Goes through all of the XML files and searches for failed tests.
	 * 
	 * @param __xmlResults The result paths for tests. 
	 * @return The set of failed tests.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/09/08
	 */
	private Set<String> __failedTests(Map<String, Path> __xmlResults)
		throws NullPointerException
	{
		if (__xmlResults == null)
			throw new NullPointerException("NARG");
		
		// Failure sequence
		Set<String> result = new TreeSet<>();
		for (Map.Entry<String, Path> test : __xmlResults.entrySet())
			try
			{
				// Check all lines of the file and see if one is found
				for (String line : Files.readAllLines(test.getValue()))
				{
					// Locate the special key
					int keyDx = line.indexOf(
						VMTestTaskAction._SPECIAL_KEY);
					if (keyDx < 0)
						continue;
					
					// Find the first colon key
					int leftCol = line.indexOf(':', keyDx);
					if (leftCol < 0)
						continue;
					
					// Then the second
					int rightCol = line.indexOf(':', leftCol + 1);
					if (rightCol < 0)
						continue;
					
					// Locate the test result
					VMTestResult testResult = VMTestResult.valueOf(
						line.substring(leftCol + 1, rightCol));
					
					// Consider this a failure
					if (testResult == VMTestResult.FAIL)
						result.add(test.getKey());
					
					// Stop processing as we found it
					break;
				}
			}
			catch (IOException e)
			{
				throw new RuntimeException("Could not read test XML.", e);
			}
		
		return result;
	}
}
