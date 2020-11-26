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
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.concurrent.Callable;
import org.gradle.api.Project;

/**
 * This calculates all of the test outputs for a given test, so that it can
 * be used to determine if it should run or run again.
 *
 * @since 2020/09/06
 */
public class VMTestOutputs
	implements Callable<Iterable<Path>>
{
	/** The task executing under. */
	protected final VMExecutableTask task;
	
	/** The source set working under. */
	protected final String sourceSet;
	
	/** The virtual machine type. */
	protected final VMSpecifier vmType;
	
	/**
	 * Initializes the handler.
	 * 
	 * @param __task The task testing under.
	 * @param __sourceSet The source set.
	 * @param __vmType The virtual machine this is created for.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/09/06
	 */
	public VMTestOutputs(VMExecutableTask __task, String __sourceSet,
		VMSpecifier __vmType)
		throws NullPointerException
	{
		if (__task == null || __sourceSet == null || __vmType == null)
			throw new NullPointerException("NARG");
		
		this.task = __task;
		this.sourceSet = __sourceSet;
		this.vmType = __vmType;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2020/09/06
	 */
	@Override
	public Iterable<Path> call()
	{
		Collection<Path> result = new LinkedHashSet<>();
		
		Project project = this.task.getProject();
		String sourceSet = this.sourceSet;
		
		// Determine the root test result directory
		Path resultRoot = VMHelpers.testResultXmlDir(
			project, this.vmType, sourceSet).get();
		
		// The output of the task will be the test results
		for (String testName :
			VMHelpers.runningTests(project, sourceSet).keySet())
			result.add(resultRoot.resolve(
				VMHelpers.testResultXmlName(testName)));
		
		// Result CSV file that contains a summary on all the tests
		result.add(VMHelpers.testResultsCsvDir(project, this.vmType, sourceSet)
			.get().resolve(VMHelpers.testResultsCsvName(project)));
		
		return result;
	}
}
