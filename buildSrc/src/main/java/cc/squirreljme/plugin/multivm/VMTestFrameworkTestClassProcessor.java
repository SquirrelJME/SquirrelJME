// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the GNU General Public License v3+, or later.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.multivm;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.gradle.api.internal.tasks.testing.DefaultTestDescriptor;
import org.gradle.api.internal.tasks.testing.DefaultTestMethodDescriptor;
import org.gradle.api.internal.tasks.testing.DefaultTestOutputEvent;
import org.gradle.api.internal.tasks.testing.TestClassProcessor;
import org.gradle.api.internal.tasks.testing.TestClassRunInfo;
import org.gradle.api.internal.tasks.testing.TestCompleteEvent;
import org.gradle.api.internal.tasks.testing.TestResultProcessor;
import org.gradle.api.internal.tasks.testing.TestStartEvent;
import org.gradle.api.tasks.testing.TestOutputEvent;
import org.gradle.api.tasks.testing.TestResult;
import org.gradle.internal.id.IdGenerator;

/**
 * Processor for test classes.
 *
 * @since 2022/09/11
 */
public class VMTestFrameworkTestClassProcessor
	implements TestClassProcessor
{
	/** Tests to run. */
	protected final List<String> runTests =
		new ArrayList<>();
	
	/** The tests that are available. */
	protected final Map<String, CandidateTestFiles> availableTests;
	
	/** The ID generator to use. */
	protected final IdGenerator<?> idGenerator;
	
	/** Test result output. */
	private volatile TestResultProcessor _resultProcessor;
	
	/**
	 * Initializes the processor.
	 *
	 * @param __availableTests The tests that are available.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/09/11
	 */
	public VMTestFrameworkTestClassProcessor(
		Map<String, CandidateTestFiles> __availableTests,
		IdGenerator<?> __idGenerator)
		throws NullPointerException
	{
		if (__availableTests == null || __idGenerator == null)
			throw new NullPointerException("NARG");
		
		this.availableTests = __availableTests;
		this.idGenerator = __idGenerator;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/09/11
	 */
	@Override
	public void startProcessing(TestResultProcessor __resultProcessor)
	{
		// Store this for later
		this._resultProcessor = __resultProcessor;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/09/11
	 */
	@Override
	public void processTestClass(TestClassRunInfo __testClass)
	{
		// Remember class for later
		this.runTests.add(__testClass.getTestClassName());
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/09/11
	 */
	@Override
	public void stop()
	{
		// Stop is a bit of a misnomer, it means stop processing and then run
		// all the tests...
		Map<String, CandidateTestFiles> availableTests = this.availableTests;
		
		// Go through and actually run all the tests
		TestResultProcessor resultProcessor = this._resultProcessor;
		IdGenerator<?> idGenerator = this.idGenerator;
		for (String testName : this.runTests)
		{
			System.err.printf(">> TEST: %s%n", testName);
			System.err.flush();
			
			// Start test
			DefaultTestDescriptor desc = new DefaultTestMethodDescriptor(
				idGenerator.generateId(), testName, testName);
			resultProcessor.started(desc,
				new TestStartEvent(System.currentTimeMillis()));
			
			System.err.printf(">> RUN: %s%n", testName);
			System.err.flush();
			
			// Test output
			resultProcessor.output(desc.getId(),
				new DefaultTestOutputEvent(TestOutputEvent.Destination.StdOut, 
					"Boop"));
			
			// Just say it failed for now
			resultProcessor.completed(desc.getId(),
				new TestCompleteEvent(System.currentTimeMillis(),
					TestResult.ResultType.FAILURE));
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/09/11
	 */
	@Override
	public void stopNow()
	{
		throw new Error("TODO");
	}
}
