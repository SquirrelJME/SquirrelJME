// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.multivm;

import cc.squirreljme.plugin.util.SimpleJavaExecSpecFiller;
import cc.squirreljme.plugin.util.SingleTaskOutputFile;
import javax.inject.Inject;
import lombok.Getter;
import org.gradle.api.tasks.Internal;
import org.gradle.api.tasks.testing.Test;
import org.gradle.workers.WorkerExecutor;

/**
 * Not Described.
 *
 * @since 2022/09/11
 */
public class VMModernTestTask
	extends Test
	implements VMExecutableTask
{
	/** The source set used. */
	@Internal
	@Getter
	protected final String sourceSet;
	
	/** The virtual machine type. */
	@Internal
	@Getter
	protected final VMSpecifier vmType;
	
	/**
	 * Initializes the task.
	 *
	 * @param __executor The executor for the task.
	 * @param __sourceSet The source set to use.
	 * @param __vmType The virtual machine type.
	 * @param __libTask The task used to create libraries, this may be directly
	 * depended upon.
	 * @since 2020/08/07
	 */
	@Inject
	public VMModernTestTask(WorkerExecutor __executor, String __sourceSet,
		VMSpecifier __vmType, VMLibraryTask __libTask)
		throws NullPointerException
	{
		if (__executor == null ||
			__sourceSet == null || __vmType == null || __libTask == null)
			throw new NullPointerException("NARG");
		
		// These are used at the test stage
		this.sourceSet = __sourceSet;
		this.vmType = __vmType;
		
		// Set details of this task
		this.setGroup("squirreljme");
		this.setDescription("Runs the various automated tests (modern).");
		
		// Use our custom test framework?
		this.getTestFrameworkProperty().set(
			new VMTestFramework(this, __sourceSet, __vmType));
		
		// Depends on the library to exist first along with the emulator
		// itself
		this.dependsOn(this.getProject().provider(
				new VMRunDependencies(this, __sourceSet, __vmType)),
			new VMEmulatorDependencies(this, __vmType));
		
		// Add the entire JAR as input, so that if it changes for any reason
		// then all tests should be considered invalid and rerun
		// All of the input source files to be tested
		this.getInputs().files(
			this.getProject().provider(
				new SingleTaskOutputFile(__libTask)),
			this.getProject().provider(
				new VMTestInputs(this, __sourceSet)));
		
		// All of the test results that are created
		this.getOutputs().files(this.getProject().provider(
			new VMTestOutputs(this, __sourceSet, __vmType)));
		
		// Add additional testing to see if our test run will not be up to
		// date when we run these. Also this is never up to date if
		// test.single/single.test is used because we do not want to
		// interfere with the caching or not running tests in such
		// situations.
		this.getOutputs().upToDateWhen((__task) ->
			{
			return new VMRunUpToDateWhen(__sourceSet, __vmType)
				.isSatisfiedBy(__task) &&
				!VMHelpers.runningTests(__task.getProject(),
					this.sourceSet).isSingle;
			});
		
		// Only run if there are actual tests to run
		this.onlyIf(new CheckForTests(__sourceSet));
	}
}
