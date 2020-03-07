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
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Objects;
import java.util.TreeSet;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.gradle.api.Project;
import org.gradle.api.internal.tasks.testing.TestExecuter;
import org.gradle.api.internal.tasks.testing.TestResultProcessor;
import org.gradle.api.tasks.testing.TestOutputEvent;
import org.gradle.process.ExecResult;

/**
 * This is the executer for tests.
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
		
		// Run for this suite/project
		EmulatedTestSuiteDescriptor suite =
			new EmulatedTestSuiteDescriptor(project);
		
		// Indicate that the suite has started execution
		__results.started(suite, EmulatedTestUtilities.startNow());
		
		// Perform testing logic
		boolean allPassed = true;
		try
		{
			// Execute test classes (find them and run them)
			allPassed = this.executeClasses(__spec, __results, suite);
			
			// Did all tests pass?
			__results.completed(suite.getId(),
				EmulatedTestUtilities.passOrFailNow(allPassed));
		}
		catch (Throwable t)
		{
			// Report the thrown exception
			__results.failure(suite.getId(), t);
			__results.completed(suite.getId(),
				EmulatedTestUtilities.failNow());
		}
		
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
	 * Goes through and executes the single test class.
	 *
	 * @param __spec The specification.
	 * @param __results The output results.
	 * @param __method The method to run.
	 * @return The exit value of the test.
	 * @since 2020/03/06
	 */
	private int executeClass(EmulatedTestExecutionSpec __spec,
		TestResultProcessor __results, EmulatedTestMethodDescriptor __method)
	{
		Project project = this._testInVMTask.getProject();
		
		// For some reason test reports do not run if there is no output for
		// them, so this for the most part forces console output to happen
		// which makes tests happen
		__results.output(__method.getId(),
			EmulatedTestUtilities.outputErr(String.format(
			"Running test %s...", __method.getDisplayName())));
		
		// Execute the test Java program
		ExecResult result = project.javaexec(__javaExecSpec ->
			{
				Collection<String> args = new LinkedList<>();
				
				// Add emulator
				args.add("-Xemulator:" + __spec.emulator);
				
				// Add snapshot path
				args.add("-Xsnapshot:" + EmulatedTestExecutor.this
					._testInVMTask.getBinaryResultsDirectory()
					.file("profile.nps").get().getAsFile()
					.toPath().toAbsolutePath());
				
				// Classpath of the target JAR
				args.add("-classpath");
				args.add(EmulatedTestUtilities.classpathString(project,
					true));
				
				// Only run a single test
				args.add("net.multiphasicapps.tac.MainSingleRunner");
				
				// Target this single test
				args.add(Objects.requireNonNull(__method.getParent(),
					"No test class?").getClassName());
				
				// Configure the VM for execution
				__javaExecSpec.classpath(EmulatedTestUtilities
					.emulatorClassPath(project, __spec.emulator));
				__javaExecSpec.setMain("cc.squirreljme.emulator.vm.VMFactory");
				__javaExecSpec.setArgs(args);
				
				// Pipe outputs to the specified areas so the console can be
				// read properly!
				__javaExecSpec.setStandardOutput(new PipeOutputStream(
					__method.getId(), __results,
					TestOutputEvent.Destination.StdOut));
				__javaExecSpec.setErrorOutput(new PipeOutputStream(
					__method.getId(), __results,
					TestOutputEvent.Destination.StdErr));
				
				// Do not throw an exception on a non-zero exit value since
				// it is important to us
				__javaExecSpec.setIgnoreExitValue(true);
			});
		
		// Return the exit value here
		return result.getExitValue();
	}
	
	/**
	 * Goes through and executes the test classes.
	 *
	 * @param __spec The specification.
	 * @param __results The output results.
	 * @param __suite The suite to run.
	 * @return Did all tests pass?
	 * @throws IOException On read errors.
	 * @since 2020/03/06
	 */
	@SuppressWarnings("FeatureEnvy")
	private boolean executeClasses(EmulatedTestExecutionSpec __spec,
		TestResultProcessor __results, EmulatedTestSuiteDescriptor __suite)
		throws IOException
	{
		// Extract all of the test classes to be executed
		Collection<String> testClasses = new TreeSet<>();
		try (ZipInputStream zip = new ZipInputStream(Files.newInputStream(
			__spec.jar.getArchiveFile().get().getAsFile().toPath(),
			StandardOpenOption.READ)))
		{
			for (;;)
			{
				// Stop at the end of the ZIP
				ZipEntry entry = zip.getNextEntry();
				if (entry == null)
					break;
				
				// Only consider the services file
				if (!EmulatedTestExecutor.SERVICE_RESOURCE
					.equals(entry.getName()))
					continue;
				
				// We cannot close a stream, so we need to copy the data over
				// first
				ByteArrayOutputStream baos = new ByteArrayOutputStream(
					(int)Math.max(0, entry.getSize()));
				for (byte[] buf = new byte[4096];;)
				{
					int rc = zip.read(buf);
					if (rc < 0)
						break;
					
					baos.write(buf, 0, rc);
				}
				
				// Read in all the test classes
				try (BufferedReader br = new BufferedReader(
					new InputStreamReader(new ByteArrayInputStream(
					baos.toByteArray()), "utf-8")))
				{
					for (;;)
					{
						// End of file?
						String ln = br.readLine();
						if (ln == null)
							break;
						
						// Add test class to possible tests to run
						if (!ln.isEmpty())
							testClasses.add(ln.trim());
					}
				}
			}
		}
		
		// Used to flag if every test passed
		boolean allPassed = true;
		
		// Now go through the tests we discovered and execute them
		for (String testClass : testClasses)
		{
			// Stop executing? Note that if this is stopped in the middle of
			// a test we cannot make the tests pass
			if (this._stopRunning)
				return false;
			
			// In SquirrelJME all tests only have a single run method, so the
			// run is the actual class of execution
			EmulatedTestClassDescriptor classy =
				new EmulatedTestClassDescriptor(__suite, testClass);
			EmulatedTestMethodDescriptor method =
				new EmulatedTestMethodDescriptor(classy);
			
			// Start execution
			__results.started(classy,
				EmulatedTestUtilities.startNow(__suite));
			__results.started(method,
				EmulatedTestUtilities.startNow(classy));
			
			// Execute each class alone
			try
			{
				// Execute individual test
				int result = this.executeClass(__spec, __results, method);
				
				// Test did not pass
				if (result != ExitValueConstants.SUCCESS &&
					result != ExitValueConstants.SKIPPED)
				{
					// Clear out the stack trace because they are really
					// annoying
					Throwable empty = new Throwable("Test failed");
					empty.setStackTrace(new StackTraceElement[0]);
					
					// Use this thrown exception
					__results.failure(method.getId(), empty);
				}
				
				// End execution
				__results.completed(method.getId(),
					EmulatedTestUtilities.passSkipOrFailNow(result));
				__results.completed(classy.getId(),
					EmulatedTestUtilities.passSkipOrFailNow(result));
			}
			catch (Throwable t)
			{
				// Report the thrown exception
				__results.failure(method.getId(), t);
				__results.completed(method.getId(),
					EmulatedTestUtilities.failNow());
				
				// Fail the class as well
				__results.completed(classy.getId(),
					EmulatedTestUtilities.failNow());
			}
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
