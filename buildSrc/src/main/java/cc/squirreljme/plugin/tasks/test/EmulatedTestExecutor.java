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
import org.gradle.api.Project;
import org.gradle.api.internal.tasks.testing.TestExecuter;
import org.gradle.api.internal.tasks.testing.TestResultProcessor;

/**
 * This is the executer for tests.
 *
 * @since 2020/03/06
 */
public final class EmulatedTestExecutor
	implements TestExecuter<EmulatedTestExecutionSpec>
{
	/** The test task. */
	private final TestInVMTask _testInVMTask;
	
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
		// Setup for this suite
		Project project = _testInVMTask.getProject();
		EmulatedTestSuiteDescriptor suite =
			new EmulatedTestSuiteDescriptor(project);
		__results.started(suite, EmulatedTestUtilities.startNow());
		
		// Setup class
		EmulatedTestClassDescriptor classy =
			new EmulatedTestClassDescriptor(suite, "foo.Foo");
		__results.started(classy, EmulatedTestUtilities.startNow(suite));
		
		// Setup method
		EmulatedTestMethodDescriptor method =
			new EmulatedTestMethodDescriptor(classy);
		__results.started(method, EmulatedTestUtilities.startNow(classy));
		
		try
		{
			// Show a message
			__results.output(method.getId(),
				EmulatedTestUtilities.output("Hello!"));
				
			Thread.sleep(5000);
		}
		catch (InterruptedException e)
		{
		}
		
		// Complete tests
		__results.completed(method.getId(),
			EmulatedTestUtilities.passNow());
		__results.completed(classy.getId(),
			EmulatedTestUtilities.passNow());
		__results.completed(suite.getId(),
			EmulatedTestUtilities.passNow());
		
		/*
		System.err.println("Initializing test.");
		Object basicId = new Object();
		TestDescriptorInternal testId = new DefaultTestSuiteDescriptor(
			basicId, "suite")
			{
				@Override
				public Object getOwnerBuildOperationId()
				{
					return basicId;
				}
			};
			
		System.err.printf("Starting test: %s%n", __results);
		__results.started(testId, new TestStartEvent(System.nanoTime()));
		
		try
		{
			Thread.sleep(5000);
		}
		catch (InterruptedException e)
		{
		}
		
		__results.output(basicId, new TestOutputEvent()
			{
				@Override
				public Destination getDestination()
				{
					return Destination.StdOut;
				}
				
				@Override
				public String getMessage()
				{
					return "Hello test output?";
				}
			});
			
			System.err.println("Completing test.");
		__results.completed(basicId, new TestCompleteEvent(
			System.nanoTime(), TestResult.ResultType.SUCCESS));
		
		//throw new Error("execute()");
		 */
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/03/06
	 */
	@Override
	public void stopNow()
	{
		System.err.println("Stopping test.");
	}
}
