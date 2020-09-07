// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.multivm;

import java.nio.file.Path;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;
import javax.inject.Inject;
import org.gradle.api.Action;
import org.gradle.api.Task;
import org.gradle.api.tasks.JavaExec;
import org.gradle.process.JavaForkOptions;
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
public class MultiVMTestTaskAction
	implements Action<Task>
{
	/** The worker executor. */
	protected final WorkerExecutor executor;
	
	/** The source set used. */
	protected final String sourceSet;
	
	/** The virtual machine type. */
	protected final VirtualMachineSpecifier vmType;
	
	/**
	 * Initializes the virtual machine task action.
	 * 
	 * @param __executor The executor for tasks.
	 * @param __sourceSet The source set.
	 * @param __vmType The virtual machine type used.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/08/23
	 */
	public MultiVMTestTaskAction(WorkerExecutor __executor, String __sourceSet,
		VirtualMachineSpecifier __vmType)
		throws NullPointerException
	{
		if (__sourceSet == null || __vmType == null)
			throw new NullPointerException("NARG");
		
		this.executor = __executor;
		this.sourceSet = __sourceSet;
		this.vmType = __vmType;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/08/07
	 */
	@Override
	public void execute(Task __task)
	{
		// Debug
		__task.getLogger().debug("Tests: {}", MultiVMHelpers.runningTests(
			__task.getProject(), this.sourceSet));
		
		// We want our tasks to run from within Gradle
		WorkQueue queue = this.executor.noIsolation();
		
		// All results will go here
		VirtualMachineSpecifier vmType = this.vmType;
		Path resultDir = MultiVMHelpers.testResultDir(__task.getProject(),
			vmType, this.sourceSet).get();
		
		// This is used to keep track of all the tests and see what happened
		// and to determine if a task passes or fails
		VMTestRunResults results = new VMTestRunResults();
		
		// Execute the tests concurrently
		for (String test : MultiVMHelpers.runningTests(__task.getProject(),
				this.sourceSet).keySet())
			queue.submit(VMTestWorkAction.class, __params ->
				{
					__params.getTestRunResults().set(results);
					//__params.getTask().set(__task);
					__params.getVmType().set(vmType);
					__params.getTestName().set(test);
					__params.getResultFile().set(resultDir.resolve(
						MultiVMHelpers.testResultXmlName(test)).toFile());
				});
		
		// Wait for the queue to finish
		queue.await();
		
		// Print any tests that failed
		for (String testName : results.failures)
			__task.getLogger().error("Failing test: {}", testName);
		
		// Throw exception on failure in this case
		if (results.failCount.get() > 0)
			throw new RuntimeException(String.format("%d tests failed.",
				results.failCount.get()));
	}
}
