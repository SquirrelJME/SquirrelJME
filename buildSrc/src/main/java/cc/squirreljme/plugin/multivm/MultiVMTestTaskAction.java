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
import java.util.Collections;
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
public class MultiVMTestTaskAction
	implements Action<Task>
{
	/** The worker executor. */
	protected final WorkerExecutor executor;
	
	/** The source set used. */
	protected final String sourceSet;
	
	/** The virtual machine type. */
	protected final VirtualMachineSpecifier vmType;
	
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
	public MultiVMTestTaskAction(WorkerExecutor __executor,
		Supplier<JavaExecSpec> __specFactory, String __sourceSet,
		VirtualMachineSpecifier __vmType)
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
	@Override
	public void execute(Task __task)
	{
		// Debug
		__task.getLogger().debug("Tests: {}", MultiVMHelpers.runningTests(
			__task.getProject(), this.sourceSet));
		
		// We want our tasks to run from within Gradle
		WorkQueue queue = this.executor.noIsolation();
		
		// We will need this as we cannot pass tasks for execution specs
		// due to a serialization barrier, so we must only pass command line
		// arguments
		Supplier<JavaExecSpec> specFactory = this.specFactory;
		
		// All results will go here
		String sourceSet = this.sourceSet;
		VirtualMachineSpecifier vmType = this.vmType;
		Path resultDir = MultiVMHelpers.testResultDir(__task.getProject(),
			vmType, sourceSet).get();
		
		// Execute the tests concurrently
		for (String testName : MultiVMHelpers.runningTests(__task.getProject(),
				sourceSet).keySet())
		{
			// Determine the arguments that are used to spawn the JVM
			JavaExecSpec execSpec = specFactory.get();
			vmType.spawnJvmArguments(__task, execSpec,
				"net.multiphasicapps.tac.MainSingleRunner",
				Collections.<String, String>emptyMap(),
				MultiVMHelpers.runClassPath((MultiVMExecutableTask)__task,
					sourceSet, vmType), testName);
			
			// Submit our work task which should be a simple JVM execute due
			// to the limitations of Gradle workers
			queue.submit(VMTestWorkAction.class, __params ->
				{
					// The test and where the results will go
					__params.getTestName().set(testName);
					__params.getResultFile().set(resultDir.resolve(
						MultiVMHelpers.testResultXmlName(testName)).toFile());
					
					// The command line to execute
					__params.getCommandLine().set(execSpec.getCommandLine());
				});
		}
		
		// Wait for the queue to finish
		queue.await();
		
		// Print any tests that failed
		if (true)
			throw new Error("TODO");
		
		// If there were failures, then fail this task
		if (true)
			throw new Error("TODO");
	}
}
