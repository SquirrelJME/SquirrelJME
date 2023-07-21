// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.multivm;

import cc.squirreljme.plugin.SquirrelJMEPluginConfiguration;
import cc.squirreljme.plugin.multivm.ident.SourceTargetClassifier;
import cc.squirreljme.plugin.util.JavaExecSpecFiller;
import cc.squirreljme.plugin.util.SerializedPath;
import cc.squirreljme.plugin.util.SimpleJavaExecSpecFiller;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;
import org.gradle.api.Action;
import org.gradle.api.Task;
import org.gradle.api.logging.Logger;
import org.gradle.api.tasks.testing.Test;
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
	
	/** Print the test manifest? */
	public static final String PRINT_TEST_MANIFEST =
		"net.multiphasicapps.tac.resultManifest";
	
	/** The maximum parallel tests that can run at once. */
	private static final int _MAX_PARALLEL_TESTS =
		4;
	
	/** Cached CPU count. */
	private static volatile int _CACHED_CPU_COUNT;
	
	/** The worker executor. */
	protected final WorkerExecutor executor;
	
	/** The classifier used. */
	protected final SourceTargetClassifier classifier;
	
	/**
	 * Initializes the virtual machine task action.
	 * 
	 * @param __executor The executor for tasks.
	 * @param __classifier The classifier used.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/08/23
	 */
	public VMTestTaskAction(WorkerExecutor __executor,
		SourceTargetClassifier __classifier)
		throws NullPointerException
	{
		if (__executor == null || __classifier == null)
			throw new NullPointerException("NARG");
		
		this.executor = __executor;
		this.classifier = __classifier;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/08/07
	 */
	@Override
	public void execute(Task __task)
	{
		// The task used for testing
		Test testTask = (Test)__task;
		
		// Debug
		Logger logger = __task.getLogger();
		logger.debug("Tests: {}", VMHelpers.runningTests(
			__task.getProject(), this.classifier.getSourceSet()));
		
		// We want our tasks to run from within Gradle
		WorkQueue queue = this.executor.noIsolation();
		
		// All results will go here
		String sourceSet = this.classifier.getSourceSet();
		VMSpecifier vmType = this.classifier.getVmType();
		Path resultDir = VMHelpers.testResultXmlDir(__task.getProject(),
			this.classifier).get();
		
		// All the result files will be read afterwards to determine whether
		// this task will pass or fail
		Map<String, Path> xmlResults = new TreeMap<>();
		
		// How many tests should be run be at once?
		int maxParallel = VMTestTaskAction.maxParallelTests();
		
		// Determine the number of tests
		Map<String, CandidateTestFiles> tests = VMHelpers.runningTests(
			__task.getProject(), sourceSet).tests;
		Set<String> testNames = tests.keySet();
		int numTests = testNames.size();
		
		// Calculate suite run parameters
		SuiteRunParameters runSuite = VMTestTaskAction.runSuite(
			(VMBaseTask)__task, this.classifier);
		
		// Force non-parallel?
		if (runSuite.noParallelTests)
			maxParallel = 1;
		
		// We only need to set the classpath once
		Path[] classPath = SerializedPath.unboxPaths(runSuite.classPath);
		
		// Debug
		logger.debug("Testing ClassPath: {}",
			Arrays.asList(classPath));
		
		// Make unique ID for logger binding for this session
		String uniqueID = runSuite.uniqueId;
		
		// Setup logger for this session
		__LogHolder__ logHolder = new __LogHolder__(logger);
		VMTestWorkAction._LOGGERS.put(uniqueID, logHolder);
		
		// Execute the tests concurrently but up to the limit, as testing is
		// very intense on CPU
		int runCount = 0;
		int submitCount = 0;
		for (String testName : testNames)
		{
			// Calculate test running parameters
			CandidateTestFiles candidate = tests.get(testName);
			TestRunParameters runTest = VMTestTaskAction.runTest(
				(VMBaseTask)__task, this.classifier, runSuite, testName,
				candidate);
			
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
					// The logger used
					__params.getUniqueId().set(uniqueID);
					
					// The test and where the results will go
					__params.getTestName().set(testName);
					__params.getResultFile().set(xmlResult.toFile());
					
					// The command line to execute
					__params.getCommandLine().set(runTest.getCommandLine());
					
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
		
		// Get the status of every test
		Map<String, ResultantTestInfo> testResults =
			this.__testResults(xmlResults);
			
		// Determine and print any test failures
		Set<String> failedTests = new LinkedHashSet<>();
		for (ResultantTestInfo test : testResults.values())
			if (test.result == VMTestResult.FAIL)
			{
				failedTests.add(test.name);
				
				logger.error("Failed test: {}", test.name);
			}
			
		// Determine and ensure the directory where CSVs go exist
		Path csvDir = VMHelpers.testResultsCsvDir(__task.getProject(),
			this.classifier).get();
		try
		{
			Files.createDirectories(csvDir);
		}
		
		// Ignore failures here
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		// Print a CSV of all the test results
		try (PrintStream ps = new PrintStream(Files.newOutputStream(
			csvDir.resolve(VMHelpers.testResultsCsvName(__task.getProject())),
				StandardOpenOption.WRITE, StandardOpenOption.CREATE,
				StandardOpenOption.TRUNCATE_EXISTING),
				true, "utf-8"))
		{
			// Print CSV Header
			ps.println("name,status,duration,simpleDuration");
			
			// Print each test
			long totalTime = 0;
			for (ResultantTestInfo e : testResults.values())
			{
				totalTime += e.nanoseconds;
				
				ps.printf("%s,%s,%d,%s%n",
					e.name, e.result.name(), e.nanoseconds,
					VMTestTaskAction.__simpleDuration(e.nanoseconds));
			}
			
			// Put out totals
			ps.printf("TOTAL,,%d,%s%n",
				totalTime, VMTestTaskAction.__simpleDuration(totalTime));
			
			// Ensure everything is written
			ps.flush();
		}
		
		// Ignore these failures
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		// Wipe logger session
		VMTestWorkAction._LOGGERS.remove(uniqueID);
		
		// If there were failures, then fail this task with an exception
		if (!failedTests.isEmpty())
			throw new RuntimeException(
				"There were failing tests: " + failedTests);
	}
	
	/**
	 * Goes through all the XML files obtains the test results.
	 * 
	 * @param __xmlResults The result paths for tests. 
	 * @return The mapping of all tests.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/09/08
	 */
	Map<String, ResultantTestInfo> __testResults(
		Map<String, Path> __xmlResults)
		throws NullPointerException
	{
		if (__xmlResults == null)
			throw new NullPointerException("NARG");
		
		// Colon positions
		List<Integer> colons = new ArrayList<>(4);
		
		// Failure sequence
		Map<String, ResultantTestInfo> result = new TreeMap<>();
		for (Map.Entry<String, Path> test : __xmlResults.entrySet())
			try
			{
				// Resulting status and key
				VMTestResult testResult = null;
				long nanoseconds = -1;
				
				// Check all lines of the file and see if one is found
				for (String line : Files.readAllLines(test.getValue()))
				{
					// Locate the special key
					int keyDx = line.indexOf(VMTestTaskAction._SPECIAL_KEY);
					if (keyDx < 0)
						continue;
					
					// Find indexes of all the colons after this key
					colons.clear();
					for (int at = keyDx; at >= 0;)
					{
						at = line.indexOf(':', at + 1);
						
						if (keyDx > 0)
							colons.add(at);
					}
					
					// Need three colons here
					if (colons.size() < 3)
						continue;
					
					// Extract the key and value
					String key = line.substring(
						colons.get(0) + 1, colons.get(1));
					String val = line.substring(
						colons.get(1) + 1, colons.get(2));
					
					// Depends on the key/value
					try
					{	
						switch (key)
						{
							case "result":
								testResult = VMTestResult.valueOf(val);
								break;
							
							case "nanoseconds":
								nanoseconds = Long.parseLong(val);
								break;
						}
					}
					catch (IllegalArgumentException e)
					{
						e.printStackTrace();
					}
				}
				
				// Store result here
				result.put(test.getKey(), new ResultantTestInfo(test.getKey(),
					testResult, nanoseconds));
			}
			catch (IOException e)
			{
				throw new RuntimeException("Could not read test XML.", e);
			}
		
		return result;
	}
	
	/**
	 * Is debugging being used?
	 * 
	 * @return If debugging is being used.
	 * @since 2022/09/15
	 */
	public static boolean isDebugging()
	{
		String jdwpProp = System.getProperty("squirreljme.xjdwp",
			System.getProperty("squirreljme.jdwp"));
		return (jdwpProp != null && !jdwpProp.isEmpty());
	}
	
	/**
	 * Sets the maximum number of parallel tests to run.
	 * 
	 * @return The max parallel tests to run at once.
	 * @since 2022/09/11
	 */
	public static int maxParallelTests()
	{
		// If debugging, do not run in parallel
		if (null != System.getProperty("squirreljme.xjdwp",
			System.getProperty("squirreljme.jdwp")))
			return 1;
		
		int cpuCount = VMTestTaskAction.physicalProcessorCount();
		return (cpuCount <= 1 ? 1 :
			Math.min(Math.max(2, cpuCount),
				VMTestTaskAction._MAX_PARALLEL_TESTS));
	}
	
	/**
	 * Returns the physical processor count.
	 * 
	 * @return The physical processor count.
	 * @since 2020/11/25
	 */
	@SuppressWarnings("CallToSystemGetenv")
	public static int physicalProcessorCount()
	{
		// Use pre-cached value if it is already known
		int rv = VMTestTaskAction._CACHED_CPU_COUNT;
		if (rv > 0)
			return rv;
		
		// If this variable is set, just force all CPUs to be used
		if (Boolean.parseBoolean(System.getenv("USE_ALL_PROCESSORS")))
			return Math.max(1, Runtime.getRuntime().availableProcessors());
		
		// We need this so we can make a better guess of our current system
		String osName = System.getProperty("os.name").toLowerCase();
		
		// Running on Windows
		if (rv <= 0 && osName.contains("windows"))
			rv = VMTestTaskAction.__cpuCountOnWindows();
		
		// Fallback to a generic CPU count
		if (rv <= 0)
			rv = Math.max(1, Runtime.getRuntime().availableProcessors() / 2);
		
		// Cache it and use it
		VMTestTaskAction._CACHED_CPU_COUNT = rv;
		return rv;
	}
	
	/**
	 * Initializes the suite parameters.
	 *
	 * @param __task The task.
	 * @param __classifier The classifier used.
	 * @return The parameters to run all tests with.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/09/11
	 */
	public static SuiteRunParameters runSuite(VMBaseTask __task,
		SourceTargetClassifier __classifier)
		throws NullPointerException
	{
		if (__task == null || __classifier == null)
			throw new NullPointerException("NARG");
		
		// Setup builder
		SuiteRunParameters.SuiteRunParametersBuilder builder =
			SuiteRunParameters.builder();
		
		// Determine system properties to use for testing
		Map<String, String> sysProps = new LinkedHashMap<>();
		if (Boolean.getBoolean("java.awt.headless"))
			sysProps.put("java.awt.headless", "true");
		
		// Any specific changes to how tests run
		SquirrelJMEPluginConfiguration config =
			SquirrelJMEPluginConfiguration.configurationOrNull(
				__task.getProject());
		if (config != null)
		{
			// If we define any system properties specifically for tests then
			// use them here. Could be used for debugging.
			sysProps.putAll(config.testSystemProperties);
			
			// Disable parallelism for these tests?
			builder.noParallelTests(config.noParallelTests);
		}
		
		// Can we directly refer to the emulator library already?
		Path emuLib = VMHelpers.findEmulatorLib(__task);
		if (emuLib != null && Files.exists(emuLib))
			sysProps.put("squirreljme.emulator.libpath", emuLib.toString());
		
		// Setup parameters and build
		return builder
			.sysProps(sysProps)
			.emuLib(new SerializedPath(emuLib))
			.uniqueId(UUID.randomUUID().toString())
			.classPath(SerializedPath.boxPaths(VMHelpers.runClassPath(
				__task, __classifier, true)))
			.build();
	}
	
	/**
	 * Determines the command line that is used to run tests.
	 * 
	 * @param __task The task this is for.
	 * @param __classifier The classifier used.
	 * @param __runSuite The existing run suite.
	 * @param __testName The name of the test.
	 * @param __candidate The test candidate, for test information.
	 * @return The parameters for running this test.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/09/11
	 */
	public static TestRunParameters runTest(VMBaseTask __task,
		SourceTargetClassifier __classifier,
		SuiteRunParameters __runSuite, String __testName,
		CandidateTestFiles __candidate)
		throws NullPointerException
	{
		if (__task == null || __classifier == null ||
			__runSuite == null || __testName == null || __candidate == null)
			throw new NullPointerException("NARG");
		
		TestRunParameters.TestRunParametersBuilder builder =
			TestRunParameters.builder();
		
		// Default arguments, could be replaced by a proxy main
		String mainClass = VMHelpers.SINGLE_TEST_RUNNER;
		String[] mainArgs = new String[]{__testName};
		
		// Are we going to use a different proxy main class for this?
		String proxyMain = __candidate.expectedValues.get("proxy-main");
		if (proxyMain != null && !proxyMain.trim().isEmpty())
		{
			mainArgs = new String[]{mainClass, mainArgs[0]};
			mainClass = proxyMain.trim();
		}
		
		// Deserialize classpath
		Path[] classPath = SerializedPath.unboxPaths(__runSuite.classPath);
		
		Map<String, String> sysProps = new LinkedHashMap<>(
			__runSuite.getSysProps());
		if (Boolean.parseBoolean(
			__candidate.expectedValues.get("test-vm-target")))
			sysProps.put("cc.squirreljme.test.vm",
				__classifier.getBangletVariant().banglet);
		
		// Print test result manifest?
		if (Boolean.getBoolean(VMTestTaskAction.PRINT_TEST_MANIFEST) ||
			(__task.hasProperty(VMTestTaskAction.PRINT_TEST_MANIFEST) &&
			Boolean.parseBoolean(Objects.toString(
				__task.property(VMTestTaskAction.PRINT_TEST_MANIFEST)))))
			sysProps.put(VMTestTaskAction.PRINT_TEST_MANIFEST, "true");
		
		// Determine the arguments that are used to spawn the JVM
		JavaExecSpecFiller execSpec = new SimpleJavaExecSpecFiller();
		__classifier.getVmType().spawnJvmArguments(__task, true,
			execSpec, mainClass, __testName, sysProps,
			classPath, classPath, mainArgs);
		
		// Get command line
		List<String> commandLine = new ArrayList<>();
		for (String arg : execSpec.getCommandLine())
			commandLine.add(arg);
		
		// Build final result
		return builder
			.commandLine(commandLine)
			.build();
	}
	
	/**
	 * Attempts to get the physical CPU count on Windows.
	 * 
	 * @return The physical CPU count on Windows, {@code 0} is returned if it
	 * could not be obtained.
	 * @since 2020/11/25
	 */
	@SuppressWarnings("UseOfProcessBuilder")
	private static int __cpuCountOnWindows()
	{
		// This information could be obtained by running a specific program
		try
		{
			// Spawn new process that will contain the CPU count
			Process proc = new ProcessBuilder()
				.command("cmd", "/C", "WMIC", "CPU", "Get", "/Format:List")
				.redirectError(ProcessBuilder.Redirect.INHERIT)
				.start();
			
			// Let the process run accordingly 
			try
			{
				// Command failed to start, so likely unreliable
				if (proc.waitFor() != 0)
					return 0;
			}
			
			// We probably want the process to go away if this happens
			catch (InterruptedException ignored)
			{
				proc.destroyForcibly();
				
				// The CPU count will be invalid here
				return 0;
			}
			
			// Look for the CPU information lines
			int numCores = 0;
			try (BufferedReader br = new BufferedReader(new InputStreamReader(
				proc.getInputStream(), StandardCharsets.UTF_8)))
			{
				for (;;)
				{
					String ln = br.readLine();
					
					if (ln == null)
						break;
					
					// Number of cores in the system
					if (ln.startsWith("NumberOfCores="))
						numCores = Integer.parseInt(
							ln.substring(ln.indexOf('=') + 1));
				}
			}
			
			// Were we able to glean the CPU count?
			return Math.max(numCores, 0);
		}
		
		// Ignore failures
		catch (IOException|NumberFormatException e)
		{
			// Toss exception
			new RuntimeException("Could not determine physical CPU count.", e)
				.printStackTrace();
			
			return 0;
		}
	}
	
	/**
	 * Returns the simple duration of the test.
	 * 
	 * @param __dur The nanoseconds to map.
	 * @return The simple duration string.
	 * @since 2020/11/26
	 */
	@SuppressWarnings("MagicNumber")
	private static String __simpleDuration(long __dur)
	{
		// Instantly finished?
		if (__dur <= 0)
			return "instant";
		
		StringBuilder sb = new StringBuilder();
		
		// Nanoseconds
		long ns = __dur % 1_000_000L;
		sb.insert(0, String.format("%dns", ns));
		__dur /= 1_000_000L;
		
		// Milliseconds
		long ms = __dur % 1_000L;
		if (ms > 0)
			sb.insert(0, String.format("%dms ", ms));
		__dur /= 1_000L;
		
		// Seconds
		long s = __dur % 60L;
		if (s > 0)
			sb.insert(0, String.format("%ds ", s));
		__dur /= 60L;
		
		// Minutes
		long m = __dur & 60L;
		if (m > 0)
			sb.insert(0, String.format("%dm ", m));
		
		return sb.toString();
	}
}
