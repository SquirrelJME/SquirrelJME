// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.tasks.test;

import cc.squirreljme.plugin.tasks.TestInVMTask;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicInteger;
import org.gradle.api.Project;
import org.gradle.api.internal.tasks.testing.TestExecuter;
import org.gradle.api.internal.tasks.testing.TestResultProcessor;
import org.gradle.process.ExecResult;

/**
 * This is the executor for tests.
 *
 * @since 2020/03/06
 */
public final class EmulatedTestExecutor
	implements TestExecuter<EmulatedTestExecutionSpec>
{
	/** The service resource file. */
	public static final String SERVICE_RESOURCE =
		"META-INF/services/net.multiphasicapps.tac.TestInterface";
	
	/** The test task. */
	private final TestInVMTask _testInVMTask;
	
	/** Emulator classpath. */
	private final Map<String, Iterable<Object>> _emuClassPathCache =
		new TreeMap<>(String.CASE_INSENSITIVE_ORDER);
	
	/** Should this test be stopped? */
	private volatile boolean _stopRunning =
		false;
	
	/**
	 * Initializes the task executor.
	 *
	 * @param __testInVMTask The VM Task test.
	 * @since 2020/03/06
	 */
	public EmulatedTestExecutor(TestInVMTask __testInVMTask)
	{
		this._testInVMTask = __testInVMTask;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/03/06
	 */
	@SuppressWarnings("FeatureEnvy")
	@Override
	public void execute(EmulatedTestExecutionSpec __spec,
		TestResultProcessor __results)
	{
		// We need the project to access details
		Project project = this._testInVMTask.getProject();
		
		project.getLogger().lifecycle("POINT A");
		
		// Initialize the classpath for our emulator!
		// Otherwise the Gradle build will fail since it tries to get the
		// classpath outside of this
		this.emulatorClassPath(project, __spec.emulator);
		
		project.getLogger().lifecycle("POINT B");
		
		// Run for this suite/project
		EmulatedTestSuiteDescriptor suite =
			new EmulatedTestSuiteDescriptor(project);
		
		project.getLogger().lifecycle("POINT C");
		
		// Perform testing logic
		boolean allPassed;
		try
		{
			project.getLogger().lifecycle("POINT E");
			
			// Load tests to run
			Collection<String> classes = EmulatedTestUtilities.readJarServices(
				__spec.jar.getArchiveFile().get().getAsFile().toPath());
				
			project.getLogger().lifecycle("POINT F");
			
			// Report on the found test size (debugging)
			project.getLogger().lifecycle(String.format(
				"Found %d tests...%n", classes.size()));
			
			project.getLogger().lifecycle("POINT G");
			
			// Indicate that the suite has started execution
			__results.started(suite, EmulatedTestUtilities.startNow());
			
			project.getLogger().lifecycle("POINT G.a");
			
			// Execute test classes (find them and run them)
			allPassed = this.__executeClasses(__spec, __results, suite,
				classes);
			
			project.getLogger().lifecycle("POINT H");
			
			// Did all tests pass?
			__results.completed(suite.getId(),
				EmulatedTestUtilities.passOrFailNow(allPassed));
			
			project.getLogger().lifecycle("POINT I");
		}
		
		// Something went wrong it seems
		catch (Throwable t)
		{
			project.getLogger().error("Throwable caught during testing.", t);
			
			throw new RuntimeException("Caught throwable during testing.", t);
		}
		
		project.getLogger().lifecycle("POINT J");
		
		// Did not pass, so cause the task to fail
		if (!allPassed)
		{
			// Throw a blank failure exception
			RuntimeException toss = new RuntimeException("Failed tests.");
			toss.setStackTrace(new StackTraceElement[0]);
			
			throw toss;
		}
	}
	
	/**
	 * Executes the test class.
	 * 
	 * @param __spec The specification.
	 * @param __suite The suite to run.
	 * @param __testClass The test class.
	 * @return If this test passed.
	 * @since 2020/06/21
	 */
	private EmulatedTestResult __executeClass(EmulatedTestExecutionSpec __spec,
		EmulatedTestSuiteDescriptor __suite, String __testClass)
	{
		// Setup result
		EmulatedTestResult result = new EmulatedTestResult(
			__suite, __testClass);
		
		// Signal as started
		result.testStart();
		
		// Execute each class alone
		try
		{
			// Execute individual test
			int exitCode = this.__executeClassVm(__spec, result);
			
			// Finish the test with the result code
			result.testFinish(exitCode, null);
		}
		catch (Throwable t)
		{
			// Just mark failure
			result.testFinish(ExitValueConstants.FAILURE, t);
			
			// Print the trace since this was outside of the VM
			synchronized (System.err)
			{
				t.printStackTrace(System.err);
			}
		}
		
		return result;
	}
	
	/**
	 * Goes through and executes the single test class.
	 *
	 * @param __spec The specification.
	 * @param __result The output result.
	 * @return The exit value of the test.
	 * @since 2020/03/06
	 */
	@SuppressWarnings("resource")
	private int __executeClassVm(EmulatedTestExecutionSpec __spec,
		EmulatedTestResult __result)
	{
		Project project = this._testInVMTask.getProject();
		
		// Setup test standard output and error streams
		OutputStream stdOut = __result.stdOut();
		OutputStream stdErr = __result.stdErr();
		
		// For some reason test reports do not run if there is no output for
		// them, so this for the most part forces console output to happen
		// which makes tests happen
		String withinClass = __result.className;
		try
		{
			stdErr.write(String.format("Starting VM for %s...", withinClass)
				.getBytes());
		}
		catch (IOException ignored)
		{
		}
		
		// Execute the test Java program
		ExecResult result = project.javaexec(__javaExecSpec ->
			{
				Collection<String> args = new LinkedList<>();
				
				// Add emulator
				args.add("-Xemulator:" + __spec.emulator);
				
				// Add snapshot path
				args.add("-Xsnapshot:" + EmulatedTestExecutor.this
					._testInVMTask.__tempRoot().resolve("nps")
					.resolve(withinClass + ".nps").toAbsolutePath());
				
				// Classpath of the target JAR
				args.add("-classpath");
				args.add(EmulatedTestUtilities.classpathString(project,
					true));
				
				// Only run a single test
				args.add("net.multiphasicapps.tac.MainSingleRunner");
				
				// Target this single test
				args.add(withinClass);
				
				// Configure the VM for execution
				__javaExecSpec.classpath(EmulatedTestExecutor.this
					.emulatorClassPath(null, __spec.emulator));
				__javaExecSpec.setMain("cc.squirreljme.emulator.vm.VMFactory");
				__javaExecSpec.setArgs(args);
				
				// Pipe outputs to the specified areas so the console can be
				// read properly!
				__javaExecSpec.setStandardOutput(stdOut);
				__javaExecSpec.setErrorOutput(stdErr);
				
				// Do not throw an exception on a non-zero exit value since
				// it is important to us
				__javaExecSpec.setIgnoreExitValue(true);
			});
		
		// The exit value is the test result
		return result.getExitValue();
	}
	
	/**
	 * Returns the classpath for the emulator.
	 * 
	 * @param __project The project to use.
	 * @param __emulator The emulator to use.
	 * @return The classpath for the emulator.
	 * @throws NullPointerException On null arguments, or if {@code __project}
	 * is null and this is not already cached.
	 * @since 2020/06/22
	 */
	private Iterable<Object> emulatorClassPath(Project __project,
		String __emulator)
		throws NullPointerException
	{
		if (__emulator == null)
			throw new NullPointerException("NARG");
		
		// Use already cached value
		Map<String, Iterable<Object>> cache = this._emuClassPathCache;
		Iterable<Object> rv = cache.get(__emulator);
		if (rv != null)
			return rv; 
		
		// Get new result
		Iterable<Object> now = EmulatedTestUtilities.emulatorClassPath(
			__project, __emulator);
		
		// Add to the cache
		List<Object> toCache = new ArrayList<>();
		for (Object item : now)
			toCache.add(item);
		
		// Store unreadable form
		cache.put(__emulator,
			(rv = Collections.unmodifiableCollection(toCache)));
		
		// Ensure it stays unreadable
		return rv;
	}
	
	/**
	 * Goes through and executes the test classes.
	 *
	 * @param __spec The specification.
	 * @param __results The output results.
	 * @param __suite The suite to run.
	 * @param __classes The classes to run.
	 * @return Did all tests pass?
	 * @throws IOException On read errors.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/03/06
	 */
	@SuppressWarnings({"FeatureEnvy", "MagicNumber"})
	private boolean __executeClasses(EmulatedTestExecutionSpec __spec,
		TestResultProcessor __results, EmulatedTestSuiteDescriptor __suite,
		Iterable<String> __classes)
		throws IOException, NullPointerException
	{
		if (__spec == null || __results == null || __suite == null ||
			__classes == null)
			throw new NullPointerException("NARG");
		
		// And any results of the runs will be placed in this pool along
		// with the number of added and finished tests
		AtomicInteger finishedTests = new AtomicInteger();
		
		// Create jobs for the tests
		Set<String> failedTests = new TreeSet<>();
		boolean allPassed = true;
		for (String testClass : __classes)
		{
			// If stopping running, discontinue and fail the tests
			if (this._stopRunning)
			{
				allPassed = false;
				break;
			}
			
			// Execute the test
			EmulatedTestResult result = this.__executeClass(
				__spec, __suite, testClass);
			
			// Report the test
			result.report(__results);
			
			// Did this fail?
			if (result.isFailure())
				failedTests.add(result.className);
			
			// Increase the test counter, as this was grabbed
			finishedTests.incrementAndGet();
		}
		
		// There were test failures?
		if (!failedTests.isEmpty())
		{
			// Not every test passed
			allPassed = false;
			
			// Print all the failed tests out
			for (String testClass : failedTests)
				System.err.printf("Failed Test: %s%n", testClass);
		}
		
		// Return how all the tests ran
		return allPassed;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/03/06
	 */
	@Override
	public void stopNow()
	{
		// Stop running and prevent the next test from stopping
		this._stopRunning = true;
	}
}
