// -*- Mode: Java; indent-tabs-mode: t; tab-width: 4 -*-
// ---------------------------------------------------------------------------
// Multi-Phasic Applications: SquirrelJME
//     Copyright (C) Stephanie Gawroriski <xer@multiphasicapps.net>
// ---------------------------------------------------------------------------
// SquirrelJME is under the Mozilla Public License Version 2.0.
// See license.mkd for licensing and copyright information.
// ---------------------------------------------------------------------------

package cc.squirreljme.plugin.multivm;

import java.io.Serializable;
import java.util.Map;
import org.gradle.api.internal.tasks.testing.TestClassProcessor;
import org.gradle.api.internal.tasks.testing.WorkerTestClassProcessorFactory;
import org.gradle.internal.id.IdGenerator;
import org.gradle.internal.service.ServiceRegistry;

/**
 * Factory for creating test class processors?
 * 
 * This needs to be completely {@link Serializable} so Gradle can pass it to
 * the worker.
 *
 * @since 2022/09/11
 */
public class VMTestFrameworkWorkerTestClassProcessorFactory
	implements WorkerTestClassProcessorFactory, Serializable
{
	/** The tests that are available. */
	protected final Map<String, CandidateTestFiles> availableTests;
	
	/** The name of the project. */
	protected final String projectName;
	
	/** The run parameters for each test, that is how they are ran. */
	protected final Map<String, TestRunParameters> runParameters;
	
	/**
	 * Initializes the processor factory.
	 * 
	 * @param __availableTests The tests that are available.
	 * @param __runParams The run parameters for each test.
	 * @param __projectName The name of the project.
	 * @throws NullPointerException On null arguments.
	 * @since 2022/09/11
	 */
	public VMTestFrameworkWorkerTestClassProcessorFactory(
		Map<String, CandidateTestFiles> __availableTests,
		Map<String, TestRunParameters> __runParams, String __projectName)
		throws NullPointerException
	{
		if (__availableTests == null || __runParams == null ||
			__projectName == null)
			throw new NullPointerException("NARG");
		
		this.availableTests = __availableTests;
		this.runParameters = __runParams;
		this.projectName = __projectName;
	}
	
	/**
	 * {@inheritDoc}
	 * @since 2022/09/11
	 */
	@Override
	public TestClassProcessor create(ServiceRegistry __serviceRegistry)
	{
		return new VMTestFrameworkTestClassProcessor(this.availableTests,
			__serviceRegistry.get(IdGenerator.class), this.projectName,
			this.runParameters);
	}
}
