// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.multivm;

import cc.squirreljme.plugin.multivm.ident.SourceTargetClassifier;
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
	
	/** The classifier used. */
	protected final SourceTargetClassifier classifier;
	
	/**
	 * Initializes the handler.
	 * 
	 * @param __task The task testing under.
	 * @param __classifier The classifier used.
	 * @throws NullPointerException On null arguments.
	 * @since 2020/09/06
	 */
	public VMTestOutputs(VMExecutableTask __task,
		SourceTargetClassifier __classifier)
		throws NullPointerException
	{
		if (__task == null || __classifier == null)
			throw new NullPointerException("NARG");
		
		this.task = __task;
		this.classifier = __classifier;
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
		String sourceSet = this.classifier.getSourceSet();
		
		// Determine the root test result directory
		Path resultRoot = VMHelpers.testResultXmlDir(
			project, this.classifier).get();
		
		// The output of the task will be the test results
		for (String testName : VMHelpers.runningTests(project, sourceSet)
			.tests.keySet())
			result.add(resultRoot.resolve(
				VMHelpers.testResultXmlName(testName)));
		
		// Result CSV file that contains a summary on all the tests
		result.add(VMHelpers.testResultsCsvDir(project, this.classifier)
			.get().resolve(VMHelpers.testResultsCsvName(project)));
		
		return result;
	}
}
